package Monitors;


import Monitors.AuxiliaryClasses.HorseInPaddock;
import Monitors.AuxiliaryClasses.Parameters;
import Threads.Broker;
import Threads.Horse;
import Threads.Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@link Paddock} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by {@link Horse}s and {@link Spectator}s.
 * <p>
 * This is where the {@link Horse}s are paraded for the {@link Spectator}s.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace
 * @see Broker
 * @see Horse
 * @see Spectator
 */

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

    /**
     * The {@link Horse}s enter the paddock and add their information to the {@link #horsesInPaddock} array, then they wait until all the {@link Spectator}s have reached the {@link Paddock}, at the end the last {@link Horse} awakes the {@link Spectator}s.
     *
     * @param horseID ID of the calling thread.
     * @param pnk Max step size.
     */

    public static void proceedToPaddock(int horseID, int pnk){
        r1.lock();
        try {
            ((Horse)Thread.currentThread()).setState("AT_THE_PADDOCK");
            horses[horsesInPaddock++] = new HorseInPaddock(horseID, pnk);
            if (horsesInPaddock == Parameters.getNumberOfHorses()) {
                int total_pnk = 0;
                for (HorseInPaddock horse : horses) total_pnk += horse.getPnk();
                for (HorseInPaddock horse : horses){
                    if(Parameters.getNumberOfHorses() > 1){
                        horse.setOdds(pnk / total_pnk / (1 - (pnk / total_pnk)));
                    }
                    else horse.setOdds(1.0);
                }
                allowSpectators = true;
                spectatorsCond.signal();
            }
        }catch(Exception ie){
            ie.printStackTrace();
        } finally{
            r1.unlock();
        }
    }

    /**
     * Called by the {@link Horse} to exit the {@link Paddock}.
     */

    public static void proceedToStartLine(){
        r1.lock();
        try{
            
            while (!allowHorses) {
                horsesCond.await();
            }

            if(--horsesInPaddock==0){
                allowHorses = false;
                spectatorsCond.signal();
            }

            horsesCond.signal();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
    }
    /**
     * Function in which the {@link Spectator} enters the {@link Paddock}. The last {@link Spectator} to enter wakes up the {@link Horse}s. In this function the {@link Spectator} determines in which {@link Horse} they will bet.
     * @return Returns the {@link Horse} in which the {@link Spectator} will bet.
     */
    public static HorseInPaddock goCheckHorses(){
        HorseInPaddock result = null;
        r1.lock();

        try{
            ((Spectator)Thread.currentThread()).setState("APPRAISING_THE_HORSES");
            while(!allowSpectators){
                spectatorsCond.await();
            }

            result = horses[ThreadLocalRandom.current().nextInt(horses.length)];

            if(++spectatorsInPaddock == Parameters.getNumberOfSpectators()){
                allowHorses = true;
                allowSpectators = false;
                spectatorsInPaddock = 0;
                horsesCond.signal();
            }

            spectatorsCond.signal();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
        return result;
    }
}
