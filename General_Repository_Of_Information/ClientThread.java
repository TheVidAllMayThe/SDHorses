import java.net.Socket;
import java.lang.Class;
import java.lang.reflect.Method;
import java.lang.NoSuchMethodException;
import java.lang.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.io.IOException;

public class ClientThread extends Thread{
    private Socket clientSocket;
    private Class monitorClass;
    
    public ClientThread(Socket clientSocket, Class monitorClass){
        this.clientSocket = clientSocket;
        this.monitorClass = monitorClass;
    }
     
    public void run(){
        try{
            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            LinkedList<Object> list = (LinkedList<Object>) in.readObject();
            pw.print(reflection(list));         

            pw.close();
            in.close();
            clientSocket.close(); 
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private Object reflection(LinkedList<Object> list){
        Class[] classArray = new Class[list.size()];
        for(int i=0; i < list.size(); i++){
            classArray[i] = list.getClass();
        }
        Method method = null;
        Object result = null;
        try{
            method = monitorClass.getMethod((String) list.get(0), classArray);
            result = method.invoke(null, list.toArray());
        } catch(NoSuchMethodException e){
            e.printStackTrace();
        } catch(IllegalAccessException e){ 
            e.printStackTrace();
        } catch(InvocationTargetException e){ 
            e.printStackTrace();
        }
        return result;
    }
}
