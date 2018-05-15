import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControlCentreAndWatchingStand_Interface extends Remote{
    void openingTheEvents() throws RemoteException;
    void summonHorsesToPaddock(Integer numRace) throws RemoteException;
    void startTheRace() throws RemoteException;
    void reportResults(Integer[] results) throws RemoteException;
    void entertainTheGuests() throws RemoteException;
}
