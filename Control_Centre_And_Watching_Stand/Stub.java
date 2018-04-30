import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.lang.ClassNotFoundException;

public class Stub{
    public static void main(String[] args){
        GeneralRepositoryOfInformation groi = null;
        try{
            //Creates input and output streams
            int sourcePort = Integer.valueOf(args[0]);
            Socket echoSocket = new Socket(InetAddress.getByName(args[1]), Integer.valueOf(args[2]));
            groi = new GeneralRepositoryOfInformation(echoSocket);
            
            //Calls method setMonitorAddress for monitor #3 (Control Centre and Watching Stand)
            groi.setMonitorAddress(InetAddress.getByName("localhost"), sourcePort, 3);

            //Gets variables necessary for ControlCentre
            int raceLength = groi.getRaceLength();
            int numSpectators = groi.getNumberOfSpectators();
            int numHorses = groi.getNumberOfHorses();

            ControlCentreAndWatchingStand ccws = new ControlCentreAndWatchingStand(groi);

        
            //Monitor is now open to requests from clients
            ServerSocket serverSocket = new ServerSocket(sourcePort);
            while(true){
                new ClientThread(serverSocket.accept(), ccws).start();
            }

        } catch(IOException e){
            e.printStackTrace();
        } finally{
            groi.close();
        }
    }
}
