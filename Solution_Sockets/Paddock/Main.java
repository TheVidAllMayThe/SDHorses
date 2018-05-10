import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
            
            //Calls method setMonitorAddress for monitor #0 (Paddock)
            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 0);
            int numRaces = groi.getNumberOfRaces();

            Paddock pd = new Paddock(groi);
        
            //Monitor is now open to requests from clients
            ServerSocket serverSocket = new ServerSocket(sourcePort);
            serverSocket.setSoTimeout(1000);
            while(closed < pd.getNumberOfSpectators() + pd.getNumberOfHorses() * numRaces){
                try{
                    new ClientThread(serverSocket.accept(), pd).start();
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
