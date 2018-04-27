package Threads;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.AuxiliaryClasses.HorseInPaddock;
import Monitors.BettingCentre;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.Paddock;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@link Spectator} class is a thread that contains the lifecycle of the {@link Spectator} during the day.
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


    /**
     *
     * @param id ID of the spectator.
     */
    public Spectator(int id){
        this.budget = ThreadLocalRandom.current().nextDouble(1000);
        this.id  = id;
    }

    @Override
    public void run(){

        double amountToBet;
        HorseInPaddock horse;

        for(int i = 0; i < Parameters.getNumberOfRaces(); i++){
            ControlCentreAndWatchingStand.waitForNextRace(this.id, this.budget);
            horse = Paddock.goCheckHorses(this.id);
            amountToBet = ThreadLocalRandom.current().nextDouble(0, this.budget);
            BettingCentre.placeABet(id, amountToBet, horse.getHorseID(), horse.getOdds(), budget);
            budget = budget - amountToBet;
            ControlCentreAndWatchingStand.goWatchTheRace(this.id);
            if(ControlCentreAndWatchingStand.haveIWon(horse.getHorseID(), this.id)){
                budget += BettingCentre.goCollectTheGains(id, budget);
            }
        }
        ControlCentreAndWatchingStand.relaxABit(this.id);
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
