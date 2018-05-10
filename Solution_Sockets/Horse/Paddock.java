import java.net.InetSocketAddress;
import java.util.LinkedList;

/**
 * The {@link Paddock} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by {@link Horse}s and Spectators.
 * <p>
 * This is where the {@link Horse}s are paraded for the Spectators.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Horse
 */

public class Paddock extends MonitorProxy {
    Paddock(InetSocketAddress address){
        super(address);
    }

    /**
     * The {@link Horse}s enter the paddock and add their information to the horsesInPaddock array, then they wait until all the Spectators have reached the {@link Paddock}, at the end the last {@link Horse} awakes the Spectators.
     *
     * @param horseID ID of the calling thread.
     * @param pnk Max step size.
     */

    public void proceedToPaddock(int horseID, int pnk){
        try{
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("proceedToPaddock");
            list.add(horseID);
            list.add(pnk);

            out.writeObject(list);
            out.flush();

            if (!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong with proceedToPaddock");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Called by the {@link Horse} to exit the {@link Paddock}.
     */

    public void proceedToStartLine(){
        try{
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("proceedToStartLine");

            out.writeObject(list);
            out.flush();

            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with proceedToStartLine");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    /**
     * Function in which the Spectator enters the {@link Paddock}. The last Spectator to enter wakes up the {@link Horse}s. In this function the Spectator determines in which {@link Horse} they will bet.
     * @return Returns the {@link Horse} in which the Spectator will bet.
     */
    public HorseInPaddock goCheckHorses(){
        HorseInPaddock result = null;
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("goCheckHorses");

            out.writeObject(list);
            out.flush();
            
            result = (HorseInPaddock)in.readObject();

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }
}
