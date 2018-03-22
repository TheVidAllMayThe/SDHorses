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
    private String state;

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
            ControlCentreAndWatchingStand.waitForNextRace();

            horse = Paddock.goCheckHorses();

            amountToBet = ThreadLocalRandom.current().nextDouble(0, this.budget);
            BettingCentre.placeABet(pid, amountToBet, horse.getHorseID(), horse.getOdds());
            budget -= amountToBet;

            ControlCentreAndWatchingStand.goWatchTheRace();

            if(ControlCentreAndWatchingStand.haveIWon(horse.getHorseID())){
                budget += BettingCentre.goCollectTheGains(pid);
            }
        }

        ControlCentreAndWatchingStand.relaxABit();
    }

    public void setState(String state){
        this.state = state;
    }
}
