import java.rmi.Remote;

public interface Stable_Interface extends Remote{
    public void summonHorsesToPaddock();
    public void entertainTheGuests();
    public void proceedToStable(Integer raceNum, Integer horseID, Integer pnk);
}
