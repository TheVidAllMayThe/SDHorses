import java.io.IOException;
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

            int numberOfRaces = groi.getNumberOfRaces();

            System.out.println(numberOfRaces);



            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(1).getHostAddress(), groi.getMonitorPort(1));
            Stable_Interface st = (Stable_Interface) registry.lookup("Stable");


            System.out.println("Stable OK");

            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(2).getHostAddress(), groi.getMonitorPort(2));
            BettingCentre_Interface bc =
                    (BettingCentre_Interface) registry.lookup("BettingCentre");

            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(3).getHostAddress(), groi.getMonitorPort(3));
            ControlCentreAndWatchingStand_Interface ccws =
                    (ControlCentreAndWatchingStand_Interface ) registry.lookup("ControlCentreAndWatchingStand");

            registry = LocateRegistry.getRegistry(groi.getMonitorAddress(4).getHostAddress(), groi.getMonitorPort(4));
            RaceTrack_Interface rt =
                    (RaceTrack_Interface) registry.lookup("RaceTrack");

            
            //Run Broker
            Broker b = new Broker(numberOfRaces, st, bc, ccws, rt);
            b.start();
            b.join(); 

        } catch(IOException | NotBoundException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
