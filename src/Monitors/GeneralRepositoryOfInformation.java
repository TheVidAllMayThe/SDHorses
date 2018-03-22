package Monitors;

import Monitors.AuxiliaryClasses.Parameters;

import java.util.concurrent.locks.ReentrantLock;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class GeneralRepositoryOfInformation{
    private static ReentrantLock r1 = new ReentrantLock(false);
    private static PrintWriter writer; 

    private static String brokerState;
    private static String[] spectatorsState;
    private static String[] horsesState;
    private static double[] spectatorsBudget;
    private static int raceNumber;
    private static int[] horsesPnk;
    private static int raceDistance;
    private static int[] spectatorsSelection;
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
            line2 += spectatorsSelection[i] + " " + spectatorsBet[i] + " ";
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

    public static void setBrokerState(String state){
        r1.lock();
        try{
            brokerState = state;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        } 
    }

    public static void setSpectatorsState(String state, int i){
        r1.lock();
        try{
            spectatorsState[i] = state;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }
    
    public static void setHorsesState(String state, int i){
        r1.lock();
        try{
            horsesState[i] = state;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setSpectatorsBudget(double budget, int i){
        r1.lock();
        try{
            spectatorsBudget[i] = budget;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setRaceNumber(int raceNumber){
        r1.lock();
        try{
            raceNumber = raceNumber;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }
    
    public static void setHorsesPnk(int pnk, int i){
        r1.lock();
        try{
            horsesPnk[i] = pnk;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setRaceDistance(int distance){
        r1.lock();
        try{
            raceDistance = distance;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setSpectatorsSelection(int horse, int i){
        r1.lock();
        try{
            spectatorsSelection[i] = horse;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setSpectatorsBet(double bet, int i){
        r1.lock();
        try{
            spectatorsBet[i] = bet;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setHorseProbability(double prob, int i){
        r1.lock();
        try{
            horseProbability[i] = prob;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setHorseIteration(int iteration, int i){
        r1.lock();
        try{
            horseIteration[i] = iteration;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setHorseTrackPosition(int position, int i){
        r1.lock();
        try{
            horseTrackPosition[i] = position;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setHorsesStanding(int standing, int i){
        r1.lock();
        try{
            horsesStanding[i] = standing;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }
}
