import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.SocketException;

public class Monitor{
    protected InetSocketAddress address;
    protected Socket clientSocket;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;

    public Monitor(InetSocketAddress address){
        try{
            this.address = address;
            clientSocket = new Socket(); 
            clientSocket.connect(address);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch(SocketException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try{
            out.writeObject("close");
            out.flush();
            out.close();
            in.close();
            clientSocket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
