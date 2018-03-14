package Threads;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.BettingCentre;
import Monitors.Interfaces.BettingCentre_Broker;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Broker;
import Monitors.Interfaces.RaceTrack_Broker;
import Monitors.Interfaces.Stable_Broker;
import Monitors.RaceTrack;

public class Broker extends Thread{


    private String state;

    @Override
    public void run(){

        for(int i = 0; i < Parameters.getNumberOfHorses(); i++){
            state = "announcing next race";
            Stable_Broker.summonHorsesToPaddock();

            state = "waiting for bets";
            BettingCentre_Broker.acceptTheBets();

            state = "supervising the race";
            RaceTrack_Broker.startTheRace();

            state = "supervising the race";
            ControlCenterAndWatchingStand_Broker.reportResults();
            Boolean anyWinners = ControlCenterAndWatchingStand_Broker.areThereAnyWinners();

            if(anyWinners){
                state = "settling accounts";
                BettingCentre_Broker.honorBets();
            }

        }

        this.state = "playing host at the bar";

    }
}
