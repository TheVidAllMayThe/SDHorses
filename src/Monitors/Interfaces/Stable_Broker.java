package Monitors.Interfaces;

import static Monitors.Stable.*;

public interface Stable_Broker {
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
