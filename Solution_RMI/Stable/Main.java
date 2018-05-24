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
        try{
            //Creates input and output streams
            int sourcePort = Integer.valueOf(args[0]);
            GeneralRepositoryOfInformation_Interface groi = null;
            Register reg = null;
            while(groi == null){
                try{
                    Registry groiregistry = LocateRegistry.getRegistry(args[1], Integer.valueOf(args[2]));
                    groi = (GeneralRepositoryOfInformation_Interface) groiregistry.lookup("GeneralRepositoryOfInformation");
                    reg = (Register) groiregistry.lookup("RegisterHandler");
                }catch(RemoteException | NotBoundException ignored){}
            }
            
            //Calls method setMonitorAddress for monitor #1 (Stable)
            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 1);

            //Monitor is now open to requests from clients
            Stable sb = new Stable(groi);
            Stable_Interface sb_i = (Stable_Interface) UnicastRemoteObject.exportObject(sb, sourcePort);
            //Registry registry = LocateRegistry.createRegistry(sourcePort);
            reg.rebind("Stable", sb_i);
        } catch(RemoteException | UnknownHostException e){
            e.printStackTrace();
        }
    }
}
