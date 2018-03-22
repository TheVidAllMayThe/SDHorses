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
        print();
        Stable.proceedToStable(this.raceNum);
        print();
        ControlCentreAndWatchingStand.proceedToPaddock();
        Paddock.proceedToPaddock(ID, pnk);
        print();
        int horsePos = RaceTrack.proceedToStartLine(ID);
        print();
        do {
            RaceTrack.makeAMove(horsePos, ThreadLocalRandom.current().nextInt(1, pnk + 1));
            print();
        }while(!RaceTrack.hasFinishLineBeenCrossed(horsePos));
        ControlCentreAndWatchingStand.makeAMove();
        print();
    }

    private void print(){
        System.out.println(getClass().getSimpleName() + " pID = " + ID + ": " + state);
    }
    public void setState(String state){
        this.state = state;
    }
}
