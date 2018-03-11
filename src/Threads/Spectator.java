package Threads;

import Monitors.ControlCentreAndWatchingStand;
import Monitors.Interfaces.*;

public class Spectator extends Thread{
    private final Paddock_Spectators paddock;
    private int numberOfRaces;
    private final ControlCenterAndWatchingStand_Spectator controlCenter;
    private final BettingCentre_Spectator bettingCenter;

    public Spectator(int numberOfRaces, ControlCenterAndWatchingStand_Spectator controlCenter, BettingCentre_Spectator bettingCenter, Paddock_Spectators paddock){
        this.numberOfRaces = numberOfRaces;
        this.controlCenter = controlCenter;
        this.bettingCenter = bettingCenter;
        this.paddock = paddock;
    }

    @Override
    public void run(){
        for(int i=0; i < this.numberOfRaces; i++){
            this.state = "waiting for a race to start";
            controlcenter.waitForNextRace();

            controlCenter.goCheckHorses();
            this.state = "appraising the horses";
            padock.goCheckHorses();

            this.state = "placing a bet";
            bettingCenter.placeABet();

            this.state = "watching a race"
            controlCenter.goWatchTheRace();

            if(controlCenter.haveIWon()){
                this.state = "collecting the gains";
                bettingCenter.goCollectTheGains();
            }
        }

        this.state = "celebrating";
        controlCenter.relaxABit();
    }
}
