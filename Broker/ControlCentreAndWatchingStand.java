import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

public class ControlCentreAndWatchingStand extends Monitor{

    public ControlCentreAndWatchingStand(int port, InetSocketAddress address){
        super(port, address);
    }

    public void openingTheEvents() {
        try {
            openConnection();

            LinkedList<Object> list = new LinkedList<>();
            list.add("openingTheEvents");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void summonHorsesToPaddock(int numRace) {
        try {
            openConnection();

            LinkedList<Object> list = new LinkedList<>();
            list.add("summonHorsesToPaddock");
            list.add(numRace);

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void startTheRace() {
        try {
            openConnection();

            LinkedList<Object> list = new LinkedList<>();
            list.add("startTheRace");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in startTheRace of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void reportResults(int[] results) {
        try {
            openConnection();

            LinkedList<Object> list = new LinkedList<>();
            list.add("reportResults");
            list.add(results);

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in reportResults of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void entertainTheGuests() {
        try {
            openConnection();

            LinkedList<Object> list = new LinkedList<>();
            list.add("entertainTheGuests");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in entertainTheGuests of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
