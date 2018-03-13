package Threads;

import Monitors.Interfaces.BettingCentre_Broker;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Broker;
import Monitors.Interfaces.RaceTrack_Broker;
import Monitors.Interfaces.Stable_Broker;

public class Broker extends Thread{
    private final int numberOfRaces;
    private final RaceTrack_Broker racetrack;
    private String state;
    private int winners[];
    private final ControlCenterAndWatchingStand_Broker controlCentre;
    private final Stable_Broker stable;
    private final BettingCentre_Broker bettingCentre;

    public Broker(int numberOfRaces, ControlCenterAndWatchingStand_Broker cc, Stable_Broker s, BettingCentre_Broker bc, RaceTrack_Broker rtb){
        this.numberOfRaces = numberOfRaces;
        this.controlCentre = cc;
        this.stable = s;
        this.bettingCentre = bc;
        this.racetrack = rtb;
    }

    @Override
    public void run(){
        this.state = "opening the event";
        
        for(int i=0; i < this.numberOfRaces; i++){
            stable.summonHorsesToPaddock();
            this.state = "announcing next race";
            controlCentre.summonHorsesToPaddock();
            
            this.state = "waiting for bets";
            bettingCentre.acceptTheBets();
            
            racetrack.startTheRace();
            this.state = "supervising the race";

            controlCentre.reportResults();
            controlCentre.areThereAnyWinners();
            this.state = "settling accounts";
            bettingCentre.honorBet();

        }

        this.state = "playing host at the bar";
        //controlCentre.entertainTheGuests();
    }
}
