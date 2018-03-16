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

    public static Condition horsesCond = r1.newCondition();

    public static Condition resultsForBroker = r1.newCondition();
    public static boolean lastHorseFinished = false;
    
    public static HorsePos[] horses = new HorsePos[Parameters.getNumberOfHorses()];
    public static int numHorses = 0;
    public static boolean[] whoseTurn = new Boolean[Parameters.getNumberOfHorses()];

    //Horses methods
    public static int proceedToStartLine(int pID){   //Returns the pos in the array of Horses
        r1.lock();
        try{
            horses[numHorses++] = new HorsePos(pID, 0);
            
            if(numHorses == Parameters.getNumberOfHorses()){
                Paddock.allowSpectators = true;
                Paddock.spectatorsCond.signal();
            }

            while(!whoseTurn[numHorses-1]){
                horsesCond.await();
            }

        }catch (IllegalMonitorStateException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return numHorses - 1;
    }

    public static boolean makeAMove(int horsePos, int moveAmount){
        r1.lock();
        try{
            horses[horsePos].addPos(moveAmount);
            whoseTurn[horsePos] = false;
            whoseTurn[(horsePos + 1) % numHorses] = true;
            horsesCond.signal();

            while (!whoseTurn[horsePos]){
                horsesCond.await();
            }

        }catch (IllegalMonitorStateException | InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
    }

    public static boolean hasFinishLineBeenCrossed(int horsePos){
        boolean returnVal = false;
        r1.lock();
        try{
            if(horses[horsePos].getPos() > Parameters.getRaceLength()) {
                returnVal = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            r1.unlock();
        }

        return returnVal;
    }


}
