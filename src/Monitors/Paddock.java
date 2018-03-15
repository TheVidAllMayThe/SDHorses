package Monitors;

import Monitors.AuxiliaryClasses.HorseInPaddock;
import Monitors.AuxiliaryClasses.Parameters;
import Monitors.Interfaces.Paddock_Spectators;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Paddock{
    static public final Lock r1;
    static public final Condition horsesLeave;
    static public final Condition spectatorsLeave;
    static public final Condition spectatorsEnter;
    static public final HorseInPaddock horses[];
    static public final int nHorses;
    static public final int nSpectators;
    static public Boolean allowHorsesEnter;
    static public Boolean allowHorsesLeave;
    static public Boolean allowSpectatorsEnter;
    static public Boolean allowSpectatorsLeave;
    static public int horsesInPaddock;
    static public int spectatorsInPaddock;

    public Paddock(int nHorses, int nSpectators){
        this.r1          = new ReentrantLock(false);
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

    public static void proceedToPaddock(int horseID, int pnk){
        r1.lock();
        try {
            horses[horsesInPaddock++] = new HorseInPaddock(horseID, pnk);
            if(horsesInPaddock == Parameters.getNumberOfHorses()){
                allowSpectatorsEnter = true;
                spectatorsEnter.signal();
            }

            while(!allowHorsesLeave){
                horsesLeave.await();
            }
            if (--horsesInPaddock == 0)
                allowHorsesLeave = false;

        }catch(InterruptedException ie){
            ie.printStackTrace();

        }finally{
            r1.unlock();
        }
    }


}
