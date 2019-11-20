#RMI Airline Ticket Purchasing Tool

There are 30 seats on the airplane. Seats 1 - 5 are Business Class and seats 6 - 30 are economy class.
Seats are sold according to demand:
 * The first 3 business class seats are $500 each
 * The last 2 business class seats are $800 each
 * The first 10 economy class seats are $200 each
 * The second 10 economy class seats are $300 each
 * The last 5 economy class seats are $450 each
 
The RMI client AirlineClient.java has access to some RMI methods exposed by the server.
The client can perform the following actions:
 * List all available seats: `<server name> list`
 * Attempt to reserve a seat: `reserve <server_name> <class> <passenger_name> <seat_number>`
  * If a reservation fails because of an invalid seat number or because that seat is already reserved an appropriate response is returned to the client.
 * List the name, class and seat number of all reservations: `passengerlist <servername>`