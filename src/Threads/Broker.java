package Threads;

import Monitors.Interfaces.BettingCentre_Broker;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Broker;
import Monitors.Interfaces.RaceTrack_Broker;
import Monitors.Interfaces.Stable_Broker;

public class Broker extends Thread{
    private final int numberOfRaces;
    private String state;
    private int winners[];

    public Broker(int numberOfRaces){
        this.numberOfRaces = numberOfRaces;
        this.state = "opening the event";
    }

    @Override
    public void run(){
        
        for(int i=0; i < this.numberOfRaces; i++){
            StableBroker.summonHorsesToPaddock();
            this.state = "announcing next race";

            ControlCentreAndWatchingStand_Broker.acceptTheBets();
            this.state = "waiting for bets";
            BettingCentre_Broker.acceptTheBets();
            
            int[] results =  RaceTrack_Broker.startTheRace();
            this.state = "supervising the race";

            ControlCentreAndWatchingStand_Broker.reportResults(results);
            ControlCentreAndWatchingStand_Broker.areThereAnyWinners();
            this.state = "settling accounts";
            BettingCentre_Broker.honorBet();
        }

        this.state = "playing host at the bar";
        //controlCentre.entertainTheGuests();
    }
}
