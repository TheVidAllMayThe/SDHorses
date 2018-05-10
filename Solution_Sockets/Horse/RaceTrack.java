import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;

/**
 * The {@link RaceTrack} class is a monitor that contains
 * necessary methods to be used in mutual exclusive access by multiple {@link Horse}s and by the Broker.
 * <p>
 * This is where the {@link Horse}s compete with each other to reach the end of the race.
 *
 * @author  David Almeida, Manuel Xarez
 * @version 1.0
 * @since   2018-03-21
 * @see Horse
 * @see HorsePos
 */

public class RaceTrack extends MonitorProxy {
    RaceTrack(InetSocketAddress address){
        super(address);
    }

    /**
     * The {@link Horse}s write their information on the array horses, and then wait for their turn to proceed.
     * @param pID Id of the calling Thread.
     * @return Returns the position of the {@link Horse} in the array horses.
     */
    public int proceedToStartLine(int pID){   //Returns the pos in the array of Horses
        int result = -1;
        try{
            
            
            LinkedList<Object> list = new LinkedList<>();
            list.add("proceedToStartLine");
            list.add(pID);
    
            out.writeObject(list);
            out.flush();

            result = (int) in.readObject();

            
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * The {@link Horse} increases its position in the race.
     * @param horsePos Index of the {@link Horse} in the array horses.
     * @param moveAmount Amount to be increased in the position of the {@link Horse}.
     * @param horseID ID of the Horse calling the method.
     */

    public void makeAMove(int horsePos, int moveAmount, int horseID) {
        try{
            

            LinkedList<Object> list = new LinkedList<>();
            list.add("makeAMove");
            list.add(horsePos);
            list.add(moveAmount);
            list.add(horseID);

            out.writeObject(list);
            out.flush();
            
            if (!in.readObject().equals("ok"))
                System.out.println("Something wrong with makeAMove of broker");

            
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Determines if the finish line has been crossed by the calling Thread.
     * @param horsePos Index of the {@link Horse} in the array horses.
     * @param horseID ID of the Horse calling the method.
     * @return Returns true if the position of the {@link Horse} is equal or greater than the race length.
     */
    public boolean hasFinishLineBeenCrossed(int horsePos, int horseID){ 
        boolean result = false;
        try{
            
            
            LinkedList<Object> list = new LinkedList<>();
            list.add("hasFinishLineBeenCrossed");
            list.add(horsePos);
            list.add(horseID);
    
            out.writeObject(list);
            out.flush();

            result = (boolean) in.readObject();

            
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }
}
