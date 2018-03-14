package Monitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RaceTrack {
    private ReentrantLock r1;

    private Condition broker;
    private Condition horses;
    private boolean brokerUnlocked;
    private boolean horsesUnlocked;

    private ArrayList<HorseInfo> horsesInfo;
    private int numHorses;
    private final int raceLength;


    public RaceTrack(int totalNumHorses, int raceLength){
        r1 = new ReentrantLock(false);

        broker= r1.newCondition();
        horses = r1.newCondition();
        brokerUnlocked = false;
        horsesUnlocked = false;

        this,horsesInfo = new ArrayList<HorseInfo>();
        this.numHorses = totalNumHorses;
        this.raceLength = raceLength;
    }

    public void blockBroker(){
        r1.lock();
        try{
            while(!brokerUnlocked){
                broker.await();
            }

            this.brokerUnlocked = false;
        }catch(InterruptedException e){
        
        }finally{
            r1.unlock();
        }
    }

    public void unlockHorse(){
        r1.lock();
        horsesUnlocked = true;
        horses.signal();
        r1.unlock();
    }

    public int[] startTheRace(){
        r1.lock();

        try{
            this.canRace = true;
            this.raceStarted.signal();
            while(!lastHorseFinished)
                resultsForBroker.await();
        }catch(IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally{
            r1.unlock();
        }
        ArrayList<HorsePos> horsestmp = new ArrayList<>(Arrays.asList(horses));
        HorsePos min = Collections.min(horsestmp);
        horsestmp.remove(min);
        for (HorsePos horse: horsestmp)
            if(horse.compareTo(min)>0)
                horsestmp.remove(horse);
        int[] winners = new int[horsestmp.size()];

        int i = 0;
        for(HorsePos horse: horsestmp){
            winners[i++] = horse.horseID;
        }
        return winners;


    }

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

    public int[] getWinners(){
        int[] result = int[horsesInfo.size()]; 
        r1.lock();
        for(int i=0; i<horseInfo.size(); i++) result[i] = horseInfo.get(i).horseID;
        r1.unlock();
        return result;
    }

    private class HorseInfo{
        int horseID;
        int pos;
        int numSteps;

        HorseInfo(int horseID, int pos) {
            this.horseID = horseID;
            this.pos = pos;
            this.numSteps = 0;
        }

        void move(int amount){
            pos += amount;
            this.numSteps++;
        }
    }
}
