import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BettingCentre_Interface extends Remote {
    void acceptTheBets() throws RemoteException;
    boolean areThereAnyWinners(Integer[] winnerList) throws RemoteException;
    void honorBets() throws RemoteException;

}
