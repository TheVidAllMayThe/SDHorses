import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args){
        Socket clientSocket = null;
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
            InetSocketAddress address1 = new InetSocketAddress(groi.getMonitorAddress(1), groi.getMonitorPort(1));
            InetSocketAddress address2 = new InetSocketAddress(groi.getMonitorAddress(2), groi.getMonitorPort(2));
            InetSocketAddress address3 = new InetSocketAddress(groi.getMonitorAddress(3), groi.getMonitorPort(3));
            InetSocketAddress address4 = new InetSocketAddress(groi.getMonitorAddress(4), groi.getMonitorPort(4));

            int numberOfRaces = groi.getNumberOfRaces();

            groi.close();

            Stable st = new Stable(address1);
            BettingCentre bc = new BettingCentre(address2);
            ControlCentreAndWatchingStand ccws = new ControlCentreAndWatchingStand(address3);
            RaceTrack rt = new RaceTrack(address4);
            
            //Run Broker
            Broker b = new Broker(numberOfRaces, st, bc, ccws, rt);
            b.start();
            b.join(); 
            st.closeConnection();
            bc.closeConnection();
            ccws.closeConnection();
            rt.closeConnection();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
