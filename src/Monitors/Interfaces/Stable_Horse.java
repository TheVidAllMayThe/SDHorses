package Monitors.Interfaces;

import Monitors.AuxiliaryClasses.Parameters;

import static Monitors.Stable.*;

public interface Stable_Horse {

    static void proceedToStable(){
        r1.lock();
        numHorses++;
        try{
            while(!canHorsesMoveToPaddock)
                horsesToPaddock.wait();


            if(numHorses == Parameters.getNumberOfHorses()){ //If it is the las horse to leave the Stable then the following horses will have to wait
                numHorses = 0;
                canHorsesMoveToPaddock = false;
            }
        }catch (IllegalMonitorStateException | InterruptedException e){
            e.printStackTrace();
        } finally {
            r1.unlock();
        }
    }
}
