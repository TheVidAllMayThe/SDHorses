package Monitors;

import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.AuxiliaryClasses.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RaceTrack {
    private static ReentrantLock r1 = new ReentrantLock();
    private static Condition horsesCond = r1.newCondition();

    private static HorsePos[] horses = new HorsePos[Parameters.getNumberOfHorses()];
    private static int numHorses = 0;
    private static int numHorsesFinished = 0;

    //Broker methods
    public static void startTheRace(){
        r1.lock();
        try{
            horses[0].setMyTurn(true);
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
        int returnValue = -1;
        r1.lock();
        try{

            horses[numHorses] = new HorsePos(pID, 0, false);
            returnValue = numHorses++;

            while(!horses[returnValue].isMyTurn()){
                horsesCond.await();
            }

        }catch (InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return returnValue;
    }

    public static void makeAMove(int horsePos, int moveAmount) {
        r1.lock();
        try {
            while (!horses[horsePos].isMyTurn()) {
                horsesCond.await();
            }
            horses[horsePos].addPos(moveAmount);
            horses[horsePos].setMyTurn(false);

            if(numHorsesFinished+1 != Parameters.getNumberOfHorses())
                for(int i = horsePos + 1; i != horsePos; i++){
                    i = i%Parameters.getNumberOfHorses();
                    if(!horses[i].isFinished()){
                        horses[i].setMyTurn(true);
                        break;
                    }
                }
            else horses[horsePos].setMyTurn(true);


        } catch (IllegalMonitorStateException | InterruptedException e) {
            e.printStackTrace();
            r1.unlock();
        }
    }

    public static boolean hasFinishLineBeenCrossed(int horsePos){ 
        boolean returnVal = false;

        try{
            if (horses[horsePos].getPos() >= Parameters.getRaceLength()) {
                returnVal = true;
                horses[horsePos].setFinished(true);
                numHorsesFinished++;
            }

            horsesCond.signalAll();

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            r1.unlock();
        }

        return returnVal;
    }

}
