package Threads;


import Monitors.AuxiliaryClasses.Parameters;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.Paddock;
import Monitors.RaceTrack;
import Monitors.Stable;

import java.util.concurrent.ThreadLocalRandom;

public class HorseAndJockey extends Thread {

    private int pnk;

    public HorseAndJockey() {
        pnk = ThreadLocalRandom.current().nextInt(2, Parameters.getRaceLength()/4);
    }

    @Override
    public void run() {
        int pID = (int) Thread.currentThread().getId();
        String state;
        for (int i = 0; i < Parameters.getNumberOfRaces(); i++) {
            state = "at the stable";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            Stable.proceedToStable();

            state = "at the paddock";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            ControlCentreAndWatchingStand.proceedToPaddock();
            Paddock.proceedToPaddock(pID, pnk);

            Paddock.proceedToStartLine();
            state = "at the start line";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            int horsePos = RaceTrack.proceedToStartLine(pID);

            state = "running";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            do {
                RaceTrack.makeAMove(horsePos, ThreadLocalRandom.current().nextInt(1, pnk));
            }while(!RaceTrack.hasFinishLineBeenCrossed(horsePos));
            ControlCentreAndWatchingStand.makeAMove();

            state = "at the finish line";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
        }

        state = "at the stable";
        System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
        Stable.proceedToStable();

        state = "at the paddock";
        System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
        Paddock.proceedToPaddock(pID,pnk);
    }
}
