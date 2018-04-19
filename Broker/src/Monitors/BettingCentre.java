package Monitors;

import Monitors.AuxiliaryClasses.Parameters;
import Threads.Broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
* The {@link BettingCentre} class is a monitor that contains all the
* necessary methods to be used in mutual exclusive access by the {@link Broker} and Spectator.
* <p>
* This is where the Bets are handled.
*
* @author  David Almeida, Manuel Xarez
* @version 1.0
* @since   2018-03-21
* @see Broker
*/

public class BettingCentre{

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

    public static void acceptTheBets(){
        try{
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            LinkedList<Object> list = new LinkedList<>();
            list.add("acceptTheBets");

            pw.print(list);
            if (!in.readLine().equals("ok"))
                System.out.println("Something wrong with acceptTheBets of broker");

            closeConnection(echoSocket, pw, in);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    static public boolean areThereAnyWinners(int[] winnerList) {
        try {
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            LinkedList<Object> list = new LinkedList<>();
            list.add("areThereAnyWinners");
            list.add(winnerList);

            pw.print(list);
            boolean returnValue = Boolean.valueOf(in.readLine());

            closeConnection(echoSocket, pw, in);
            return returnValue;
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    public static void honorBets() {
        try {
            Socket echoSocket = initConnection();
            PrintWriter pw = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            LinkedList<Object> list = new LinkedList<>();
            
            pw.print(list);
            if(!in.readLine().equals("ok"))
                System.out.println("Something wrong in honorBets of Broker");

            closeConnection(echoSocket, pw, in);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static Socket initConnection() throws IOException{
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
