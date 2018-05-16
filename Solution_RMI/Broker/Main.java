import java.io.IOException;
import java.rmi.NotBoundException; import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args){

        try{
            //Creates input and output streams
            Registry registry;
            GeneralRepositoryOfInformation_Interface groi = null;
            Stable_Interface st = null;
            BettingCentre_Interface bc = null;
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
            int numberOfRaces = groi.getNumberOfRaces();

            while(st == null){
                try{
                    registry = LocateRegistry.getRegistry(groi.getMonitorAddress(1).getHostAddress(), groi.getMonitorPort(1));
                    st = (Stable_Interface) registry.lookup("Stable");
                }catch(RemoteException | NotBoundException ignored){}
            }
                
            while(bc == null){
                try{
                    registry = LocateRegistry.getRegistry(groi.getMonitorAddress(2).getHostAddress(), groi.getMonitorPort(2));
                    bc = (BettingCentre_Interface) registry.lookup("BettingCentre");
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

            //Run Broker
            Broker b = new Broker(numberOfRaces, st, bc, ccws, rt);
            b.start();
            b.join();  

        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
