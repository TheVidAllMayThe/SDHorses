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
            Socket echoSocket = new Socket(InetAddress.getByName(args[1]), Integer.valueOf(args[2]), sourceAddress, sourcePort);
            echoSocket.setReuseAddress(true);
            GeneralRepositoryOfInformation groi = new GeneralRepositoryOfInformation(echoSocket);
            
            //Gets address and port of all necessary monitors
            InetSocketAddress address0 = new InetSocketAddress(groi.getMonitorAddress(0), groi.getMonitorPort(0));
            InetSocketAddress address2 = new InetSocketAddress(groi.getMonitorAddress(2), groi.getMonitorPort(2));
            InetSocketAddress address3 = new InetSocketAddress(groi.getMonitorAddress(3), groi.getMonitorPort(3));
            InetSocketAddress address4 = new InetSocketAddress(groi.getMonitorAddress(4), groi.getMonitorPort(4));

            int numberOfRaces = groi.getNumberOfRaces();

            groi.close();
            clientSocket = new Socket();
            clientSocket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), sourcePort + 1));

            Stable st = new Stable(clientSocket, address0);
            BettingCentre bc = new BettingCentre(clientSocket, address2);
            ControlCentreAndWatchingStand ccws = new ControlCentreAndWatchingStand(clientSocket, address3);
            RaceTrack rt = new RaceTrack(clientSocket, address4);
            
            //Run Broker
            Broker b = new Broker(numberOfRaces, st, bc, ccws, rt);
            b.start();
            b.join(); 
        } catch(IOException e){
            e.printStackTrace();
        } catch(InterruptedException e){
            e.printStackTrace();
        } finally {
            try{
                clientSocket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
