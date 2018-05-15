import java.io.Serializable;

/**
 * The {@link HorseInPaddock} class holds all the necessary about a Horse that is/has been in the {@link Paddock}.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 */


public class HorseInPaddock implements Serializable{
    private int horseID;
    private int pnk;
    private double odds;

    /**
     *
     * @param horseID ID of the Thread calling the method.
     * @param pnk Maximum length of the move of a Horse.
     */
    HorseInPaddock(int horseID, int pnk) {
        this.horseID = horseID;
        this.pnk = pnk;
    }

    public int getHorseID(){
        return this.horseID;
    }

    public int getPnk(){
        return this.pnk;
    }

    public double getOdds(){
        return this.odds;
    }
    
    public void setOdds(double odds){
        this.odds = odds;
    }
}
