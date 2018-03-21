package Threads;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.BettingCentre;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.Paddock;

import java.util.concurrent.ThreadLocalRandom;

/**
* The Spectator class is a thread that contains the lifecycle of the Spectator during the day.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see HorseRace
*/

public class Spectator extends Thread{
    private String state;
    private int budget;
    private int pid;

    public Spectator(){
        this.budget = ThreadLocalRandom.current().nextInt(1000);
    }

    @Override
    public void run(){

        pid = (int)Thread.currentThread().getId();
        int amountToBet;
        int horse;

        for(int i = 0; i < Parameters.getNumberOfRaces(); i++){
            state = "waiting for a race to start";
            print(state);
            ControlCentreAndWatchingStand.waitForNextRace();

            state = "appraising the horses";
            print(state);
            int[] horses = Paddock.goCheckHorses();

            state = "placing a bet";
            print(state);

            amountToBet = ThreadLocalRandom.current().nextInt(0, this.budget+1);
            horse = horses[ThreadLocalRandom.current().nextInt(0, Parameters.getNumberOfHorses())];
            BettingCentre.placeABet(pid, amountToBet, horse);
            budget -= amountToBet;

            state = "watching a race";
            print(state);

            ControlCentreAndWatchingStand.goWatchTheRace();

            if(ControlCentreAndWatchingStand.haveIWon(horse)){
                state = "collecting the gains";
                print(state);
                budget += BettingCentre.goCollectTheGains(pid);
            }
        }

        state = "celebrating";
        print(state);
        ControlCentreAndWatchingStand.relaxABit();
    }
    
    private void print(String state){
        System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
    }
}
