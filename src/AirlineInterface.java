import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AirlineInterface extends Remote {
    //Interface for methods accessible via RMI
    String listSeats() throws RemoteException;
    String reserveSeat(String seatClass, String passengerName, String seatNumberString) throws RemoteException;
    String listPassenger() throws RemoteException;
}