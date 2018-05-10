import java.io.IOException;
import java.util.LinkedList;
import java.net.InetSocketAddress;

/**
 * The {@link Paddock} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by Horses and {@link Spectator}s.
 * <p>
 * This is where the Horses are paraded for the {@link Spectator}s.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Spectator
 */

public class Paddock extends MonitorProxy {
    
    Paddock(InetSocketAddress address){
        super(address);
    }

    /**
     * Function in which the {@link Spectator} enters the {@link Paddock}. The last {@link Spectator} to enter wakes up the Horses. In this function the {@link Spectator} determines in which Horse they will bet.
     * @param spectatorID ID of the spectator.
     * @return Returns the Horse in which the {@link Spectator} will bet.
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

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }
}
