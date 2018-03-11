import Monitors.BettingCentre;
import Monitors.Interfaces.BettingCentre_Broker;
import Monitors.Paddock;
import Monitors.Stable;
import Threads.Broker;

public class HorseRace {
    public void main(String[] args){
        //Simulation variables
        int nHorses = 4;
        int nSpectators = 4;
        int numRaces = 4;
        //Monitors
        Paddock paddock = new Paddock(nHorses,nSpectators);
        BettingCentre bettingCentre = new BettingCentre(nSpectators);
        Stable stable = new Stable();

        //Threads
        Broker broker = new Broker((BettingCentre_Broker) bettingCentre);

        //Starting threads
        broker.run();
    }
}
