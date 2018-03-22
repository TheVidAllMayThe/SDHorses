package Monitors;

import Monitors.AuxiliaryClasses.Parameters;
import Threads.Broker;
import Threads.Horse;
import Threads.Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
* The ControlCentreAndWatchingStand class is a monitor that contains
* necessary methods to be used in mutual exclusive access by Broker, Horses and Spectators.
* <p>
* This is where the broker mostly operates and the spectators watch the race.
* 
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Main.HorseRace
* @see Broker
* @see Horse
* @see Spectator
*/

public class ControlCentreAndWatchingStand{
private static ReentrantLock r1= new ReentrantLock(false); private static Condition brokerCond = r1.newCondition();
    private static boolean lastHorseFinished = false;
    private static Condition spectatorsCond = r1.newCondition(); private static boolean allowSpectators = false;
    private static int[] winnerHorses;
    private static int nSpectators = 0;
    private static int nSpectatorsRace = 0;
    private static int nHorsesInPaddock = 0;
    private static int nHorsesFinishedRace = 0;
    private static boolean allowSpectatorsToWatch = false;
    private static Condition spectatorsCondRace = r1.newCondition();

    public static void openingTheEvents(){
        ((Broker)Thread.currentThread()).setState("OPENING_THE_EVENT");
    }
    
    /**
     * Broker waits for all spectator threads to have reached the ControlCentre before proceeding.
     */
    public static void summonHorsesToPaddock(){

        r1.lock();
        try{
            ((Broker)Thread.currentThread()).setState("ANNOUNCING_NEXT_RACE");
            while(nSpectators != Parameters.getNumberOfSpectators()){
                brokerCond.await();
            }
            nSpectators = 0;
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    /**
     * Broker waits for all horse threads to have reached the finish line before proceeding.
     */
    public static void startTheRace(){
        r1.lock();
        try{ 
            ((Broker)Thread.currentThread()).setState("SUPERVISING_THE_RACE");
            while(!lastHorseFinished){
                brokerCond.await();
            }

            lastHorseFinished = false;
        }catch(IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally{
            r1.unlock();
        }

    }

    /**
     * Broker declares the horses who won and wakes up the spectators watching the race.  
     *
     * @param   list  An integer array containing the ID of the horses who won the race.
     */
    public static void reportResults(int[] list) {
        r1.lock();
        try { 
            ((Broker)Thread.currentThread()).setState("SUPERVISING_THE_RACE");
            winnerHorses = list;
            allowSpectatorsToWatch = true;
            spectatorsCondRace.signal();
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }


    /**
     * Last function of broker lifecycle.
     */
    static public void entertainTheGuests(){ 
        ((Broker)Thread.currentThread()).setState("PLAYING_HOST_AT_THE_BAR");
    }

    
    /**
     * Spectator waits for next race of the day, last spectator waiting wakes the broker 
     * who's ready to start the race.
     */
    static public void waitForNextRace(){
        r1.lock();
        try{
            ((Spectator)Thread.currentThread()).setState("WAITING_FOR_A_RACE_TO_START");
            while(!allowSpectators){
                spectatorsCond.await();
            }
            if(++nSpectators == Parameters.getNumberOfSpectators()){
                allowSpectators = false;
                brokerCond.signal();
            }
            else spectatorsCond.signal();

        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    /**
     * Spectator waits while watching the race. 
     */
    static public void goWatchTheRace(){
        r1.lock();
        try {
            ((Spectator)Thread.currentThread()).setState("WATCHING_A_RACE");
            while (!allowSpectatorsToWatch) {
                spectatorsCondRace.await();
            }

            if (++nSpectatorsRace == Parameters.getNumberOfSpectators()) {
                allowSpectatorsToWatch = false;
                nSpectatorsRace = 0;
            }
            else spectatorsCondRace.signal();

        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
    }
    
    /**
     * Spectator checks if he won his bet.  
     *
     * @param   horseID  ID of the horse whom the spectator bet on. 
     * @return  boolean  weather spectator won.
     */
    static public boolean haveIWon(int horseID){
        boolean result = false;
        r1.lock();
        try{
            ((Spectator)Thread.currentThread()).setState("WATCHING_A_RACE");
            for (int winnerHorse : winnerHorses) {
                if (horseID == winnerHorse) {
                    result = true;
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
        return result;
    }

    /**
     * Last function of Spectator lifecycle.  
     */
    static public void relaxABit(){ 
        ((Spectator)Thread.currentThread()).setState("CELEBRATING");
    }

    /**
     * Horse proceeds to paddock, last horse awakes spectators 
     * that are waiting for the horses to enter the paddock.
     */
    static public void proceedToPaddock(){
        r1.lock();
        try {
            if (++nHorsesInPaddock == Parameters.getNumberOfHorses()) {
                allowSpectators = true;
                nHorsesInPaddock = 0;
                spectatorsCond.signal();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }
    
    /**
     * The last Horse announces in the ControlCentre that he finished the race waking up the broker.
     */
    static public void makeAMove(){
        r1.lock();
        try {
            if(++nHorsesFinishedRace == Parameters.getNumberOfHorses()){
                nHorsesFinishedRace = 0;
                lastHorseFinished = true;
                brokerCond.signal();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }
}
