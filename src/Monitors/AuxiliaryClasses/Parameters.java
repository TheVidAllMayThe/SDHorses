package Monitors.AuxiliaryClasses;

public class Parameters {
    private static int numberOfRaces, numberOfHorses, numberOfSpectators;
    private static boolean set = false;

    public static void initialize(int numberOfRaces, int numberOfHorses, int numberOfSpectators){
        if(set) return;
        Parameters.numberOfHorses = numberOfHorses;
        Parameters.numberOfRaces = numberOfRaces;
        Parameters.numberOfSpectators = numberOfSpectators;
        set = true;
    }

    public static int getNumberOfRaces() {
        return numberOfRaces;
    }

    public static int getNumberOfHorses() {
        return numberOfHorses;
    }

    public static int getNumberOfSpectators() {
        return numberOfSpectators;
    }
}
