package Monitors;

import Monitors.Interfaces.BettingCentre_Broker;
import Monitors.Interfaces.BettingCentre_Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;

public class BettingCentre implements BettingCentre_Broker, BettingCentre_Spectator{
    private final Lock r1;

    private final Condition brokerCond;
    private final Condition spectator;
    private boolean brokerUnlocked;
    private boolean spectatorUnlocked;
    private boolean previousBetAccepted;
    private boolean previousBetHonored;
    private boolean currentlyRefunding;

    private final int numSpectators;
    private final ArrayList<Bet> bets;
    private int currentNumberOfSpectators;

    public BettingCentre(int numSpectators){
        this.r1 = new ReentrantLock(false);

        this.spectator = r1.newCondition();
        this.broker = r1.newCondition();

        this.brokerUnlocked = false;
        this.spectatorUnlocked = false;
        this.spectatorUnlocked = false;
        this.currentlyRefunding = false;

        this.currentNumberOfSpectators = 0;
        this.numSpectators = numSpectators;
        this.bets = new ArrayList<Bet>();;
    }

    public void blockBroker(){
        r1.lock();
        try{
            while(!brokerUnlocked){
                broker.await();
            } 
            brokerUnlocked = false;
        catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public void unlockSpectator(){
        r1.lock();
        spectatorUnlocked = true;
        spectator.signal();
        r1.unlock();
    }
    
    public void placeBet(int procID, int betAmount)  throws IllegalMonitorStateException{
        r1.lock();
        try{

            while(!previousBetAccepted)
                spectatorCond.await();
            bets.add(new Bet(procID, betAmount);
            currentNumberOfSpectators++;
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

    public boolean betsDone(){
        boolean result;
        r1.lock();
        result = this.currentNumOfSpectators == this.numSpectators;
        r1.unlock();
        return result;
    }

    public boolean paidAllSpectators(){
        boolean result;
        r1.lock();
        result = (this.bets.size() == 0);
        r1.unlock();
        return result;
    }

    private class Bet{
        public int spectatorID;
        public int betAmount;

        public Bet(int proc, int betAmount) {
            this.spectatorID = proc;
            this.betAmount = betAmount;
        }
    }
}
