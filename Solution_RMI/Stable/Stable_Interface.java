import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Stable_Interface extends Remote{
    void summonHorsesToPaddock() throws RemoteException;
    void entertainTheGuests() throws RemoteException;
    void proceedToStable(Integer raceNum, Integer horseID, Integer pnk) throws RemoteException;
    void close()throws RemoteException;
}
