package edu.pdx.cs410J.anthot.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.anthot.client.Airline;
import edu.pdx.cs410J.anthot.client.Flight;
import edu.pdx.cs410J.anthot.client.AirlineService;

/**
 * The server-side implementation of the Airline service
 */
public class AirlineServiceImpl extends RemoteServiceServlet implements AirlineService
{
  Airline airline = new Airline();
  @Override
  public Airline getAirline() {
    /*
    Airline airline = new Airline();
    airline.addFlight(new Flight());
    */
    return this.airline;
  }


  public Airline addAirline(String airlineName){
    airline.addName(airlineName);
    return airline;
  }

  public Airline addFlight(Flight flight){
    this.airline.addFlight(flight);
    return this.airline;
  }

  @Override
  public void throwUndeclaredException() {
    throw new IllegalStateException("Expected undeclared exception");
  }

  @Override
  public void throwDeclaredException() throws IllegalStateException {
    throw new IllegalStateException("Expected declared exception");
  }

  /**
   * Log unhandled exceptions to standard error
   *
   * @param unhandled
   *        The exception that wasn't handled
   */
  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }
}
