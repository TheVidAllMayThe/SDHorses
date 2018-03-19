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
        System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
        for(int i = 0; i < Parameters.getNumberOfHorses(); i++){

            state = "announcing next race";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);

            Stable.summonHorsesToPaddock();
            ControlCentreAndWatchingStand.summonHorsesToPaddock();

            state = "waiting for bets";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            BettingCentre.acceptTheBets();


            state = "supervising the race";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            RaceTrack.startTheRace();
            ControlCentreAndWatchingStand.startTheRace();

            state = "supervising the race";
            System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
            ArrayList<HorsePos> list = RaceTrack.reportResults();
            ControlCentreAndWatchingStand.reportResults(list);

            Bet[] bets = BettingCentre.areThereAnyWinners();
            if(ControlCentreAndWatchingStand.areThereAnyWinners(bets)){
                state = "settling accounts";
                System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
                BettingCentre.honorBets();
            }
        }
        
        state = "playing host at the bar";
        System.out.println(getClass().getSimpleName() + " pID = " + getId() + ": " + state);
        Stable.entertainTheGuests();
        ControlCentreAndWatchingStand.entertainTheGuests();
    }
}
