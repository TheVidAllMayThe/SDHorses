package Monitors;

import Threads.Broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;


/**
 * The {@link RaceTrack} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by multiple {@link Horse}s and by the {@link Broker}.
 * <p>
 * This is where the {@link Horse}s compete with each other to reach the end of the race.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Main.HorseRace

 */

@SuppressWarnings("JavadocReference")
public class RaceTrack {

    private static InetAddress targetAddress;
    private static int targetPort;
    private static boolean[] availablePorts;

    public static void initialize(String ip, int port){
        try {
            targetAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        targetPort = port;
        availablePorts = new boolean[]{true, true, true, true, true, true, true, true, true, true};
    }

    public static void startTheRace(){
        try {
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());

            LinkedList<Object> list = new LinkedList<>();
            list.add("startTheRace");

            pw.print(list);
            if(!((String)in.readObject()).equals("ok"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            closeConnection(echoSocket, pw, in);
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    public static int[] reportResults(){
        int[] result = null;
        try {
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());

            LinkedList<Object> list = new LinkedList<>();
            list.add("reportResults");

            pw.print(list);
            
            result = (int[])in.readObject();
            closeConnection(echoSocket, pw, in);
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }

    private static Socket initConnection() throws IOException {
        int inputPort = -1;
        Socket echoSocket = null;
        for(int i=0; i < 10; i++){
            if ( availablePorts[i] ){
                inputPort = 23040 + i;
                availablePorts[i] = false;
                break;
            }
        }
        try{
            echoSocket = new Socket(targetAddress, targetPort, InetAddress.getByName("localhost"), inputPort);
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
        return echoSocket;
    }

    private static void closeConnection(Socket echoSocket, PrintWriter pw, ObjectInputStream in) throws IOException{
        in.close();
        pw.close();
        availablePorts[echoSocket.getPort() - 23040] = true;
        echoSocket.close();
    }

}
