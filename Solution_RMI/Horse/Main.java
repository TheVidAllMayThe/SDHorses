import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args){
        try{
            //Creates input and output streams
            int port = -1;
            String host = null;

            if(args.length >= 2){
                host = args[0];
                port = Integer.parseInt(args[1]);
            }

            Registry registry = LocateRegistry.getRegistry(host, port);
            GeneralRepositoryOfInformation_Interface groi =
                    (GeneralRepositoryOfInformation_Interface) registry.lookup("GeneralRepositoryOfInformation");

            int numberOfHorses = groi.getNumberOfHorses();
            int numberOfRaces = groi.getNumberOfRaces();

            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(1).getHostAddress(), groi.getMonitorPort(1));
            Stable_Interface st =
                    (Stable_Interface) registry.lookup("Stable");

            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(0).getHostAddress(), groi.getMonitorPort(0));
            Paddock_Interface pd =
                    (Paddock_Interface) registry.lookup("Paddock");

            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(3).getHostAddress(), groi.getMonitorPort(3));
            ControlCentreAndWatchingStand_Interface ccws =
                    (ControlCentreAndWatchingStand_Interface ) registry.lookup("ControlCentreAndWatchingStand");

            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(4).getHostAddress(), groi.getMonitorPort(4));
            RaceTrack_Interface rt =
                    (RaceTrack_Interface) registry.lookup("RaceTrack");

            //Run Horses
            Horse[] horses = new Horse[numberOfHorses*numberOfRaces];

            for(int i=0; i<numberOfHorses * numberOfRaces; i++){
                horses[i] = new Horse(i%numberOfHorses, i/numberOfHorses, pd, st, ccws, rt);
                horses[i].start();
            }

            for(int i=0; i<numberOfRaces*numberOfHorses; i++){
                horses[i].join();
            }

        } catch(IOException | InterruptedException | NotBoundException e){
            e.printStackTrace();
        }
    }
}
