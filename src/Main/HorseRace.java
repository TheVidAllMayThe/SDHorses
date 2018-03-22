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

        Parameters.initialize(100,40, 40, 100);

        Broker brokerInst = new Broker();
        brokerInst.start();
        Spectator[] spectators = new Spectator[Parameters.getNumberOfSpectators()];
        Horse[] horses = new Horse[Parameters.getNumberOfHorses() * Parameters.getNumberOfRaces()];

        for(int i = 0; i<Parameters.getNumberOfHorses() * Parameters.getNumberOfRaces(); i++){
            horses[i] = new Horse(i, i/Parameters.getNumberOfHorses());
            horses[i].start();
        }

        for(int i = 0; i < Parameters.getNumberOfSpectators(); i++){
            spectators[i] = new Spectator(i);
            spectators[i].start();
        }

        try{
            for(Spectator s: spectators)
                s.join();

            for(Spectator h: spectators)
                h.join();

            brokerInst.join();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }
    }
}
