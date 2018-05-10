import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;


/**
* The {@link BettingCentre} class is a proxy monitor that contains all the
* necessary methods to be used in mutual exclusive access by the {@link Broker} and Spectator.
* <p>
* This is where the Bets are handled.
*
 * This class will request the server responsible for the Betting Center to execute it's methods.
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Broker
*/

public class BettingCentre extends MonitorProxy{


    /**
     * Constructor for the class.
     * @param address address of the server responsible for the betting center.
     */

    BettingCentre(InetSocketAddress address){
        super(address);
    }

    /**
     * Broker accepts a Bet, wakes next Spectator in line and waits for the next one until all Bet's are taken.
     */

    public void acceptTheBets(){
        try{
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("acceptTheBets");

            out.writeObject(list);
            out.flush();
            
            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with acceptTheBets of broker");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    /**
     * Broker} verifies if any spectator won a bet.
     *
     * @param   winnerList  Integer array containing the ID of all the Horse}s who won.
     * @return  boolean     Returns true if any spectator won a bet.
     */
    public boolean areThereAnyWinners(Integer[] winnerList) {
        boolean result = false;
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("areThereAnyWinners");
            list.add(winnerList);

            out.writeObject(list);
            out.flush();
            
            result = (boolean) in.readObject();

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * Broker honors a Bet, wakes next spectator in line and waits for the next spectator claiming a reward until all rewards are given.
     */

    public void honorBets() {
        try {
            LinkedList<Object> list = new LinkedList<>();
            list.add("honorBets");
            
            out.writeObject(list);
            out.flush();
            
            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with honorBets of broker");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
