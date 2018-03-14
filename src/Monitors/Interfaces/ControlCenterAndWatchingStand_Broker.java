package Monitors.Interfaces;

import Monitors.AuxiliaryClasses.Bet;
import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.BettingCentre;
import Monitors.RaceTrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static Monitors.ControlCentreAndWatchingStand.*;

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
}
