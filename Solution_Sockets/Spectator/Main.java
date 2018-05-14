import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Main {
    public static void main(String[] args){
        try{
            //Creates input and output streams
            Socket echoSocket = null;
            while(echoSocket == null){
                try{
                    echoSocket = new Socket(InetAddress.getByName(args[0]), Integer.valueOf(args[1]));
                }catch(IOException e){
                }
            }


            GeneralRepositoryOfInformation groi = new GeneralRepositoryOfInformation(echoSocket);
            
            //Gets address and port of all necessary monitors
            InetSocketAddress address0 = new InetSocketAddress(groi.getMonitorAddress(0), groi.getMonitorPort(0));
            InetSocketAddress address2 = new InetSocketAddress(groi.getMonitorAddress(2), groi.getMonitorPort(2));
            InetSocketAddress address3 = new InetSocketAddress(groi.getMonitorAddress(3), groi.getMonitorPort(3));

            int numberOfSpectators = groi.getNumberOfSpectators();
            int numberOfRaces = groi.getNumberOfRaces();
            groi.close();

            Paddock[] pd = new Paddock[numberOfSpectators];
            BettingCentre[] bc = new BettingCentre[numberOfSpectators];
            ControlCentreAndWatchingStand[] ccws = new ControlCentreAndWatchingStand[numberOfSpectators];
            for(int i=0; i<numberOfSpectators; i++){
                pd[i] = new Paddock(address0);
                bc[i] = new BettingCentre(address2);
                ccws[i] = new ControlCentreAndWatchingStand(address3);
            }
            
            //Run Horses
            Spectator[] spectators = new Spectator[numberOfSpectators];
            for(int i=0; i<numberOfSpectators; i++){
                spectators[i] = new Spectator(i, numberOfRaces, pd[i], bc[i], ccws[i]);
                spectators[i].start();
            }
            for(int i=0; i<numberOfSpectators; i++){
                spectators[i].join();
                pd[i].closeConnection();
                bc[i].closeConnection();
                ccws[i].closeConnection();
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
