package Monitors;

import Monitors.Interfaces.BettingCentre_Broker;
import Monitors.Interfaces.BettingCentre_Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BettingCentre implements BettingCentre_Broker, BettingCentre_Spectator{
    private final Lock r1;
    private final Condition brokerCond;
    private final Condition spectatorCond;
    private boolean previousBetAccepted;
    private boolean previousBetHonored;
    private boolean anyBets;
    private boolean currentlyRefunding;
    private final int numSpectators;
    private final Bet[] bets;
    private int currentNumberOfSpectators;

    public BettingCentre(int numSpectators){
        this.r1 = new ReentrantLock(false);
        this.spectatorCond = r1.newCondition();
        this.brokerCond = r1.newCondition();
        this.numSpectators = numSpectators;
        this.bets = new Bet[numSpectators];
        this.currentNumberOfSpectators = 0;
        this.anyBets = false;
        this.previousBetAccepted = true;
        this.previousBetAccepted= true;
        this.currentlyRefunding = false;
    }

    public void acceptTheBets() throws IllegalMonitorStateException{
        r1.lock();

        try {
            do {
                while (!anyBets)
                    brokerCond.await();

                bets[currentNumberOfSpectators - 1].accepted = true;

                spectatorCond.signal();
            }while (currentNumberOfSpectators != numSpectators);
        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }

    public void placeBet(int procID, int betAmount)  throws IllegalMonitorStateException{
        r1.lock();
        try{

            while(!previousBetAccepted)
                spectatorCond.await();
            bets[currentNumberOfSpectators++] = new Bet(procID, betAmount);
            anyBets = true;
            previousBetAccepted = false;
            brokerCond.signal();
        }catch (IllegalMonitorStateException | InterruptedException e)
        {e.printStackTrace();}
        finally {
            r1.unlock();
        }
    }

    public void honorBet(){
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

    public int collectGains(int procID){
        r1.lock();
        int returnValue = 0;
        try{
            while(!currentlyRefunding)
                spectatorCond.await();

            for(Bet e: bets){
                if(e.spectatorID == procID){
                    returnValue = e.betAmount;
                    e = null;
                    break;
                }
            }
            brokerCond.signal();
            previousBetHonored = false;
        }
        catch (IllegalMonitorStateException | InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return returnValue;
    }

    private class Bet{
        public int spectatorID;
        public int betAmount;
        public boolean accepted;

        public Bet(int proc, int betAmount) {
            this.spectatorID = proc;
            this.betAmount = betAmount;
            this.accepted = false;
        }
    }
}
