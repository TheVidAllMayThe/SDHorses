public class HorseRace {
    public void main(String[] args){
        //Simulation variables
        int nHorses = 4;
        int nSpectators = 4;

        //Monitors
        Paddock paddock = new Paddock(nHorses,nSpectators);
        BettingCentre bettingCentre = new BettingCentre(nSpectators);

        //Threads
        Broker broker = new Broker((Paddock_Broker) paddock, (BettingCentre_Broker) bettingCentre);

        //Starting threads
        broker.run();
    }
}
