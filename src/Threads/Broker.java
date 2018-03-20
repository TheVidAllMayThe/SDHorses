package Threads;

import Monitors.AuxiliaryClasses.Bet;
import Monitors.AuxiliaryClasses.HorsePos;
import Monitors.AuxiliaryClasses.Parameters;
import Monitors.BettingCentre;
import Monitors.ControlCentreAndWatchingStand;
import Monitors.RaceTrack;
import Monitors.Stable;

import java.util.ArrayList;

public class Broker extends Thread{


    @Override
    public void run(){
        String state = "opening the event";
        print(state);
        for(int i = 0; i < Parameters.getNumberOfRaces(); i++){
            System.out.println("---------------------- " + i + " ------------------");
            state = "announcing next race";
            print(state);

            Stable.summonHorsesToPaddock();
            ControlCentreAndWatchingStand.summonHorsesToPaddock();

            state = "waiting for bets";
            print(state);
            BettingCentre.acceptTheBets();


            state = "supervising the race";
            print(state);
            RaceTrack.startTheRace();
            ControlCentreAndWatchingStand.startTheRace();

            state = "supervising the race";
            print(state);
            int[] list = RaceTrack.reportResults();
            ControlCentreAndWatchingStand.reportResults(list);

            if(BettingCentre.areThereAnyWinners(list)){
                state = "settling accounts";
                print(state);
                BettingCentre.honorBets();
            }
        }
        
        state = "playing host at the bar";
        print(state);
        Stable.entertainTheGuests();
        ControlCentreAndWatchingStand.entertainTheGuests();
    }

    private void print(String state){
        System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
    }
}
