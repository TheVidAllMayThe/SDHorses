import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.rmi.RemoteException;


/**
 * The {@link RaceTrack} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by multiple Horses and by the Broker.
 * <p>
 * This is where the Horses compete with each other to reach the end of the race.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see HorsePos
 */

public class RaceTrack implements RaceTrack_Interface{
    private ReentrantLock r1;
    private Condition horsesCond;

    private HorsePos[] horses;
    private int numHorses;
    private int numHorsesFinished;
    private GeneralRepositoryOfInformation_Interface groi;
    private int numberOfHorses;
    private int raceLength;

    RaceTrack(GeneralRepositoryOfInformation_Interface groi){
        r1 = new ReentrantLock();
        horsesCond = r1.newCondition();
        try{
            numberOfHorses = groi.getNumberOfHorses();
            raceLength = groi.getRaceLength();
        }catch(RemoteException e){
            e.printStackTrace();
        }
        horses = new HorsePos[numberOfHorses];
        numHorses = 0;
        numHorsesFinished = 0;
        this.groi = groi;
        System.out.println("raceLength: " + raceLength);
        System.out.println("numberOfHorses: " + numberOfHorses);
    }

    /**
     * The Broker allows the first Horse to start running.
     */
    @Override
    public void startTheRace(){
        r1.lock();
        try{
            for(int i = 0; i < numberOfHorses; i++)
                if(horses[i] == null)
                    horses[i] = new HorsePos(-1, false);

            horses[0].setMyTurn(true);
            horsesCond.signalAll();
        }catch(IllegalMonitorStateException e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    /**
     * The Broker enters the {@link RaceTrack} to see which Horses have won the race
     * @return Array containing the ID's of the winning Horses.
     */

    @Override
    public Integer[] reportResults(){

        Integer[] result = null;
        r1.lock();
        try{
            ArrayList<HorsePos> winnerHorsesTmp = new ArrayList<>();

            HorsePos min = Collections.min(Arrays.asList(horses));

            for(HorsePos horse : horses){
                if (horse.compareTo(min) == 0)
                    winnerHorsesTmp.add(horse);
            }

            result = new Integer[winnerHorsesTmp.size()];

            for(int i = 0; i < result.length; i++)
                result[i] = winnerHorsesTmp.get(i).getHorseID();

            horses = new HorsePos[numberOfHorses];
            numHorses = 0;
            numHorsesFinished = 0;

        }catch(IllegalMonitorStateException e){
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
     * @return Returns the position of the Horse in the array {@link #horses}.
     */

    @Override
    public int proceedToStartLine(Integer pID){   //Returns the pos in the array of Horses
        int returnValue = -1;
        r1.lock();
        try{
            groi.setHorsesState("ASL", pID);
            groi.setHorseTrackPosition(0, pID);
            groi.setHorseIteration(0,pID);
            groi.setHorsesStanding('F',pID);
            if(horses[numHorses] == null)
                horses[numHorses] = new HorsePos(pID, false);
            else
                horses[numHorses].setHorseID(pID);
            returnValue = numHorses++;

            while(!horses[returnValue].isMyTurn()){
                horsesCond.await();
            }

        }catch (RemoteException | InterruptedException e){
            e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return returnValue;
    }


    /**
     * The Horse increases its position in the race.
     * @param horsePos Index of the Horse in the array {@link #horses}.
     * @param moveAmount Amount to be increased in the position of the Horse.
     * @param horseID ID of the horse calling the method.
     */
    @Override
    public void makeAMove(Integer horsePos, Integer moveAmount, Integer horseID) {
        r1.lock();
        try {
            groi.setHorsesState("RUN", horseID);
            while (!horses[horsePos].isMyTurn()) {
                horsesCond.await();
            }
            horses[horsePos].addPos(moveAmount);
            groi.setHorseIteration(horses[horsePos].getNumSteps(), horseID);
            groi.setHorseTrackPosition(horses[horsePos].getPos(), horseID);
            horses[horsePos].setMyTurn(false);

        } catch (IllegalMonitorStateException | InterruptedException | RemoteException e) {
            e.printStackTrace();
        } finally{
            r1.unlock();
        }
    }

    /**
     * Determines if the finish line has been crossed by the calling Thread.
     * @param horsePos Index of the Horse in the array {@link #horses}.
     * @param horseID ID of the horse calling the method.
     * @return Returns true if the position of the Horse is equal or greater than the race length.
     */
    @Override
    public boolean hasFinishLineBeenCrossed(Integer horsePos, Integer horseID){ 
        boolean returnVal = false;
        r1.lock();
        try{
            if(numHorsesFinished+1 != numberOfHorses) {
                for (int i = horsePos + 1; i != horsePos; i++) {
                    i = i % numberOfHorses;
                    if (!horses[i].isFinished()) {
                        horses[i].setMyTurn(true);
                        break;
                    }
                }
            }
            else horses[horsePos].setMyTurn(true);

            if (horses[horsePos].getPos() >= raceLength) {
                groi.setHorsesState("AFL", horseID);
                returnVal = true;
                horses[horsePos].setFinished(true);
                groi.setHorsesStanding('T', horseID);
                numHorsesFinished++;
            }
            else{
                groi.setHorsesState("RUN", horseID);
            }

            horsesCond.signalAll();

        }catch (IllegalMonitorStateException | RemoteException e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
        return returnVal;
    }
}
