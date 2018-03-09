public class HorseAndJockey extends Thread{
    private String state;
    private int numberOfRaces;
    private final ControlCentre_Broker controlcentre;
    private final Stable_Broker stable;
    private final BettingCentre_Broker bettingcentre;

    public HorseAndJockey(int n, ControlCentre_Broker cc, Stable_Broker s, BettingCentre_Broker bc, RaceTrack_Broker rtb){
        this.numberOfRaces = n;
        this.controlcentre = cc;
        this.stable = s;
        this.bettingcentre = bc;
        this.racetrack = rtb;
    }

    @Override
    public void run(){
        for(int i=0; i < this.numberOfRaces; i++){
            this.state = "at the stable";
            stable.proceedToStable();
            
            this.state = "at the paddock";
            paddock.proceedToPaddock();

            this.state = "at the start line";
            racetrack.proceedToStartLine();

            this.state = "running";
            do{
                racetrack.makeAMove();
            }while(!racetrack.hasFinishLineBeenCrossed());

            //blocks here because a horse who has finished is only given access to monitor after all horses finished
            this.state = "at the finish line";
            hasFinishLineBeenCrossed();}
    }
}
