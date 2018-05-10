package Broker;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;

/**
 * The {@link ControlCentreAndWatchingStand} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by the Broker, Spectators and Horses.
 * <p>
 * This is where the Broker mostly operates and the Spectators watch the race.
 * This class is a proxy that through the use of sockets will request that the methods are invoked in an external server.
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21

 */

public class ControlCentreAndWatchingStand extends Broker.MonitorProxy {


    /**
     * Constructor of the class.
     * @param address Address of the server responsible for the ControlCentreAndWatchingStand.
     */
    ControlCentreAndWatchingStand(InetSocketAddress address){
        super(address);
    }


    /**
     * Method used to set the Broker initial state.
     */
    public void openingTheEvents() {
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("openingTheEvents");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * The Broker waits for all the Spectator threads to have reached the {@link ControlCentreAndWatchingStand} before proceeding.
     * @param numRace Number of the next race.
     */



    public void summonHorsesToPaddock(int numRace) {
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("summonHorsesToPaddock");
            list.add(numRace);

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     *  Broker  waits for all  Horse threads to have reached the finish line before proceeding.
     */

    public void startTheRace() {
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("startTheRace");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in startTheRace of Broker");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     *  Broker declares the Horses who won and wakes up the Spectators watching the race.
     *
     * @param   results  An integer array containing the ID of the Horses who won the race.
     */
    public void reportResults(Integer[] results) {
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("reportResults");
            list.add(results);

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in reportResults of Broker");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Last function of Broker lifecycle.
     */

    public void entertainTheGuests() {
        try {
            LinkedList<Object> list = new LinkedList<>();
            list.add("entertainTheGuests");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in entertainTheGuests of Broker");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
