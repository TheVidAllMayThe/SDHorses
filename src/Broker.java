public class Broker extends Thread{
    private String state;
    private int winners[];
    private final ControlCentre_Broker controlcentre;
    private final Stable_Broker stable;
    private final BettingCentre_Broker bettingcentre;

    public Broker(int n, ControlCentre_Broker cc, Stable_Broker s, BettingCentre_Broker bc, RaceTrack_Broker rtb){
        this.numberOfRaces = n;
        this.controlcentre = cc;
        this.stable = s;
        this.bettingcentre = bc;
        this.racetrack = rtb;
    }

    @Override
    public void run(){
        this.state = "opening the event";
        
        for(int i=0; i < this.numberOfRaces; i++){
            stable.summonHorsesToPaddock();
            this.state = "announcing next race";
            controlcentre.summonHorsesToPaddock();
            
            this.state = "waiting for bets";
            bettingcentre.acceptTheBets();
            
            racetrack.startTheRace();
            this.state = "supervising the race";
            controlcentre.startTheRace();

            controlcentre.reportResults();
            if(controlcentre.areThereAnyWinners()){
                this.state = "settling accounts";
                bettingcentre.honourTheBets();
            }
        }

        controlcentre.entertainTheGuests();
        this.state = "playing host at the bar";
    }
}
