import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;


/**
 * The {@link RaceTrack} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by multiple {@link Horse}s and by the {@link Broker}.
 * <p>
 * This is where the {@link Horse}s compete with each other to reach the end of the race.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace

 */

@SuppressWarnings("JavadocReference")
public class RaceTrack {
    private InetSocketAddress address;
    private int port;

    public RaceTrack(int port, InetSocketAddress address){
        this.address = address;
        this.port = port;
    }

    public void startTheRace(){
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

            if(!((String)in.readObject()).equals("ok"))
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


    public int[] reportResults(){
        int[] result = null;
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

            out.writeObject(list);
            out.flush();
            
            result = (int[])in.readObject();

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
        return result;
    }
}
