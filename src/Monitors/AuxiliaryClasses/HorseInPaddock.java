package Monitors.AuxiliaryClasses;

public class HorseInPaddock {
    private int horseID;
    private int pnk;
    private double odds;

    public HorseInPaddock(int horseID, int pnk) {
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
