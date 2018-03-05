public class HorseRace {
    public void main(String[] args){
        Paddock paddock = new Paddock();
        BettingCentre bettingCentre = new BettingCentre();
        Broker broker = new Broker((Paddock_Broker) paddock, (BettingCentre_Broker) bettingCentre);

        broker.run();
    }
}
