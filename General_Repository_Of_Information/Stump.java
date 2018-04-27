import java.io.IOException;
import java.net.ServerSocket;

public class Stump{
   public static void main(String[] args){
        GeneralRepositoryOfInformation groi = new GeneralRepositoryOfInformation(5,4,4, 100);
        
        try{
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(args[0]));
            while(true){
                new ClientThread(serverSocket.accept(), GeneralRepositoryOfInformation.class, groi).start();
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{ 
            groi.close();
        }
    }
}
