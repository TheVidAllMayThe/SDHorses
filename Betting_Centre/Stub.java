import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.lang.ClassNotFoundException;
import java.net.SocketTimeoutException;

public class Stub{
    public static int closed = 0;

    public static void main(String[] args){
        GeneralRepositoryOfInformation groi = null;
        try{
            //Creates input and output streams
            int sourcePort = Integer.valueOf(args[0]);
            Socket echoSocket = new Socket(InetAddress.getByName(args[1]), Integer.valueOf(args[2]));
            groi = new GeneralRepositoryOfInformation(echoSocket);
            
            //Calls method setMonitorAddress for monitor #2 (Betting_centre)
            groi.setMonitorAddress(InetAddress.getByName("localhost"), sourcePort, 2);

            BettingCentre bc = new BettingCentre(groi);
        
            //Monitor is now open to requests from clients
            ServerSocket serverSocket = new ServerSocket(sourcePort);
            serverSocket.setSoTimeout(1000);
            while(closed < 1 + bc.getNumberOfSpectators()){
                try{
                    new ClientThread(serverSocket.accept(), bc).start();
                }catch (SocketTimeoutException e){
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            groi.close();
        }
    }
}
