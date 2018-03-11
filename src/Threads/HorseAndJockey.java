package Threads;


import Monitors.Interfaces.ControlCenterAndWatchingStand_Horse;
import Monitors.Interfaces.Paddock_Horses;
import Monitors.Interfaces.RaceTrack_Horse;
import Monitors.Interfaces.Stable_Horse;
import java.util.concurrent.ThreadLocalRandom;

public class HorseAndJockey extends Thread {
    private final RaceTrack_Horse raceTrack;
    private final Paddock_Horses paddock;
    private int numberOfRaces;
    private final ControlCenterAndWatchingStand_Horse controlCentre;
    private final Stable_Horse stable;
    private final int raceLength;
    private int pnk;
    private int pID;

    public HorseAndJockey(int n, ControlCenterAndWatchingStand_Horse cc, Stable_Horse s, RaceTrack_Horse rtb, Paddock_Horses ph, int raceLength) {
        this.numberOfRaces = n;
        this.controlCentre = cc;
        this.stable = s;
        this.paddock = ph;
        this.raceTrack = rtb;
        this.raceLength = raceLength;
        pnk = ThreadLocalRandom.current().nextInt(1, raceLength/4);

    }

    @Override
    public void run() {
        pID = (int)Thread.currentThread().getId();
        for (int i = 0; i < this.numberOfRaces; i++) {
            //this.state = "at the stable";
            stable.proceedToPaddock();

            //this.state = "at the paddock";
            paddock.proceedToPaddock(pID, pnk);

            paddock.proceedToStartLine();
            //this.state = "at the start line";
            int racePos = raceTrack.proceedToStartLine(pID);

            //this.state = "running";
            int tmp = 0;
            while (tmp == 1){//!raceTrack.hasFinishLineBeenCrossed()) {
                raceTrack.makeAMove(racePos, ThreadLocalRandom.current().nextInt(1, pnk));
            }
            // I have my doubts on what to do here
            // Depois de acabar a corrida vai ao control center registar, se for o ultimo acorda o broker

            controlCentre.reportResults(pID);
        }

        stable.proceedToPaddock();
    }
}
