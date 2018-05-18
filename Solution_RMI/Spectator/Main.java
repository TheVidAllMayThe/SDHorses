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
            Registry registry = null;
            GeneralRepositoryOfInformation_Interface groi = null;
            Paddock_Interface pd = null;
            BettingCentre_Interface bc = null;
            ControlCentreAndWatchingStand_Interface ccws = null;
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
            int numberOfSpectators = groi.getNumberOfSpectators();
            int numberOfRaces = groi.getNumberOfRaces();


            while(pd == null){
                try{
                    pd = (Paddock_Interface) registry.lookup("Paddock");
                }catch(RemoteException | NotBoundException ignored){}
            }
                
            while(bc == null){
                try{
                    bc = (BettingCentre_Interface) registry.lookup("BettingCentre");
                }catch(RemoteException | NotBoundException ignored){}
            }

            while(ccws == null){
                try{
                    ccws = (ControlCentreAndWatchingStand_Interface) registry.lookup("ControlCentreAndWatchingStand");
                }catch(RemoteException | NotBoundException ignored){}
            }

            Spectator[] spectators = new Spectator[numberOfSpectators];
            for(int i=0; i<numberOfSpectators; i++){
                spectators[i] = new Spectator(i, numberOfRaces, pd, bc, ccws);
                spectators[i].start();
            }
            for(int i=0; i<numberOfSpectators; i++){
                spectators[i].join();
            }

        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
