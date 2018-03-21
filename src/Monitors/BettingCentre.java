package Monitors;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.AuxiliaryClasses.Bet;
import Threads.Broker;
import Threads.Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* The BettingCentre class is a monitor that contains all the
* necessary methods to be used in mutual exclusive access by Broker and Spectators.
* <p>
* This is where the bets are handled.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Broker
* @see Spectator
*/

public class BettingCentre{
    
    private static final Lock r1 = new ReentrantLock();

    private static final Condition brokerCond = r1.newCondition();
    private static boolean waitingOnBroker = false;

    private static final Condition spectatorCond = r1.newCondition();
    private static boolean resolvedSpectator = false;

    private static boolean currentlyRefunding = true;
    private static final Bet[] bets  = new Bet[Parameters.getNumberOfSpectators()];
    private static int numWinners = 0;
    private static int potWinners = 0;
    private static int currentNumberOfSpectators = 0;
    private static int potValue = 0;


    /**
     * Broker accepts a bet, wakes next spectator in line and waits for the next one until all bets are taken.
     */
    public static void acceptTheBets() throws IllegalMonitorStateException{
        r1.lock();
        try {
            while(currentNumberOfSpectators != Parameters.getNumberOfSpectators()){
                resolvedSpectator = true;
                waitingOnBroker = false;
                spectatorCond.signal();

                while (!waitingOnBroker){
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
     * Broker verifies if anyone won a bet.
     *
     * @param   winnerList  integer array containing the ID of all the horses who won.
     * @return  boolean     wether any spectator won a bet.
     */
    static public boolean areThereAnyWinners(int[] winnerList) {
        boolean returnValue = false;

        r1.lock();
        try {
            for (Bet bet: bets){
                for (int winner : winnerList){
                    if (bet.getHorseID() == winner) {
                        returnValue = true;
                        numWinners++;
                        potWinners += bet.getBetAmount();
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
     * Broker honors a bet, wakes next spectator in line and waits for the next spectator claiming a reward until all rewards are given.
     */
    public static void honorBets(){
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
            potValue = 0;
            potWinners = 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            r1.unlock();
        }
    }

    /**
     * Spectator waits in line, places a bet and then wakes the broker.
     */
    public static void placeABet(int pid, int value, int horseID){
        r1.lock();
        try{  
            while(!resolvedSpectator){
                spectatorCond.await();
            }

            waitingOnBroker = true;
            brokerCond.signal();
            bets[currentNumberOfSpectators++] = new Bet(pid, value, horseID);
            resolvedSpectator = false;
            potValue += value;

        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    /**
     * Spectator waits in line, claims his reward and then wakes the broker.
     *
     * @return  int  reward amount
     */
    public static int goCollectTheGains(int spectatorID){
        int result = 0;
        r1.lock();
        try{

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
                    result = (bet.getBetAmount() / potWinners) * potValue;
                }

            }
            //Spectator gets percentage of total pot according to how much he bet compared to other winners
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
        return result;
    }
}
