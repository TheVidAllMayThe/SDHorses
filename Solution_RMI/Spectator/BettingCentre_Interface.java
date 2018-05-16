import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BettingCentre_Interface extends Remote{
    void placeABet(Integer pid, Double value, Integer horseID, Double odds, Double budget) throws RemoteException;
    double goCollectTheGains(Integer spectatorID, Double budget) throws RemoteException;
}
