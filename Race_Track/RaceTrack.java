import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * The {@link RaceTrack} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by multiple {@link Horse}s and by the {@link Broker}.
 * <p>
 * This is where the {@link Horse}s compete with each other to reach the end of the race.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace
 * @see Horse
 * @see HorsePos
 */

public class RaceTrack {
    private ReentrantLock r1;
    private Condition horsesCond;

    private HorsePos[] horses;
    private int numHorses;
    private int numHorsesFinished;
    private GeneralRepositoryOfInformation groi;
    private int numberOfHorses;
    private int raceLength;

    public RaceTrack(GeneralRepositoryOfInformation groi){
        r1 = new ReentrantLock();
        horsesCond = r1.newCondition();
        numberOfHorses = groi.getNumberOfHorses();
        raceLength = groi.getRaceLength();
        horses = new HorsePos[numberOfHorses];
        numHorses = 0;
        numHorsesFinished = 0;
        this.groi = groi;
        System.out.println("raceLength: " + raceLength);
        System.out.println("numberOfHorses: " + numberOfHorses);
    }

    /**
     * The {@link Broker} allows the first {@link Horse} to start running.
     */
    public void startTheRace(){
        r1.lock();
        try{
            for(int i = 0; i < numberOfHorses; i++)
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
     * The {@link Broker} enters the {@link RaceTrack} to see which {@link Horse}s have won the race
     * @return Array containing the ID's of the winning {@link Horse}s.
     */

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

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
        return result;
    }

    //Horses methods

    /**
     * The {@link Horse}s write their information on the array {@link #horses}, and then wait for their turn to proceed.
     * @param pID Id of the calling Thread.
     * @return Returns the position of the {@link Horse} in the array {@link #horses}.
     */
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

        }catch (InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return returnValue;
    }


    /**
     * The {@link Horse} increases its position in the race.
     * @param horsePos Index of the {@link Horse} in the array {@link #horses}.
     * @param moveAmount Amount to be increased in the position of the {@link Horse}.
     */
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

        } catch (IllegalMonitorStateException | InterruptedException e) {
            e.printStackTrace();
        } finally{
            r1.unlock();
        }
    }

    /**
     * Determines if the finish line has been crossed by the calling Thread.
     * @param horsePos Index of the {@link Horse} in the array {@link #horses}.
     * @return Returns true if the position of the {@link Horse} is equal or greater than the race length.
     */
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

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
        return returnVal;
    }

}
