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
import java.io.ObjectOutputStream;

public class ClientThread extends Thread{
    private Socket clientSocket;
    private Class monitorClass;
    
    public ClientThread(Socket clientSocket, Class monitorClass){
        this.clientSocket = clientSocket;
        this.monitorClass = monitorClass;
    }
     
    public void run(){
        try{
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            Object input;
            LinkedList<Object> list;
            while(!(input = in.readObject()).equals("close")){
                list = (LinkedList<Object>) input;
                out.writeObject(reflection(list));         
                out.flush();
            }
            out.writeObject("ok");
            out.flush();
        
            out.close();
            in.close();
            clientSocket.close(); 
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private Object reflection(LinkedList<Object> list){
        Class[] classArray = new Class[list.size() - 1];
        Object[] args = new Object[list.size()-1];
        for(int i=1; i < list.size(); i++){
            classArray[i-1] = list.get(i).getClass();
            args[i-1] = list.get(i);
        }
        Method method = null;
        Object result = null;
        try{
            method = monitorClass.getMethod((String) list.get(0), classArray);
            result = method.invoke(null, args);
            if (result == null) result = (Object) "ok";
        } catch(NoSuchMethodException e){
            e.printStackTrace();
        } catch(IllegalAccessException e){ 
            e.printStackTrace();
        } catch(InvocationTargetException e){ 
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }
}
