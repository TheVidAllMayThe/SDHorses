import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * The {@link Stable} class is a monitor that contains all the
 * necessary methods to be used in mutual exclusive access by the {@link Broker} and {@link Horse}s to itself.
 * <p>
* This is where the {@link Horse}s are initially and after a race.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Main.HorseRace
 *@see Threads.Broker
 *@see Horse
*/

public class Stable extends MonitorProxy{
    Stable(InetSocketAddress address){
        super(address);
    }

    /**
     * {@link Horse}s wait to move to {@link Paddock}.
     *
     * @param raceNum Number of the race in which the calling {@link Horse} will participate.
     */
    public void proceedToStable(int raceNum, int horseID, int pnk){
        try{ 
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("proceedToStable");
            list.add(raceNum);
            list.add(horseID);
            list.add(pnk);
        
            out.writeObject(list);
            out.flush();

            if (!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong with proceedToStable");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
