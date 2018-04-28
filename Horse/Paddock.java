import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

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
    public Paddock(int port, InetSocketAddress address){
        super(port, address);
    }

    /**
     * The {@link Horse}s enter the paddock and add their information to the {@link #horsesInPaddock} array, then they wait until all the {@link Spectator}s have reached the {@link Paddock}, at the end the last {@link Horse} awakes the {@link Spectator}s.
     *
     * @param horseID ID of the calling thread.
     * @param pnk Max step size.
     */

    public void proceedToPaddock(int horseID, int pnk){
        try{
            openConnection();

            LinkedList<Object> list = new LinkedList<>();
            list.add("proceedToPaddock");
            list.add(horseID);
            list.add(pnk);

            out.writeObject(list);
            out.flush();

            if (!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong with proceedToPaddock");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Called by the {@link Horse} to exit the {@link Paddock}.
     */

    public void proceedToStartLine(){
        try{
            openConnection();

            LinkedList<Object> list = new LinkedList<>();
            list.add("proceedToStartLine");

            out.writeObject(list);
            out.flush();

            if (!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong with proceedToStartLine");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    /**
     * Function in which the {@link Spectator} enters the {@link Paddock}. The last {@link Spectator} to enter wakes up the {@link Horse}s. In this function the {@link Spectator} determines in which {@link Horse} they will bet.
     * @return Returns the {@link Horse} in which the {@link Spectator} will bet.
     */
    public HorseInPaddock goCheckHorses(){
        HorseInPaddock result = null;
        try {
            openConnection();

            LinkedList<Object> list = new LinkedList<>();
            list.add("goCheckHorses");

            out.writeObject(list);
            out.flush();
            
            result = (HorseInPaddock)in.readObject();

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }
}
