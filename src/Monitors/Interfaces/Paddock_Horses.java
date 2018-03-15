package Monitors.Interfaces;

import Monitors.AuxiliaryClasses.HorseInPaddock;
import Monitors.AuxiliaryClasses.Parameters;
import Monitors.Paddock;

import static Monitors.Paddock.*;

public interface Paddock_Horses {
    static void proceedToPaddock(int horseID, int pnk){
        r1.lock();
        try {
            horses[horsesInPaddock++] = new HorseInPaddock(horseID, pnk);
            if(horsesInPaddock == Parameters.getNumberOfHorses()){
                allowSpectatorsEnter = true;
                spectatorsEnter.signal();
            }

            while(!allowHorsesLeave){
                horsesLeave.await();
            }
            if (--horsesInPaddock == 0)
                allowHorsesLeave = false;

        }catch(InterruptedException ie){
            ie.printStackTrace();
        }finally{
            r1.unlock();
        }
    }
    ;
    void proceedToStartLine();
}
