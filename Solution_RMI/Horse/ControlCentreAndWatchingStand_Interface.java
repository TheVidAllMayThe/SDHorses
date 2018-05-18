import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControlCentreAndWatchingStand_Interface extends Remote{
    void proceedToPaddock()throws RemoteException;
    void makeAMove(int hID)throws RemoteException;
}
