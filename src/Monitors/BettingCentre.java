package Monitors;

import Monitors.AuxiliaryClasses.Bet;
import Monitors.Interfaces.BettingCentre_Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BettingCentre implements BettingCentre_Spectator{
    
    public static final Lock r1 = new ReentrantLock(false);

    public static final Condition brokerCond = r1.newCondition();
    public static boolean waitingOnBroker = false;

    public static final Condition spectatorCond = r1.newCondition();
    public static boolean resolvedSpectator = true;

    public static boolean currentlyRefunding = true;
    public static final Bet[] bets  = new Bet[Parameters.getNumberOfSpectators()];

    public static int currentNumberOfSpectators = 0;
    public static int potValue = 0;
    public static int amountOfWinners = 0;


    //Broker methods
    public static void acceptTheBets() throws IllegalMonitorStateException{
        r1.lock();
        try {
            do{
                while (!waitingOnBroker){
                    brokerCond.await();
                }

                waitingOnBroker = false;
                resolvedSpectator = true;
                spectatorCond.signal();

            }while(currentNumberOfSpectators != Parameters.getNumberOfSpectators());

            currentNumberOfSpectators = 0;
        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }
    
    public static Bet[] areThereAnyWinners(){
        r1.lock();
        try{
            return bets;
        }catch(Exception e){
        
        }finally{
            r1.unlock();
        }
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            r1.unlock();
        }
    }

    //Spectators methods
    public static void placeABet(int pid, int value){
        r1.lock();
        try{
            bets[currentNumberOfSpectators++] = new Bet(pid, value, horseID);
            waitingOnBroker = true;
            brokerCond.signal();
            
            while(!resolvedSpectator){
                spectatorCond.await();
            }
            resolvedSpectator = false;
            potValue += value;

        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public static int goCollectTheGains(){
        r1.lock();
        try{
            waitingOnBroker = true;
            brokerCond.signal();

            while(!resolvedSpectator){
                spectatorCond.await();
            }
            resolvedSpectator = false;
            return potValue/ControlCentreAndWatchingStand.numberOfWinners;
        }catch(InterruptedException ie){
        
        }finally{
            r1.unlock();
        }
    }
}
