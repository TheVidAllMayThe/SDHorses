import java.util.LinkedList;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;

public class GeneralRepositoryOfInformation{
    private static Socket clientSocket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void initialize(Socket socket){
        try{
            clientSocket = socket;
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static void close(){
        try{
            out.close();
            in.close();
            clientSocket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void setBrokerState(String state){
        LinkedList<Object> list = new LinkedList<>();
        list.add("setBrokerState");
        list.add(state);

        try{
            out.writeObject(list);
            out.flush();
            if(!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong in setBrokerState of GRI");
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();    
        }
    }

    public static void setSpectatorsState(String state, int spectatorID){
        LinkedList<Object> list = new LinkedList<>();
        list.add("setSpectatorsState");
        list.add(state);
        list.add(spectatorID);

        try{
            out.writeObject(list);
            out.flush();
            if(!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong in setBrokerState of GRI");
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();    
        }
        
    }

    public static void setSpectatorsBudget(double budget, int spectatorID){
        LinkedList<Object> list = new LinkedList<>();
        list.add("setSpectatorsBudget");
        list.add(budget);
        list.add(spectatorID);

        try{ 
            out.writeObject(list);
            out.flush();
       
            if(!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong in setBrokerState of GRI");
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();    
        }
    }

    public static void setSpectatorsBet(double value, int spectatorID){
        LinkedList<Object> list = new LinkedList<>();
        list.add("setSpectatorsBet");
        list.add(value);
        list.add(spectatorID);

        
        try{
            out.writeObject(list);
            out.flush();
            if(!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong in setBrokerState of GRI");
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();    
        }
    }
}
