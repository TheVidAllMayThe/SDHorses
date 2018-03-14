import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ControlCentreAndWatchingStand {
    private ReentrantLock r1;
    private int horsesThatWon[];
    private Condition brokerLeave;
    private Condition spectatorWaitingForResult;
    private boolean canBrokerLeave;
    private boolean resultsReported;

    ControlCentreAndWatchingStand(int nHorses){
        this.horsesThatWon = new int[nHorses];
        this.r1 = new ReentrantLock(false);
        this.brokerLeave = r1.newCondition();
        this.spectatorWaitingForResult = r1.newCondition();
        this.canBrokerLeave = false;
        this.resultsReported = false;
    }

    public void summonHorsesToPaddock(){
        r1.lock();
        try{
            while(!canBrokerLeave){
                brokerLeave.await();
            }

            this.canBrokerLeave = false;
        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public void startTheRace(){
        r1.lock();
        try{
            while(!canBrokerLeave){
                brokerLeave.await();
            }

            canBrokerLeave = false;

        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public void reportResults(){
        r1.lock();
        this.resultsReported = true;
        this.spectatorWaitingForResult.signal();
        r1.unlock();
    }
}
