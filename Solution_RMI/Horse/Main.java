import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args){
        try{
            //Creates input and output streams
            Registry registry;
            GeneralRepositoryOfInformation_Interface groi = null;
            Paddock_Interface pd = null;
            Stable_Interface st = null;
            ControlCentreAndWatchingStand_Interface ccws = null;
            RaceTrack_Interface rt = null; 
            int port = -1;
            String host = null;

            if(args.length >= 2){
                host = args[0];
                port = Integer.parseInt(args[1]);
            }

            while(groi == null){
                try{
                    registry = LocateRegistry.getRegistry(host, port);
                    groi = (GeneralRepositoryOfInformation_Interface) registry.lookup("GeneralRepositoryOfInformation");
                }catch(RemoteException | NotBoundException ignored){}
            } 

            int numberOfHorses = groi.getNumberOfHorses();
            int numberOfRaces = groi.getNumberOfRaces();
            
            while(pd == null){
                try{
                    registry = LocateRegistry.getRegistry(groi.getMonitorAddress(0).getHostAddress(), groi.getMonitorPort(0));
                    pd = (Paddock_Interface) registry.lookup("Paddock");
                }catch(RemoteException | NotBoundException ignored){}
            }

            while(st == null){
                try{
                    registry = LocateRegistry.getRegistry(groi.getMonitorAddress(1).getHostAddress(), groi.getMonitorPort(1));
                    st = (Stable_Interface) registry.lookup("Stable");
                }catch(RemoteException | NotBoundException ignored){}
            }
                
            while(ccws == null){
                try{
                    registry = LocateRegistry.getRegistry(groi.getMonitorAddress(3).getHostAddress(), groi.getMonitorPort(3));
                    ccws = (ControlCentreAndWatchingStand_Interface) registry.lookup("ControlCentreAndWatchingStand");
                }catch(RemoteException | NotBoundException ignored){}
            }

            while(rt == null){
                try{
                    registry = LocateRegistry.getRegistry(groi.getMonitorAddress(4).getHostAddress(), groi.getMonitorPort(4));
                    rt = (RaceTrack_Interface) registry.lookup("RaceTrack");
                }catch(RemoteException | NotBoundException ignored){}
            }

            //Run Horses
            Horse[] horses = new Horse[numberOfHorses*numberOfRaces];

            for(int i=0; i<numberOfHorses * numberOfRaces; i++){
                horses[i] = new Horse(i%numberOfHorses, i/numberOfHorses, pd, st, ccws, rt);
                horses[i].start();
            }

            for(int i=0; i<numberOfRaces*numberOfHorses; i++){
                horses[i].join();
            }

        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
