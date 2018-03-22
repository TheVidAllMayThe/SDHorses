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
        print();

        for(int i = 0; i < Parameters.getNumberOfRaces(); i++){

            print();
            Stable.summonHorsesToPaddock();
            ControlCentreAndWatchingStand.summonHorsesToPaddock();
            print();
            BettingCentre.acceptTheBets();
            print();
            RaceTrack.startTheRace();
            ControlCentreAndWatchingStand.startTheRace();
            print();
            int[] list = RaceTrack.reportResults();
            ControlCentreAndWatchingStand.reportResults(list);
            print();
            if(BettingCentre.areThereAnyWinners(list)){
                BettingCentre.honorBets();
            }
            print();
        }
        
        Stable.entertainTheGuests();
        ControlCentreAndWatchingStand.entertainTheGuests();
    }

    private void print(){
        System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
    }

    public void setState(String state){
        this.state = state;
    }
}
