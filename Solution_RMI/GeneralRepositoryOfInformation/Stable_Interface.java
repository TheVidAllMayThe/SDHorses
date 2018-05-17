import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Stable_Interface extends Remote{

    void close() throws RemoteException;
}
