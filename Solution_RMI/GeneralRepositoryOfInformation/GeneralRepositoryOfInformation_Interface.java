import java.net.InetAddress;
import java.rmi.Remote;

public interface GeneralRepositoryOfInformation_Interface extends Remote{
    public void setMonitorAddress(InetAddress address, Integer port, Integer monitor);
    public InetAddress getMonitorAddress(Integer monitor);
    public int getMonitorPort(Integer monitor);
    public void setBrokerState(String state);
    public void setSpectatorsState(String state, Integer i);
    public void setHorsesState(String state, Integer i);
    public void setSpectatorsBudget(Double budget, Integer i);
    public void setRaceNumber(Integer raceNu);
    public void setHorsesPnk(Integer pnk, Integer i);
    public void setRaceDistance(Integer distance);
    public void setSpectatorsSelection(Integer horse, Integer i);
    public void setSpectatorsBet(Double bet, Integer i);
    public void setHorseProbability(Double prob, Integer i);
    public void setHorseIteration(Integer iteration, Integer i);
    public void setHorseTrackPosition(Integer position, Integer i);
    public void setHorsesStanding(Character standing, Integer i);
    public int getNumberOfSpectators();
    public int getNumberOfHorses();
    public int getRaceLength();
    public int getNumberOfRaces();
}
