package Stable;
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

public class Main {
    private static int closed = 0;
    private static ReentrantLock r1 = new ReentrantLock(false);

    public static void main(String[] args){
        GeneralRepositoryOfInformation groi = null;
        try{
            //Creates input and output streams
            int sourcePort = Integer.valueOf(args[0]);
            Socket echoSocket = new Socket(InetAddress.getByName(args[1]), Integer.valueOf(args[2]));
            groi = new GeneralRepositoryOfInformation(echoSocket);
            
            //Calls method setMonitorAddress for monitor #1 (Stable)
            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 1);
            int numRaces = groi.getNumberOfRaces();

            Stable sb = new Stable(groi);

            //Monitor is now open to requests from clients
            ServerSocket serverSocket = new ServerSocket(sourcePort);
            serverSocket.setSoTimeout(1000);
            while(closed < 1 + sb.getNumberOfHorses() * numRaces){
                try{
                    new ClientThread(serverSocket.accept(), sb).start();
                } catch(SocketTimeoutException ignored){
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
