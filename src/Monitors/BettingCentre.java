package Monitors;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.AuxiliaryClasses.Bet;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BettingCentre{
    
    private static final Lock r1 = new ReentrantLock();

    private static final Condition brokerCond = r1.newCondition();
    private static boolean waitingOnBroker = false;

    private static final Condition spectatorCond = r1.newCondition();
    private static boolean resolvedSpectator = false;

    private static boolean currentlyRefunding = true;
    private static final Bet[] bets  = new Bet[Parameters.getNumberOfSpectators()];
    private static int numWinners = 0;
    private static int currentNumberOfSpectators = 0;
    private static int potValue = 0;


    //Broker methods
    public static void acceptTheBets() throws IllegalMonitorStateException{
        r1.lock();
        try {
            do{
                resolvedSpectator = true;
                waitingOnBroker = false;
                spectatorCond.signal();

                while (!waitingOnBroker){
                    brokerCond.await();
                }


            }while(currentNumberOfSpectators != Parameters.getNumberOfSpectators());

            currentNumberOfSpectators = 0;
        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }
    
    public static int[] areThereAnyWinners(){
        int[] result = new int[bets.length];
        r1.lock();
        try{
            for(int i = 0; i < bets.length; i++)
                result[i] = bets[i].getHorseID();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
        return result;
    }

    static public boolean areThereAnyWinners(int[] winnerList) {
        boolean returnValue = false;

        r1.lock();
        try {
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

    public static void honorBets(){
        r1.lock();
        try{
            while(currentlyRefunding){
                while(!waitingOnBroker){
                    brokerCond.await();
                }
                
                waitingOnBroker = false;
                resolvedSpectator = true;
                spectatorCond.signal();
            }
            currentlyRefunding = true;
            numWinners = 0;
            potValue = 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            r1.unlock();
        }
    }

    //Spectators methods
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

    public static double goCollectTheGains(){
        double result = 0.0;
        r1.lock();
        try{
            waitingOnBroker = true;
            brokerCond.signal();

            while(!resolvedSpectator){
                spectatorCond.await();
            }
            resolvedSpectator = false;
            result = potValue/numWinners;
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
        return result;
    }
}
