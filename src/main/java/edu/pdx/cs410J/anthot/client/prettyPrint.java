package edu.pdx.cs410J.anthot.client;

public class prettyPrint {

    public prettyPrint(){

    }

    public String prettyItUp(Airline prettyAirline){
        StringBuilder prettySB = new StringBuilder();
        for(Flight flight : prettyAirline.getFlights()){
            prettySB.append(prettyAirline.getName());
            prettySB.append(" ");
            prettySB.append(flight.getNumber());
            prettySB.append(" ");
            prettySB.append(flight.getSource());
            prettySB.append(" ");
            prettySB.append(flight.getDepartureStringPretty());
            prettySB.append(" ");
            prettySB.append(flight.getDestination());
            prettySB.append(" ");
            prettySB.append(flight.getArrivalStringPretty());
            prettySB.append("\n");
        }
        return prettySB.toString();
    }

}
