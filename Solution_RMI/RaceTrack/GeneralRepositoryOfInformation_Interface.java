import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GeneralRepositoryOfInformation_Interface extends Remote{
    void setMonitorAddress(InetAddress address, Integer port, Integer monitor) throws RemoteException;
    InetAddress getMonitorAddress(Integer monitor) throws RemoteException;
    int getMonitorPort(Integer monitor) throws RemoteException;
    void setBrokerState(String state) throws RemoteException;
    void setSpectatorsState(String state, Integer i) throws RemoteException;
    void setHorsesState(String state, Integer i) throws RemoteException;
    void setSpectatorsBudget(Double budget, Integer i) throws RemoteException;
    void setRaceNumber(Integer raceNu) throws RemoteException;
    void setHorsesPnk(Integer pnk, Integer i) throws RemoteException;
    void setRaceDistance(Integer distance) throws RemoteException;
    void setSpectatorsSelection(Integer horse, Integer i) throws RemoteException;
    void setSpectatorsBet(Double bet, Integer i) throws RemoteException;
    void setHorseProbability(Double prob, Integer i) throws RemoteException;
    void setHorseIteration(Integer iteration, Integer i) throws RemoteException;
    void setHorseTrackPosition(Integer position, Integer i) throws RemoteException;
    void setHorsesStanding(Character standing, Integer i) throws RemoteException;
    int getNumberOfSpectators() throws RemoteException;
    int getNumberOfHorses() throws RemoteException;
    int getRaceLength() throws RemoteException;
    int getNumberOfRaces() throws RemoteException;
}
