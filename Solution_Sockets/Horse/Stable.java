import java.io.IOException;
import java.util.LinkedList;

/**
 * The {@link Stable} class is a monitor that contains all the
 * necessary methods to be used in mutual exclusive access by the Broker and {@link Horse}s to itself.
 * <p>
* This is where the {@link Horse}s are initially and after a race.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21

 *@see Horse
*/

public class Stable extends MonitorProxy {
    Stable(InetSocketAddress address){
        super(address);
    }

    /**
     * {@link Horse}s wait to move to {@link Paddock}.
     * @param horseID ID of the Horse calling the method.
     * @param pnk Indicates the chance to win of the horse.
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
