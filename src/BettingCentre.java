import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BettingCentre implements BettingCentre_Broker{
    private final Lock r1;
    private final Condition broker;

    public BettingCentre(){
        this.r1 = new ReentrantLock(false);
        this.broker = r1.newCondition();
    }

    public void acceptTheBets(){
        System.out.println("Waiting for bets");
    }
}
