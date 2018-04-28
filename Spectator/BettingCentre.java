import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
* The {@link BettingCentre} class is a monitor that contains all the
* necessary methods to be used in mutual exclusive access by the {@link Broker} and {@link Spectator}.
* <p>
* This is where the {@link Bet}s are handled.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Broker
* @see Spectator
*/

public class BettingCentre extends Monitor{
    
    public BettingCentre(int port, InetSocketAddress address){
        super(port, address);
    }

    /**
     * {@link Spectator} waits in line, places a {@link Bet} and then wakes the {@link Broker}.
     *
     * @param pid ID of the thread calling the method.
     * @param value Amount to bet.
     * @param horseID ID of the {@link Horse} in which to bet.
     * @param odds Odds of the {@link Horse} in which to bet.
     */

    public void placeABet(int pid, double value, int horseID, double odds, double budget){
        try{
            openConnection();

            LinkedList<Object> list = new LinkedList<>();
            list.add("placeABet");
            list.add(value);
            list.add(horseID);
            list.add(odds);
            list.add(budget);

            out.writeObject(list);
            out.flush();
            
            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with placeABet");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Called by a {@link Spectator} to collect the gain of after having won a {@link Bet}.
     *
     * @param spectatorID ID of the thread calling the method.
     */
    public double goCollectTheGains(int spectatorID, double budget){
        double result = 0.0;
        try {
            openConnection();

            LinkedList<Object> list = new LinkedList<>();
            list.add("goCollectTheGains");
            list.add(spectatorID);
            list.add(budget);

            out.writeObject(list);
            out.flush();
            
            result = (double) in.readObject();

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }
}
