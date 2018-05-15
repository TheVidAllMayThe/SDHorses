import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RaceTrack_Interface extends Remote{
    public void startTheRace() throws RemoteException;
    public Integer[] reportResults() throws RemoteException;
    public int proceedToStartLine(Integer pID) throws RemoteException;
    public void makeAMove(Integer horsePos, Integer moveAmount, Integer horseID) throws RemoteException;
    public boolean hasFinishLineBeenCrossed(Integer horsePos, Integer horseID) throws RemoteException;
}
