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


}
