package Monitors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

public class ControlCentreAndWatchingStand {

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

    public static void openingTheEvents() {
        try {
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            LinkedList<Object> list = new LinkedList<>();
            list.add("openingTheEvents");

            pw.print(list);
            if(!in.readLine().equals("ok"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            closeConnection(echoSocket, pw, in);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void summonHorsesToPaddock(int numRace) {
        try {
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            LinkedList<Object> list = new LinkedList<>();
            list.add("summonHorsesToPaddock");
            list.add(numRace);

            pw.print(list);
            if(!in.readLine().equals("ok"))
                System.out.println("Something wrong in openingTheEvents of Broker");

            closeConnection(echoSocket, pw, in);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void startTheRace() {
        try {
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            LinkedList<Object> list = new LinkedList<>();
            list.add("startTheRace");

            pw.print(list);
            if(!in.readLine().equals("ok"))
                System.out.println("Something wrong in startTheRace of Broker");

            closeConnection(echoSocket, pw, in);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void reportResults(int[] results) {
        try {
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            LinkedList<Object> list = new LinkedList<>();
            list.add("reportResults");
            list.add(results);

            pw.print(list);
            if(!in.readLine().equals("ok"))
                System.out.println("Something wrong in reportResults of Broker");

            closeConnection(echoSocket, pw, in);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    static public void entertainTheGuests() {
        try {
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            LinkedList<Object> list = new LinkedList<>();
            list.add("entertainTheGuests");

            pw.print(list);
            if(!in.readLine().equals("ok"))
                System.out.println("Something wrong in entertainTheGuests of Broker");

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
            }
        }
        try{
            echoSocket = new Socket(targetAddress, targetPort, InetAddress.getByName("localhost"), inputPort);
        } catch (UnknownHostException e) {
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
