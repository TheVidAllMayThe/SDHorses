package Monitors;

import Monitors.Interfaces.ControlCenterAndWatchingStand_Broker;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ControlCentreAndWatchingStand implements ControlCenterAndWatchingStand_Broker, ControlCenterAndWatchingStand_Spectator{
    private ReentrantLock r1;

    private Condition broker;
    private Condition spectators;
    private boolean brokerUnlocked;
    private boolean spectatorsUnlocked;;

    private int numHorsesFinished;
    private int horsesThatWon[];

    public ControlCentreAndWatchingStand(int nHorses){
        this.r1 = new ReentrantLock(false);

        this.broker = r1.newCondition();
        this.spectator = r1.newCondition();
        this.brokerUnlocked = false;
        this.spectatorsUnlocked = false;

        this.numHorsesFinished = 0;
    }

    public void blockBroker(){
        r1.lock();
        try{
            while(!brokerUnlocked){
                broker.await();
            }

            this.brokerUnlocked = false;
        }catch(InterruptedException){

        }finally{
            r1.unlock();
        }
    }

    public void unlockSpectators(){
        r1.lock();
        this.spectatorsUnlocked = true;
        spectators.signal();
        r1.unlock();
    }

    public void writeHorsesThatWon(int[] winners){
        r1.lock();
        horsesThatWon = winners;
        r1.unlock();
    }

    public boolean areThereAnyWinners(){
        boolean result;
        r1.lock();
        result = (this.horsesThatWon != null);
        r1.unlock();
        return result;
    }

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
