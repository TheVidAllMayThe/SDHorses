import java.rmi.Remote;

public interface RaceTrack_Interface extends Remote{
    public void startTheRace();
    public Integer[] reportResults();
    public int proceedToStartLine(Integer pID);
    public void makeAMove(Integer horsePos, Integer moveAmount, Integer horseID);
    public boolean hasFinishLineBeenCrossed(Integer horsePos, Integer horseID);
}
