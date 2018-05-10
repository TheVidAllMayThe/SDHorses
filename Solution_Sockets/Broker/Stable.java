import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;


/**
 * The {@link Stable} class is a monitor that contains all the
 * necessary methods to be used in mutual exclusive access by the {@link Broker} and Horses to itself.
 * <p>
* This is where the Horses are initially and after a race.
*
 * This class serves as a proxy to communicate with the server responsible for the Stable.
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21

*/


public class Stable extends MonitorProxy {
    public Stable(InetSocketAddress address){
        super(address);
    }

    /**
     * {@link Broker} awakes the Horses who are waiting to enter the Paddock.
     */
    public void summonHorsesToPaddock(){
        try{
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("summonHorsesToPaddock");

            out.writeObject(list);
            out.flush();

            if (!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong with summonHorsesToPaddock");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Last function of {@link Broker} lifecycle, awakes Horses waiting to enter Paddock.
     */
    public void entertainTheGuests(){
        try{
            
            
            LinkedList<Object> list = new LinkedList<>();
            list.add("entertainTheGuests");

            out.writeObject(list);
            out.flush();

            if (!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong with entertainTheGuests");

            
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }
}
