package Monitors.Interfaces;

import Monitors.AuxiliaryClasses.Bet;
import static Monitors.BettingCentre.*;

public interface BettingCentre_Spectator {

    static public int collectGains(int procID){
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
    static void placeBet(int procID, int betAmount)  throws IllegalMonitorStateException{
        r1.lock();
        try{

            while(!previousBetAccepted)
                spectatorCond.await();
            bets[currentNumberOfSpectators++] = new Bet(procID, betAmount);
            newBets = true;
            previousBetAccepted = false;
            brokerCond.signal();
        }catch (IllegalMonitorStateException | InterruptedException e)
        {e.printStackTrace();}
        finally {
            r1.unlock();
        }
    }
}
