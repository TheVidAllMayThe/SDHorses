package Monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Stable {

    private ReentrantLock r1;
    private Condition horsesToPaddock;
    private boolean canHorsesMoveToPaddock;
    private int totalNumHorses;
    private int numHorses;

    public Stable(int totalNumHorses){
        r1 = new ReentrantLock();
        horsesToPaddock = r1.newCondition();
        canHorsesMoveToPaddock = false;
        this.totalNumHorses = totalNumHorses;
        this.numHorses = 0;
    }

    void summonHorsesToPaddock(){
        r1.lock();
        try{
            canHorsesMoveToPaddock = true;
            horsesToPaddock.signal();
        }catch (IllegalMonitorStateException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
    }

    void proceedToPaddock(){
        r1.lock();
        numHorses++;
        try{
            while(!canHorsesMoveToPaddock)
                horsesToPaddock.wait();

            if(numHorses == totalNumHorses){ //If it is the las horse to leave the Stable then the following horses will have to wait
                numHorses = 0;
                canHorsesMoveToPaddock = false;
            }
        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }


}
