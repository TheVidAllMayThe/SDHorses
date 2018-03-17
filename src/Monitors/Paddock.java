package Monitors;

import Monitors.AuxiliaryClasses.HorseInPaddock;
import Monitors.AuxiliaryClasses.Parameters;
import Monitors.Interfaces.Paddock_Spectators;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Paddock{
    static public final Lock r1 = new ReentrantLock(false);

    static public final Condition horsesCond = r1.newCondition();
    static public Boolean allowHorses = false;

    static public final Condition spectatorsCond = r1.newCondition();
    static public Boolean allowSpectators = false;

    static public final HorseInPaddock horses[] = new HorseInPaddock[Parameters.getNumberOfHorses()];
    static public int horsesInPaddock = 0;
    static public int spectatorsInPaddock = 0;

    //Horses methods
    public static void proceedToPaddock(int horseID, int pnk){
        r1.lock();
        try {
            horses[horsesInPaddock++] = new HorseInPaddock(horseID, pnk);
            if(horsesInPaddock == Parameters.getNumberOfHorses()){
                ControlCentreAndWatchingStand.allowSpectators = true;
                ControlCentreAndWatchingStand.spectatorsCond.signal();
            }

            while(!allowHorses){
                horsesCond.await();
            }

        }catch(InterruptedException ie){
            ie.printStackTrace();

        }finally{
            r1.unlock();
        }
    }

    public static void proceedToStartLine(){
        r1.lock();
        try{
            if(--horsesInPaddock == 0){
                allowHorses = false;
                allowSpectators = true;
                spectatorsCond.signal();
            }
        }catch(Exception e){
        
        }finally{
            r1.unlock();
        }
    }

    //Spectators methods
    public static void goCheckHorses(){
        r1.lock();
        try{
            if(++spectatorsInPaddock = Parameters.getNumberOfSpectators()){
                Stable.isLastSpectatorInPaddock = true;
                Stable.lastSpectatorInPaddock.signal();
            }

            while(!allowSpectators){
                spectatorsCond.await();
            }

            if(--spectatorsInPaddock == 0){
                allowSpectators = false;
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
                horsesCond.signal();
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
}
