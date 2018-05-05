package BettingCentre;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that initializes the server.
 */

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
            
            //Calls method setMonitorAddress for monitor #2 (Betting_centre)
            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 2);

            BettingCentre bc = new BettingCentre(groi);
        
            //Monitor is now open to requests from clients
            ServerSocket serverSocket = new ServerSocket(sourcePort);
            serverSocket.setSoTimeout(1000);
            while(closed < 1 + bc.getNumberOfSpectators()){
                try{
                    new ClientThread(serverSocket.accept(), bc).start();
                }catch (SocketTimeoutException ignored){

                }
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            if (groi != null) {
                groi.close();
            }
        }
    }



    static void closed(){
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
