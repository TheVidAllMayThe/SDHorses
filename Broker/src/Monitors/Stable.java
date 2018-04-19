package Monitors;
import Threads.Broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;


/**
 * The {@link Stable} class is a monitor that contains all the
 * necessary methods to be used in mutual exclusive access by the {@link Broker} and {@link Horse}s to itself.
 * <p>
* This is where the {@link Horse}s are initially and after a race.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Main.HorseRace
 *@see Threads.Broker
 *@see Horse
*/

@SuppressWarnings("JavadocReference")
public class Stable {

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

    /**
     * {@link Broker} awakes the {@link Horse}s who are waiting to enter the {@link Paddock}.
     */
    public static void summonHorsesToPaddock(){
        try{
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            LinkedList<Object> list = new LinkedList<>();
            list.add("summonHorsesToPaddock");

            pw.print(list);
            if (!in.readLine().equals("ok"))
                System.out.println("Something wrong with summonHorsesToPaddock");

            closeConnection(echoSocket, pw, in);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Last function of {@link Broker} lifecycle, awakes {@link Horse}s waiting to enter {@link Paddock}.
     */
    public static void entertainTheGuests(){
        try{
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            LinkedList<Object> list = new LinkedList<>();
            list.add("entertainTheGuests");

            pw.print(list);
            if (!in.readLine().equals("ok"))
                System.out.println("Something wrong with entertainTheGuests");

            closeConnection(echoSocket, pw, in);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private static Socket initConnection() throws IOException {
        int inputPort = -1;
        Socket echoSocket = null;
        for(int i=0; i < 10; i++){
            if( availablePorts[i] ){
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

    private static void closeConnection(Socket echoSocket, PrintWriter pw, BufferedReader in) throws IOException{
        in.close();
        pw.close();
        availablePorts[echoSocket.getPort() - 23040] = true;
        echoSocket.close();
    }

}
