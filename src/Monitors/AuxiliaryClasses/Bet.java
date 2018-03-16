package Monitors.AuxiliaryClasses;

public class Bet{
    private int spectatorID;
    private int betAmount;
    private int horseID:


    public Bet(int pID, int betAmount, int horseID) {
        this.spectatorID = pID;
        this.betAmount = betAmount;
        this.horseID = horseID;
    }

    public int getSpectatorID() {
        return spectatorID;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public int getHorseID() {
        return horseID;
    }
}
