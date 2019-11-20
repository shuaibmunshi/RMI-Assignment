import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.*;


public class AirlineServer extends UnicastRemoteObject implements AirlineInterface {
    //Iterators for the number of seats at each price point
    int seatBusinessCountFirst = 3;
    int seatBusinessCountSecond = 2;
    int seatEconomyCountFirst = 10;
    int seatEconomyCountSecond = 10;
    int seatEconomyCountThird = 5;

    //Array lists of Seat objects for each type of seat
    static ArrayList<Seat> businessList = new ArrayList<Seat>();
    static ArrayList<Seat> economyList = new ArrayList<Seat>();

    static class Seat {
        //Class that builds a seat object containing the relevant data for each reserved seat
        int price;
        int seatNumber;
        String passengerName;
        String seatClass;
        public Seat(int seatNumber, int price, String passengerName, String seatClass){
            this.seatNumber = seatNumber;
            this.price = price;
            this.passengerName = passengerName;
            this.seatClass = seatClass;
        }
        public int getSeatNumber(){
            return this.seatNumber;
        }
        public String getPassengerName(){
            return this.passengerName;
        }
        public String getSeatClass(){
            return this.seatClass;
        }
    }

    AirlineServer() throws RemoteException {
        System.out.println("New AirlineServer");
    }

    public String businessSeatNumbersAvail(){
        //Returns a string of seat numbers that are available in the business section.
        //If the list of business class seat objects is empty then we return all of the seats
        //because all of them are available at this point
        String returnString = " ";
        if(businessList.isEmpty() == true){
            for(int i=1; i <= 5; i++){ //Seats 1-5 are business class
                returnString = returnString.concat(String.valueOf(i));
                if (i < 5){
                    returnString = returnString.concat(", ");
                }
            }
            return returnString;
        }
        //If there is at least one seat object in the list then we find the list of seats
        //that are not yet taken.
        //seatsAvail is an ArrayList of ints of the seat numbers that are not yet reserved
        else {
            ArrayList<Integer> seatsAvail = findAvailSeats(businessList, "businessList");
            for (int i = 0; i < seatsAvail.size(); i++){
                //Cast the int seat number to a string and concatenate to the return string
                returnString = returnString.concat(String.valueOf(seatsAvail.get(i)));
                if (i <= seatsAvail.size() - 2) {
                    returnString = returnString.concat(", ");
                }
            }
            return returnString;
        }
    }

    public String economySeatNumbersAvail() {
        //Does the same thing as function above but for economy seats
        String returnString = " ";
        if (economyList.isEmpty() == true) {
            for (int i = 6; i <= 30; i++) { //Seats 6-30 are economy class
                returnString = returnString.concat(String.valueOf(i));
                if (i < 30){
                    returnString = returnString.concat(", ");
                }
            }
            return returnString;
        } else {
            ArrayList<Integer> seatsAvail = findAvailSeats(economyList, "economyList");
            for (int i = 0; i < seatsAvail.size(); i++){
                returnString = returnString.concat(String.valueOf(seatsAvail.get(i)));
                if (i <= seatsAvail.size() - 2) {
                    returnString = returnString.concat(", ");
                }
            }
            return returnString;
        }
    }

    public static ArrayList<Integer> findAvailSeats(ArrayList<Seat> list, String listName){
        //Returns an ArrayList of integers corresponding to the seats that are available
        //for a particular class of tickets.
        ArrayList seatOptions = new ArrayList<Integer>();
        ArrayList seatsTaken = new ArrayList<Integer>();
        if (listName.equals("businessList")){
            //First we make an ArrayList of integers of the possible seat numbers
            for (int i = 1; i <= 5; i++){
                seatOptions.add(i);
            }
            for (Seat seat : list){
                //Then we make an ArrayList of the integers of the seats that are taken
                seatsTaken.add(seat.getSeatNumber());
            }
            //Lastly we find the difference between the two lists and return it
            seatOptions.removeAll(seatsTaken);
            return seatOptions;
        }
        else if (listName.equals("economyList")){
            for (int i = 6; i <= 30; i++){
                seatOptions.add(i);
            }
            for (Seat seat : list){
                seatsTaken.add(seat.getSeatNumber());
            }
            seatOptions.removeAll(seatsTaken);
            return seatOptions;
        }
        return seatOptions;
    }

    public boolean isSeatAvail(int seatNumber, ArrayList<Seat> list) {
        //Returns a boolean if any particular seat is available in any particular list of Seat objects
        if (list.isEmpty() == true) {
            return true;
        } else {
            for (Seat seat : list) {
                //System.out.println("Obj var is: " + seat.getSeatNumber());
                if (seat.getSeatNumber() == seatNumber) {
                    //System.out.println("Seat number is: " + seat.getSeatNumber());
                    return false;
                } else {
                    //System.out.println(seatNumber + " is not taken");
                    return true;
                }
            }
        }
        return false;
    }

    public String listSeats() throws RemoteException {
//        String businessSeatNumbersAvail = "";
//        String economySeatNumbersAvail = "";
        String businessSeatNumbersAvail = businessSeatNumbersAvail();
        String economySeatNumbersAvail = economySeatNumbersAvail();
        String returnString =
            "Business Class: \n" +
            seatBusinessCountFirst + " seats at $500 each \n" +
            seatBusinessCountSecond + " seats at $800 each \n" +
            "Seat Numbers:" + businessSeatNumbersAvail + " \n" +
            "Economy Class: \n" +
            seatEconomyCountFirst + " seats at $200 each \n" +
            seatEconomyCountSecond + " seats at $300 each \n" +
            seatEconomyCountThird + " seats at $450 each \n" +
            "Seat Numbers:" + economySeatNumbersAvail;
        return returnString;
    }

    public int setPrice(ArrayList<Seat> list, String listName){
        //Figures out the price of any given seat given the ticket class and the number of tickets sold so far
        System.out.println("Trying to set the price for " + listName);
        int itemCount = list.size();
        if(listName == "businessList"){
            if (itemCount > 0 && itemCount <= 3){
                return 500;
            }
            else if (itemCount > 3 && itemCount < 5){
                return 800;
            }
        }
        else if(listName == "economyList"){
            if (itemCount > 0 && itemCount <= 10){
                return 200;
            }
            else if (itemCount > 10 && itemCount <= 20){
                return 300;
            }
            else if (itemCount > 20 && itemCount <= 25){
                return 450;
            }
        }
        return 0;
    }

    public String reserveSeat(String seatClass, String passengerName, String seatNumberString) throws RemoteException {
        //RMI method for reserving a seat
        System.out.println(" " + seatClass + " " + passengerName + " " + seatNumberString);
        String returnString = " ";
        int seatNumber = Integer.parseInt(seatNumberString);
        if (seatClass.equals("business")){
            if (seatNumber >= 1 && seatNumber <= 5){
                //Check for valid seat number
                if(isSeatAvail(seatNumber, businessList) == true){
                    //If seat is not reserved
                    //Build new business class seat object
                    int seatPrice = setPrice(businessList, "businessList");
                    businessList.add(new Seat(seatNumber, seatPrice, passengerName, seatClass));
                    //Decrement relevant iterators so we can keep track of number of tickets sold for
                    //ticket price purposes
                    if (seatBusinessCountFirst > 0){
                        seatBusinessCountFirst = seatBusinessCountFirst - 1;
                    }
                    else if (seatBusinessCountSecond > 0){
                        seatBusinessCountSecond = seatBusinessCountSecond - 1;
                    }
                    return "Successfully reserved seat " + seatNumber + " for passenger " + passengerName;
                }
                else {
                    return "Seat number " + seatNumber + " is unavailable";
                }
            }
            else {
                return "Seat number " + seatNumber + " is invalid";
            }
        }
        //Economy is the same as business class
        else if (seatClass.equals("economy")){
            System.out.println("Inside economy");
            if (seatNumber >= 6 && seatNumber <= 30){
                if(isSeatAvail(seatNumber, economyList) == true){
                    int seatPrice = setPrice(economyList, "economyList");
                    economyList.add(new Seat(seatNumber, seatPrice, passengerName, seatClass));
                    if (seatEconomyCountFirst > 0){
                        seatEconomyCountFirst = seatEconomyCountFirst - 1;
                    }
                    else if (seatEconomyCountSecond > 0){
                        seatEconomyCountSecond = seatEconomyCountSecond - 1;
                    }
                    else if (seatEconomyCountThird > 0){
                        seatEconomyCountThird = seatEconomyCountThird - 1;
                    }
                    return "Successfully reserved seat " + seatNumber + " for passenger " + passengerName;
                }
                else {
                    return "Seat number " + seatNumber + " is unavailable";
                }
            }
            else {
                return "Seat number " + seatNumber + " is invalid";
            }
        }
        return "Could not reserve seat";
    }
    public String listPassenger() throws RemoteException {
        //Lists the names, seat number and ticket class of any passengers who have reserved a seat
        String returnString = "";
        if (businessList.isEmpty() == true) {
            returnString = returnString.concat("No business seats reserved \n");
        }
        if (economyList.isEmpty() == true) {
            returnString = returnString.concat("No economy seats reserved \n");
        }
        for (Seat seat : businessList) {
            returnString = returnString.concat(seat.getPassengerName() + " " + seat.getSeatClass() + " " + String.valueOf(seat.getSeatNumber()) + "\n");
        }
        for (Seat seat : economyList) {
            returnString = returnString.concat(seat.getPassengerName() + " " + seat.getSeatClass() + " " + String.valueOf(seat.getSeatNumber()) + "\n");
        }
        System.out.println(returnString);
        return returnString;
    }
    public static void main(String [] args){
        try {
            AirlineServer server = new AirlineServer();
            Naming.rebind("rmi://localhost/Airline", server);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }


}