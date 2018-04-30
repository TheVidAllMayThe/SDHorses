import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.net.InetSocketAddress;

/**
 * The {@link Paddock} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by {@link Horse}s and {@link Spectator}s.
 * <p>
 * This is where the {@link Horse}s are paraded for the {@link Spectator}s.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace
 * @see Broker
 * @see Horse
 * @see Spectator
 */

public class Paddock extends Monitor{
    
    public Paddock(InetSocketAddress address){
        super(address);
    }

    /**
     * Function in which the {@link Spectator} enters the {@link Paddock}. The last {@link Spectator} to enter wakes up the {@link Horse}s. In this function the {@link Spectator} determines in which {@link Horse} they will bet.
     * @return Returns the {@link Horse} in which the {@link Spectator} will bet.
     */
    public HorseInPaddock goCheckHorses(int spectatorID){
        HorseInPaddock result = null;
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("goCheckHorses");
            list.add(spectatorID);

            out.writeObject(list);
            out.flush();
            
            result = (HorseInPaddock) in.readObject();

            
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }
}
