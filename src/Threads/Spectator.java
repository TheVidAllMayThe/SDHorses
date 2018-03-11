package Threads;

import Monitors.Interfaces.BettingCentre_Broker;

public class Spectator extends Thread{
    private String state;
    private int numberOfRaces;
    private final ControlCentre_Broker controlcentre;
    private final Stable_Broker stable;
    private final BettingCentre_Broker bettingcentre;

    public Spectator(int n, ControlCentre_Broker cc, Stable_Broker s, BettingCentre_Broker bc, RaceTrack_Broker rtb){
        this.numberOfRaces = n;
        this.controlcentre = cc;
        this.stable = s;
        this.bettingcentre = bc;
        this.racetrack = rtb;
    }

    @Override
    public void run(){
        for(int i=0; i < this.numberOfRaces; i++){
            this.state = "waiting for a race to start";
            controlcenter.waitForNextRace();

            controlcentre.goCheckHorses();
            this.state = "appraising the horses";
            padock.goCheckHorses();

            this.state = "placing a bet";
            bettingcentre.placeABet();

            this.state = "watching a race"
            controlcentre.goWatchTheRace();

            if(controlcentre.haveIWon()){
                this.state = "collecting the gains";
                bettingcentre.goCollectTheGains();
            }
        }

        this.state = "celebrating";
        controlcentre.relaxABit();
    }
}
