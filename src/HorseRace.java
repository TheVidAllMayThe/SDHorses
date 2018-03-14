import Monitors.*;
import Monitors.Interfaces.*;
import Threads.Broker;
import Threads.HorseAndJockey;
import Threads.Spectator;

public class HorseRace {
    public static void main(String[] args){
        //Simulation variables
        int nHorses = 4;
        int nSpectators = 4;
        int numRaces = 4;
        int raceLength = 10;
        //Monitors
        Paddock paddock = new Paddock(nHorses,nSpectators);
        BettingCentre bettingCentre = new BettingCentre(nSpectators);
        Stable stable = new Stable(nHorses);
        ControlCentreAndWatchingStand controlCentre = new ControlCentreAndWatchingStand(nHorses);
        RaceTrack raceTrack = new RaceTrack(nHorses, raceLength);

        //Threads
        Thread horses[] = new Thread[nHorses];
        Thread spectators[] = new Thread[nSpectators];
        Broker broker = new Broker(numRaces, controlCentre, (Stable_Broker) stable, bettingCentre, (RaceTrack_Broker) raceTrack);

        for(Thread i: horses){
            i = new HorseAndJockey(numRaces, raceLength, (Stable_Horse) stable, (RaceTrack_Horse) raceTrack, (Paddock_Horses) paddock);
        }

        for(Thread i: spectators){
            i = new Spectator(numRaces, controlCentre, bettingCentre, paddock);
        }

        broker.start();
        for(Thread i: horses){
            i.start();
        }

        for(Thread i: spectators){
            i.start();
        }

    }
}
