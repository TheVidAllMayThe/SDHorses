package Main;

import java.util.Random;

import Monitors.GeneralRepositoryOfInformation;
import Monitors.AuxiliaryClasses.Parameters;
import Main.ClientThread;
import java.net.ServerSocket;
import java.io.IOException;
import java.lang.ClassNotFoundException;

/**
* The Main.HorseRace program creates one thread of type Broker and several
* of type Horse and Spectator and runs them.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Broker
* @see Horse
* @see Spectator
*/

public class HorseRace {
    public enum Monitor {
        CONTROLCENTRE, BETTINGCENTRE, STABLE, PADDOCK, RACETRACK    
    }

    public static void main(String[] args){
        //Simulation variables
        Random rng = new Random();
        Parameters.initialize(5,4,4, rng.nextInt(99)+1);
        GeneralRepositoryOfInformation.initialize();

        try{
            ServerSocket serverSocket = new ServerSocket(23040);
            while(true){
                new ClientThread(serverSocket.accept(), Class.forName("Monitors.GeneralRepositoryOfInformation")).run();
                
            }
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }        

        GeneralRepositoryOfInformation.close();    
    }
}
