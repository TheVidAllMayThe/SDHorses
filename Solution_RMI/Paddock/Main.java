import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class that initializes the server.
 */

public class Main {
    public static void main(String[] args){
        try{
            //Creates input and output streams
            int sourcePort = Integer.valueOf(args[0]);
            GeneralRepositoryOfInformation_Interface groi = null;
            Registry groiregistry = null;
            while(groi == null){
                try{
                    groiregistry = LocateRegistry.getRegistry(args[1], Integer.valueOf(args[2]));
                    groi = (GeneralRepositoryOfInformation_Interface) groiregistry.lookup("GeneralRepositoryOfInformation");
                }catch(RemoteException | NotBoundException ignored){}
            }
            
            //Calls method setMonitorAddress for monitor #0 (Paddock)
            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 0);

            //Monitor is now open to requests from clients
            Paddock pd = new Paddock(groi);
            Paddock_Interface pd_i = (Paddock_Interface) UnicastRemoteObject.exportObject(pd, sourcePort);
            Registry registry = LocateRegistry.createRegistry(sourcePort);
            registry.bind("Paddock", pd_i);
        } catch(RemoteException | AlreadyBoundException | UnknownHostException e){
            e.printStackTrace();
        }
    }
}
