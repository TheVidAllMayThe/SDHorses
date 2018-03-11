package Monitors;

import Monitors.Interfaces.ControlCenterAndWatchingStand_Broker;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Horse;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ControlCentreAndWatchingStand implements ControlCenterAndWatchingStand_Broker, ControlCenterAndWatchingStand_Spectator, ControlCenterAndWatchingStand_Horse{
    private ReentrantLock r1;
    private int horsesThatWon[];
    private Condition brokerLeave;
    private Condition spectatorWaitingForResult;
    private boolean canBrokerLeave;
    private boolean resultsReported;

    public ControlCentreAndWatchingStand(int nHorses){
        this.horsesThatWon = new int[nHorses];
        this.r1 = new ReentrantLock(false);
        this.brokerLeave = r1.newCondition();
        this.spectatorWaitingForResult = r1.newCondition();
        this.canBrokerLeave = false;
        this.resultsReported = false;
    }

    public void summonHorsesToPaddock(){
        r1.lock();
        try{
            while(!canBrokerLeave){
                brokerLeave.await();
            }

            this.canBrokerLeave = false;
        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public void startTheRace(){
        r1.lock();
        try{
            while(!canBrokerLeave){
                brokerLeave.await();
            }

            canBrokerLeave = false;

        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public void reportResults(int pID){
        r1.lock();
        this.resultsReported = true;
        this.spectatorWaitingForResult.signal();

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
