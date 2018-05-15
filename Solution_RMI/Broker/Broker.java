import java.rmi.RemoteException;

/**
 * The {@link Broker} class is a thread that contains the lifecycle of the {@link Broker} during the day.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 */

public class Broker extends Thread{
    private int numberOfRaces;
    private Stable_Interface st;
    private BettingCentre_Interface bc;
    private ControlCentreAndWatchingStand_Interface ccws;
    private RaceTrack_Interface rt;

    /**
     * Constructor for the class Broker.
     * @param numberOfRaces Number of races to be held.
     * @param st Instance of Stable.
     * @param bc Instance of BettingCentre.
     * @param ccws Instance of ControlCenterAndWatchingStand.
     * @param rt Instance of RaceTrack.
     */

    Broker(int numberOfRaces, Stable_Interface st, BettingCentre_Interface bc, ControlCentreAndWatchingStand_Interface ccws, RaceTrack_Interface rt){
        this.numberOfRaces = numberOfRaces;
        this.st = st;
        this.bc = bc;
        this.ccws = ccws;
        this.rt = rt;
    }

    /**
     * Main execution of the thread.
     */

    @Override
    public void run() {

        try {
            System.out.println("OpeningTheEvents");

            ccws.openingTheEvents();


            for (int i = 0; i < numberOfRaces; i++) {
                System.out.println("summonHorsesToPaddock (st)");
                st.summonHorsesToPaddock();
                System.out.println("summonHorsesToPaddock (ccws)");
                ccws.summonHorsesToPaddock(i);
                System.out.println("acceptTheBets");
                bc.acceptTheBets();
                System.out.println("startTheRace (rt)");
                rt.startTheRace();
                System.out.println("startTheRace (ccws)");
                ccws.startTheRace();
                System.out.println("reportResults (rt)");
                Integer[] list = rt.reportResults();
                System.out.println("reportResults (ccws)");
                ccws.reportResults(list);
                System.out.println("areThereAnyWinners");
                if (bc.areThereAnyWinners(list))
                    System.out.println("honorTheBets");
                bc.honorBets();

            }

            System.out.println("entertainTheGuests (st)");
            st.entertainTheGuests();
            System.out.println("entertainTheGuests (ccws)");
            ccws.entertainTheGuests();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
