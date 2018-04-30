import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;


/**
 * The {@link Stable} class is a monitor that contains all the
 * necessary methods to be used in mutual exclusive access by the {@link Broker} and {@link Horse}s to itself.
 * <p>
* This is where the {@link Horse}s are initially and after a race.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Main.HorseRace
 *@see Threads.Broker
 *@see Horse
*/

public class Stable {
    private ReentrantLock r1;
    private Condition horsesToPaddock;
    private boolean canHorsesMoveToPaddock;
    private Condition newRace;
    private int numHorses;
    private int raceNumber;
    private GeneralRepositoryOfInformation groi;
    private int numberOfHorses, numberOfRaces, raceLength;

    public Stable(GeneralRepositoryOfInformation groi){
        r1 = new ReentrantLock();
        horsesToPaddock = r1.newCondition();
        canHorsesMoveToPaddock = false;
        newRace = r1.newCondition();
        numHorses = 0;
        raceNumber = -1;
        numberOfHorses = groi.getNumberOfHorses();
        numberOfRaces = groi.getNumberOfRaces();
        raceLength = groi.getRaceLength();
        this.groi = groi;
        System.out.println("numberOfHorses: " + numberOfHorses);
        System.out.println("numberOfRaces: " + numberOfRaces);
        System.out.println("raceLength: " + raceLength);
    }

    /**
     * {@link Broker} awakes the {@link Horse}s who are waiting to enter the {@link Paddock}.
     */
    public void summonHorsesToPaddock(){
        r1.lock();
        try{
            raceNumber++;
            canHorsesMoveToPaddock = true;
            newRace.signalAll();
            horsesToPaddock.signal();
        }catch (IllegalMonitorStateException e){
            e.printStackTrace();
        }finally {
            r1.unlock();
        }
    }

    /**
     * Last function of {@link Broker} lifecycle, awakes {@link Horse}s waiting to enter {@link Paddock}.
     */
    public void entertainTheGuests(){
        r1.lock();
        try{
            raceNumber++;
            canHorsesMoveToPaddock = true;
            newRace.signalAll();
            horsesToPaddock.signal();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    /**
     * {@link Horse}s wait to move to {@link Paddock}.
     *
     * @param raceNum Number of the race in which the calling {@link Horse} will participate.
     */

    public void proceedToStable(Integer raceNum, Integer horseID, Integer pnk){
        r1.lock();
        try{ 
            while(raceNum != raceNumber){
                newRace.await();
            }

            groi.setHorsesState("ATS",horseID);
            groi.setHorsesPnk(pnk, horseID); 
            groi.setHorseProbability(-1, horseID);
            groi.setHorseIteration(-1, horseID);
            groi.setHorseTrackPosition(-1, horseID);
            groi.setHorsesStanding('-', horseID); 

            while(!canHorsesMoveToPaddock)
                horsesToPaddock.await();

            if(++numHorses == numberOfHorses){
                numHorses = 0;
                canHorsesMoveToPaddock = false;
                if(raceNumber == numberOfRaces){
                    raceNumber = 0;
                }
            }
            horsesToPaddock.signal();

        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }

    public int getNumberOfHorses(){
        return this.numberOfHorses;
    }
}
