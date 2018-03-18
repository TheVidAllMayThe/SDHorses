import Monitors.AuxiliaryClasses.Parameters;
import Threads.Broker;
import Threads.HorseAndJockey;
import Threads.Spectator;

public class HorseRace {
    public static void main(String[] args){
        //Simulation variables
        Parameters.initialize(4,3,3, 100, 100);

        new Broker().start();

        for(int i = 0; i<Parameters.getNumberOfHorses(); i++){
            new HorseAndJockey().start();
        }

        for(int i = 0; i<Parameters.getNumberOfSpectators(); i++){
            new Spectator().start();
        }
    }
}
