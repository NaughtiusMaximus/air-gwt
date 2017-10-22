package edu.pdx.cs410J.anthot.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;
import edu.pdx.cs410J.AbstractFlight;

import java.awt.*;
import java.util.Date;

public class Flight extends AbstractFlight implements Comparable<Flight>
{
  /**
   * In order for GWT to serialize this class (so that it can be sent between
   * the client and the server), it must have a zero-argument constructor.
   */
  private int flightNumber = 0;
  private String flightSource = null;
  private String dateDeparture = null;
  private String timeDeparture = null;
  private String destination = null;
  private String dateArrival = null;
  private String timeArrival = null;
  private Date datedepart = null;
  private Date datearrive = null;

  public Flight() {

  }

  public Flight(String flightNumber, String origin, String dateDeparture,
                String timeDeparture, String destination, String dateArrival,
                String timeArrival, Date dDepart, Date dArrive){
      try{
        this.flightNumber = Integer.parseInt(flightNumber);
      }
      catch(NumberFormatException e){
        Window.alert(flightNumber + " Is not a number");
      }
      if(origin.length() != 3 || destination.length() != 3){
        if(origin.length() != 3) {
          Window.alert(origin + "Is not of the right length");
        } else{
          Window.alert(destination + "Is not of the right length");
        }
      }
      else{
        this.flightSource = origin;
        this.destination = destination;
      }
      this.dateArrival = dateArrival;
      this.dateDeparture = dateDeparture;
      this.timeDeparture = timeDeparture;
      this.timeArrival = timeArrival;
      this.datedepart = dDepart;
      this.datearrive = dArrive;
  }

  @Override
  public int getNumber() {
    return this.flightNumber;
  }

  @Override
  public String getSource() {
    return this.flightSource;
  }

  @Override
  public Date getDeparture() {
    return this.datedepart;
  }

  public String getDepartureString() {
    return "DEPART " + this.dateDeparture + " " + this.timeDeparture;
  }

  public String getDestination() {
    return this.destination;
  }

  public Date getArrival() {
    return this.datearrive;
  }

  public String getArrivalString() {
    return "ARRIVE " + this.dateArrival + " " + this.timeArrival;
  }

  public String getDepartureStringPretty(){
      String dateString = DateTimeFormat.getFormat("MM/dd/yy").format(getDeparture());
      return "DEPART " + dateString + " " + this.timeDeparture;
  }

  public String getArrivalStringPretty(){
      String dateString = DateTimeFormat.getFormat("MM/dd/yy").format(getArrival());
      return "ARRIVE " + this.dateArrival + " " + this.timeArrival;
  }

  public int getTimeHours(){
      String [] time = timeDeparture.split(" ");
      String [] justhour = time[0].split(":");
      int hour = Integer.parseInt(justhour[0]);
      return hour;
  }

  public int getTimeMinute(){
      String [] time = timeDeparture.split(" ");
      String [] justhour = time[0].split(":");
      int minute = Integer.parseInt(justhour[1]);
      return minute;
  }

  public String timeOfDay(){
      String [] time = timeDeparture.split(" ");
      return time[1];
  }

    public int compareTo(Flight var1) throws ClassCastException{
        if(var1 == null){
            System.err.println("No Flight to be check");
            return -2;
        }
        char [] compareWithVar1 = this.flightSource.toCharArray();
        char [] compareVar1 = var1.flightSource.toCharArray();
        if(this.flightSource.matches(var1.flightSource)){
            if(this.timeOfDay().matches("PM") && var1.timeOfDay().matches("AM")){
                return -1;
            } else if (this.timeOfDay().matches("AM") && var1.timeOfDay().matches("PM")) {
                return 1;
            }
            if(getTimeHours() == var1.getTimeHours()) {

                if (getTimeMinute() == var1.getTimeMinute() && this.timeOfDay().matches(var1.timeOfDay())) {
                    return 0;
                } else if (getTimeMinute() < var1.getTimeMinute()) {
                    return 1;
                }
                else{
                    return -1;
                }
            }
            else if(getTimeHours() < var1.getTimeHours()){
                return 1;
            }
            else{
                return -1;
            }
        }
        else{
            for(int z = 0; z < compareVar1.length; ++z){
                if(compareWithVar1[z] < compareVar1[z]){
                    return 1;
                }
                else if(compareWithVar1[z] > compareVar1[z]){
                    return -1;
                }
            }
        }
        return 0;
    }



}
