import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Paddock implements Paddock_Broker{
    private final Lock r1;
    private final Condition broker;
    private final Condition horses;
    private final Condition spectators;

    public Paddock(){
        this.r1 = new ReentrantLock(false);
        r1.lock();
        this.broker = r1.newCondition();
        this.horses = r1.newCondition();
        this.spectators = r1.newCondition();
    }

    public void SummonHorsesToPaddock(){
        System.out.println("Announcing next race");
        r1.unlock();
    }
}
