package Monitors.Interfaces;

public interface BettingCentre_Spectator {
    void placeBet(int procID, int betAmount);
    int collectGains(int procID);
}
