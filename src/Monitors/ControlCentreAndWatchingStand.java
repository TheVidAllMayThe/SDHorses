package Monitors;

import Monitors.Interfaces.ControlCenterAndWatchingStand_Broker;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ControlCentreAndWatchingStand implements ControlCenterAndWatchingStand_Broker, ControlCenterAndWatchingStand_Spectator{
    private ReentrantLock r1;

    private Condition broker;
    private Condition spectator;

    private boolean brokerUnlocked;
    private boolean spectatorUnlocked;

    private int horsesThatWon[];

    public ControlCentreAndWatchingStand(int nHorses){
        this.r1 = new ReentrantLock(false);

        this.broker = r1.newCondition();
        this.spectator = r1.newCondition();

        this.brokerUnlocked = false;
        this.spectatorUnlocked = false;

        this.horsesThatWon = new int[nHorses];
    }

    public void acceptTheBets(){
        r1.lock();
        try{
            while(!canBrokerLeave){
                broker.await();
            }

            this.brokerUnlocked = false;
        }catch(InterruptedException | IllegalMonitorStateException e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    public int[] areThereAnyWinners(){
        r1.lock();
        int[] returnValue = null;

        try{
            while(!canBrokerLeave){
                brokerLeave.await();
            }

            canBrokerLeave = false;
            returnValue = horsesThatWon;
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }

        return returnValue;
    }

    public void reportResults(int[] winners){
        r1.lock();
        horsesThatWon = winners;
        r1.unlock();
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
