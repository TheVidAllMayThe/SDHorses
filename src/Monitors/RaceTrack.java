package Monitors;

import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.AuxiliaryClasses.Parameters;

import javax.net.ssl.HostnameVerifier;
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
            for(int i = 0; i < Parameters.getNumberOfHorses(); i++)
                if(horses[i] == null)
                    horses[i] = new HorsePos(-1,0,false);

            horses[0].setMyTurn(true);
            horsesCond.signalAll();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    public static int[] reportResults(){

        int[] result = null;
        r1.lock();
        try{
            ArrayList<HorsePos> winnerHorsesTmp = new ArrayList<>();

            HorsePos min = Collections.min(Arrays.asList(horses));

            for(HorsePos horse : horses){
                if (horse.compareTo(min) == 0)
                    winnerHorsesTmp.add(horse);
            }

            result = new int[winnerHorsesTmp.size()];

            for(int i = 0; i < result.length; i++)
                result[i] = winnerHorsesTmp.get(i).getHorseID();

            horses = new HorsePos[Parameters.getNumberOfHorses()];
            numHorses = 0;
            numHorsesFinished = 0;

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
            if(horses[numHorses] == null)
                horses[numHorses] = new HorsePos(pID, 0, false);
            else
                horses[numHorses].setHorseID(pID);
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

            if(numHorsesFinished+1 != Parameters.getNumberOfHorses()) {
                for (int i = horsePos + 1; i != horsePos; i++) {
                    i = i % Parameters.getNumberOfHorses();
                    if (!horses[i].isFinished()) {
                        horses[i].setMyTurn(true);
                        break;
                    }
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
