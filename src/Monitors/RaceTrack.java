package Monitors;

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


    public RaceTrack(int totalNumHorses){
        r1 = new ReentrantLock();
        raceStarted = r1.newCondition();
        resultsForBroker = r1.newCondition();
        lastHorseFinished = false;
        canRace = false;
        horses = new HorsePos[numHorses];
        this.numHorses = 0;
    }

    public void startTheRace(){
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
        }catch (IllegalMonitorStateException | InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
    }

    private class HorsePos{
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
        }
    }
}
