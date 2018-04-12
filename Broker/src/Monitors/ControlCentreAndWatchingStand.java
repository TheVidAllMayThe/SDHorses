package Monitors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ControlCentreAndWatchingStand {

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
        ControlCentreAndWatchingStand.port = port;
    }

    public static void openingTheEvents() {
        try {
            initConnection();

            pw.print("openingTheEvents");
            if(!in.readLine().equals("true"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void summonHorsesToPaddock(int numRace) {
        try {
            initConnection();

            pw.print("summonHorsesToPaddock/" + numRace);

            if(!in.readLine().equals("true"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void startTheRace() {
        try {
            initConnection();

            pw.print("startTheRace");

            if(!in.readLine().equals("true"))
                System.out.println("Something wrong in startTheRace of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void reportResults(int[] list) {
        try {
            initConnection();

            pw.print("reportResults/" + Arrays.toString(list));

            if(!in.readLine().equals("true"))
                System.out.println("Something wrong in reportResults of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    static public void entertainTheGuests() {
        try {
            initConnection();

            pw.print("entertainTheGuests");

            if(!in.readLine().equals("true"))
                System.out.println("Something wrong in entertainTheGuests of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }
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