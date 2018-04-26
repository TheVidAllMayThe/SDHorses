import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.lang.ClassNotFoundException;

public class Stump{
    public static void main(String[] args){
        try{
            InetAddress sourceAddress = InetAddress.getByName("localhost");
            int sourcePort = Integer.valueOf(args[0]);
            Socket echoSocket = new Socket(InetAddress.getByName(args[1]), Integer.valueOf(args[2]), sourceAddress, sourcePort);
            ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());
            
            LinkedList<Object> list = new LinkedList<>();
            list.add("setMonitorAddress");
            list.add(sourceAddress);
            list.add(sourcePort);
            list.add(2);

            out.writeObject(list);
            out.flush();
            if(!((String)in.readObject()).equals("ok"))
                System.out.println("Someting wrong in setting address of Betting_Centre");

            list = new LinkedList<>();
            list.add("getNumberOfSpectators");

            out.writeObject(list);
            out.flush();
            BettingCentre.initialize((int)in.readObject());

            in.close();
            out.close();
            echoSocket.close();
        
            ServerSocket serverSocket = new ServerSocket(sourcePort);
            while(true){
                new ClientThread(serverSocket.accept(), BettingCentre.class).run();
            }

        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        } finally{
            GeneralRepositoryOfInformation.close();
        }
    }
}
