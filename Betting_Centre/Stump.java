import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.util.LinkedList;

public class Stump{
    public static void main(String[] args){
        InetAddress sourceAddress = InetAddress.getByName("localhost");
        int sourcePort = 23040;
        Socket echoSocket = new Socket(InetAddress.getByName("ip of general rep"), 23040, sourceAddress, sourcePort);
        PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
        ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());
        
        LinkedList<Object> list = new LinkedList<>();
        list.add("setMonitorAddress");
        list.add(sourceAddress);
        list.add(sourcePort);
        list.add(2);

        pw.print(list);
        if(!((String)in.readObject()).equals("ok"))
            System.out.println("Someting wrong in setting address of Betting_Centre");

        in.close();
        pw.close();
        echoSocket.close();
        
        try{
            ServerSocket serverSocket = new ServerSocket(23040);
            while(true){
                new ClientThread(serverSocket.accept(), BettingCentre.class).run();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
