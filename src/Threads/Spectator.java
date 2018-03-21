package Threads;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.AuxiliaryClasses.HorseInPaddock;
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
* @see Main.HorseRace
*/

public class Spectator extends Thread{
    private double budget;

    public Spectator(){
        this.budget = ThreadLocalRandom.current().nextDouble(1000);
    }

    @Override
    public void run(){

        int pid = (int) Thread.currentThread().getId();
        double amountToBet;
        HorseInPaddock horse;

        String state;
        for(int i = 0; i < Parameters.getNumberOfRaces(); i++){
            state = "waiting for a race to start";
            print(state);
            ControlCentreAndWatchingStand.waitForNextRace();

            state = "appraising the horses";
            print(state);
            horse = Paddock.goCheckHorses();

            state = "placing a bet";
            print(state);

            amountToBet = ThreadLocalRandom.current().nextDouble(0, this.budget);
            BettingCentre.placeABet(pid, amountToBet, horse.getHorseID(), horse.getOdds());
            budget -= amountToBet;

            state = "watching a race";
            print(state);

            ControlCentreAndWatchingStand.goWatchTheRace();

            if(ControlCentreAndWatchingStand.haveIWon(horse.getHorseID())){
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
