import java.io.IOException;
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
public class ControlCentreAndWatchingStand extends MonitorProxy{
    /**
     * Constructor of the class.
     * @param address Address of the server responsible for the ControlCentreAndWatchingStand.
     */
    ControlCentreAndWatchingStand(InetSocketAddress address){
        super(address);
    }

    /**
     * Horse proceeds to paddock, last Horse awakes Spectators
     * that are waiting for the Horses to enter the Paddock.
     */
    public void proceedToPaddock(){
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("proceedToPaddock");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in proceedToPaddock");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * The last Horse announces in the {@link ControlCentreAndWatchingStand} that he finished the race waking up the Broker.
     */
    public void makeAMove(){
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("makeAMove");

            out.writeObject(list);
            out.flush();

            if(!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong in makeAMove");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
