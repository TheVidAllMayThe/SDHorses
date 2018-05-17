import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Paddock_Interface extends Remote{
    void proceedToPaddock(Integer horseID, Integer pnk) throws RemoteException;
    void proceedToStartLine() throws RemoteException;
    HorseInPaddock goCheckHorses(Integer spectatorID) throws RemoteException;
    void close()throws RemoteException;
}
