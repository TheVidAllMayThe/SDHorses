package Monitors.AuxiliaryClasses;

public class HorsePos implements Comparable<HorsePos>{
    private int horseID;
    private int pos;
    private int numSteps;

    public HorsePos(int horseID, int pos) {
        this.horseID = horseID;
        this.pos = pos;
        this.numSteps = 0;
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
        if (horse.numSteps<this.numSteps)
            return -1;

        else if (horse.numSteps>this.numSteps)
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
