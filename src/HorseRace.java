import java.util.Random;

import Monitors.AuxiliaryClasses.Parameters;
import Threads.Broker;
import Threads.HorseAndJockey;
import Threads.Spectator;

public class HorseRace {
    public static void main(String[] args){
        //Simulation variables
        Random rng = new Random();
        Parameters.initialize(rng.nextInt(100),rng.nextInt(100)+ 1,rng.nextInt(100) + 1, rng.nextInt(100) + 1);
        
        Thread[] threads = new Thread[Parameters.getNumberOfHorses() + Parameters.getNumberOfSpectators() + 1];

        int i = 0;
        threads[i] = new Broker();
        threads[i].start();

        for(i = 1; i<Parameters.getNumberOfHorses() + 1; i++){
            threads[i] = new HorseAndJockey();
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
        }
    }
}
