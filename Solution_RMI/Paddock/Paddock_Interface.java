import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Paddock_Interface extends Remote{
    public void proceedToPaddock(Integer horseID, Integer pnk) throws RemoteException;
    public void proceedToStartLine() throws RemoteException;
    public HorseInPaddock goCheckHorses(Integer spectatorID) throws RemoteException;
}
