package Monitors;

import Monitors.AuxiliaryClasses.Bet;
import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Broker;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Spectator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static Monitors.RaceTrack.horses;

public class ControlCentreAndWatchingStand implements ControlCenterAndWatchingStand_Broker, ControlCenterAndWatchingStand_Spectator{
    static public ReentrantLock r1= new ReentrantLock(false);
    static public Condition brokerLeave = r1.newCondition();
    static public Condition spectatorWaitingForResult = r1.newCondition();
    public static boolean resultsReported = false;
    static public int[] winnerHorses;



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
