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
     * {@link Spectator} waits for next race of the day, last {@link Spectator} waiting wakes the {@link Broker}
     * who's ready to start the race.
     */
    public void waitForNextRace(int spectatorID, double budget){
        try{
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("waitForNextRace");
            list.add(spectatorID);
            list.add(budget);

            out.writeObject(list);
            out.flush();
            
            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with waitForNextRace");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * {@link Spectator} waits while watching the race.
     */
    public void goWatchTheRace(int spectatorID){
        try{
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("goWatchTheRace");
            list.add(spectatorID);

            out.writeObject(list);
            out.flush();
            
            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with goWatchTheRace");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
    /**
     * {@link Spectator} checks if he won his bet.
     *
     * @param   horseID  ID of the {@link Horse} whom the {@link Spectator} bet on.
     * @return  True if the {@link Spectator} won.
     */
    public boolean haveIWon(int horseID, int spectatorID){
        boolean result = false;
        try {
            LinkedList<Object> list = new LinkedList<>();
            list.add("haveIWon");
            list.add(horseID);
            list.add(spectatorID);

            out.writeObject(list);
            out.flush();
            
            result = (boolean) in.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Last function of {@link Spectator} lifecycle.
     */
    public void relaxABit(Integer spectatorID){ 
        try{
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("relaxABit");
            list.add(spectatorID);

            out.writeObject(list);
            out.flush();
            
            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with relaxABit");
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
