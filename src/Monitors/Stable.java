package Monitors;

import Monitors.AuxiliaryClasses.Parameters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Stable {
    
    public static ReentrantLock r1 = new ReentrantLock();

    public static Condition horsesToPaddock = r1.newCondition();
    public static boolean canHorsesMoveToPaddock = false;

    public static Condition lastSpectatorInPaddock = r1.newCondition();
    public static boolean isLastSpectatorInPaddock = false;

    public static int numHorses = 0;

    //Broker methods
    public static void summonHorsesToPaddock(){
        r1.lock();
        try{
            canHorsesMoveToPaddock = true;
            horsesToPaddock.signal();
            
            while(!isLastSpectatorInPaddock){
                lastSpectatorInPaddock.await();
            }
            
            isLastSpectatorInPaddock = false;
        }catch (IllegalMonitorStateException | InterruptedException e){
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
        
        }finally{
            r1.unlock();
        }
    }

    //Horses methods
    public static void proceedToStable(){
        r1.lock();
        numHorses++;
        try{
            while(!canHorsesMoveToPaddock)
                horsesToPaddock.wait();

            if(numHorses == Parameters.getNumberOfHorses()){
                numHorses = 0;
                canHorsesMoveToPaddock = false;
            }
        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }
}
