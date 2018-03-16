package Threads;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.BettingCentre;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.Stable;

public class Broker extends Thread{


    private String state;
    @Override
    public void run(){
        state = "opening the event";
        for(int i = 0; i < Parameters.getNumberOfHorses(); i++){

            state = "announcing next race";
            Stable.summonHorsesToPaddock();

            state = "waiting for bets";
            BettingCentre.acceptTheBets();

            state = "supervising the race";
            ControlCentreAndWatchingStand.startTheRace();

            state = "supervising the race";
            ControlCentreAndWatchingStand.reportResults();

            if(ControlCentreAndWatchingStand.areThereAnyWinners()){
                state = "settling accounts";
                BettingCentre.honorBets();
            }
        }
        
        this.state = "playing host at the bar";
        ControlCentreAndWatchingStand.entertainTheGuests();
    }
}
