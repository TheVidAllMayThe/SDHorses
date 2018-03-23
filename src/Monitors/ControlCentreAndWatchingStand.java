package Monitors;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.GeneralRepositoryOfInformation;
import Threads.Broker;
import Threads.Horse;
import Threads.Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
* The {@link ControlCentreAndWatchingStand} class is a monitor that contains
* necessary methods to be used in mutual exclusive access by the {@link Broker}, {@link Spectator}s and {@link Horse}s.
* <p>
* This is where the {@link Broker} mostly operates and the {@link Spectator}s watch the race.
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


    /**
     * Method used to set the {@link Broker} initial state.
     */

    public static void openingTheEvents(){
        ((Broker)Thread.currentThread()).setState("OPENING_THE_EVENT");
    }
    
    /**
     * The {@link Broker} waits for all the {@link Spectator} threads to have reached the {@link ControlCentreAndWatchingStand} before proceeding.
     */
    public static void summonHorsesToPaddock(int numRace){

        r1.lock();
        try{
            ((Broker)Thread.currentThread()).setState("ANNOUNCING_NEXT_RACE");
            GeneralRepositoryOfInformation.setBrokerState("ANRA");
            GeneralRepositoryOfInformation.setRaceNumber(numRace);
            GeneralRepositoryOfInformation.setRaceDistance(Parameters.getRaceLength());
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
     *  {@link Broker}  waits for all  {@link Horse} threads to have reached the finish line before proceeding.
     */
    public static void startTheRace(){
        r1.lock();
        try{ 
            ((Broker)Thread.currentThread()).setState("SUPERVISING_THE_RACE");
            GeneralRepositoryOfInformation.setBrokerState("STRA");
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
     *  {@link Broker} declares the {@link Horse}s who won and wakes up the {@link Spectator}s watching the race.
     *
     * @param   list  An integer array containing the ID of the {@link Horse}s who won the race.
     */
    public static void reportResults(int[] list) {
        r1.lock();
        try { 
            ((Broker)Thread.currentThread()).setState("SUPERVISING_THE_RACE");
            GeneralRepositoryOfInformation.setBrokerState("STRA");
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
     * Last function of {@link Broker} lifecycle.
     */
    static public void entertainTheGuests(){ 
        ((Broker)Thread.currentThread()).setState("PLAYING_HOST_AT_THE_BAR");
        GeneralRepositoryOfInformation.setBrokerState("PHAB");
    }

    
    /**
     * {@link Spectator} waits for next race of the day, last {@link Spectator} waiting wakes the {@link Broker}
     * who's ready to start the race.
     */
    static public void waitForNextRace(){
        r1.lock();
        try{
            Spectator sInst = (Spectator)Thread.currentThread();
            sInst.setState("WAITING_FOR_A_RACE_TO_START");
            GeneralRepositoryOfInformation.setSpectatorsState("WRS",sInst.getID());
            GeneralRepositoryOfInformation.setSpectatorsBudget(sInst.getBudget(),sInst.getID());

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
     * {@link Spectator} waits while watching the race.
     */
    static public void goWatchTheRace(){
        r1.lock();
        try {
            Spectator sInst = (Spectator)Thread.currentThread();
            sInst.setState("WATCHING_A_RACE");
            GeneralRepositoryOfInformation.setSpectatorsState("WAR", sInst.getID());
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
     * {@link Spectator} checks if he won his bet.
     *
     * @param   horseID  ID of the {@link Horse} whom the {@link Spectator} bet on.
     * @return  True if the {@link Spectator} won.
     */
    static public boolean haveIWon(int horseID){
        boolean result = false;
        r1.lock();
        try{
            Spectator sInst = (Spectator)Thread.currentThread();
            sInst.setState("WATCHING_A_RACE");
            GeneralRepositoryOfInformation.setSpectatorsState("WAR", sInst.getID());
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
     * Last function of {@link Spectator} lifecycle.
     */
    static public void relaxABit(){ 
        Spectator sInst = (Spectator)Thread.currentThread();
        sInst.setState("CELEBRATING");
        GeneralRepositoryOfInformation.setSpectatorsState("CEL", sInst.getID());
    }

    /**
     * {@link Horse} proceeds to paddock, last {@link Horse} awakes {@link Spectator}s
     * that are waiting for the {@link Horse}s to enter the {@link Paddock}.
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
     * The last {@link Horse} announces in the {@link ControlCentreAndWatchingStand} that he finished the race waking up the {@link Broker}.
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
