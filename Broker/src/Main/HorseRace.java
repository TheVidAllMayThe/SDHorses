package Main;

import Monitors.AuxiliaryClasses.Parameters;
import Threads.Broker;

import java.util.Random;

/**
* The Main.HorseRace program creates one thread of type Broker and several
* of type Horse and Spectator and runs them.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Broker
*/

public class HorseRace {
    public static void main(String[] args){
        //Simulation variables
        Random rng = new Random();
        Parameters.initialize(5,4,4, rng.nextInt(99)+1);


        Broker brokerInst = new Broker();
        brokerInst.start();
        try{
            brokerInst.join();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }
    }
}
