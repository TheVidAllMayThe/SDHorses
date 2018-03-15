package Monitors;

import Monitors.AuxiliaryClasses.Parameters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Stable {

    public static ReentrantLock r1 = new ReentrantLock();
    public static Condition horsesToPaddock = r1.newCondition();
    public static Condition lastSpectatorInPaddock = r1.newCondition();
    public static boolean canHorsesMoveToPaddock = false;
    public static int numHorses = 0;
    public static boolean isLastSpectatorInPaddock = false;


    static void summonHorsesToPaddock(){
        r1.lock();
        try{
            canHorsesMoveToPaddock = true;
            horsesToPaddock.signal();
            while(!isLastSpectatorInPaddock)
                lastSpectatorInPaddock.await();
            isLastSpectatorInPaddock = false;
        }catch (IllegalMonitorStateException | InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
    }
}
