package Monitors;

import Monitors.Interfaces.Paddock_Horses;
import Monitors.Interfaces.Paddock_Spectators;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Paddock implements Paddock_Horses, Paddock_Spectators {
    private final Lock r1;
    private final Condition brokerLeave;
    private final Condition horsesEnter;
    private final Condition horsesLeave;
    private final Condition spectatorsEnter;
    private final Condition spectatorsLeave;
    private final HorseInPaddock horses[];
    private final int nHorses;
    private final int nSpectators;

    private Boolean allowHorsesEnter;
    private Boolean allowHorsesLeave;
    private Boolean allowSpectatorsEnter;
    private Boolean allowSpectatorsLeave;
    private int horsesInPaddock;
    private int spectatorsInPaddock;

    public Paddock(int nHorses, int nSpectators){
        this.r1 = new ReentrantLock(false);
        this.horsesEnter = r1.newCondition();
        this.horsesLeave = r1.newCondition();
        this.brokerLeave = r1.newCondition();
        this.spectatorsEnter = r1.newCondition();
        this.spectatorsLeave = r1.newCondition();
        this.allowHorsesEnter = false;
        this.allowHorsesLeave = false;
        this.allowSpectatorsEnter = false;
        this.allowSpectatorsLeave = false;
        this.nHorses = nHorses;
        this.nSpectators = nSpectators;
        this.horsesInPaddock = 0;
        this.spectatorsInPaddock = 0;
        this.horses = new HorseInPaddock[nHorses];
    }

    public void proceedToPaddock(int horseID, int pnk){
        r1.lock();
        try {
            while (!allowHorsesEnter) {
                horsesEnter.await();
            }

            this.horses[this.horsesInPaddock++] = new HorseInPaddock(horseID, pnk);

            if(horsesInPaddock == nHorses){
                this.allowSpectatorsEnter = true;
                this.allowHorsesEnter = false;
                spectatorsEnter.signal();
            }
            else{
                horsesEnter.signal();
            }

        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public HorseInPaddock[] goCheckHorses(){
        r1.lock();
        try {
            while (!allowSpectatorsEnter){
                spectatorsEnter.await();
            }

            this.spectatorsInPaddock++;

            if(spectatorsInPaddock == nSpectators){
                allowHorsesLeave = true;
                allowSpectatorsEnter = false;
                horsesLeave.signal();
                brokerLeave.signal();
            }
            else{
                spectatorsEnter.signal();
            }


        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
        return horses;
    }

    public void proceedToStartLine(){
        r1.lock();
        try {
            while (!allowHorsesLeave) {
                horsesLeave.await();
            }

            this.horsesInPaddock--;

            if(horsesInPaddock == 0){
                allowSpectatorsLeave = true;
                allowHorsesLeave = false;
                spectatorsLeave.signal();
            }
            else{
                horsesLeave.signal();
            }

        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public void goPlaceBet(){
        r1.lock();
        try{
            while(!allowSpectatorsLeave){
                spectatorsLeave.await();
            }

            this.spectatorsInPaddock--;

            if(spectatorsInPaddock == 0){
                allowSpectatorsLeave = false;
            }
            else{
                spectatorsLeave.signal();
            }

        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public class HorseInPaddock {
        public int horseID;
        public int pnk;

        HorseInPaddock(int horseID, int pnk) {
            this.horseID = horseID;
            this.pnk = pnk;
        }
    }
}
