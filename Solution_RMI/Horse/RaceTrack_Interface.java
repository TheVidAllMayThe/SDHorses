import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RaceTrack_Interface extends Remote{
    int proceedToStartLine(Integer pID) throws RemoteException;
    void makeAMove(Integer horsePos, Integer moveAmount, Integer horseID) throws RemoteException;
    boolean hasFinishLineBeenCrossed(Integer horsePos, Integer horseID) throws RemoteException;
}
