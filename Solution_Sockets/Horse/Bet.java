 /** The {@link Bet} class holds all the information regarding a {@link Bet}.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 */

public class Bet{
    private int spectatorID;
    private double betAmount;
    private int horseID;
    private double odds;


    /**
     * Spectator checks if he won his bet.
     *
     * @param   pID ID of the Spectator who's making the bet.
     * @param   betAmount Amount of money that the Spectator wishes to bet.
     * @param horseID ID of the Horse in  which the Spectator wishes to bet.
     * @param   odds  Odds of the bet.
     */
    public Bet(int pID, double betAmount, int horseID, double odds) {
        this.spectatorID = pID;
        this.betAmount = betAmount;
        this.horseID = horseID;
        this.odds = odds;
    }


    public int getSpectatorID() {
        return spectatorID;
    }


    public double getBetAmount() {
        return betAmount;
    }


    public int getHorseID() {
        return horseID;
    }


    public double getOdds() {
        return odds;
    }
}
