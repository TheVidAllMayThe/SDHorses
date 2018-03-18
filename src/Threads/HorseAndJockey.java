package Threads;


import Monitors.AuxiliaryClasses.Parameters;
import Monitors.Interfaces.RaceTrack_Horse;
import Monitors.Paddock;
import Monitors.RaceTrack;
import Monitors.Stable;

import java.util.concurrent.ThreadLocalRandom;

public class HorseAndJockey extends Thread {

    private int pnk;
    private int pID;
    private String state;

    public HorseAndJockey() {
        pnk = ThreadLocalRandom.current().nextInt(1, Parameters.getRaceLenght()/4);
    }

    @Override
    public void run() {
        pID = (int)Thread.currentThread().getId();
        for (int i = 0; i < Parameters.getNumberOfRaces(); i++) {
            state = "at the stable";
            Stable.proceedToStable();

            state = "at the paddock";
            ControlCentreAndWatchingStand.proceedToPaddock();
            Paddock.proceedToPaddock(pID, pnk);

            state = "at the start line";
            Paddock.proceedToStartLine();
            int horsePos = RaceTrack.proceedToStartLine(pID);

            state = "running";
            boolean[] finishLineCrossed;
            do {
                RaceTrack.makeAMove(horsePos, ThreadLocalRandom.current().nextInt(1, pnk));
                finishLineCrossed = RaceTrack.HasFinishLineBeenCrossed(horsePos);
            }while(finishLineCrossed[0]); 
            if (finishLineCrossed[1]) ControlCentreAndWatchingStand.makeAMove();

            state = "at the finish line";
        }

        state = "at the stable";
        Stable.proceedToStable();

        state = "at the paddock";
        Paddock.proceedToPaddock(pID,pnk);
    }
}
