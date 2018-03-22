package Monitors;

import Monitors.AuxiliaryClasses.Parameters;

import java.util.concurrent.locks.ReentrantLock;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;

public class GeneralRepositoryOfInformation{
    private static ReentrantLock r1 = new ReentrantLock(false);
    private static PrintWriter writer; 

    private static String brokerState;
    private static String[] spectatorsState;
    private static String[] horsesState;
    private static String[] spectatorsBudget;
    private static int raceNumber ;
    private static String[] horsesPnk;
    private static String raceDistance;
    private static String[] spectatorsSelection;
    private static String[] spectatorsBet;
    private static String[] horseProbability; 
    private static String[] horseIteration;
    private static String[] horseTrackPosition;
    private static String[] horsesStanding;

    public static void initialize(){
        try{
            writer = new PrintWriter("log.txt");
        }catch(FileNotFoundException fe){
        }

        brokerState = "----";
        spectatorsState = new String[Parameters.getNumberOfSpectators()];
        for(int i=0; i < spectatorsState.length; i++) spectatorsState[i] = "---";
        horsesState = new String[Parameters.getNumberOfHorses()];
        for(int i=0; i < horsesState.length; i++) horsesState[i] = "---";
        spectatorsBudget = new String[Parameters.getNumberOfSpectators()];
        for(int i=0; i < spectatorsBudget.length; i++) spectatorsBudget[i] = "----";
        raceNumber = 0; 
        horsesPnk = new String[Parameters.getNumberOfHorses()];
        for(int i=0; i < horsesPnk.length; i++) horsesPnk[i] = "--";
        raceDistance = "--";
        spectatorsSelection = new String[Parameters.getNumberOfSpectators()];
        for(int i=0; i < spectatorsSelection.length; i++) spectatorsSelection[i] = "-";
        spectatorsBet = new String[Parameters.getNumberOfSpectators()];
        for(int i=0; i < spectatorsBet.length; i++) spectatorsBet[i] = "----";
        horseProbability = new String[Parameters.getNumberOfHorses()];
        for(int i=0; i < horseProbability.length; i++) horseProbability[i] = "---";
        horseIteration = new String[Parameters.getNumberOfHorses()];
        for(int i=0; i < horseIteration.length; i++) horseIteration[i] = "--";
        horseTrackPosition = new String[Parameters.getNumberOfHorses()];
        for(int i=0; i < horseTrackPosition.length; i++) horseTrackPosition[i] = "--";
        horsesStanding = new String[Parameters.getNumberOfHorses()];
        for(int i=0; i < horsesStanding.length; i++) horsesStanding[i] = "-";
        
        writer.println("MAN/BRK           SPECTATOR/BETTER              HORSE/JOCKEY PAIR at Race RN");
        writer.println("  Stat  St0  Am0 St1  Am1 St2  Am2 St3  Am3 RN St0 Len0 St1 Len1 St2 Len2 St3 Len3");
        writer.println("                                        Race RN Status");
        writer.println("RN Dist BS0 BA0 BS1  BA1 BS2  BA2 BS3  BA3  Od0 N0 Ps0 SD0 Od1 N1 Ps1 Sd1 Od2 N2 Ps2 Sd2 Od3 N3 Ps3 St3");
        
    }

    public static void close(){
        writer.close();
    }
 
    public static void log(){
        try{
            String line1 = brokerState + " ";
            String line2 = raceNumber + " " + raceDistance + " ";

            for(int i=0; i<Parameters.getNumberOfSpectators(); i++){
                line1 += spectatorsState[i] + " " + spectatorsBudget[i] + " ";
                line2 += spectatorsSelection[i] + " " + spectatorsBet[i] + " ";
            }
            
            line1 += raceNumber + " ";

            for(int i=0; i<Parameters.getNumberOfHorses(); i++){
                line1 += horsesState[i] + " " + horsesPnk[i] + " ";
                line2 += horseProbability[i] + " " + horseIteration[i] + " " + horseTrackPosition[i] + " " + horsesStanding[i] + " ";
            }

            writer.println(line1);
            writer.println(line2);
        }catch(Exception fe){
            fe.printStackTrace();
        }
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
            String temp = "" + ((int)budget);
            for(int j=temp.length(); j<4; j++) temp = "0" + temp;
            spectatorsBudget[i] = temp;
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
    } public static void setHorsesPnk(int pnk, int i){
        r1.lock();
        try{
            String temp = "" + pnk;
            for(int j=temp.length(); j<3; j++) temp = "0" + temp;
            horsesPnk[i] = temp;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setRaceDistance(int distance){
        r1.lock();
        try{
            if(distance < 10) raceDistance = "0"+distance;
            else raceDistance = "" + distance;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setSpectatorsSelection(int horse, int i){
        r1.lock();
        try{
            spectatorsSelection[i] = "" + horse;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setSpectatorsBet(double bet, int i){
        r1.lock();
        try{
            String temp = "" + ((int)bet);
            for(int j=temp.length(); j<4; j++) temp = "0" + temp;
            spectatorsBet[i] = temp;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setHorseProbability(double prob, int i){
        r1.lock();
        try{
            String temp;
            if(prob < 0) temp = "---";
            else{
                temp = "" + ((int)prob);
                for(int j=temp.length(); j<3; j++) temp = "0" + temp;
            }
            horseProbability[i] = temp;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setHorseIteration(int iteration, int i){
        r1.lock();
        try{
            if(iteration < 0) horseIteration[i] = "--";
            else{
                if(iteration < 10) horseIteration[i] = "0" + iteration;
                horseIteration[i] = "" + iteration;
            }
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setHorseTrackPosition(int position, int i){
        r1.lock();
        try{
            if(position < 0) horseTrackPosition[i] = "--";
            else{
                if(position < 10) horseTrackPosition[i] = "0" + position;
                horseTrackPosition[i] = "" + position;
            }
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }

    public static void setHorsesStanding(char standing, int i){
        r1.lock();
        try{
            horsesStanding[i] = "" + standing;
            log();
        }catch(Exception e){
        }finally{
            r1.unlock();
        }
    }
}
