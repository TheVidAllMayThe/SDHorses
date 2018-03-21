package Main;

import java.util.Random;

import Monitors.AuxiliaryClasses.Parameters;
import Threads.Broker;
import Threads.HorseAndJockey;
import Threads.Spectator;

/**
* The Main.HorseRace program creates one thread of type Broker and several
* of type HorseAndJockey and Spectator and runs them.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Broker
* @see HorseAndJockey
* @see Spectator
*/

public class HorseRace {
    public static void main(String[] args){
        //Simulation variables
        Random rng = new Random();
        Parameters.initialize(rng.nextInt(400),rng.nextInt(1000)+ 1,rng.nextInt(1000) + 1, rng.nextInt(100) + 1);
        
        Thread[] threads = new Thread[Parameters.getNumberOfHorses() + Parameters.getNumberOfSpectators() + 1];

        int i = 0;
        threads[i] = new Broker();
        threads[i].start();

        for(i = 1; i<Parameters.getNumberOfHorses() + 1; i++){
            threads[i] = new HorseAndJockey(i-1);
            threads[i].start();
        }

        for(; i<Parameters.getNumberOfSpectators() + Parameters.getNumberOfHorses() + 1; i++){
            threads[i] = new Spectator();
            threads[i].start();
        }

        try{
            for(i=0; i<threads.length; i++){
                threads[i].join();
            }
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }
    }
}
