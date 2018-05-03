import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
* The {@link ControlCentreAndWatchingStand} class is a monitor that contains
* necessary methods to be used in mutual exclusive access by the {@link Broker}, {@link Spectator}s and {@link Horse}s.
* <p>
* This is where the {@link Broker} mostly operates and the {@link Spectator}s watch the race.
* 
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Main.HorseRace
* @see Broker
* @see Horse
* @see Spectator
*/

public class ControlCentreAndWatchingStand extends MonitorProxy{
    ControlCentreAndWatchingStand(InetSocketAddress address){
        super(address);
    }

    /**
     * {@link Horse} proceeds to paddock, last {@link Horse} awakes {@link Spectator}s
     * that are waiting for the {@link Horse}s to enter the {@link Paddock}.
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
     * The last {@link Horse} announces in the {@link ControlCentreAndWatchingStand} that he finished the race waking up the {@link Broker}.
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
