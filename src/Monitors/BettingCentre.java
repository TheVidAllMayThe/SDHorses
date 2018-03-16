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
    public static int numSpectators;
    public static final Bet[] bets  = new Bet[numSpectators];

    public static int currentNumberOfSpectators;

    static public void initialize(int numSpectators){
        BettingCentre.numSpectators = numSpectators;
        BettingCentre.currentNumberOfSpectators = 0;
    }

    //Broker methods
    public static void acceptTheBets() throws IllegalMonitorStateException{
        r1.lock();
        try {
            while(currentNumberOfSpectators != numSpectators){
                while (!waitingOnBroker){
                    brokerCond.await();
                }

                waitingOnBroker = false;
                resolvedSpectator = true;
                spectatorCond.signal();
            }
        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
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
}
