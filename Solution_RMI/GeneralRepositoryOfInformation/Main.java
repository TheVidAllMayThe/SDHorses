import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;
import java.rmi.server.UnicastRemoteObject;

public class Main {
   public static void main(String[] args) throws UnknownHostException {


        try{
            String rmiRegHostName = InetAddress.getLocalHost().getHostAddress();
            int rmiRegPortNumb = Integer.valueOf(args[0]);

            RegisterRemoteObject regEngine = new RegisterRemoteObject (rmiRegHostName, rmiRegPortNumb);
            Register regEngineStub = null;
            int listeningPort = Integer.valueOf(args[0]);
            try
            { regEngineStub = (Register) UnicastRemoteObject.exportObject (regEngine, listeningPort);
            }
            catch (RemoteException e)
            {
                System.out.println("RegisterRemoteObject stub generation exception: " + e.getMessage ());
                System.exit (1);
            }
            System.out.println("Stub was generated!");

            String nameEntry = "RegisterHandler";
            Registry registry = null;

            try
            { registry = LocateRegistry.createRegistry(rmiRegPortNumb);
            }
            catch (RemoteException e)
            {
                System.out.println("RMI registry creation exception: " + e.getMessage ());
                System.exit (1);
            }
            System.out.println("RMI registry was created!");

            try {
                registry.rebind (nameEntry, regEngineStub);
            }
            catch (RemoteException e){
                e.printStackTrace();
                System.exit (1);
            }
            System.out.println("RegisterRemoteObject object was registered!");


            GeneralRepositoryOfInformation groi = new GeneralRepositoryOfInformation(5,4,4, 10, registry);
            GeneralRepositoryOfInformation_Interface groi_i = (GeneralRepositoryOfInformation_Interface) UnicastRemoteObject.exportObject(groi, rmiRegPortNumb);
            registry.rebind("GeneralRepositoryOfInformation", groi_i);
        } catch(RemoteException e){
            e.printStackTrace();
        }
   }
}
