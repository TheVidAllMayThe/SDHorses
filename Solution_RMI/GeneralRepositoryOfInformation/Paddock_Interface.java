import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Paddock_Interface extends Remote{

    void close() throws RemoteException;
}
