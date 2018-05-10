import java.rmi.Remote;

public interface BettingCentre_Interface extends Remote{
    void acceptTheBets();
    boolean areThereAnyWinners(Integer[] winnerList);
    void honorBets();
    void placeABet(Integer pid, Double value, Integer horseID, Double odds, Double budget);
    void goCollectTheGains(Integer spectatorID, Double budget);
}
