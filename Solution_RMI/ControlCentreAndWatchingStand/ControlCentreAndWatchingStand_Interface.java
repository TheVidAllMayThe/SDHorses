import java.rmi.Remote;

public interface ControlCentreAndWatchingStand_Interface extends Remote{
    public void openingTheEvents();
    public void summonHorsesToPaddock(Integer numRace);
    public void startTheRace();
    public void reportResults(Integer[] list);
    public void entertainTheGuests();
    public void waitForNextRace(Integer spectatorID, Double budget);
    public void goWatchTheRace(Integer spectatorID);
    public boolean haveIWon(Integer horseID, Integer spectatorID);
    public void relaxABit(Integer spectatorID);
    public void proceedToPaddock();
    public void makeAMove();
}
