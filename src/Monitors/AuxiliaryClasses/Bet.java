package Monitors.AuxiliaryClasses;

public class Bet{
    private int spectatorID;
    private double betAmount;
    private int horseID;
    private double odds;


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
