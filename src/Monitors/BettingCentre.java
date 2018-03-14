package Monitors;

import Monitors.AuxiliaryClasses.Bet;
import Monitors.Interfaces.BettingCentre_Broker;
import Monitors.Interfaces.BettingCentre_Spectator;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BettingCentre implements BettingCentre_Broker, BettingCentre_Spectator{
    public static final Lock r1 = new ReentrantLock(false);
    public static final Condition brokerCond = r1.newCondition();
    public static final Condition spectatorCond = r1.newCondition();
    public static boolean newBets = false;
    public static boolean previousBetAccepted = true;
    public static boolean currentlyRefunding = false;
    public static int numSpectators;
    public static final Bet[] bets  = new Bet[numSpectators];
    public static int currentNumberOfSpectators;
    public static boolean previousBetHonored;

    static public void initialize(int numSpectators){
        BettingCentre.numSpectators = numSpectators;
        BettingCentre.currentNumberOfSpectators = 0;
    }


}
