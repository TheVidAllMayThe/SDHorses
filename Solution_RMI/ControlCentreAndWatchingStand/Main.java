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
            while(groi == null){
                try{
                    Registry groiregistry = LocateRegistry.getRegistry(args[1], Integer.valueOf(args[2]));
                    groi = (GeneralRepositoryOfInformation_Interface) groiregistry.lookup("GeneralRepositoryOfInformation");
                }catch(RemoteException | NotBoundException e){
                }
            }

            //Calls method setMonitorAddress for monitor #3 (Control Centre and Watching Stand)
            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 3);


            //Monitor is now open to requests from clients
            Registry registry = LocateRegistry.createRegistry(sourcePort);
            ControlCentreAndWatchingStand ccws = new ControlCentreAndWatchingStand(groi);
            ControlCentreAndWatchingStand_Interface ccws_i = (ControlCentreAndWatchingStand_Interface) UnicastRemoteObject.exportObject(ccws, sourcePort);
            registry.bind("ControlCentreAndWatchingStand", ccws_i);
        } catch(RemoteException | AlreadyBoundException | UnknownHostException e){
            e.printStackTrace();
        }
    }
}
