import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

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
    private ReentrantLock r1; 
    private Condition brokerCond;
    private boolean lastHorseFinished;
    private Condition spectatorsCond; 
    private boolean allowSpectators;
    private int[] winnerHorses;
    private int nSpectators;
    private int nSpectatorsRace;
    private int nHorsesInPaddock;
    private int nHorsesFinishedRace;
    private boolean allowSpectatorsToWatch;
    private static Condition spectatorsCondRace;
    private int raceLength;
    private int numberOfSpectators;
    private int numberOfHorses;
    private GeneralRepositoryOfInformation groi;


    public ControlCentreAndWatchingStand(GeneralRepositoryOfInformation groi){
        this.r1 = new ReentrantLock(false);
        this.brokerCond = r1.newCondition();
        this.lastHorseFinished = false;
        this.spectatorsCond = r1.newCondition();
        this.allowSpectators = false;
        this.nSpectators = 0;
        this.nSpectatorsRace = 0;
        this.nHorsesInPaddock = 0;
        this.nHorsesFinishedRace = 0;
        this.allowSpectatorsToWatch = false;
        this.spectatorsCondRace = r1.newCondition();
        this.raceLength = groi.getRaceLength();
        this.numberOfSpectators = groi.getNumberOfSpectators();
        this.numberOfHorses = groi.getNumberOfHorses();
        this.groi = groi;
    }

    /**
     * Method used to set the {@link Broker} initial state.
     */

    public void openingTheEvents(){
    }
    
    /**
     * The {@link Broker} waits for all the {@link Spectator} threads to have reached the {@link ControlCentreAndWatchingStand} before proceeding.
     */
    public void summonHorsesToPaddock(int numRace){

        r1.lock();
        try{
            groi.setBrokerState("ANRA");
            groi.setRaceNumber(numRace);
            groi.setRaceDistance(raceLength);
            while(nSpectators != numberOfSpectators){
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
    public void startTheRace(){
        r1.lock();
        try{ 
            groi.setBrokerState("STRA");
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
    public void reportResults(int[] list) {
        r1.lock();
        try { 
            groi.setBrokerState("STRA");
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
    public void entertainTheGuests(){ 
        groi.setBrokerState("PHAB");
    }

    
    /**
     * {@link Spectator} waits for next race of the day, last {@link Spectator} waiting wakes the {@link Broker}
     * who's ready to start the race.
     */
    public void waitForNextRace(int spectatorID, double budget){
        r1.lock();
        try{
            groi.setSpectatorsState("WRS", spectatorID);
            groi.setSpectatorsBudget(budget, spectatorID);

            while(!allowSpectators){
                spectatorsCond.await();
            }
            if(++nSpectators == numberOfSpectators){
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
    public void goWatchTheRace(int spectatorID){
        r1.lock();
        try {
            groi.setSpectatorsState("WAR", spectatorID);
            while (!allowSpectatorsToWatch) {
                spectatorsCondRace.await();
            }

            if (++nSpectatorsRace == numberOfSpectators) {
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
    public boolean haveIWon(int horseID, int spectatorID){
        boolean result = false;
        r1.lock();
        try{
            groi.setSpectatorsState("WAR", spectatorID);
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
    public void relaxABit(int spectatorID){ 
        groi.setSpectatorsState("CEL", spectatorID);
    }

    /**
     * {@link Horse} proceeds to paddock, last {@link Horse} awakes {@link Spectator}s
     * that are waiting for the {@link Horse}s to enter the {@link Paddock}.
     */
    public void proceedToPaddock(){
        r1.lock();
        try {
            if (++nHorsesInPaddock == numberOfHorses) {
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
    public void makeAMove(){
        r1.lock();
        try {
            if(++nHorsesFinishedRace == numberOfHorses){
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
