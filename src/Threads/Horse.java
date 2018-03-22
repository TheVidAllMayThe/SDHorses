package Threads;


import Monitors.AuxiliaryClasses.Parameters;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.Paddock;
import Monitors.RaceTrack;
import Monitors.Stable;

import java.util.concurrent.ThreadLocalRandom;

public class Horse extends Thread {

    private int pnk;
    private int pID;
    private int raceNum;

    public Horse(int id, int raceNum) {
        this.pID = id;
        this.raceNum = raceNum;
        this.pnk = ThreadLocalRandom.current().nextInt(1, Parameters.getRaceLength() + 1);
    }

    @Override
    public void run() {
        String state;
        state = "at the stable";
        print(state);
        Stable.proceedToStable(this.raceNum);

        state = "at the paddock";
        print(state);
        ControlCentreAndWatchingStand.proceedToPaddock();
        Paddock.proceedToPaddock(pID, pnk);
        Paddock.proceedToStartLine();
        state = "at the start line";
        print(state);
        int horsePos = RaceTrack.proceedToStartLine(pID);

        state = "running";
        print(state);
        do {
            RaceTrack.makeAMove(horsePos, ThreadLocalRandom.current().nextInt(1, pnk + 1));
        }while(!RaceTrack.hasFinishLineBeenCrossed(horsePos));
        ControlCentreAndWatchingStand.makeAMove();

        state = "at the finish line";
        print(state);

        state = "at the stable";
        print(state);
        Stable.proceedToStable(Parameters.getNumberOfRaces());
    }

    private void print(String state){
        System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
    }
}
