package Monitors;
import Threads.Broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


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

    private static InetAddress address;
    private static int port;
    private static Socket echoSocket;
    private static PrintWriter pw;
    private static BufferedReader in;


    public static void initialize(String ip, int port){
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Stable.port = port;
    }

    /**
     * {@link Broker} awakes the {@link Horse}s who are waiting to enter the {@link Paddock}.
     */
    public static void summonHorsesToPaddock(){

    }

    /**
     * Last function of {@link Broker} lifecycle, awakes {@link Horse}s waiting to enter {@link Paddock}.
     */
    public static void entertainTheGuests(){

    }

    private static void initConnection() throws IOException {
        echoSocket = new Socket(address, port);
        pw = new PrintWriter(echoSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
    }

    private static void closeConnection() throws IOException{
        in.close();
        pw.close();
        echoSocket.close();
    }

}
