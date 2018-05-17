import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BettingCentre_Interface extends Remote{

    void close() throws RemoteException;
}
