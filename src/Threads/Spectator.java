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
    private final int id;
    private double budget;
    private String state;

    public Spectator(int id){
        this.budget = ThreadLocalRandom.current().nextDouble(1000);
        this.id  = id;
    }

    @Override
    public void run(){

        int pid = (int) Thread.currentThread().getId();
        double amountToBet;
        HorseInPaddock horse;

        for(int i = 0; i < Parameters.getNumberOfRaces(); i++){
            ControlCentreAndWatchingStand.waitForNextRace();
            horse = Paddock.goCheckHorses();
            amountToBet = ThreadLocalRandom.current().nextDouble(0, this.budget);
            BettingCentre.placeABet(pid, amountToBet, horse.getHorseID(), horse.getOdds());
            ControlCentreAndWatchingStand.goWatchTheRace();
            if(ControlCentreAndWatchingStand.haveIWon(horse.getHorseID())){
                BettingCentre.goCollectTheGains(pid);
            }
        }
        ControlCentreAndWatchingStand.relaxABit();
    }

    public int getID(){
        return this.id;
    }

    public double getBudget(){
        return this.budget;
    }

    public void setState(String state){
        this.state = state;
    }

    public void setBudget(double value){
        this.budget = value;
    }
}
