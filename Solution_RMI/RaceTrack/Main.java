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
            
            //Calls method setMonitorAddress for monitor #4 (Race_Track)
            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 4);

            //Monitor is now open to requests from clients
            RaceTrack rt = new RaceTrack(groi);
            RaceTrack_Interface rt_i = (RaceTrack_Interface) UnicastRemoteObject.exportObject(rt, sourcePort);
            registry.bind("RaceTrack", rt_i);
        } catch(RemoteException | NotBoundException | AlreadyBoundException | UnknownHostException e){
            e.printStackTrace();
        }
    }
}
