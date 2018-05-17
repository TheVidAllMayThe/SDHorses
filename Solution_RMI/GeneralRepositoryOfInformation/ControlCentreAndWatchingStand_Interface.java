import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControlCentreAndWatchingStand_Interface extends Remote{

    void close() throws RemoteException;
}
