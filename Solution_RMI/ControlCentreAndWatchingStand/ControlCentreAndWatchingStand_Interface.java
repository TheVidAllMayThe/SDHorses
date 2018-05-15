import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControlCentreAndWatchingStand_Interface extends Remote{
    public void openingTheEvents() throws RemoteException;
    public void summonHorsesToPaddock(Integer numRace) throws RemoteException;
    public void startTheRace() throws RemoteException;
    public void reportResults(Integer[] list) throws RemoteException;
    public void entertainTheGuests() throws RemoteException;
    public void waitForNextRace(Integer spectatorID, Double budget) throws RemoteException;
    public void goWatchTheRace(Integer spectatorID) throws RemoteException;
    public boolean haveIWon(Integer horseID, Integer spectatorID) throws RemoteException;
    public void relaxABit(Integer spectatorID) throws RemoteException;
    public void proceedToPaddock() throws RemoteException;
    public void makeAMove() throws RemoteException;
}
