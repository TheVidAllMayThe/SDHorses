/**
 * The {@link Broker} class is a thread that contains the lifecycle of the {@link Broker} during the day.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace
 */

public class Broker extends Thread{
    private int numberOfRaces;
    private Stable st;
    private BettingCentre bc;
    private ControlCentreAndWatchingStand ccws;
    private RaceTrack rt;

    Broker(int numberOfRaces, Stable st, BettingCentre bc, ControlCentreAndWatchingStand ccws, RaceTrack rt){
        this.numberOfRaces = numberOfRaces;
        this.st = st;
        this.bc = bc;
        this.ccws = ccws;
        this.rt = rt;
    }

    @Override
    public void run(){

        System.out.println("OpeningTheEvents");
        ccws.openingTheEvents();

        for(int i = 0; i < numberOfRaces; i++){
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
            if(bc.areThereAnyWinners(list))
                System.out.println("honorTheBets");
                bc.honorBets();
        }
        
        System.out.println("entertainTheGuests (st)");
        st.entertainTheGuests();
        System.out.println("entertainTheGuests (ccws)");
        ccws.entertainTheGuests();
    }
}
