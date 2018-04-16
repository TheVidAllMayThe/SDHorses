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
        BettingCentre.port = port;
    }

    public static void acceptTheBets(int[] winnerList){
        try{
            initConnection();
            pw.print("acceptTheBets/" + Arrays.toString(winnerList));
            if (!in.readLine().equals("ok"))
                System.out.println("Something wrong with acceptTheBets of broker");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    static public boolean areThereAnyWinners(int[] winnerList) {
        try {
            initConnection();
            pw.print("areThereAnyWinners/" + Arrays.toString(winnerList));
            boolean returnValue = Boolean.valueOf(in.readLine());
            closeConnection();
            return returnValue;
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    public static void honorBets() {
        try {
            initConnection();

            pw.print("honorBets");
            if(!in.readLine().equals("ok"))
                System.out.println("Something wrong in honorBets of Broker");

            closeConnection();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void initConnection() throws IOException{
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
