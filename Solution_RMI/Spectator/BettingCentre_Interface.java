import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BettingCentre_Interface extends Remote{

    void placeABet(int pid, double value, int horseID, double odds, double budget) throws RemoteException;
    double goCollectTheGains(int spectatorID, double budget) throws RemoteException;
}
