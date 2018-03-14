package Monitors.Interfaces;

import static Monitors.RaceTrack.lastHorseFinished;
import static Monitors.RaceTrack.resultsForBroker;
import static Monitors.RaceTrack.canRace;
import static Monitors.RaceTrack.raceStarted;
import static Monitors.RaceTrack.r1;

public interface RaceTrack_Broker {
    static void startTheRace(){
        r1.lock();

        try{
            canRace = true;
            raceStarted.signal();
            while(!lastHorseFinished)
                resultsForBroker.await();
            lastHorseFinished = false;
        }catch(IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally{
            r1.unlock();
        }

    }
}
