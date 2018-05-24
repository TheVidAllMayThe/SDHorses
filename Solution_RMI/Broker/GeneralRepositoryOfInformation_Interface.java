
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GeneralRepositoryOfInformation_Interface extends Remote{
    public void setMonitorAddress(InetAddress address, Integer port, Integer monitor) throws RemoteException;
    public InetAddress getMonitorAddress(Integer monitor) throws RemoteException;
    public int getMonitorPort(Integer monitor) throws RemoteException;
    public void setBrokerState(String state) throws RemoteException;
    public void setSpectatorsState(String state, Integer i) throws RemoteException;
    public void setHorsesState(String state, Integer i) throws RemoteException;
    public void setSpectatorsBudget(Double budget, Integer i) throws RemoteException;
    public void setRaceNumber(Integer raceNu) throws RemoteException;
    public void setHorsesPnk(Integer pnk, Integer i) throws RemoteException;
    public void setRaceDistance(Integer distance) throws RemoteException;
    public void setSpectatorsSelection(Integer horse, Integer i) throws RemoteException;
    public void setSpectatorsBet(Double bet, Integer i) throws RemoteException;
    public void setHorseProbability(Double prob, Integer i) throws RemoteException;
    public void setHorseIteration(Integer iteration, Integer i) throws RemoteException;
    public void setHorseTrackPosition(Integer position, Integer i) throws RemoteException;
    public void setHorsesStanding(Character standing, Integer i) throws RemoteException;
    public int getNumberOfSpectators() throws RemoteException;
    public int getNumberOfHorses() throws RemoteException;
    public int getRaceLength() throws RemoteException;
    public int getNumberOfRaces() throws RemoteException;
}
