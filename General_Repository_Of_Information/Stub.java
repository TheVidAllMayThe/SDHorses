import java.io.IOException;
import java.net.ServerSocket;

public class Stub{
   public static void main(String[] args){
        GeneralRepositoryOfInformation groi = new GeneralRepositoryOfInformation(5,4,4, 10);
        
        try{
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(args[0]));
            while(true){
                new ClientThread(serverSocket.accept(), groi).start();
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{ 
            groi.close();
        }
    }
}
