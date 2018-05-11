import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

/**
* The {@link ControlCentreAndWatchingStand} class is a monitor that contains
* necessary methods to be used in mutual exclusive access by the Broker, Spectators and Horses.
* <p>
* This is where the Broker mostly operates and the Spectators watch the race.
* 
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21

*/

public class ControlCentreAndWatchingStand implements ControlCentreAndWatchingStand_Interface{
    private ReentrantLock r1; 
    private Condition brokerCond;
    private boolean lastHorseFinished;
    private Condition spectatorsCond; 
    private boolean allowSpectators;
    private Integer[] winnerHorses;
    private int nSpectators;
    private int nSpectatorsRace;
    private int nHorsesInPaddock;
    private int nHorsesFinishedRace;
    private boolean allowSpectatorsToWatch;
    private static Condition spectatorsCondRace;
    private int raceLength;
    private int numberOfSpectators;
    private int numberOfHorses;
    private GeneralRepositoryOfInformation_Interface groi;


    public ControlCentreAndWatchingStand(GeneralRepositoryOfInformation_Interface groi){
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
        System.out.println("raceLength: " + raceLength);
        System.out.println("numberOfSpectators: " + numberOfSpectators);
        System.out.println("numberOfHorses: " + numberOfHorses);
    }

    /**
     * Method used to set the Broker initial state.
     */

    @Override
    public void openingTheEvents(){
    }
    
    /**
     * The Broker waits for all the Spectator threads to have reached the {@link ControlCentreAndWatchingStand} before proceeding.
     * @param numRace Number of the next race.
     */

    @Override
    public void summonHorsesToPaddock(Integer numRace){

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
     *  Broker  waits for all  Horse threads to have reached the finish line before proceeding.
     */

    @Override
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

    @Override
    /**
     *  Broker declares the Horses who won and wakes up the Spectators watching the race.
     *
     * @param   list  An integer array containing the ID of the Horses who won the race.
     */
    public void reportResults(Integer[] list) {
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


    @Override
    /**
     * Last function of Broker lifecycle.
     */
    public void entertainTheGuests(){ 
        r1.lock();
        try { 
            groi.setBrokerState("PHAB");
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }

    
    /**
     * Spectator waits for next race of the day, last Spectator waiting wakes the Broker
     * who's ready to start the race.
     * @param spectatorID ID of the spectator calling the method.
     * @param budget Budget of the spectator.
     */

    @Override
    public void waitForNextRace(Integer spectatorID, Double budget){
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
     * Spectator waits while watching the race.
     * @param spectatorID ID of the spectator calling the method.
     */

    @Override
    public void goWatchTheRace(Integer spectatorID){
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
     * Spectator checks if he won his bet.
     *
     * @param   horseID  ID of the Horse whom the Spectator bet on.
     * @param spectatorID ID of the spectator calling the method.
     * @return  True if the Spectator won.
     */

    @Override
    public boolean haveIWon(Integer horseID, Integer spectatorID){
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
     * Last function of Spectator lifecycle.
     * @param spectatorID ID of the spectator calling the method.
     */

    @Override
    public void relaxABit(Integer spectatorID){ 
        r1.lock();
        try{
            groi.setSpectatorsState("CEL", spectatorID);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    /**
     * Horse proceeds to paddock, last Horse awakes Spectators
     * that are waiting for the Horses to enter the Paddock.
     */

    @Override
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
     * The last Horse announces in the {@link ControlCentreAndWatchingStand} that he finished the race waking up the Broker.
     */

    @Override
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
