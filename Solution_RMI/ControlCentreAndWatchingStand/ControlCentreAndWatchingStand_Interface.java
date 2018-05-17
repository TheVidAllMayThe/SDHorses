import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControlCentreAndWatchingStand_Interface extends Remote{
    void openingTheEvents() throws RemoteException;
    void summonHorsesToPaddock(Integer numRace) throws RemoteException;
    void startTheRace() throws RemoteException;
    void reportResults(Integer[] list) throws RemoteException;
    void entertainTheGuests() throws RemoteException;
    void waitForNextRace(Integer spectatorID, Double budget) throws RemoteException;
    void goWatchTheRace(Integer spectatorID) throws RemoteException;
    boolean haveIWon(Integer horseID, Integer spectatorID) throws RemoteException;
    void relaxABit(Integer spectatorID) throws RemoteException;
    void proceedToPaddock() throws RemoteException;
    void makeAMove() throws RemoteException;
    void close() throws RemoteException;
}
