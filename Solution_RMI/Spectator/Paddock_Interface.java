import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Paddock_Interface extends Remote{
    HorseInPaddock goCheckHorses(Integer spectatorID) throws RemoteException;
}
