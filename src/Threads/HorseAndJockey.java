package Threads;

import Monitors.Interfaces.*;

public class HorseAndJockey extends Thread{
    private final RaceTrack_Horse racetrack;
    private String state;
    private int numberOfRaces;
    private final ControlCenterAndWatchingStand_Horse controlcentre;
    private final Stable_Broker stable;
    private final BettingCentre_Broker bettingcentre;

    public HorseAndJockey(int n, ControlCenterAndWatchingStand_Horse cc, Stable_Broker s, BettingCentre_Broker bc, RaceTrack_Horse rtb){
        this.numberOfRaces = n;
        this.controlcentre = cc;
        this.stable = s;
        this.bettingcentre = bc;
        this.racetrack = rtb;
    }

    @Override
    public void run(){
        for(int i=0; i < this.numberOfRaces; i++){
            this.state = "at the stable";
            stable.proceedToStable();

            controlcentre.proceedToPaddock();
            this.state = "at the paddock";
            paddock.proceedToPaddock();

            paddock.proceedToStartLine();
            this.state = "at the start line";
            racetrack.proceedToStartLine();

            this.state = "running";
            while(!racetrack.hasFinishLineBeenCrossed()){
                racetrack.makeAMove();
            }
            // I have my doubts on what to do here
            // Depois de acabar a corrida vai ao control center registar, se for o ultimo acorda o broker
            this.state = "at the finish line";
            controlcentre.makeAMove();
        }

        this.state = "at the stable";
        stable.proceedToStable();
}
