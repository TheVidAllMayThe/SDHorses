package Monitors.Interfaces;

public interface RaceTrack_Horse {
    int proceedToStartLine(int pID);
    void makeAMove(int horsePos, int moveAmount);
    boolean hasFinishLineBeenCrossed();
}
