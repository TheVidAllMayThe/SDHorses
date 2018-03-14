package Monitors;

import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.AuxiliaryClasses.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RaceTrack {
    public static ReentrantLock r1 = new ReentrantLock(true);
    public static Condition raceStarted = r1.newCondition();
    public static Condition resultsForBroker = r1.newCondition();
    public static boolean lastHorseFinished = false;
    public static boolean canRace = false;
    public static HorsePos[] horses = new HorsePos[Parameters.getNumberOfHorses()];
    public static int numHorses;

    public int proceedToStartLine(int pID){   //Returns the pos in the array of Horses
        r1.lock();
        try{
            horses[numHorses++] = new HorsePos(pID, 0);
        }catch (IllegalMonitorStateException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return numHorses - 1;
    }

    public void makeAMove(int horsePos, int moveAmount){
        r1.lock();
        try{
            while (!canRace)
                raceStarted.await();

            this.horses[horsePos].addPos(moveAmount);
            raceStarted.signal();

        }catch (IllegalMonitorStateException | InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
    }

    public boolean hasFinishLineBeenCrossed(int pID){
        r1.lock();
        boolean returnVal = false;
        for (int i = 0; i < numHorses; i++) {
            if (horses[i].horseID == pID){
                if(horses[i].pos > this.raceLength) {
                    returnVal = true;
                    break;
                }
            }
        }

        r1.unlock();
        return returnVal;
    }


}
