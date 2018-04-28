import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.lang.ClassNotFoundException;

public class Stump{
    public static void main(String[] args){
        Socket clientSocket = null;
        try{
            //Creates input and output streams
            InetAddress sourceAddress = InetAddress.getByName("localhost");
            int sourcePort = Integer.valueOf(args[0]);
            Socket echoSocket = new Socket();
            echoSocket.setReuseAddress(true);
            echoSocket.bind(new InetSocketAddress(sourceAddress, sourcePort));
            echoSocket.connect(new InetSocketAddress(InetAddress.getByName(args[1]), Integer.valueOf(args[2])));
            GeneralRepositoryOfInformation groi = new GeneralRepositoryOfInformation(echoSocket);
            
            //Gets address and port of all necessary monitors
            InetSocketAddress address1 = new InetSocketAddress(groi.getMonitorAddress(1), groi.getMonitorPort(1));
            InetSocketAddress address2 = new InetSocketAddress(groi.getMonitorAddress(2), groi.getMonitorPort(2));
            InetSocketAddress address3 = new InetSocketAddress(groi.getMonitorAddress(3), groi.getMonitorPort(3));
            InetSocketAddress address4 = new InetSocketAddress(groi.getMonitorAddress(4), groi.getMonitorPort(4));

            int numberOfRaces = groi.getNumberOfRaces();

            groi.close();

            Stable st = new Stable(sourcePort, address1);
            BettingCentre bc = new BettingCentre(sourcePort, address2);
            ControlCentreAndWatchingStand ccws = new ControlCentreAndWatchingStand(sourcePort, address3);
            RaceTrack rt = new RaceTrack(sourcePort, address4);
            
            //Run Broker
            Broker b = new Broker(numberOfRaces, st, bc, ccws, rt);
            b.start();
            b.join(); 
        } catch(IOException e){
            e.printStackTrace();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
