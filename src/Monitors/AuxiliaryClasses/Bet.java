package Monitors.AuxiliaryClasses;

/**
 * The Bet class is used to facilitate the storage of parameters related with each bet made by a spectator.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Monitors.BettingCentre;
 */

public class Bet{
    private int spectatorID;
    private double betAmount;
    private int horseID;
    private double odds;


    /**
     * Spectator checks if he won his bet.
     *
     * @param   pID ID of the spectator who's making the bet.
     * @param   betAmount Amount of money that the spectator wishes to bet.
     * @param horseID ID of the horse in  which the spectator wishes to bet.
     * @param   odds    double of odd of the horse.
     */
    public Bet(int pID, double betAmount, int horseID, double odds) {
        this.spectatorID = pID;
        this.betAmount = betAmount;
        this.horseID = horseID;
        this.odds = odds;
    }

    /**
     *
     * @return Returns the ID of the Spectator thread that made the bet
     */
    public int getSpectatorID() {
        return spectatorID;
    }

    /**
     *
     * @return Returns the amount of money put on the bet
     */
    public double getBetAmount() {
        return betAmount;
    }

    /**
     *
     * @return Returns the ID of the horse that the spectator chose to bet
     */
    public int getHorseID() {
        return horseID;
    }

    public double getOdds() {
        return odds;
    }
}
