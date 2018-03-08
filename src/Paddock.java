import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Paddock implements Paddock_Broker{
    private final Lock r1;
    private final Condition brokerWait;
    private final Condition horsesWait;
    private final Condition spectatorsWait;
    private final ArrayList<HorseInPaddock> horses = new ArrayList<>();

    Paddock(){
        this.r1 = new ReentrantLock(false);
        this.brokerWait = r1.newCondition();
        this.horsesWait = r1.newCondition();
        this.spectatorsWait = r1.newCondition();
    }

    public void SummonHorsesToPaddock(){
        
    }

    private class HorseInPaddock{
        public int horseID;
        public int pnk;
    }


}

