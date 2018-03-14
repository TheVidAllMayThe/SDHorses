package Monitors.Interfaces;
import static Monitors.BettingCentre.*;


public interface BettingCentre_Broker {
    static void acceptTheBets() throws IllegalMonitorStateException{
        r1.lock();

        try {
            do {
                while (!newBets)
                    brokerCond.await();
                newBets = false;
                bets[currentNumberOfSpectators - 1].accepted = true;
                spectatorCond.signal();
            }while (currentNumberOfSpectators != numSpectators);
        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }
    static void honorBets(){
        r1.lock();
        try{
            while(!previousBetHonored)
                brokerCond.await();

            spectatorCond.signal();
            previousBetHonored = true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            r1.unlock();
        }
    }
}
