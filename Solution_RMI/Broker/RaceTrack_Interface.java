import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RaceTrack_Interface extends Remote{
    void startTheRace() throws RemoteException;
    Integer[] reportResults() throws RemoteException;
}
