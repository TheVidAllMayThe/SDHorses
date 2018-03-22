package Main;

import java.util.Random;

import Monitors.AuxiliaryClasses.Parameters;
import Threads.Broker;
import Threads.Horse;
import Threads.Spectator;

/**
* The Main.HorseRace program creates one thread of type Broker and several
* of type Horse and Spectator and runs them.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Broker
* @see Horse
* @see Spectator
*/

public class HorseRace {
    public static void main(String[] args){
        //Simulation variables
        Random rng = new Random();
        int numRaces = rng.nextInt(20)+ 1;
        Parameters.initialize(numRaces,4, rng.nextInt(20) + 1, rng.nextInt(100) + 1);
        
        Thread[] threads = new Thread[Parameters.getNumberOfHorses()*numRaces + Parameters.getNumberOfSpectators() + 1];

        int i = 0;
        threads[i] = new Broker();
        threads[i].start();

        for(i = 1; i<Parameters.getNumberOfHorses()*numRaces + 1; i++){
            threads[i] = new Horse(i-1, (i-1)/4);
            threads[i].start();
        }

        for(; i<Parameters.getNumberOfSpectators() + Parameters.getNumberOfHorses()*numRaces + 1; i++){
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
