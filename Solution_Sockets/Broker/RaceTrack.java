package Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;


/**
 * The {@link RaceTrack} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by multiple Horses and by the {@link Broker}.
 * <p>
 * This is where the Horses compete with each other to reach the end of the race.
 *
 * This class serves as a proxy to communicate with the server responsible for the RaceTrack.
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21


 */


public class RaceTrack extends MonitorProxy{


    /**
     * Constructor of the class RaceTrack.
     * @param address Address of the server responsible for the raceTrack.
     */
    RaceTrack(InetSocketAddress address){
        super(address);
    }


    /**
     * The {@link Broker} allows the first Horse to start running.
     */
    public void startTheRace(){
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("startTheRace");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    /**
     * The {@link Broker} enters the {@link RaceTrack} to see which Horses have won the race
     * @return Array containing the ID's of the winning Horses.
     */

    public Integer[] reportResults(){
        Integer[] result = null;
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("reportResults");

            out.writeObject(list);
            out.flush();
            
            result = (Integer[])in.readObject();

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }
}
