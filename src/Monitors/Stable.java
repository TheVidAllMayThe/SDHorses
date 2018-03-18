package Monitors;

import Monitors.AuxiliaryClasses.Parameters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Stable {

    private static ReentrantLock r1 = new ReentrantLock();
    private static Condition horsesToPaddock = r1.newCondition();
    private static boolean canHorsesMoveToPaddock = false;
    private static int numHorses = 0;

    //Broker methods
    public static void summonHorsesToPaddock(){
        r1.lock();
        try{
            canHorsesMoveToPaddock = true;
            horsesToPaddock.signal();
        }catch (IllegalMonitorStateException e){
            e.printStackTrace();
        }finally {
            r1.unlock();
        }
    }

    public static void entertainTheGuests(){
        r1.lock();
        try{
            canHorsesMoveToPaddock = true;
            horsesToPaddock.signal();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    //Horses methods
    public static void proceedToStable(){
        r1.lock();

        try{
            while(!canHorsesMoveToPaddock)
                horsesToPaddock.await();

            if(++numHorses == Parameters.getNumberOfHorses()){
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
