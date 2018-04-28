import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* The {@link BettingCentre} class is a monitor that contains all the
* necessary methods to be used in mutual exclusive access by the {@link Broker} and {@link Spectator}.
* <p>
* This is where the {@link Bet}s are handled.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Broker
* @see Spectator
*/

public class BettingCentre{
    private int numberOfSpectators;
    
    private Bet[] bets;
    private Lock r1;
    private Condition brokerCond;
    private Condition spectatorCond;
    private boolean waitingOnBroker;
    private boolean resolvedSpectator;
    private int numWinners;
    private int currentNumberOfSpectators;
    private int numHorsesFirst;
    private GeneralRepositoryOfInformation groi;

    public BettingCentre(GeneralRepositoryOfInformation groi){
        this.numberOfSpectators = groi.getNumberOfSpectators();
        this.bets = new Bet[numberOfSpectators];
        this.r1 = new ReentrantLock();
        this.brokerCond = r1.newCondition();
        this.spectatorCond = r1.newCondition();
        this.waitingOnBroker = false;
        this.resolvedSpectator = false;
        this.numWinners = 0;
        this.currentNumberOfSpectators = 0;
        this.numHorsesFirst = 0;
        this.groi = groi;
        System.out.println("numberOfSpectators: " + numberOfSpectators);
    }

    /**
     * {@link Broker} accepts a {@link Bet}, wakes next {@link Spectator} in line and waits for the next one until all {@link Bet}s are taken.
     */

    public void acceptTheBets() throws IllegalMonitorStateException{
        r1.lock();
        try {
            groi.setBrokerState("WFBE"); 
            while(currentNumberOfSpectators != numberOfSpectators){
                resolvedSpectator = true;
                waitingOnBroker = false; spectatorCond.signal(); while (!waitingOnBroker){
                    brokerCond.await();
                }

            }

            currentNumberOfSpectators = 0;
        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }
    
    /**
     * {@link Broker} verifies if any {@link Spectator} won a {@link Bet}.
     *
     * @param   winnerList  Integer array containing the ID of all the {@link Horse}s who won.
     * @return  boolean     Returns true if any {@link Spectator} won a {@link Bet}.
     */

    public boolean areThereAnyWinners(Integer[] winnerList) {
        boolean returnValue = false;
r1.lock();
        try {
            numHorsesFirst = winnerList.length; 
            for (Bet bet: bets){
                for (int winner : winnerList){
                    if (bet.getHorseID() == winner) {
                        returnValue = true;
                        numWinners++;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            r1.unlock();
        }

        return returnValue;
    }

    /**
     * {@link Broker} honors a {@link Bet}, wakes next {@link Spectator} in line and waits for the next {@link Spectator} claiming a reward until all rewards are given.
     */
    public void honorBets(){
        r1.lock();
        try{
            while(currentNumberOfSpectators != numWinners){
                waitingOnBroker = false;
                resolvedSpectator = true;
                spectatorCond.signal();
                while(!waitingOnBroker){
                    brokerCond.await();
                }
                
            }
            currentNumberOfSpectators = 0;
            numWinners = 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            r1.unlock();
        }
    }


    /**
     * {@link Spectator} waits in line, places a {@link Bet} and then wakes the {@link Broker}.
     *
     * @param pid ID of the thread calling the method.
     * @param value Amount to bet.
     * @param horseID ID of the {@link Horse} in which to bet.
     * @param odds Odds of the {@link Horse} in which to bet.
     */

    public void placeABet(Integer pid, Double value, Integer horseID, Double odds, Double budget){
        r1.lock();
        try{  
            groi.setSpectatorsState("PAB", pid);
            while(!resolvedSpectator){
                spectatorCond.await();
            }

            waitingOnBroker = true;
            groi.setSpectatorsBudget(budget, pid);
            bets[currentNumberOfSpectators++] = new Bet(pid, value, horseID, odds);
            groi.setSpectatorsBet(value, pid);
            resolvedSpectator = false;
            brokerCond.signal();

        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    /**
     * Called by a {@link Spectator} to collect the gain of after having won a {@link Bet}.
     *
     * @param spectatorID ID of the thread calling the method.
     */
    public double goCollectTheGains(Integer spectatorID, Double budget){
        double result = 0;
        r1.lock(); try{
            groi.setSpectatorsState("CTG", spectatorID);
            while(!resolvedSpectator){
                spectatorCond.await();
            }

            currentNumberOfSpectators++;
            waitingOnBroker = true;
            resolvedSpectator = false;
            brokerCond.signal();
            
            Bet bet;
            for (Bet bet1 : bets) {
                if (bet1.getSpectatorID() == spectatorID) {
                    bet = bet1;
                    result = bet.getBetAmount() * bet.getOdds() / numHorsesFirst;
                    break;
                }

            }
            groi.setSpectatorsBudget(budget, spectatorID);
        
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
        return result;
    }
}
