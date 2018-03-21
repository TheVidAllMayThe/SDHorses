package Monitors;

import Monitors.AuxiliaryClasses.Parameters;

import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class GeneralRepositoryOfInformation{
    private static PrintWriter writer; 
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
    private static int[] horsesStanding;
 
    public static void log() throws FileNotFoundException{
        if(writer == null) writer = new PrintWriter("log.txt");
        String line1 = brokerState + " ";
        String line2 = raceNumber + " " + raceDistance + " ";

        for(int i=0; i<Parameters.getNumberOfSpectators(); i++){
            line1 += spectatorsState[i] + " " + spectatorsBudget[i] + " ";
            line2 += spectatorsHorse[i] + " " + spectatorsBet[i] + " ";
        }
        
        line1 += raceNumber + " ";

        for(int i=0; i<Parameters.getNumberOfHorses(); i++){
            line1 += horsesState[i] + " " + horsesPnk[i] + " ";
            line2 += horseProbability[i] + " " + horseIteration[i] + " " + horseTrackPosition[i] + " " + horsesStanding + " ";
        }

        writer.println(line1);
        writer.println(line2);

        writer.close();
    }    
}
