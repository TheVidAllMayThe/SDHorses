package Monitors;

public class GeneralRepositoryOfInformation {
    private final int numSpectators;
    private final int numHorses;

    GeneralRepositoryOfInformation(int numHorses, int numSpectators){
        this.numHorses = numHorses;
        this.numSpectators = numSpectators;
    }

    public int getNumSpectators() {
        return numSpectators;
    }

    public int getNumHorses() {
        return numHorses;
    }
}
