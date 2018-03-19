package Monitors;

import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.AuxiliaryClasses.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RaceTrack {
    public static ReentrantLock r1 = new ReentrantLock();

    public static Condition horsesCond = r1.newCondition();
    public static boolean[] whoseTurn = new boolean[Parameters.getNumberOfHorses()];
    public static HorsePos[] horses = new HorsePos[Parameters.getNumberOfHorses()];
    public static int numHorses = 0;
    public static boolean finished = false;

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
            for(int i=0; i < winnerHorsesTmp.size(); i++){
                if (winnerHorsesTmp.get(i).compareTo(min) > 0)
                    winnerHorsesTmp.remove(i);
            }
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
        int idx = -1;
        r1.lock();
        try{
            idx = numHorses++;
            horses[idx] = new HorsePos(pID, 0);
            
            while(!whoseTurn[idx]){
                horsesCond.await();
            }

        }catch (InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return idx;
    }

    public static void makeAMove(int horsePos, int moveAmount){
        r1.lock();
        try{
            while (!finished && !whoseTurn[horsePos]){
                horsesCond.await();
            }
            horses[horsePos].addPos(moveAmount);
            whoseTurn[horsePos] = false;
            whoseTurn[(horsePos + 1) % Parameters.getNumberOfHorses()] = true;
            horsesCond.signalAll();

        }catch (IllegalMonitorStateException | InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
    }

    public static boolean hasFinishLineBeenCrossed(int horsePos){ 
        boolean returnVal = false;
        r1.lock();
        try{
            if(finished = true) returnVal = true;
            else{
                for(int i = 0; i < numHorses; i++){
                    if(horses[i].getPos() >= Parameters.getRaceLength()){
                        finished = true;
                        returnVal = true;
                        break;
                    }
                } 
            }
            if(returnVal){
                if(--numHorses==0){
                    for(int i=0; i < Parameters.getNumberOfHorses(); i++) whoseTurn[i] = false;
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
