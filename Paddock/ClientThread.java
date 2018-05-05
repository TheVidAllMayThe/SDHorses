package Paddock;

import java.net.Socket;
import java.lang.Class;
import java.lang.reflect.Method;
import java.lang.NoSuchMethodException;
import java.lang.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *  An instance of this class will be created in order to execute the function requested by the client.
 */
public class ClientThread extends Thread{
    private Socket clientSocket;
    private Class monitorClass;
    private Object obj;


    /**
     * Contructor of the class.
     * @param clientSocket Socket to which the return value of the function to be execute will be sent.
     * @param obj List consisting of arguments and function name.
     */
    ClientThread(Socket clientSocket, Object obj){
        this.clientSocket = clientSocket;
        this.monitorClass = obj.getClass();
        this.obj = obj;
    }
    /**
     * Main execution of the thread.
     */
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
            Main.closed();
        
            out.close();
            in.close();
            clientSocket.close(); 
        } catch (ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Executes methods using reflection.
     * @param list List consisting of the method to be executed name and arguments.
     * @return Return value of the method executed by reflection.
     */
    private Object reflection(LinkedList<Object> list){
        Class[] classArray = new Class[list.size() - 1];
        Object[] args = new Object[list.size()-1];
        for(int i=1; i < list.size(); i++){
            classArray[i-1] = list.get(i).getClass();
            args[i-1] = list.get(i);
        }
        Method method;
        Object result = null;
        try{
            method = monitorClass.getMethod((String) list.get(0), classArray);
            result = method.invoke(obj, args);
            if (result == null) result = (Object) "ok";
        } catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }
        return result;
    }
}
