import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Paddock implements Paddock_Broker{
    private final Lock r1;
    private final Condition brokerCond;
    private final Condition horsesEnter;
    private final Condition horsesLeave;
    private final Condition spectatorsEnter;
    private final Condition spectatorsLeave;
    private final HorseInPaddock horses[];
    private final int nHorses;
    private final int nSpectators;

    private Boolean allowHorsesEnter;
    private Boolean allowHorsesLeave;
    private Boolean allowSpectatorsEnter;
    private Boolean allowSpectatorsLeave;
    private int horsesInPaddock;
    private int spectatorsInPaddock;

    Paddock(int nHorses, int nSpectators){
        this.r1 = new ReentrantLock(false);
        this.horsesEnter = r1.newCondition();
        this.horsesLeave = r1.newCondition();
        this.brokerCond = r1.newCondition();
        this.spectatorsEnter = r1.newCondition();
        this.spectatorsLeave = r1.newCondition();
        this.allowHorsesEnter = false;
        this.allowHorsesLeave = false;
        this.allowSpectatorsEnter = false;
        this.allowSpectatorsLeave = false;
        this.nHorses = nHorses;
        this.nSpectators = nSpectators;
        this.horsesInPaddock = 0;
        this.spectatorsInPaddock = 0;
        this.horses = new HorseInPaddock[nHorses];
    }

    public void proceedToPaddock(int horseID, int pnk){
        r1.lock();
        try {
            while (!allowHorsesEnter) {
                horsesEnter.await();
            }

            this.horses[this.horsesInPaddock++] = new HorseInPaddock(horseID, pnk);

            if(horsesInPaddock == nHorses){
                this.allowSpectatorsEnter = true;
                spectatorsEnter.signal();
            }
            else{
                horsesEnter.signal();
            }

        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public HorseInPaddock[] goCheckHorses(){
        r1.lock();
        try {
            while (!allowSpectatorsEnter){
                spectatorsEnter.await();
            }

            this.spectatorsInPaddock++;

            if(spectatorsInPaddock == nSpectators){
                allowHorsesLeave = true;
                horsesLeave.signal();
                brokerCond.signal();
            }
            else{
                spectatorsEnter.signal();
            }

            return horses;

        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public void proceedToStartLine(){
        r1.lock();
        try {
            while (!allowHorsesLeave) {
                horsesLeave.await();
            }


        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    private class HorseInPaddock {
        public int horseID;
        public int pnk;

        HorseInPaddock(int horseID, int pnk) {
            this.horseID = horseID;
            this.pnk = pnk;
        }
    }
}
