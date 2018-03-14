package Monitors.Interfaces;

public interface ControlCenterAndWatchingStand_Broker {
    void summonHorsesToPaddock();
    int[] areThereAnyWinners();
    void reportResults(int[] winners);
}
