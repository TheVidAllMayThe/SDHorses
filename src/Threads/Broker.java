package Threads;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.BettingCentre;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.Stable;

import java.util.ArrayList;

public class Broker extends Thread{


    private String state;
    @Override
    public void run(){
        state = "opening the event";
        for(int i = 0; i < Parameters.getNumberOfHorses(); i++){

            state = "announcing next race";
            Stable.summonHorsesToPaddock();
            ControlCentreAndWatchingStand.summonHorsesToPaddock();

            state = "waiting for bets";
            BettingCentre.acceptTheBets();

            state = "supervising the race";
            RaceTrack.startTheRace();
            ControlCentreAndWatchingStand.startTheRace();

            state = "supervising the race";
            ArrayList<HorsePos> list = RaceTrack.reportResults();
            ControlCentreAndWatchingStand.reportResults(list);

            Bet[] bets = BettingCentre.areThereAnyWinners();
            if(ControlCentreAndWatchingStand.areThereAnyWinners(bets)){
                state = "settling accounts";
                BettingCentre.honorBets();
            }
        }
        
        this.state = "playing host at the bar";
        Stable.entertainTheGuests();
        ControlCentreAndWatchingStand.entertainTheGuests();
    }
}
