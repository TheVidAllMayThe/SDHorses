package Monitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RaceTrack {
    private ReentrantLock r1;
    private Condition raceStarted;
    private Condition resultsForBroker;
    private boolean lastHorseFinished;
    private boolean canRace;
    private HorsePos[] horses;
    private int numHorses;
    private final int raceLength;


    public RaceTrack(int totalNumHorses, int raceLength){
        r1 = new ReentrantLock(true);
        raceStarted = r1.newCondition();
        resultsForBroker = r1.newCondition();
        lastHorseFinished = false;
        canRace = false;
        horses = new HorsePos[numHorses];
        this.numHorses = totalNumHorses;
        this.raceLength = raceLength;
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

    private class HorsePos implements Comparable<HorsePos>{
        int horseID;
        int pos;
        int numSteps;

        HorsePos(int horseID, int pos) {
            this.horseID = horseID;
            this.pos = pos;
            this.numSteps = 0;
        }

        void addPos(int amount){
            pos += amount;
            this.numSteps++;
        }

        @Override
        public int compareTo(HorsePos horse){
            if (horse.numSteps<this.numSteps)
                return -1;

            else if (horse.numSteps>this.numSteps)
                return 1;

            else {
                if (horse.pos<this.pos)
                    return -1;
                else if (horse.pos>this.pos)
                    return 1;
            }
            return 0;


        }
    }
}
