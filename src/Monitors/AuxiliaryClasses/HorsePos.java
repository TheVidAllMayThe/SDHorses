package Monitors.AuxiliaryClasses;

public class HorsePos implements Comparable<HorsePos>{
    private int horseID;
    private int pos;
    private int numSteps;
    private boolean myTurn;

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

    private boolean finished;


    public HorsePos(int horseID, int pos, boolean myTurn) {
        this.horseID = horseID;
        this.pos = pos;
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

    public int getNumSteps() {
        return numSteps;
    }

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
