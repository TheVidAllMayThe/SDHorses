package Threads;

import Monitors.ControlCentreAndWatchingStand;
import Monitors.Interfaces.*;

import java.util.concurrent.ThreadLocalRandom;

public class Spectator extends Thread{
    private String state;
    private int budget;
    private int pid;
    
    public Spectator(int budget){
        this.budget = budget;
    }

    @Override
    public void run(){
        pid = (int)Thread.currentThread().getID(); 
        int amountToBet = budget/4;
        int horse;
        for(int i=0; i < Parameters.getNumberOfRaces(); i++){
            state = "waiting for a race to start";
            ControlCentreAndWatchingStand.waitForNextRace();

            state = "appraising the horses";
            ControlCentreAndWatchingStand.goCheckHorses();
            Paddock.goCheckHorses();

            //Must decide on amount and horse somehow, for now placeholder a quarter of what he has and bets on random
            state = "placing a bet";
            if(amountToBet > budget) amountToBet = budget;
            horse = ThreadLocalRandom.current().nextInt(0, Parameters.getNumberOfHorses() + 1);
            BettingCenter.placeABet(pid, amountToBet, horse);
            budget -= amountToBet;

            state = "watching a race"
            ControlCentreAndWatchingStand.goWatchTheRace();

            if(ControlCentreAndWatchingStand.haveIWon(horse)){
                state = "collecting the gains";
                budget += BettingCenter.goCollectTheGains();
            }
        }

        state = "celebrating";
        ControlCentreAndWatchingStand.relaxABit();
    }
}
