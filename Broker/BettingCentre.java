import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* The {@link BettingCentre} class is a monitor that contains all the
* necessary methods to be used in mutual exclusive access by the {@link Broker} and Spectator.
* <p>
* This is where the Bets are handled.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Broker
*/

public class BettingCentre extends Monitor{

    public BettingCentre(InetSocketAddress address){
        super(address);
    }
 
    public void acceptTheBets(){
        try{
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("acceptTheBets");

            out.writeObject(list);
            out.flush();
            
            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with acceptTheBets of broker");

            
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    
    public boolean areThereAnyWinners(Integer[] winnerList) {
        boolean result = false;
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("areThereAnyWinners");
            list.add(winnerList);

            out.writeObject(list);
            out.flush();
            
            result = (boolean) in.readObject();

            
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }

    public void honorBets() {
        try {
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("honorBets");
            
            out.writeObject(list);
            out.flush();
            
            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with honorBets of broker");

            
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
