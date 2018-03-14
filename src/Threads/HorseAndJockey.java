package Threads;


import Monitors.Interfaces.Paddock_Horses;
import Monitors.Interfaces.RaceTrack_Horse;
import Monitors.Interfaces.Stable_Horse;
import java.util.concurrent.ThreadLocalRandom;

public class HorseAndJockey extends Thread {
    private final RaceTrack_Horse raceTrack;
    private final Paddock_Horses paddock;
    private int numberOfRaces;
    private final Stable_Horse stable;
    private int pnk;
    private int pID;
    private string state;

    public HorseAndJockey(int numberOfRaces, int raceLength, Stable_Horse s, RaceTrack_Horse rtb, Paddock_Horses ph) {
        this.numberOfRaces = numberOfRaces;
        this.stable = s;
        this.paddock = ph;
        this.raceTrack = rtb;
        pnk = ThreadLocalRandom.current().nextInt(1, raceLength/4);

    }

    private void proceedToStable(){
        this.state = "at the stable";
        stable.blockHorses();        
        if(stable.isEmpty()) stable.setUnlockHorses(false);
    }

    @Override
    public void run() {
        pID = (int)Thread.currentThread().getId();
        for (int i = 0; i < this.numberOfRaces; i++) {
            //this.state = "at the stable";
            stable.proceedToStable();

            //this.state = "at the paddock";
            paddock.proceedToPaddock(pID, pnk);

            paddock.proceedToStartLine();
            //this.state = "at the start line";
            int racePos = raceTrack.proceedToStartLine(pID);

            //this.state = "running";
            do{
                raceTrack.makeAMove(racePos, ThreadLocalRandom.current().nextInt(1, pnk));
            }while (!raceTrack.hasFinishLineBeenCrossed(pID));

            //this.state = "at the finish line";
        }

        //this.state = "at the stable";
        stable.proceedToStable();
    }
}
