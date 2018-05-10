package Spectator;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;

/**
* The {@link BettingCentre} class is a monitor that contains all the
* necessary methods to be used in mutual exclusive access by the Broker and {@link Spectator}.
* <p>
* This is where the Bets are handled.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Spectator
*/

public class BettingCentre extends MonitorProxy{
    
    BettingCentre(InetSocketAddress address){
        super(address);
    }

    /**
     * {@link Spectator} waits in line, places a Bet and then wakes the Broker.
     *
     * @param pid ID of the thread calling the method.
     * @param value Amount to bet.
     * @param horseID ID of the Horse in which to bet.
     * @param odds Odds of the Horse in which to bet.
     * @param budget Budget of the bet.
     */

    public void placeABet(int pid, double value, int horseID, double odds, double budget){
        try{
            LinkedList<Object> list = new LinkedList<>();
            list.add("placeABet");
            list.add(pid);
            list.add(value);
            list.add(horseID);
            list.add(odds);
            list.add(budget);

            out.writeObject(list);
            out.flush();
            
            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with placeABet");
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Called by a {@link Spectator} to collect the gain of after having won a Bet.
     *
     * @param spectatorID ID of the thread calling the method.
     * @param budget Budget of the bet.
     * @return Gains collected.
     */

    public double goCollectTheGains(int spectatorID, double budget){
        double result = 0.0;
        try {
            LinkedList<Object> list = new LinkedList<>();
            list.add("goCollectTheGains");
            list.add(spectatorID);
            list.add(budget);

            out.writeObject(list);
            out.flush();
            
            result = (double) in.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }
}
