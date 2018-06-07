import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The ControlCentreAndWatchingStand class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by the Broker, Spectators and Horses.
 * <p>
 * This is where the Broker mostly operates and the Spectators watch the race.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21

 */

public interface GeneralRepositoryOfInformation_Interface extends Remote{
    void setMonitorAddress(InetAddress address, Integer port, Integer monitor) throws RemoteException;
    void setBrokerState(String state) throws RemoteException;
    void setSpectatorsState(String state, Integer i) throws RemoteException;
    void setSpectatorsBudget(Double budget, Integer i) throws RemoteException;
    void setSpectatorsBet(Double bet, Integer i) throws RemoteException;
    int getNumberOfSpectators() throws RemoteException;
}
