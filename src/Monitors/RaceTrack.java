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
    public static Boolean[] whoseTurn = new Boolean[Parameters.getNumberOfHorses()];
    public static HorsePos[] horses = new HorsePos[Parameters.getNumberOfHorses()];
    public static int numHorses = 0;

    //Broker methods
    public static void startTheRace(){
        r1.lock();
        try{
            whoseTurn[0] = true;
            horsesCond.signal();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    public static ArrayList<HorsePos> reportResults(){
        ArrayList<HorsePos> result = null;
        r1.lock();
        try{
            ArrayList<HorsePos> winnerHorsesTmp = new ArrayList<>(Arrays.asList(horses));
            HorsePos min = Collections.min(winnerHorsesTmp);
            for(HorsePos horse: winnerHorsesTmp)
                if (horse.compareTo(min) > 0)
                    winnerHorsesTmp.remove(horse);
            result = winnerHorsesTmp;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
        return result;
    }

    //Horses methods
    public static int proceedToStartLine(int pID){   //Returns the pos in the array of Horses
        r1.lock();
        try{
            horses[numHorses++] = new HorsePos(pID, 0);
            
            while(!whoseTurn[numHorses-1]){
                horsesCond.await();
            }

        }catch (InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return numHorses - 1;
    }

    public static void makeAMove(int horsePos, int moveAmount){
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

    public static boolean[] hasFinishLineBeenCrossed(int horsePos){ //first boolean is if horse finished, last bool is if all finished
        boolean[] returnVal = {false, false};
        r1.lock();
        try{
            if(horses[horsePos].getPos() > Parameters.getRaceLength()) {
                returnVal[0] = true;
                if(--numHorses == 0){
                    whoseTurn[(horsePos + 1) % numHorses] = false;
                    returnVal[1] = true;
                }
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
