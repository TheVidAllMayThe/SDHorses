package Threads;


import Monitors.AuxiliaryClasses.Parameters;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.Paddock;
import Monitors.RaceTrack;
import Monitors.Stable;

import java.util.concurrent.ThreadLocalRandom;

public class HorseAndJockey extends Thread {

    private int pnk;
    private int pID;
    private String state;

    public HorseAndJockey() {
        pnk = ThreadLocalRandom.current().nextInt(1, Parameters.getRaceLength()/4);
    }

    @Override
    public void run() {
        pID = (int)Thread.currentThread().getId();
        for (int i = 0; i < Parameters.getNumberOfRaces(); i++) {
            state = "at the stable";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            Stable.proceedToStable();

            state = "at the paddock";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            ControlCentreAndWatchingStand.proceedToPaddock();
            Paddock.proceedToPaddock(pID, pnk);

            state = "at the start line";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            Paddock.proceedToStartLine();
            int horsePos = RaceTrack.proceedToStartLine(pID);

            state = "running";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            boolean[] finishLineCrossed;
            do {
                RaceTrack.makeAMove(horsePos, ThreadLocalRandom.current().nextInt(1, pnk));
                finishLineCrossed = RaceTrack.hasFinishLineBeenCrossed(horsePos);
            }while(finishLineCrossed[0]); 
            if (finishLineCrossed[1]) ControlCentreAndWatchingStand.makeAMove();

            state = "at the finish line";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
        }

        state = "at the stable";
        Stable.proceedToStable();

        state = "at the paddock";
        Paddock.proceedToPaddock(pID,pnk);
    }
}
