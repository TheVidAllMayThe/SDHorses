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
        RaceTrack.port = port;
    }

    public static void startTheRace(){
        try {
        initConnection();

        pw.print("startTheRace");

        if(!in.readLine().equals("ok"))
            System.out.println("Something wrong in openingTheEvents of Broker");

        closeConnection();
    }catch (IOException e){
        e.printStackTrace();
    }
}


    public static int[] reportResults(){

        return null;
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
