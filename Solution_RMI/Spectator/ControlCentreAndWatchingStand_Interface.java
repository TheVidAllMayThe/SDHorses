import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControlCentreAndWatchingStand_Interface extends Remote{
    void waitForNextRace(Integer spectatorID, Double budget) throws RemoteException;
    void goWatchTheRace(Integer spectatorID) throws RemoteException;
    boolean haveIWon(Integer horseID, Integer spectatorID) throws RemoteException;
    void relaxABit(Integer spectatorID) throws RemoteException;
}
