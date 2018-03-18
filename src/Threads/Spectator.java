package Threads;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.BettingCentre;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.Paddock;

import java.util.concurrent.ThreadLocalRandom;

public class Spectator extends Thread{
    private String state;
    private int budget;
    private int pid;
    
    public Spectator(){
        this.budget = Parameters.getBudget();
    }

    @Override
    public void run(){

        pid = (int)Thread.currentThread().getId();
        int amountToBet = budget/4;
        int horse;

        for(int i = 0; i < Parameters.getNumberOfRaces(); i++){
            state = "waiting for a race to start";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            ControlCentreAndWatchingStand.waitForNextRace();

            state = "appraising the horses";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            Paddock.goCheckHorses();

            //Must decide on amount and horse somehow, for now placeholder a quarter of what he has and bets on random
            state = "placing a bet";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            if(amountToBet > budget) amountToBet = budget;
            horse = ThreadLocalRandom.current().nextInt(0, Parameters.getNumberOfHorses() + 1);
            BettingCentre.placeABet(pid, amountToBet, horse);
            budget -= amountToBet;

            state = "watching a race";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            ControlCentreAndWatchingStand.goWatchTheRace();

            if(ControlCentreAndWatchingStand.haveIWon(horse)){
                state = "collecting the gains";
                System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
                budget += BettingCentre.goCollectTheGains();
            }
        }

        state = "celebrating";
        System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
        ControlCentreAndWatchingStand.relaxABit();
    }
}
