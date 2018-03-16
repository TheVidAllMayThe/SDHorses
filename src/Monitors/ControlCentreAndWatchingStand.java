package Monitors;


import Monitors.AuxiliaryClasses.Bet;
import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Spectator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static Monitors.RaceTrack.*;
import static Monitors.RaceTrack.lastHorseFinished;


public class ControlCentreAndWatchingStand implements ControlCenterAndWatchingStand_Broker, ControlCenterAndWatchingStand_Spectator{

    static public ReentrantLock r1= new ReentrantLock(false);

    static public Condition brokerCond = r1.newCondition();
    static public boolean lastHorseFinished = false;

    static public Condition spectatorCond = r1.newCondition();
    public static boolean allowSpectators = false;

    static public int[] winnerHorses;
    static public int nSpectators = 0;
    static public int numberOfWinners = 0;


    //Broker Methods
    public static void startTheRace(){
        r1.lock();
        try{
            RaceTrack.whoseTurn[0] = true;
            RaceTrack.horseCond.signal();

            while(!lastHorseFinished){
                brokerCond.await();
            }

            for(int i=0; i < Parameters.getNumberOfHorses(); i++){
                RaceTrack.whoseTurn[i] = false;
            }
            lastHorseFinished = false;
        }catch(IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally{
            r1.unlock();
        }

    }

    public static void reportResults() {
        r1.lock();
        try {
            ArrayList<HorsePos> winnerHorsesTmp = new ArrayList<>(Arrays.asList(RaceTrack.horses));
            HorsePos min = Collections.min(winnerHorsesTmp);
            //Hey David Almeida, 76377: Acho que esta linha estava aqui mal. Reve isto bitte
            //winnerHorsesTmp.remove(min);
            for (HorsePos horse : winnerHorsesTmp)
                if (horse.compareTo(min) > 0)
                    winnerHorsesTmp.remove(horse);

            winnerHorses = new int[winnerHorsesTmp.size()];
            int i = 0;
            for (HorsePos horse : winnerHorsesTmp)
                winnerHorses[i++] = horse.getHorseID();

            allowSpectator = true;
            spectatorCond.signal();
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
        r1.unlock();
    }

    static public boolean areThereAnyWinners() {
        boolean returnValue = false;
        r1.lock();
        try {
            for (Bet bet : BettingCentre.bets)
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
        Stable.canHorseMoveToStable = true;
        Stable.horsesToPaddock.signal();
    }

    //Spectators methods
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

    static public void watchRace(){
        r1.lock();
        try{
            nSpectators++;
            while(!allowSpectators){
                spectatorsCond.await();
            }
            if(--nSpectators == 0){
                allowSpectators = false;
            }
        catch(InterruptedException ie){
        
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
            return result;
        }catch(Exception e){
        
        }finally{
            r1.unlock();
        }
    }

    static public void relaxABit(){
    }
}
