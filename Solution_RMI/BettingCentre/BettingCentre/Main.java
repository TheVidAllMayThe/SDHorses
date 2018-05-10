package BettingCentre;

import java.net.InetAddress;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class that initializes the server.
 */

public class Main {
    public static void main(String[] args){
        GeneralRepositoryOfInformation groi = null;
        Registry registry;
        try{
            //Creates input and output streams
            int sourcePort = Integer.valueOf(args[0]);
            registry = LocateRegistry.createRegistry(sourcePort);
            groi = (GeneralRepositoryOfInformation) registry.lookup("GeneralRepositoryOfInformation");
            
            //Calls method setMonitorAddress for monitor #2 (Betting_centre)
            groi.setMonitorAddress(InetAddress.getLocalHost(), sourcePort, 2);

            //Monitor is now open to requests from clients
            BettingCentre bc = new BettingCentre();
            BettingCentre_Interface bc_i = (BettingCentre_Interface) UnicastRemoteObject.exportObject();
            registry.bind("BettingCentre", bc_i);
        } catch(RemoteException e){
            e.printStackTrace();
        }
    }
}
