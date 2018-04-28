import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.lang.ClassNotFoundException;

public class Stump{
    public static void main(String[] args){
        Socket clientSocket = null;
        try{
            //Creates input and output streams
            InetAddress sourceAddress = InetAddress.getByName("localhost");
            int sourcePort = Integer.valueOf(args[0]);
            Socket echoSocket = new Socket();
            echoSocket.setReuseAddress(true);
            echoSocket.bind(new InetSocketAddress(sourceAddress, sourcePort));
            echoSocket.connect(new InetSocketAddress(InetAddress.getByName(args[1]), Integer.valueOf(args[2])));
            GeneralRepositoryOfInformation groi = new GeneralRepositoryOfInformation(echoSocket);
            
            //Gets address and port of all necessary monitors
            InetSocketAddress address0 = new InetSocketAddress(groi.getMonitorAddress(0), groi.getMonitorPort(0));
            InetSocketAddress address1 = new InetSocketAddress(groi.getMonitorAddress(1), groi.getMonitorPort(1));
            InetSocketAddress address2 = new InetSocketAddress(groi.getMonitorAddress(3), groi.getMonitorPort(3));
            InetSocketAddress address4 = new InetSocketAddress(groi.getMonitorAddress(4), groi.getMonitorPort(4));

            int numberOfHorses = groi.getNumberOfHorses();
            int numberOfRaces = groi.getNumberOfRaces();
            groi.close();

            Paddock[] pd = new Paddock[numberOfHorses];
            Stable[] st = new Stable[numberOfHorses];
            ControlCentreAndWatchingStand[] ccws = new ControlCentreAndWatchingStand[numberOfHorses];
            RaceTrack[] rt = new RaceTrack[numberOfHorses];
            for(int i=0; i<numberOfHorses; i++){
                pd[i] = new Paddock(sourcePort + i, address0);
                st[i] = new Stable(sourcePort + i, address1);
                ccws[i] = new ControlCentreAndWatchingStand(sourcePort + i, address3);
                rt[i] = new RaceTrack(sourcePort + i, address4);
            }
            
            //Run Horses
            Horse[] horses = new Horse[numberOfHorses];
            for(int j=0; j<numberOfRaces; j++){
                for(int i=0; i<numberOfHorses; i++){
                    horses[j*numberOfHorses + i] = new Horse(i,j, pd[i], st[i], ccws[i], rt[i]);
                    horses[j*numberOfHorses + i].start();
                }
            }
            for(int i=0; i<numberOfRaces*numberOfHorses; i++){
                horses[i].join();
            }
        } catch(IOException e){
            e.printStackTrace();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
