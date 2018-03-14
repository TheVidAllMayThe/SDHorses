package Monitors;

import Monitors.AuxiliaryClasses.Bet;
import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Broker;
import Monitors.Interfaces.ControlCenterAndWatchingStand_Spectator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

<<<<<<< HEAD
import static Monitors.RaceTrack.horses;
=======
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
>>>>>>> c8c52db1988e170538acfe5a8c1b5ef6365b4610

public class ControlCentreAndWatchingStand implements ControlCenterAndWatchingStand_Broker, ControlCenterAndWatchingStand_Spectator{
    static public ReentrantLock r1= new ReentrantLock(false);
    static public Condition brokerLeave = r1.newCondition();
    static public Condition spectatorWaitingForResult = r1.newCondition();
    public static boolean resultsReported = false;
    static public int[] winnerHorses;



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
