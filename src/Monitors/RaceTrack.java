package Monitors;

import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.AuxiliaryClasses.Parameters;
import Threads.Horse;
import Threads.Broker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * The {@link RaceTrack} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by multiple Horses and by the Broker.
 * <p>
 * This is where the Horses compete with each other to reach the end of the race.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace
 * @see Horse
 * @see HorsePos
 */

public class RaceTrack {
    private static ReentrantLock r1 = new ReentrantLock();
    private static Condition horsesCond = r1.newCondition();

    private static HorsePos[] horses = new HorsePos[Parameters.getNumberOfHorses()];
    private static int numHorses = 0;
    private static int numHorsesFinished = 0;

    /**
     * The Broker allows the first horse to start running
     */
    public static void startTheRace(){
        r1.lock();
        try{
            for(int i = 0; i < Parameters.getNumberOfHorses(); i++)
                if(horses[i] == null)
                    horses[i] = new HorsePos(-1, false);

            horses[0].setMyTurn(true);
            horsesCond.signalAll();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    /**
     * The broker enters the racetrack to see which Horses have won the race
     * @return Array containing the ID's of the winning Horses.
     */

    public static int[] reportResults(){

        int[] result = null;
        r1.lock();
        try{
            ArrayList<HorsePos> winnerHorsesTmp = new ArrayList<>();

            HorsePos min = Collections.min(Arrays.asList(horses));

            for(HorsePos horse : horses){
                if (horse.compareTo(min) == 0)
                    winnerHorsesTmp.add(horse);
            }

            result = new int[winnerHorsesTmp.size()];

            for(int i = 0; i < result.length; i++)
                result[i] = winnerHorsesTmp.get(i).getHorseID();

            horses = new HorsePos[Parameters.getNumberOfHorses()];
            numHorses = 0;
            numHorsesFinished = 0;

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
        return result;
    }

    //Horses methods

    /**
     * The Horses write their information on the array {@link #horses}, and then wait for their turn to proceed.
     * @param pID Id of the calling Thread.
     * @return Returns the position of the horse in the array {@link #horses}.
     */
    public static int proceedToStartLine(int pID){   //Returns the pos in the array of Horses
        int returnValue = -1;
        r1.lock();
        try{
            ((Horse)Thread.currentThread()).setState("AT_THE_START_LINE");
            GeneralRepositoryOfInformation.setHorsesState("ASL", pID);
            GeneralRepositoryOfInformation.setHorseTrackPosition(0, pID);
            GeneralRepositoryOfInformation.setHorseIteration(0,pID);
            GeneralRepositoryOfInformation.setHorsesStanding('F',pID);
            if(horses[numHorses] == null)
                horses[numHorses] = new HorsePos(pID, false);
            else
                horses[numHorses].setHorseID(pID);
            returnValue = numHorses++;

            while(!horses[returnValue].isMyTurn()){
                horsesCond.await();
            }

        }catch (InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return returnValue;
    }


    /**
     * The Horse increases its position in the race.
     * @param horsePos Index of the Horse in the array {@link #horses}.
     * @param moveAmount Amount to be increased in the position of the Horse.
     */
    public static void makeAMove(int horsePos, int moveAmount) {
        r1.lock();
        try {
            Horse hInst = (Horse)Thread.currentThread();
            hInst.setState("RUNNING");
            GeneralRepositoryOfInformation.setHorsesState("RUN", hInst.getID());
            while (!horses[horsePos].isMyTurn()) {
                horsesCond.await();
            }
            horses[horsePos].addPos(moveAmount);
            GeneralRepositoryOfInformation.setHorseIteration(horses[horsePos].getNumSteps(), hInst.getID());
            GeneralRepositoryOfInformation.setHorseTrackPosition(horses[horsePos].getPos(), hInst.getID());
            horses[horsePos].setMyTurn(false);

            if(numHorsesFinished+1 != Parameters.getNumberOfHorses()) {
                for (int i = horsePos + 1; i != horsePos; i++) {
                    i = i % Parameters.getNumberOfHorses();
                    if (!horses[i].isFinished()) {
                        horses[i].setMyTurn(true);
                        break;
                    }
                }
            }
            else horses[horsePos].setMyTurn(true);


        } catch (IllegalMonitorStateException | InterruptedException e) {
            e.printStackTrace();
            r1.unlock();
        }
    }

    /**
     * Determines if the finish line has been crossed by the calling Thread.
     * @param horsePos Index of the Horse in the array {@link #horses}.
     * @return Returns true if the Position of the horse in equal or greater than the race length.
     */
    public static boolean hasFinishLineBeenCrossed(int horsePos){ 
        boolean returnVal = false;
        try{
            Horse hInst = (Horse)Thread.currentThread();
            if (horses[horsePos].getPos() >= Parameters.getRaceLength()) {
                hInst.setState("AT_THE_FINISH_LINE");
                GeneralRepositoryOfInformation.setHorsesState("AFL", hInst.getID());
                returnVal = true;
                horses[horsePos].setFinished(true);
                GeneralRepositoryOfInformation.setHorsesStanding('T', hInst.getID());
                numHorsesFinished++;
            }
            else{
                hInst.setState("RUNNING");
                GeneralRepositoryOfInformation.setHorsesState("RUN", hInst.getID());
            }

            horsesCond.signalAll();

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            r1.unlock();
        }

        return returnVal;
    }

}
