package edu.pdx.cs410J.anthot.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client-side interface to the airline service
 */
public interface AirlineServiceAsync {

  /**
   * Return an airline created on the server
   */
  void getAirline(AsyncCallback<Airline> async);

  /**
   * Add an airline
   * @param async
   */
  void addAirline(String nameAirline, AsyncCallback<Airline> async);

  /**
   * Always throws an exception so that we can see how to handle uncaught
   * exceptions in GWT.
   */
  void throwUndeclaredException(AsyncCallback<Void> async);

  /**
   * Always throws a declared exception so that we can see GWT handles it.
   */
  void throwDeclaredException(AsyncCallback<Void> async);

  void addFlight(Flight flight, AsyncCallback<Airline> asyncCallback);
}
