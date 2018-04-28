import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

public class ControlCentreAndWatchingStand {
    private int port;
    private InetSocketAddress address;

    public ControlCentreAndWatchingStand(int port, InetSocketAddress address){
        this.port = port;
        this.address = address;
    }

    public void openingTheEvents() {
        try {
            Socket clientSocket = new Socket();
            clientSocket.setReuseAddress(true);
            clientSocket.bind(new InetSocketAddress(InetAddress.getByName("localHost"), port));
            clientSocket.connect(address);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            LinkedList<Object> list = new LinkedList<>();
            list.add("openingTheEvents");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            out.writeObject("close");
            out.flush();
            out.close();
            in.close();
            clientSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void summonHorsesToPaddock(int numRace) {
        try {
            Socket clientSocket = new Socket();
            clientSocket.setReuseAddress(true);
            clientSocket.bind(new InetSocketAddress(InetAddress.getByName("localHost"), port));
            clientSocket.connect(address);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            LinkedList<Object> list = new LinkedList<>();
            list.add("summonHorsesToPaddock");
            list.add(numRace);

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            out.writeObject("close");
            out.flush();
            out.close();
            in.close();
            clientSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void startTheRace() {
        try {
            Socket clientSocket = new Socket();
            clientSocket.setReuseAddress(true);
            clientSocket.bind(new InetSocketAddress(InetAddress.getByName("localHost"), port));
            clientSocket.connect(address);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            LinkedList<Object> list = new LinkedList<>();
            list.add("startTheRace");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in startTheRace of Broker");

            out.writeObject("close");
            out.flush();
            out.close();
            in.close();
            clientSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void reportResults(int[] results) {
        try {
            Socket clientSocket = new Socket();
            clientSocket.setReuseAddress(true);
            clientSocket.bind(new InetSocketAddress(InetAddress.getByName("localHost"), port));
            clientSocket.connect(address);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            LinkedList<Object> list = new LinkedList<>();
            list.add("reportResults");
            list.add(results);

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in reportResults of Broker");

            out.writeObject("close");
            out.flush();
            out.close();
            in.close();
            clientSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public void entertainTheGuests() {
        try {
            Socket clientSocket = new Socket();
            clientSocket.setReuseAddress(true);
            clientSocket.bind(new InetSocketAddress(InetAddress.getByName("localHost"), port));
            clientSocket.connect(address);
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            LinkedList<Object> list = new LinkedList<>();
            list.add("entertainTheGuests");

            out.writeObject(list);
            out.flush();

            if(!in.readObject().equals("ok"))
                System.out.println("Something wrong in entertainTheGuests of Broker");

            out.writeObject("close");
            out.flush();
            out.close();
            in.close();
            clientSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
