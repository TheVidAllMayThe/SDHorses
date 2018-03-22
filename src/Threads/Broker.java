package Threads;

import Monitors.AuxiliaryClasses.Parameters;
import Monitors.BettingCentre;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.RaceTrack;
import Monitors.Stable;

public class Broker extends Thread{
    private String state;

    @Override
    public void run(){
        ControlCentreAndWatchingStand.openingTheEvents();

        for(int i = 0; i < Parameters.getNumberOfRaces(); i++){
            Stable.summonHorsesToPaddock();
            ControlCentreAndWatchingStand.summonHorsesToPaddock();

            BettingCentre.acceptTheBets();

            RaceTrack.startTheRace();
            ControlCentreAndWatchingStand.startTheRace();

            int[] list = RaceTrack.reportResults();
            ControlCentreAndWatchingStand.reportResults(list);

            if(BettingCentre.areThereAnyWinners(list)){
                BettingCentre.honorBets();
            }
        }
        
        Stable.entertainTheGuests();
        ControlCentreAndWatchingStand.entertainTheGuests();
    }
    
    public void setState(String state){
        this.state = state;
    }
}
