import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.lang.ClassNotFoundException;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class Stub{
    public static int closed = 0;
    public static ReentrantLock r1 = new ReentrantLock(false);

    public static void main(String[] args){
        GeneralRepositoryOfInformation groi = null;
        try{
            //Creates input and output streams
            int sourcePort = Integer.valueOf(args[0]);
            Socket echoSocket = new Socket(InetAddress.getByName(args[1]), Integer.valueOf(args[2]));
            groi = new GeneralRepositoryOfInformation(echoSocket);
            
            //Calls method setMonitorAddress for monitor #3 (Control Centre and Watching Stand)
            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 3);

            //Gets variables necessary for ControlCentre
            int nRaces = groi.getNumberOfRaces();
            ControlCentreAndWatchingStand ccws = new ControlCentreAndWatchingStand(groi);

        
            //Monitor is now open to requests from clients
            ServerSocket serverSocket = new ServerSocket(sourcePort);
            serverSocket.setSoTimeout(1000);
            while(closed < 1 + ccws.getNumberOfSpectators() + ccws.getNumberOfHorses() * nRaces){
                System.out.println(closed);
                try{
                    new ClientThread(serverSocket.accept(), ccws).start();
                } catch (SocketTimeoutException e){
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        } finally{
            groi.close();
        }
    }

    public static void closed(){
        r1.lock();
        try{
            closed++;
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            r1.unlock();
        }
    }

}
