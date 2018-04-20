import java.util.concurrent.locks.ReentrantLock;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.io.IOException;


/**
 * This class stores the state of all the components of the program. Every time that there's a change the program logs the new state to a file (log.txt).
 */

public class GeneralRepositoryOfInformation{
    private static ReentrantLock r1 = new ReentrantLock(false);
    private static PrintWriter writer;

    private static int numberOfRaces, numberOfHorses, numberOfSpectators, raceLength;

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

    public static void main(String[] args){
        initialize(5,4,4, 100);
        
        try{
            ServerSocket serverSocket = new ServerSocket(23040);
            while(true){
                new ClientThread(serverSocket.accept(), GeneralRepositoryOfInformation.class).run();
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        close();
    }

    public static void initialize(int numberOfRaces, int numberOfHorses, int numberOfSpectators, int raceLength){
        numberOfRaces = numberOfRaces;
        numberOfHorses = numberOfHorses;
        numberOfSpectators = numberOfSpectators;
        raceLength = raceLength;

        try{
            writer = new PrintWriter("log.txt");

        }catch(FileNotFoundException fe){
            fe.printStackTrace();
        }

        brokerState = "----";
        spectatorsState = new String[numberOfSpectators];
        for(int i=0; i < spectatorsState.length; i++) spectatorsState[i] = "---";
        horsesState = new String[numberOfHorses];
        for(int i=0; i < horsesState.length; i++) horsesState[i] = "---";
        spectatorsBudget = new String[numberOfSpectators];
        for(int i=0; i < spectatorsBudget.length; i++) spectatorsBudget[i] = "----";
        raceNumber = 0; 
        horsesPnk = new String[numberOfHorses];
        for(int i=0; i < horsesPnk.length; i++) horsesPnk[i] = "--";
        raceDistance = "--";
        spectatorsSelection = new String[numberOfSpectators];
        for(int i=0; i < spectatorsSelection.length; i++) spectatorsSelection[i] = "-";
        spectatorsBet = new String[numberOfSpectators];
        for(int i=0; i < spectatorsBet.length; i++) spectatorsBet[i] = "----";
        horseProbability = new String[numberOfHorses];
        for(int i=0; i < horseProbability.length; i++) horseProbability[i] = "----";
        horseIteration = new String[numberOfHorses];
        for(int i=0; i < horseIteration.length; i++) horseIteration[i] = "--";
        horseTrackPosition = new String[numberOfHorses];
        for(int i=0; i < horseTrackPosition.length; i++) horseTrackPosition[i] = "--";
        horsesStanding = new String[numberOfHorses];
        for(int i=0; i < horsesStanding.length; i++) horsesStanding[i] = "-";



        writer.println("MAN/BRK           SPECTATOR/BETTER              HORSE/JOCKEY PAIR at Race RN");
        writer.println("  Stat  St0  Am0 St1  Am1 St2  Am2 St3  Am3 RN St0 Len0 St1 Len1 St2 Len2 St3 Len3");
        writer.println("                                        Race RN Status");
        writer.println("RN Dist BS0  BA0 BS1  BA1 BS2  BA2 BS3  BA3  Od0  N0 Ps0 SD0 Od1  N1 Ps1 Sd1 Od2  N2 Ps2 Sd2 Od3  N3 Ps3 St3");
        writer.flush();

    }

    public static void close(){
        writer.close();
    }
 
    private static void log(){
        try{

            StringBuilder line1 = new StringBuilder(String.format("  %4s ", brokerState));
            StringBuilder line2 = new StringBuilder(String.format(" %1d  %2s  ", raceNumber, raceDistance));


            for(int i=0; i<numberOfSpectators; i++){
                line1.append(String.format("%4s %3s", spectatorsState[i], spectatorsBudget[i]));
                line2.append(String.format(" %1s  %4s ", spectatorsSelection[i], spectatorsBet[i]));
            }
            
            line1.append(String.format("  %1d ", raceNumber));

            for(int i=0; i<numberOfHorses; i++){
                line1.append(String.format("%3s  %2s  ", horsesState[i], horsesPnk[i]));
                line2.append(String.format("%4s  %2s  %2s  %1s ", horseProbability[i], horseIteration[i], horseTrackPosition[i], horsesStanding[i]));
            }

            writer.println(line1);
            writer.println(line2);
        }catch(Exception fe){
            fe.printStackTrace();
        }
        writer.flush();
    }    

    public static void setBrokerState(String state){
        r1.lock();
        try{
            brokerState = state;
            log();
        }catch(Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    public static void setRaceNumber(int raceNu){
        r1.lock();
        try{
            raceNumber = raceNu;
            log();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    } public static void setHorsesPnk(int pnk, int i){
        r1.lock();
        try{
            String temp = "" + pnk;
            for(int j=temp.length(); j<2; j++) temp = "0" + temp;
            horsesPnk[i] = temp;
            log();
        }catch(Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    public static void setHorseProbability(double prob, int i){
        r1.lock();
        try{

            String temp;
            if(prob < 0) temp = "----";
            else{
                temp = "" + ((int)prob);
                for(int j=temp.length(); j<4; j++) temp = "0" + temp;
            }
            horseProbability[i] = temp;
            log();
        }catch(Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }
}
