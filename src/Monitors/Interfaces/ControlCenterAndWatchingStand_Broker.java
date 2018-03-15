package Monitors.Interfaces;

import Monitors.AuxiliaryClasses.Bet;
import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.BettingCentre;
import Monitors.RaceTrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static Monitors.ControlCentreAndWatchingStand.r1;
import static Monitors.ControlCentreAndWatchingStand.winnerHorses;
import static Monitors.ControlCentreAndWatchingStand.resultsReported;
import static Monitors.ControlCentreAndWatchingStand.canBrokerLeave;
import static Monitors.ControlCentreAndWatchingStand.spectatorWaitingForResult;

import static Monitors.RaceTrack.lastHorseFinished;
import static Monitors.RaceTrack.raceStarted;
import static Monitors.RaceTrack.resultsForBroker;
import static Monitors.RaceTrack.canRace;

public interface ControlCenterAndWatchingStand_Broker {
    void summonHorsesToPaddock();

    static public boolean areThereAnyWinners() {
        r1.lock();
        boolean returnValue = false;
        try {
            for (Bet bet : BettingCentre.bets)
                if (Arrays.asList(winnerHorses).contains(bet.getHorseID())) {
                    returnValue = true;
                    break;
                }

            canBrokerLeave = false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
