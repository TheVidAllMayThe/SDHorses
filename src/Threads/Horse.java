package Threads;


import Monitors.AuxiliaryClasses.Parameters;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.Paddock;
import Monitors.RaceTrack;
import Monitors.Stable;

import java.util.concurrent.ThreadLocalRandom;

public class Horse extends Thread {

    private int pnk;
    private int ID;
    private int raceNum;
    private String state;

    public Horse(int id, int raceNum) {
        this.ID = id;
        this.raceNum = raceNum;
        this.pnk = ThreadLocalRandom.current().nextInt(1, Parameters.getRaceLength() + 1);
    }

    @Override
    public void run() {
        Stable.proceedToStable(this.raceNum);
        ControlCentreAndWatchingStand.proceedToPaddock();
        Paddock.proceedToPaddock(ID, pnk);
        Paddock.proceedToStartLine();
        int horsePos = RaceTrack.proceedToStartLine(ID);
        do {
            RaceTrack.makeAMove(horsePos, ThreadLocalRandom.current().nextInt(1, pnk + 1));
        }while(!RaceTrack.hasFinishLineBeenCrossed(horsePos));
        ControlCentreAndWatchingStand.makeAMove();
    }

    public void setState(String state){
        this.state = state;
    }
}
