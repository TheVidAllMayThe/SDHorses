package Monitors.AuxiliaryClasses;

import Threads.Horse;

/**
 * The {@link HorsePos} class holds all the necessary information about a {@link Horse} that's racing or that has finished a race.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 */

public class HorsePos implements Comparable<HorsePos>{
    private int horseID;
    private int pos;
    private int numSteps;
    private boolean myTurn;


    private boolean finished;


    /**
     *
     * @param horseID ID of the Thread invoking the constructor.
     * @param myTurn Set to true if it's the starting {@link Horse}.
     */

    public HorsePos(int horseID, boolean myTurn) {
        this.horseID = horseID;
        this.pos = 0;
        this.numSteps = 0;
        this.myTurn = myTurn;
        this.myTurn = false;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void addPos(int amount){
        pos += amount;
        this.numSteps++;
    }

    public int getHorseID() {
        return horseID;
    }

    public int getPos() {
        return pos;
    }
    
    public int getNumSteps(){
        return numSteps;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public void setHorseID(int horseID) {
        this.horseID = horseID;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     *
     * @param horse {@link HorsePos} object to be compared.
     * @return Returns -1 if the position of the {@link HorsePos} passed as argument is lower, 0 if its equal and 1 if its greater.
     */

    @Override
    public int compareTo(HorsePos horse){
        if (horse.numSteps < this.numSteps)
            return -1;

        else if (horse.numSteps > this.numSteps)
            return 1;

        else {
            if (horse.pos<this.pos)
                return -1;
            else if (horse.pos>this.pos)
                return 1;
        }
        return 0;


    }
}
