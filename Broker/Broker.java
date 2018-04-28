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

    public Broker(int numberOfRaces, Stable st, BettingCentre bc, ControlCentreAndWatchingStand ccws, RaceTrack rt){
        this.numberOfRaces = numberOfRaces;
        this.st = st;
        this.bc = bc;
        this.ccws = ccws;
        this.rt = rt;
    }

    @Override
    public void run(){

        ccws.openingTheEvents();

        for(int i = 0; i < numberOfRaces; i++){
            bc.honorBets();
            st.summonHorsesToPaddock();
            ccws.summonHorsesToPaddock(i);
            bc.acceptTheBets();
            rt.startTheRace();
            ccws.startTheRace();
            int[] list = rt.reportResults();
            ccws.reportResults(list);
            if(bc.areThereAnyWinners(list))
                bc.honorBets();
        }
        
        st.entertainTheGuests();
        ccws.entertainTheGuests();
    }
}
