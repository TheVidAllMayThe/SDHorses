import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.Socket;
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

@SuppressWarnings("JavadocReference")
public class Stable extends Monitor{
    public Stable(InetSocketAddress address){
        super(address);
    }

    /**
     * {@link Broker} awakes the {@link Horse}s who are waiting to enter the {@link Paddock}.
     */
    public void summonHorsesToPaddock(){
        try{
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("summonHorsesToPaddock");

            out.writeObject(list);
            out.flush();

            if (!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong with summonHorsesToPaddock");

            
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Last function of {@link Broker} lifecycle, awakes {@link Horse}s waiting to enter {@link Paddock}.
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
