import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

public class Stub{
   public static int closed = 0;

   public static void main(String[] args){
        GeneralRepositoryOfInformation groi = new GeneralRepositoryOfInformation(5,4,4, 10);
        
        try{
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(args[0]));
            serverSocket.setSoTimeout(1000);
            while(closed < 5){
                try{
                    new ClientThread(serverSocket.accept(), groi).start();
                } catch(SocketTimeoutException e){
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{ 
            groi.close();
        }
    }
}
