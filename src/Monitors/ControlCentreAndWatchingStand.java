package Monitors;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.AuxiliaryClasses.Bet;
import Monitors.AuxiliaryClasses.HorsePos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ControlCentreAndWatchingStand{

    static public ReentrantLock r1= new ReentrantLock(false);

    static public Condition brokerCond = r1.newCondition();
    static public boolean lastHorseFinished = false;

    static public Condition spectatorsCond = r1.newCondition();
    public static boolean allowSpectators = false;

    static public int[] winnerHorses;
    static public int nSpectators = 0;
    static public int numberOfWinners = 0;
    static public int nHorsesInPaddock = 0;
    static public int nSpectatorsInPaddock = 0;

    //Broker Methods
    public static void summonHorsesToPaddock(){
        r1.lock();
        try{
            while(nSpectatorsInPaddock != Parameters.getNumberOfSpectators()){ 
                brokerCond.await();
            }
            nSpectatorsInPaddock = 0;
        }catch(InterruptedException e){
        
        }finally{
            r1.unlock();
        }
    }

    public static void startTheRace(){
        r1.lock();
        try{
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

    public static void reportResults(ArrayList<HorsePos> list) {
        r1.lock();
        try {
            winnerHorses = new int[list.size()];
            int i = 0;
            for (HorsePos horse : list)
                winnerHorses[i++] = horse.getHorseID();

            allowSpectators = true;
            spectatorsCond.signal();
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }

    static public boolean areThereAnyWinners(Bet[] bets) {
        boolean returnValue = false;
        r1.lock();
        try {
            for (Bet bet : bets)
                if (Arrays.asList(winnerHorses).contains(bet.getHorseID())) {
                    returnValue = true;
                    break;
                }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            r1.unlock();
        }

        return returnValue;
    }

    static public void entertainTheGuests(){
    }

    //Spectators methods
    static public void goCheckHorses(){
        r1.lock();
        try {
            if (++nSpectatorsInPaddock == Parameters.getNumberOfSpectators()) {
                brokerCond.signal();
            }
        }catch(Exception e){
        
        }finally{
            r1.unlock();
        }
    }
    
    static public void waitForNextRace(){
        r1.lock();
        try{
            nSpectators++;
            while(!allowSpectators){
                spectatorsCond.await();
            }
            if(--nSpectators == 0){
                allowSpectators = false;
            }
        }catch(InterruptedException e){
        
        }finally{
            r1.unlock();
        }
    }

    static public void goWatchTheRace(){
        r1.lock();
        try {
            nSpectators++;
            while (!allowSpectators) {
                spectatorsCond.await();
            }
            if (--nSpectators == 0) {
                allowSpectators = false;
            }
        }catch(InterruptedException ie){
        
        }finally{
            r1.unlock();
        }
    }
    
    static public boolean haveIWon(int horseIndex){
        boolean result = false;
        r1.lock();
        try{
            for(int i=0; i < winnerHorses.length; i++){
                if(horseIndex == winnerHorses[i]){
                    result = true;
                    numberOfWinners++;
                    break;
                }
            }
        }catch(Exception e){
        
        }finally{
            r1.unlock();
        }
        return result;
    }

    static public void relaxABit(){
    }

    //Horses methods
    static public void proceedToPaddock(){
        r1.lock();
        try {
            if (++nHorsesInPaddock == Parameters.getNumberOfHorses()) {
                allowSpectators = true;
                spectatorsCond.signal();
                nHorsesInPaddock = 0;
            }
        }catch(Exception e){
        
        }finally{
            r1.unlock();
        }
    }
    
    static public void makeAMove(){
        r1.lock();
        try {
            lastHorseFinished = true;
            brokerCond.signal();
        }catch(Exception e){
        
        }finally{
            r1.unlock();
        }
    }
}
