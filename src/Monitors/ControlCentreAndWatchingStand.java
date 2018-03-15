package Monitors;


import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Broker;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Spectator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static Monitors.RaceTrack.*;
import static Monitors.RaceTrack.lastHorseFinished;


public class ControlCentreAndWatchingStand implements ControlCenterAndWatchingStand_Broker, ControlCenterAndWatchingStand_Spectator{
    static public ReentrantLock r1= new ReentrantLock(false);
    static public boolean canBrokerLeave = false;
    static public Condition brokerLeave = r1.newCondition();
    static public Condition spectatorWaitingForResult = r1.newCondition();
    public static boolean resultsReported = false;
    static public int[] winnerHorses;



    public int watchRace(){
        r1.lock();
        int returnValue = -1;
        try{
            while(!resultsReported){
                spectatorWaitingForResult.await();
            }
            returnValue = horsesThatWon[0];
        }catch (IllegalMonitorStateException | InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return returnValue;
    }

    static void reportResults() {
        r1.lock();
        try {
            ArrayList<HorsePos> winnerHorsesTmp = new ArrayList<>(Arrays.asList(RaceTrack.horses));
            HorsePos min = Collections.min(winnerHorsesTmp);
            winnerHorsesTmp.remove(min);
            for (HorsePos horse : winnerHorsesTmp)
                if (horse.compareTo(min) > 0)
                    winnerHorsesTmp.remove(horse);

            winnerHorses = new int[winnerHorsesTmp.size()];
            int i = 0;
            for (HorsePos horse : winnerHorsesTmp)
                winnerHorses[i++] = horse.getHorseID();

            resultsReported = true;
            spectatorWaitingForResult.signal();
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
        r1.unlock();
    }

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
