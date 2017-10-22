package edu.pdx.cs410J.anthot.client;

import com.google.gwt.user.client.Window;
import edu.pdx.cs410J.AbstractAirline;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Airline extends AbstractAirline<Flight>
{
  private String nameOfAirline;

  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  public Airline() {

  }

  private Collection<Flight> flights = new ArrayList<>();

  @Override
  public String getName() {
    return this.nameOfAirline;
  }

  @Override
  public void addFlight(Flight flight) {
    if(this.nameOfAirline == null){
      Window.alert("No Airline Found");
    }
    ArrayList<Flight> flite = (ArrayList)flights;
    int count = 0;
    for(int z = 0; z < flite.size(); ++z){
    count = z;
    if(flight.compareTo(flite.get(z))  == 1){
          flite.add(z, flight);
          return;
        }else if(flight.compareTo(flite.get(z)) == 0){
          flite.add(z, flight);
          return;
        }else{
          continue;
        }
      }
    flite.add(flight);
    flights = flite;
    return;
  }




  @Override
  public Collection<Flight> getFlights() {
    return this.flights;
  }

  public void addName(String airlineName) {
    if(airlineName == null){
      Window.alert("No Airport name Found");
    }
    else{
      this.nameOfAirline = airlineName;
    }
    return;
  }
}
