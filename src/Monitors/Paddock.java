package Monitors;

import Monitors.AuxiliaryClasses.HorseInPaddock;
import Monitors.AuxiliaryClasses.Parameters;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;

public class Paddock{
    private static final Lock r1 = new ReentrantLock(false);

    private static final Condition horsesCond = r1.newCondition();
    private static Boolean allowHorses = false;

    private static final Condition spectatorsCond = r1.newCondition();
    private static Boolean allowSpectators = false;
    private static final HorseInPaddock horses[] = new HorseInPaddock[Parameters.getNumberOfHorses()];
    private static int horsesInPaddock = 0;
    private static int spectatorsInPaddock = 0;


    //Horses methods
    public static void proceedToPaddock(int horseID, int pnk){
        r1.lock();
        try {
            horses[horsesInPaddock++] = new HorseInPaddock(horseID, pnk);
            if (horsesInPaddock == Parameters.getNumberOfHorses()) {
                allowSpectators = true;
                int total_pnk = 0;
                for (HorseInPaddock horse : horses) total_pnk += horse.getPnk();
                for (HorseInPaddock horse : horses){
                    if(Parameters.getNumberOfHorses() > 1){
                        horse.setOdds(pnk / total_pnk / (1 - (pnk / total_pnk)));
                    }
                    else horse.setOdds(1.0);
                }
                spectatorsCond.signal();
            }
            while (!allowHorses) {
                horsesCond.await();
            }

        }catch(InterruptedException ie){
            ie.printStackTrace();
        } finally{
            r1.unlock();
        }
    }

    public static void proceedToStartLine(){
        r1.lock();
        try{

            if(--horsesInPaddock==0){
                allowHorses = false;
                spectatorsCond.signal();
            }

            horsesCond.signal();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            r1.unlock();
        }
    }

    //Spectators methods
    public static HorseInPaddock goCheckHorses(){
        HorseInPaddock result = null;
        r1.lock();

        try{
            while(!allowSpectators){
                spectatorsCond.await();
            }

            if(++spectatorsInPaddock == Parameters.getNumberOfSpectators()){
                allowHorses = true;
                horsesCond.signal();
                allowSpectators = false;
                spectatorsInPaddock = 0;
            }

            result = horses[ThreadLocalRandom.current().nextInt(horses.length)];

            spectatorsCond.signal();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
        return result;
    }
}
