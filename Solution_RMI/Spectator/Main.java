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

            int port = -1;
            String host = null;

            if(args.length >= 2){
                host = args[0];
                port = Integer.parseInt(args[1]);
            }

            Registry registry = LocateRegistry.getRegistry(host, port);
            GeneralRepositoryOfInformation_Interface groi =
                    (GeneralRepositoryOfInformation_Interface) registry.lookup("GeneralRepositoryOfInformation");

            //Gets address and port of all necessary monitors
            InetSocketAddress address0 = new InetSocketAddress(groi.getMonitorAddress(0), groi.getMonitorPort(0));
            InetSocketAddress address2 = new InetSocketAddress(groi.getMonitorAddress(2), groi.getMonitorPort(2));
            InetSocketAddress address3 = new InetSocketAddress(groi.getMonitorAddress(3), groi.getMonitorPort(3));

            int numberOfSpectators = groi.getNumberOfSpectators();
            int numberOfRaces = groi.getNumberOfRaces();


            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(0).getHostAddress(), groi.getMonitorPort(0));
            Paddock_Interface pd =
                    (Paddock_Interface) registry.lookup("Paddock");

            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(2).getHostAddress(), groi.getMonitorPort(2));
            BettingCentre_Interface bc =
                    (BettingCentre_Interface) registry.lookup("BettingCentre");


            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(3).getHostAddress(), groi.getMonitorPort(3));
            ControlCentreAndWatchingStand_Interface ccws =
                    (ControlCentreAndWatchingStand_Interface ) registry.lookup("ControlCentreAndWatchingStand");


            Spectator[] spectators = new Spectator[numberOfSpectators];
            for(int i=0; i<numberOfSpectators; i++){
                spectators[i] = new Spectator(i, numberOfRaces, pd, bc, ccws);
                spectators[i].start();
            }
            for(int i=0; i<numberOfSpectators; i++){
                spectators[i].join();
            }

        } catch(IOException | InterruptedException | NotBoundException e){
            e.printStackTrace();
        }
    }
}
