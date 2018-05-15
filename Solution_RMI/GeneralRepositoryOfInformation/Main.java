import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;

public class Main {
   public static void main(String[] args) throws UnknownHostException {

        Registry registry;
        try{
            int sourcePort = Integer.valueOf(args[0]);
            GeneralRepositoryOfInformation groi = new GeneralRepositoryOfInformation(5,4,4, 10);
            GeneralRepositoryOfInformation_Interface groi_i = (GeneralRepositoryOfInformation_Interface) UnicastRemoteObject.exportObject(groi, sourcePort);
            registry = LocateRegistry.createRegistry(sourcePort);
            registry.bind("GeneralRepositoryOfInformation", groi_i);
        } catch(RemoteException | AlreadyBoundException e){
            e.printStackTrace();
        }
    }
}
