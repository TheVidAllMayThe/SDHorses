import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@link Horse} class is a thread that contains the lifecycle of the {@link Horse} during the day.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace
 */

public class Horse extends Thread {

    private int pnk;
    private int ID;
    private int raceNum;
    private Paddock pd;
    private Stable st;
    private ControlCentreAndWatchingStand ccws;
    private RaceTrack rt;

    /**
     *
     * @param id ID of the Horse in each race.
     * @param raceNum Number of the race in which the race will participate.
     */

    public Horse(int id, int raceNum, Paddock pd, Stable st, ControlCentreAndWatchingStand ccws, RaceTrack rt) {
        this.ID = id;
        this.raceNum = raceNum;
        this.pnk = ThreadLocalRandom.current().nextInt(1, 10);
        this.pd = pd;
        this.st = st;
        this.ccws = ccws;
        this.rt = rt;
    }

    @Override
    public void run() {
        st.proceedToStable(this.raceNum, this.ID, this.pnk);
        ccws.proceedToPaddock();
        pd.proceedToPaddock(ID, pnk);
        pd.proceedToStartLine();
        int horsePos = rt.proceedToStartLine(ID);
        do {
            rt.makeAMove(horsePos, ThreadLocalRandom.current().nextInt(1, pnk + 1), this.ID);
        }while(!rt.hasFinishLineBeenCrossed(horsePos, this.ID));
        ccws.makeAMove();
    }
}
