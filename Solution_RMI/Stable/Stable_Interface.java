import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Stable_Interface extends Remote{
    public void summonHorsesToPaddock() throws RemoteException;
    public void entertainTheGuests() throws RemoteException;
    public void proceedToStable(Integer raceNum, Integer horseID, Integer pnk) throws RemoteException;
}
