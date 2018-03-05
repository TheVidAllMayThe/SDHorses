public class Broker extends Thread{
    private final Paddock_Broker pb;
    private final BettingCentre_Broker bcb;

    public Broker(Paddock_Broker pb, BettingCentre_Broker bcb){
        this.pb = pb;
        this.bcb = bcb;
    }

    @Override
    public void run(){
        pb.SummonHorsesToPaddock();
        bcb.acceptTheBets();
    }
}
