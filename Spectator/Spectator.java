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
    private int numberOfRaces;
    private BettingCentre bc;
    private Paddock pd;
    private ControlCentreAndWatchingStand ccws;


    /**
     *
     * @param id ID of the spectator.
     */
    public Spectator(int id, int numberOfRaces, Paddock pd, BettingCentre bc, ControlCentreAndWatchingStand ccws){
        this.budget = ThreadLocalRandom.current().nextDouble(1000);
        this.id  = id;
        this.numberOfRaces = numberOfRaces;
        this.pd = pd;
        this.bc = bc;
        this.ccws = ccws;
    }

    @Override
    public void run(){

        double amountToBet;
        HorseInPaddock horse;

        for(int i = 0; i < numberOfRaces; i++){
            ccws.waitForNextRace(this.id, this.budget);
            horse = pd.goCheckHorses(this.id);
            amountToBet = ThreadLocalRandom.current().nextDouble(0, this.budget);
            bc.placeABet(id, amountToBet, horse.getHorseID(), horse.getOdds(), budget);
            budget = budget - amountToBet;
            ccws.goWatchTheRace(this.id);
            if(ccws.haveIWon(horse.getHorseID(), this.id)){
                budget += bc.goCollectTheGains(id, budget);
            }
        }
        ccws.relaxABit(this.id);
    }
}
