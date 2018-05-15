import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Stable_Interface extends Remote{
    void proceedToStable(Integer raceNum, Integer horseID, Integer pnk) throws RemoteException;
}
