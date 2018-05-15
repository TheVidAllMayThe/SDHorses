import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RaceTrack_Interface extends Remote{
    void startTheRace() throws RemoteException;
    Integer[] reportResults() throws RemoteException;
    int proceedToStartLine(Integer pID) throws RemoteException;
    void makeAMove(Integer horsePos, Integer moveAmount, Integer horseID) throws RemoteException;
    boolean hasFinishLineBeenCrossed(Integer horsePos, Integer horseID) throws RemoteException;
}
