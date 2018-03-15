package Monitors;

import Monitors.AuxiliaryClasses.Bet;
import Monitors.Interfaces.BettingCentre_Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BettingCentre implements BettingCentre_Spectator{
    public static final Lock r1 = new ReentrantLock(false);
    public static final Condition brokerCond = r1.newCondition();
    public static final Condition spectatorCond = r1.newCondition();
    public static boolean newBets = false;
    public static boolean previousBetAccepted = true;
    public static boolean currentlyRefunding = false;
    public static int numSpectators;
    public static final Bet[] bets  = new Bet[numSpectators];
    public static int currentNumberOfSpectators;
    public static boolean previousBetHonored;

    static public void initialize(int numSpectators){
        BettingCentre.numSpectators = numSpectators;
        BettingCentre.currentNumberOfSpectators = 0;
    }


    public static void acceptTheBets() throws IllegalMonitorStateException{
        r1.lock();

        try {
            do {
                while (!newBets)
                    brokerCond.await();
                newBets = false;
                bets[currentNumberOfSpectators - 1].accepted = true;
                spectatorCond.signal();
            }while (currentNumberOfSpectators != numSpectators);
        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }
    public static void honorBets(){
        r1.lock();
        try{
            while(!previousBetHonored)
                brokerCond.await();

            spectatorCond.signal();
            previousBetHonored = true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            r1.unlock();
        }
    }


}
