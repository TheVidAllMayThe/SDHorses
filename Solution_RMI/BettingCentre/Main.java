import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

/**
 * Class that initializes the server.
 */

public class Main {
    public static void main(String[] args){
        try{
            //Creates input and output streams
            int sourcePort = Integer.valueOf(args[0]);
            GeneralRepositoryOfInformation_Interface groi = null;
            while(groi == null){
                try{
                    Registry groiregistry = LocateRegistry.getRegistry(args[1], Integer.valueOf(args[2]));
                    groi = (GeneralRepositoryOfInformation_Interface) groiregistry.lookup("GeneralRepositoryOfInformation");
                }catch(RemoteException | NotBoundException e){
                    e.printStackTrace();
                }
            }
            
            //Calls method setMonitorAddress for monitor #2 (Betting_centre)

            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 2);


            //Monitor is now open to requests from clients
            BettingCentre bc = new BettingCentre(groi);
            BettingCentre_Interface bc_i = (BettingCentre_Interface) UnicastRemoteObject.exportObject(bc, sourcePort);
            Registry registry = LocateRegistry.createRegistry(sourcePort);
            registry.bind("BettingCentre", bc_i);
        } catch(RemoteException | AlreadyBoundException | UnknownHostException e){
            e.printStackTrace();
        }
    }
}
