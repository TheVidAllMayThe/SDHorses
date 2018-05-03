import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
   private static int closed = 0;
   private static ReentrantLock r1 = new ReentrantLock(false);

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
