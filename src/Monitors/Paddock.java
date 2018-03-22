package Monitors;


import Monitors.AuxiliaryClasses.HorseInPaddock;
import Monitors.AuxiliaryClasses.Parameters;
import Threads.Horse;
import Threads.Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The Paddock class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by Horses and Spectators.
 * <p>
 * This is where the Horses are paraded for the spectators.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace
 * @see Threads.Broker
 * @see Horse
 * @see Threads.Spectator
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
     * The Horses enter the paddock and add their information to the {@link #horsesInPaddock} array, then they wait until all the spectators have reached the paddock, at the end the last horse awakes the spectators.
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

            if(--horsesInPaddock==0){
                allowHorses = false;
                spectatorsCond.signal();
            }

            horsesCond.signal();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        } finally{
            r1.unlock();
        }
    }

    /**
     * Function in which the Spectator enters the paddock. The last Spectator to enter wakes up the Horses. In this function the spectator determines in which horse they will bet.
     * @return Returns the Horse in which the spectator will bet.
     */
    public static HorseInPaddock goCheckHorses(){
        HorseInPaddock result = null;
        r1.lock();

        try{
            ((Spectator)Thread.currentThread()).setState("APPRAISING_THE_HORSES");
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
