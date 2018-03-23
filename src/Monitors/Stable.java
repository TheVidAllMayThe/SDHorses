package Monitors;
import Monitors.AuxiliaryClasses.Parameters;
import Threads.Horse;
import Threads.Broker;

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

    private static ReentrantLock r1 = new ReentrantLock();
    private static Condition horsesToPaddock = r1.newCondition();
    private static boolean canHorsesMoveToPaddock = false;
    private static Condition newRace = r1.newCondition();
    private static int numHorses = 0;
    private static int raceNumber = -1;

    /**
     * {@link Broker} awakes the {@link Horse}s who are waiting to enter the {@link Paddock}.
     */
    public static void summonHorsesToPaddock(){
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
    public static void entertainTheGuests(){
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

    public static void proceedToStable(int raceNum){
        r1.lock();
        try{ 
            while(raceNum != raceNumber){
                newRace.await();
            }

            Horse hInst = (Horse)Thread.currentThread();
            hInst.setState("AT_THE_STABLE");
            GeneralRepositoryOfInformation.setHorsesState("ATS",hInst.getID());
            hInst.setPnk(ThreadLocalRandom.current().nextInt(1,Parameters.getRaceLength() + 1));
            GeneralRepositoryOfInformation.setHorsesPnk(hInst.getPnk(), hInst.getID()); 
            GeneralRepositoryOfInformation.setHorseProbability(-1, hInst.getID());
            GeneralRepositoryOfInformation.setHorseIteration(-1, hInst.getID());
            GeneralRepositoryOfInformation.setHorseTrackPosition(-1, hInst.getID());
            GeneralRepositoryOfInformation.setHorsesStanding('-', hInst.getID()); 

            while(!canHorsesMoveToPaddock)
                horsesToPaddock.await();

            if(raceNumber == Parameters.getNumberOfRaces()){
                if(++numHorses == Parameters.getNumberOfHorses()*raceNumber){
                    numHorses = 0;
                    canHorsesMoveToPaddock = false;
                }
            }
            else if(++numHorses == Parameters.getNumberOfHorses()){
                numHorses = 0;
                canHorsesMoveToPaddock = false;
            }
            horsesToPaddock.signal();

        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }
}
