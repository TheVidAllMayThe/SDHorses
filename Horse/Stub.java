import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.lang.ClassNotFoundException;

public class Stub{
    public static void main(String[] args){
        Socket clientSocket = null;
        try{
            //Creates input and output streams
            InetAddress sourceAddress = InetAddress.getByName("localhost");
            Socket echoSocket = new Socket(InetAddress.getByName(args[0]), Integer.valueOf(args[1]));
            GeneralRepositoryOfInformation groi = new GeneralRepositoryOfInformation(echoSocket);
            
            //Gets address and port of all necessary monitors
            InetSocketAddress address0 = new InetSocketAddress(groi.getMonitorAddress(0), groi.getMonitorPort(0));
            InetSocketAddress address1 = new InetSocketAddress(groi.getMonitorAddress(1), groi.getMonitorPort(1));
            InetSocketAddress address3 = new InetSocketAddress(groi.getMonitorAddress(3), groi.getMonitorPort(3));
            InetSocketAddress address4 = new InetSocketAddress(groi.getMonitorAddress(4), groi.getMonitorPort(4));

            int numberOfHorses = groi.getNumberOfHorses();
            int numberOfRaces = groi.getNumberOfRaces();
            groi.close();

            Paddock[] pd = new Paddock[numberOfHorses * numberOfRaces];
            Stable[] st = new Stable[numberOfHorses * numberOfRaces];
            ControlCentreAndWatchingStand[] ccws = new ControlCentreAndWatchingStand[numberOfHorses * numberOfRaces];
            RaceTrack[] rt = new RaceTrack[numberOfHorses * numberOfRaces];
            for(int i=0; i<numberOfHorses * numberOfRaces; i++){
                pd[i] = new Paddock(address0);
                st[i] = new Stable(address1);
                ccws[i] = new ControlCentreAndWatchingStand(address3);
                rt[i] = new RaceTrack(address4);
            }
            
            //Run Horses
            Horse[] horses = new Horse[numberOfHorses*numberOfRaces];
            for(int i=0; i<numberOfHorses * numberOfRaces; i++){
                horses[i] = new Horse(i%numberOfHorses, i/numberOfHorses, pd[i], st[i], ccws[i], rt[i]);
                horses[i].start();
            }
            for(int i=0; i<numberOfRaces*numberOfHorses; i++){
                horses[i].join();
                pd[i].closeConnection();
                st[i].closeConnection();
                ccws[i].closeConnection();
                rt[i].closeConnection();
            }
        } catch(IOException e){
            e.printStackTrace();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
