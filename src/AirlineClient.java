import java.rmi.*;

public class AirlineClient {
    public static void main(String [] args){
        try {
            if (!args[1].equals("Airline")){
                System.out.println("Server name should be Airline");
            }
            String serverName = "rmi://localhost/" + args[1];

            AirlineInterface client = (AirlineInterface) Naming.lookup(serverName);
            System.out.println("Inside Client");
            if (args[0].equals("list")) {
                System.out.println(client.listSeats());
            }
            if (args[0].equals("reserve")){
                System.out.println(client.reserveSeat(args[2], args[3], args[4]));
            }
            if (args[0].equals("passengerlist")){
                System.out.println(client.listPassenger());
            }

        } catch (Exception ex){
            System.out.println(ex);
        }


    }
}