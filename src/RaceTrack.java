import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RaceTrack {
    private ReentrantLock r1;
    private Condition raceStarted;
    private boolean canRace;
    private HorsePos[] horses;
    private int numHorses;

    RaceTrack(int totalNumHorses){
        r1 = new ReentrantLock();
        canRace = false;
        horses = new HorsePos[numHorses];
        this.numHorses = 0;
    }

    public void startTheRace(){
        r1.lock();

        try{
            this.canRace = true;
            this.raceStarted.signal();
        }catch(InterruptedException ie){

        }finally{
            r1.unlock();
        }
    }

    public int proceedToStartLine(int pID){   //Returns the pos in the array of Horses
        r1.lock();
        try{
            horses[numHorses++] = new HorsePos(pID, 0);
        }catch (IllegalMonitorStateException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
        return numHorses - 1;
    }

    public void makeAMove(int horsePos, int moveAmount){
        r1.lock();
        try{
            while (!canRace)
                raceStarted.await();
        }catch (IllegalMonitorStateException | InterruptedException e){e.printStackTrace();}
        finally {
            r1.unlock();
        }
    }

    private class HorsePos{
        int horseID;
        int pos;

        HorsePos(int horseID, int pos) {
            this.horseID = horseID;
            this.pos = pos;
        }

        void addPos(int amount){
            pos += amount;
        }
    }
}
