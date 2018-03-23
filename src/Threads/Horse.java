package Threads;



import Monitors.AuxiliaryClasses.Parameters;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.Paddock;
import Monitors.RaceTrack;
import Monitors.Stable;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@link Horse} class is a thread that contains the lifecycle of the {@link Horse} during the day.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace
 */

public class Horse extends Thread {

    private int pnk;
    private int ID;
    private int raceNum;
    private String state;

    /**
     *
     * @param id ID of the Horse in each race.
     * @param raceNum Number of the race in which the race will participate.
     */

    public Horse(int id, int raceNum) {
        this.ID = id;
        this.raceNum = raceNum;
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
    
    public void setPnk(int pnk){
        this.pnk = pnk;
    }

    public int getID(){
        return this.ID;
    }

    public int getPnk(){
        return this.pnk;
    }
}
