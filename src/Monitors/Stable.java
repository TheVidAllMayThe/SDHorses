package Monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Stable {

    private ReentrantLock r1;

    private Condition horses;
    private boolean horsesUnlocked;

    private int totalNumHorses;
    private int numHorses;

    public Stable(int totalNumHorses){
        r1 = new ReentrantLock();

        horses = r1.newCondition();
        horsesUnlocked = false;

        this.totalNumHorses = totalNumHorses;
        this.numHorses = 0;
    }

    void blockHorses(){
        r1.lock();
        try{
            while(!horsesUnlocked){
                horses.await();
            }
        catch(InterruptedException ie){
        
        }finally{
            r1.unlock();
        }
        }
    }
    
    void unlockHorses(){
        r1.lock();
        horsesUnlocked = true;
        horses.signal();
        r1.unlock();
    }

    void setHorsesUnlocked(boolean b){
        r1.lock();
        this.horsesUnlocked = b;
        r1.unlock();
    }

    void boolean isEmpty(){
        boolean result;
        r1.lock();
        result = numHorses == 0;
        r1.unlock();
        return result;
    }

    void proceedToStable(){
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
