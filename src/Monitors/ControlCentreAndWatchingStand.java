package Monitors;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.AuxiliaryClasses.Bet;
import Monitors.AuxiliaryClasses.HorsePos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ControlCentreAndWatchingStand{

    private static ReentrantLock r1= new ReentrantLock(false);
    private static Condition brokerCond = r1.newCondition();
    private static boolean lastHorseFinished = false;
    private static Condition spectatorsCond = r1.newCondition();
    private static boolean allowSpectators = false;
    private static int[] winnerHorses;
    private static int nSpectators = 0;
    static public int numberOfWinners = 0;
    private static int nHorsesInPaddock = 0;
    private static int nHorsesFinishedRace = 0;


    //Broker Methods
    public static void summonHorsesToPaddock(){
        r1.lock();
        try{
            while(nSpectators != Parameters.getNumberOfSpectators()){
                brokerCond.await();
            }
            nSpectators = 0;
        }catch(InterruptedException e){
            e.printStackTrace();
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

    
    static public void waitForNextRace(){
        r1.lock();
        try{

            while(!allowSpectators){
                spectatorsCond.await();
            }
            if(++nSpectators == Parameters.getNumberOfSpectators()){
                allowSpectators = false;
                brokerCond.signal();
            }

            spectatorsCond.signal();
        }catch(InterruptedException e){
            e.printStackTrace();
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
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
    }
    
    static public boolean haveIWon(int horseIndex){
        boolean result = false;
        r1.lock();
        try{
            for (int winnerHorse : winnerHorses) {
                if (horseIndex == winnerHorse) {
                    result = true;
                    numberOfWinners++;
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }
    
    static public void makeAMove(){
        r1.lock();
        try {
            if(++nHorsesFinishedRace == Parameters.getNumberOfHorses()){
                nHorsesFinishedRace = 0;
                lastHorseFinished = true;
                brokerCond.signal();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }
}
