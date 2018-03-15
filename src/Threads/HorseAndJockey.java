package Threads;


import Monitors.AuxiliaryClasses.Parameters;
import Monitors.Interfaces.Paddock_Horses;
import Monitors.Interfaces.RaceTrack_Horse;
import Monitors.Interfaces.Stable_Horse;
import Monitors.Paddock;
import Monitors.RaceTrack;
import Monitors.Stable;

import java.util.concurrent.ThreadLocalRandom;

public class HorseAndJockey extends Thread {

    private int pnk;
    private int pID;
    private String state;

    public HorseAndJockey() {
        pnk = ThreadLocalRandom.current().nextInt(1, Parameters./4);
    }

    @Override
    public void run() {
        pID = (int)Thread.currentThread().getId();
        for (int i = 0; i < Parameters.getNumberOfRaces(); i++) {
            state = "at the stable";
            Stable_Horse.proceedToStable();

            state = "at the paddock";
            Paddock_Horses.proceedToPaddock(pID, pnk);

            state = "at the start line";
            Paddock_Horses.proceedToStartLine();

            state = "running";
            RaceTrack_Horse.makeAMove();


            state = "at the finish line";
        }

        this.state = "at the stable";
        Stable_Horse.proceedToStable();
    }
}
