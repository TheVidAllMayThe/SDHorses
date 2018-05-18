import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@link Horse} class is a thread that contains the lifecycle of the {@link Horse} during the day.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 */

public class Horse extends Thread {

    private int pnk;
    private int ID;
    private int raceNum;
    private Paddock_Interface pd;
    private Stable_Interface st;
    private ControlCentreAndWatchingStand_Interface ccws;
    private RaceTrack_Interface rt;

    /**
     *
     * @param id ID of the Horse in each race.
     * @param raceNum Number of the race in which the race will participate.
     */

    Horse(int id, int raceNum, Paddock_Interface pd, Stable_Interface st, ControlCentreAndWatchingStand_Interface ccws, RaceTrack_Interface rt) {
        this.ID = id;
        this.raceNum = raceNum;
        this.pnk = ThreadLocalRandom.current().nextInt(5, 10);
        this.pd = pd;
        this.st = st;
        this.ccws = ccws;
        this.rt = rt;
    }

    @Override
    public void run() {
        try {
            System.out.println("proceedToStable " + this.ID);
            st.proceedToStable(this.raceNum, this.ID, this.pnk);
            System.out.println("proceedToPaddock (ccws) " + this.ID);
            ccws.proceedToPaddock();
            System.out.println("proceedToPaddock (pd) " + this.ID);
            pd.proceedToPaddock(ID, pnk);
            System.out.println("proceedToStartLine (pd) " + this.ID);
            pd.proceedToStartLine();
            System.out.println("proceedToStartLine (rt) " + this.ID);
            int horsePos = rt.proceedToStartLine(ID);
            do {
                System.out.println("makeAMove (rt) " + this.ID);
                rt.makeAMove(horsePos, ThreadLocalRandom.current().nextInt(1, pnk + 1), this.ID);
                System.out.println("hasFinishLineBeenCrossed " + this.ID);
            } while (!rt.hasFinishLineBeenCrossed(horsePos, this.ID));
            System.out.println("makeAMove(ccws) " + this.ID);
            ccws.makeAMove(this.ID);

        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
}
