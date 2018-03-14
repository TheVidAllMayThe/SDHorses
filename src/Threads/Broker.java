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
        this.state = "opening the event";
    }

    private void summonHorsesToPaddock(){
        this.state = "announcing next race";
        stable.unlockHorses();
        controlCentre.brokerBlock();
    }

    private void acceptTheBets(){
        this.state = "waiting for bets";

        while(!bettingCentre.betsDone()){
            bettingCentre.unlockSpectator();
            bettingCentre.blockBroker();
        }
    }

    private void startTheRace(){
        this.state = "supervising the race";
        racetrack.unlockHorses();
        racetrack.blockBroker();
    }

    private void reportResults(){
        int[] results = racetrack.getWinners();
        concontrolCentre.writeHorsesThatWon(results);
        controlCentre.unlockSpectator();
    }

    private boolean areThereAnyWinners(){
        return controlCentre.areThereAnyWinners();
    }

    private void honourTheBets(){
        this.state = "settling accounts";
        
        while(!bettingCentre.paidAllSpectators()){
            bettingCentre.blockBroker();
            bettingCentre.unlockSpectators();
        }
    }

    private void entertainTheGuests(){
        this.state = "playing host at the bar";
        stable.unlockHorses();
    }

    @Override
    public void run(){
        
        for(int i=0; i < this.numberOfRaces; i++){
            this.summonHorsesToPaddock(); 
            this.acceptTheBets(); 
            this.startTheRace();
            this.reportResults();
            if(this.areThereAnyWinners()){
                this.honourTheBets();
            }
        }

        this.entertainTheGuests();
    }
}
