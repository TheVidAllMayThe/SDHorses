package Monitors;

import Monitors.AuxiliaryClasses.Parameters;
import Threads.Horse;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
* The Stable class is a monitor that contains all the
* necessary methods to be used in mutual exclusive access by Broker and Horses to itself.
* <p>
* This is where the horses are intially or after a race.
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
     * Broker awakes the horses who are waiting to enter the paddock.
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
     * Last function of broker lifecycle, awakes horses waiting to enter paddock.
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
     * Horses wait to move to paddock. 
     */
    public static void proceedToStable(int raceNum){
        r1.lock();

        try{
            while(raceNum != raceNumber){
                newRace.await();
            }
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
