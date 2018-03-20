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
            print(state);
            Stable.proceedToStable();

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
                RaceTrack.makeAMove(horsePos, ThreadLocalRandom.current().nextInt(1, pnk));
            }while(!RaceTrack.hasFinishLineBeenCrossed(horsePos));
            ControlCentreAndWatchingStand.makeAMove();

            state = "at the finish line";
            print(state);
        }

        state = "at the stable";
        print(state);
        Stable.proceedToStable();

        state = "at the paddock";
        print(state);
        Paddock.proceedToPaddock(pID,pnk);
    }
    private void print(String state){
        System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
    }
}
