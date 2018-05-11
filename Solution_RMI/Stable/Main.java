import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args){
        GeneralRepositoryOfInformation_Interface groi = null;
        Registry registry;
        try{
            //Creates input and output streams
            int sourcePort = Integer.valueOf(args[0]);
            registry = LocateRegistry.createRegistry(sourcePort);
            groi = (GeneralRepositoryOfInformation_Interface) registry.lookup("GeneralRepositoryOfInformation");
            
            //Calls method setMonitorAddress for monitor #1 (Stable)
            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 1);

            //Monitor is now open to requests from clients
            Stable sb = new Stable(groi);
            Stable_Interface sb_i = (Stable_Interface) UnicastRemoteObject.exportObject(sb, sourcePort);
            registry.bind("Stable", sb_i);
        } catch(RemoteException | NotBoundException | AlreadyBoundException | UnknownHostException e){
            e.printStackTrace();
        }
    }
}
