import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RaceTrack_Interface extends Remote{
    int proceedToStartLine(int pID) throws RemoteException;
    void makeAMove(int horsePos, int moveAmount, int horseID) throws RemoteException;
    boolean hasFinishLineBeenCrossed(int horsePos, int horseID) throws RemoteException;
}
