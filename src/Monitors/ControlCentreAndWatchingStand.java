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

    static public Condition resultsForBroker = r1.newCondition();
    static public boolean lastHorseFinished = false;

    static public Condition raceStarted = r1.newCondition();
    static public boolean canRace = false;

    static public Condition spectatorWaitingForResult = r1.newCondition();
    public static boolean resultsReported = false;

    static public int[] winnerHorses;


    //Broker Methods
    public static void startTheRace(){
        r1.lock();
        try{
            canRace = true;
            raceStarted.signal();
            while(!lastHorseFinished){
                resultsForBroker.await();
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

            resultsReported = true;
            spectatorWaitingForResult.signal();
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

            canBrokerLeave = false;
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

    public int watchRace(){
        r1.lock();
        int returnValue = -1;
        try{
            while(!resultsReported){
                spectatorWaitingForResult.await();
            }
            returnValue = horsesThatWon[0];
        }catch (IllegalMonitorStateException | InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return returnValue;
    }
}
