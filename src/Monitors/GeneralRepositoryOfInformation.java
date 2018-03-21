package Monitors;

import java.util.PrintWriter;

public class GeneralRepositoryOfInformation {
    private static String brokerState;
    private static String[] spectatorsState;
    private static String[] horsesState;
    private static double[] spectatorsBudget;
    private static int raceNumber;
    private static int[] horsesPnk;
    private static int raceDistance;
    private static int[] spectatorsHorse;
    private static double[] spectatorsBet;
    private static double[] horseProbability;
    private static int[] horseIteration;
    private static int[] horseTrackPosition;
    private static int[] horseStanding;
 
    public static void log(){
        PrintWriter writer = new PrintWriter("log.txt", "UTF-8");
        
        writer.println("The second line");
        writer.close();
    }    
}
