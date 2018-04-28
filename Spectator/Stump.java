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
            InetSocketAddress address0 = new InetSocketAddress(groi.getMonitorAddress(0), groi.getMonitorPort(0));
            InetSocketAddress address2 = new InetSocketAddress(groi.getMonitorAddress(2), groi.getMonitorPort(2));
            InetSocketAddress address3 = new InetSocketAddress(groi.getMonitorAddress(3), groi.getMonitorPort(3));

            int numberOfSpectators = groi.getNumberOfSpectators();
            int numberOfRaces = groi.getNumberOfRaces();
            groi.close();

            Paddock[] pd = new Paddock[numberOfSpectators];
            BettingCentre[] bc = new BettingCentre[numberOfSpectators];
            ControlCentreAndWatchingStand[] ccws = new ControlCentreAndWatchingStand[numberOfSpectators];
            for(int i=0; i<numberOfSpectators; i++){
                pd[i] = new Paddock(sourcePort + i, address0);
                bc[i] = new BettingCentre(sourcePort + i, address2);
                ccws[i] = new ControlCentreAndWatchingStand(sourcePort + i, address3);
            }
            
            //Run Horses
            Spectator[] spectators = new Spectator[numberOfSpectators];
            for(int i=0; i<numberOfSpectators; i++){
                spectators[i] = new Spectator(i, numberOfRaces, pd[i], bc[i], ccws[i]);
                spectators[i].start();
            }
            for(int i=0; i<numberOfSpectators; i++){
                spectators[i].join();
            }
        } catch(IOException e){
            e.printStackTrace();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
