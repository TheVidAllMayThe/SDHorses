package Threads;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.BettingCentre;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.RaceTrack;
import Monitors.Stable;

/**
 * The {@link Broker} class is a thread that contains the lifecycle of the {@link Broker} during the day.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace
 */

public class Broker extends Thread{
    @Override
    public void run(){


        ControlCentreAndWatchingStand.openingTheEvents();

        for(int i = 0; i < Parameters.getNumberOfRaces(); i++){
            BettingCentre.honorBets();
            Stable.summonHorsesToPaddock();
            ControlCentreAndWatchingStand.summonHorsesToPaddock(i);
            BettingCentre.acceptTheBets();
            RaceTrack.startTheRace();
            ControlCentreAndWatchingStand.startTheRace();
            int[] list = RaceTrack.reportResults();
            ControlCentreAndWatchingStand.reportResults(list);
            if(BettingCentre.areThereAnyWinners(list))
                BettingCentre.honorBets();
        }
        
        Stable.entertainTheGuests();
        ControlCentreAndWatchingStand.entertainTheGuests();
    }
}
