package edu.pdx.cs410J.anthot.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.sun.tools.hat.internal.model.Root;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A basic GWT class that makes sure that we can send an airline back from the server
 */
public class AirlineGwt implements EntryPoint {

  private final Alerter alerter;
  private final AirlineServiceAsync airlineService;
  private final Logger logger;

  private Date departDate = null;
  private Date arriveDate = null;

  private final MenuBar menu = new MenuBar();
  private final DatePicker dayPickerDepart = new DatePicker();

  private final DatePicker dayPickerArrive = new DatePicker();

  private final ListBox listBoxHourDepart = new ListBox();
  private final ListBox listBoxMinuteDepart = new ListBox();
  private final ListBox listBoxTimeOfDay = new ListBox();

  private final ListBox listBoxHourArrive = new ListBox();
  private final ListBox listBoxMinuteArrive = new ListBox();
  private final ListBox listBoxTimeofDayArrv = new ListBox();

  private final TextBox AirlineNameBox = new TextBox();
  private final TextBox flightNumber = new TextBox();
  private final TextBox flightSource = new TextBox();
  private final Label flightSourceDepartureDate = new Label();
  private final Label flightSourceDepartureTime = new Label();
  private final TextBox flightDestination = new TextBox();
  private final Label flightDestinationArrivalDate = new Label();
  private final TextBox flightDestinationArrivalTime = new TextBox();

  private final RichTextArea displayFlights = new RichTextArea();
  private final Button Submit = new Button("Submit");
  private final Button prettyText = new Button("Purty");
  private final Button searchFlights = new Button("Find me flights");

  private final TextBox findSource = new TextBox();
  private final TextBox findDestin = new TextBox();

  private final Button help = new Button ("Help");
  private final Button README = new Button("READMEH");

  @VisibleForTesting
  Button showAirlineButton;

  @VisibleForTesting
  Button showUndeclaredExceptionButton;

  @VisibleForTesting
  Button showDeclaredExceptionButton;

  @VisibleForTesting
  Button showClientSideExceptionButton;

  public AirlineGwt() {
    this(new Alerter() {
      @Override
      public void alert(String message) {
        Window.alert(message);
      }
    });
  }

  @VisibleForTesting
  AirlineGwt(Alerter alerter) {
    this.alerter = alerter;
    this.airlineService = GWT.create(AirlineService.class);
    this.logger = Logger.getLogger("airline");
    Logger.getLogger("").setLevel(Level.INFO);  // Quiet down the default logging
  }

  private void alertOnException(Throwable throwable) {
    Throwable unwrapped = unwrapUmbrellaException(throwable);
    StringBuilder sb = new StringBuilder();
    sb.append(unwrapped.toString());
    sb.append('\n');

    for (StackTraceElement element : unwrapped.getStackTrace()) {
      sb.append("  at ");
      sb.append(element.toString());
      sb.append('\n');
    }

    this.alerter.alert(sb.toString());
  }

  private Throwable unwrapUmbrellaException(Throwable throwable) {
    if (throwable instanceof UmbrellaException) {
      UmbrellaException umbrella = (UmbrellaException) throwable;
      if (umbrella.getCauses().size() == 1) {
        return unwrapUmbrellaException(umbrella.getCauses().iterator().next());
      }
    }
    return throwable;
  }

  //-----------------WIDGETS--------------------------------
  private void addWidgets(VerticalPanel panel) {
    final StringBuilder AirlineFlightInfo = new StringBuilder();

    help.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
       helpguide();
      }
    });

    README.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        READMEguide();
      }
    });

    //-----------DATE of DEPARTURE-----------------------------------------
    dayPickerDepart.addValueChangeHandler((new ValueChangeHandler<Date>() {
      @Override
      public void onValueChange(ValueChangeEvent<Date> valueChangeEvent) {
        Date date = valueChangeEvent.getValue();
        departDate = date;
        String dateString = DateTimeFormat.getFormat("MM/dd/yyyy").format(date);
        flightSourceDepartureDate.setText(dateString);
      }
    }));
    //--------------------------------------------------------------------

    //-----------DATE of ARRIVAL-----------------------------------------
    dayPickerArrive.addValueChangeHandler((new ValueChangeHandler<Date>() {
      @Override
      public void onValueChange(ValueChangeEvent<Date> valueChangeEvent) {
        Date date = valueChangeEvent.getValue();
        arriveDate = date;
        String dateString = DateTimeFormat.getFormat("MM/dd/yyyy").format(date);
        flightDestinationArrivalDate.setText(dateString);
      }
    }));
    //--------------------------------------------------------------------

    //-------------------DEPART TIME--------------------------------------
    listBoxHourDepart.addItem("1");
    listBoxHourDepart.addItem("2");
    listBoxHourDepart.addItem("3");
    listBoxHourDepart.addItem("4");
    listBoxHourDepart.addItem("5");
    listBoxHourDepart.addItem("6");
    listBoxHourDepart.addItem("7");
    listBoxHourDepart.addItem("8");
    listBoxHourDepart.addItem("9");
    listBoxHourDepart.addItem("10");
    listBoxHourDepart.addItem("11");
    listBoxHourDepart.addItem("12");

    listBoxMinuteDepart.addItem("00");
    listBoxMinuteDepart.addItem("15");
    listBoxMinuteDepart.addItem("30");
    listBoxMinuteDepart.addItem("45");

    listBoxTimeOfDay.addItem("AM");
    listBoxTimeOfDay.addItem("PM");
    if(flightSourceDepartureTime != null){
      flightSourceDepartureTime.setText("");
    }


    //--------------------------------------------------------------------

    //------------------------ARRIVAL-------------------------------------
    listBoxHourArrive.addItem("1");
    listBoxHourArrive.addItem("2");
    listBoxHourArrive.addItem("3");
    listBoxHourArrive.addItem("4");
    listBoxHourArrive.addItem("5");
    listBoxHourArrive.addItem("6");
    listBoxHourArrive.addItem("7");
    listBoxHourArrive.addItem("8");
    listBoxHourArrive.addItem("9");
    listBoxHourArrive.addItem("10");
    listBoxHourArrive.addItem("11");
    listBoxHourArrive.addItem("12");

    listBoxMinuteArrive.addItem("00");
    listBoxMinuteArrive.addItem("15");
    listBoxMinuteArrive.addItem("30");
    listBoxMinuteArrive.addItem("45");

    listBoxTimeofDayArrv.addItem("AM");
    listBoxTimeofDayArrv.addItem("PM");
    if(flightDestinationArrivalTime != null){
      flightDestinationArrivalTime.setText("");
    }

    //--------------------------------------------------------------------

    this.displayFlights.setSize("120", "50");
    this.AirlineNameBox.setVisibleLength(15);
    this.flightNumber.setVisibleLength(15);
    this.flightSource.setVisibleLength(15);
    this.flightDestination.setVisibleLength(15);
    this.findSource.setVisibleLength(15);
    this.findDestin.setVisibleLength(15);

    Button showText = new Button("Show Text");
    showText.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        addAirline(AirlineNameBox.getText());
     //   alerter.alert("The textbox contained " + tb.getText());
      }
    });

    searchFlights.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        searchForFlights(findSource.getText(), findDestin.getText());
      }
    });

    prettyText.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        showPrettyAirline();
      }
    });

    Submit.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        if(AirlineFlightInfo != null){
          AirlineFlightInfo.delete(0, AirlineFlightInfo.length());
        }

        flightSourceDepartureTime.setText(listBoxHourArrive.getSelectedItemText() + ":" + listBoxMinuteDepart.getSelectedItemText() + " " + listBoxTimeOfDay.getSelectedItemText());

        flightDestinationArrivalTime.setText(listBoxHourArrive.getSelectedItemText() + ":" + listBoxMinuteArrive.getSelectedItemText() + " " + listBoxTimeOfDay.getSelectedItemText());

        AirlineFlightInfo.append(AirlineNameBox.getText());
        AirlineFlightInfo.append(" ");
        AirlineFlightInfo.append(flightNumber.getText());
        AirlineFlightInfo.append(" ");
        AirlineFlightInfo.append(flightSource.getText());
        AirlineFlightInfo.append(" ");
        AirlineFlightInfo.append(flightSourceDepartureDate.getText());
        AirlineFlightInfo.append(" ");
        AirlineFlightInfo.append(flightSourceDepartureTime.getText());
        AirlineFlightInfo.append(" ");
        AirlineFlightInfo.append(flightDestination.getText());
        AirlineFlightInfo.append(" ");
        AirlineFlightInfo.append(flightDestinationArrivalDate.getText());
        AirlineFlightInfo.append(" ");
        AirlineFlightInfo.append(flightDestinationArrivalTime.getText());
        alerter.alert(AirlineFlightInfo.toString());
        String FlightInfo = new String(AirlineFlightInfo);
        String [] flight = FlightInfo.split(" ");
        String fnum = flight[1];
        String fsrc = flight[2];
        String fdateD = flight[3];
        String timeD = flight[4];
        timeD = timeD.concat(" " + flight[5]);
        String fdest = flight[6];
        String fdateA = flight[7];
        String timeA = flight[8];
        timeA = timeA.concat(" " + flight[9]);
        addAirline(flight[0]);
        addFlight(makeFlight(fnum, fsrc, fdateD, timeD, fdest, fdateA, timeA));
      }
    });

    showAirlineButton = new Button("Show Airline that is mine");
    showAirlineButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        showAirline();
      }
    });
    panel.add(showText);
    panel.add(showAirlineButton);
  }

  private void READMEguide() {
    StringBuilder readme = new StringBuilder();
    readme.append("\tThe Airline name Takes an airline name.\n");
    readme.append("Flight number only allows numbers, otherwise it will throw a fit.\n");
    readme.append("Departure Airport is the source, or where you will be taking off,\n");
    readme.append("and arrival airport is where you're expected to land.\n");
    readme.append("Both of these take are only 3 digit code for the airports\n");
    readme.append("The time is simple, just select the time you want and the time\n");
    readme.append("of day you want to leave (AM/PM).\n");
    readme.append("In order to save the flight, you must hit the 'submit' button\n");
    readme.append("The 'Purty' button pretty prints all the flights.\n");
    readme.append("\tSearching for flights is to the right of the big text box.\n");
    readme.append("The search takes a source airport and a destination airport.");
    readme.append("Everything can be seen in the giant text box in the middle.");
    Window.alert(readme.toString());

  }

  private void helpguide() {
    StringBuilder HelpInfo = new StringBuilder();
    HelpInfo.append("\tHi, this is an Airline application that is very basic.\n");
    HelpInfo.append("In order to get started here is what you need to know, is\n");
    HelpInfo.append("the left portion of the big empty text box is for adding\n");
    HelpInfo.append("an Airline name and flights to the airline. The right portion\n");
    HelpInfo.append("of the big empty text box is for searching flights. Once you have\n");
    HelpInfo.append("filled in the text boxes and selected a day in the calender\n");
    HelpInfo.append("you must hit submit in order to save the inputted information,\n");
    HelpInfo.append("otherwise no flights or airline will be saved. The information\n");
    HelpInfo.append("the belong in the text boxes will be specified in the READMEH,\n");
    HelpInfo.append("along with what the different buttons do. Try it out!");
    Window.alert(HelpInfo.toString());
  }
  //--------------------------------------------------------

  private void searchForFlights(String src1, String dest1) {
    final String src = src1;
    final String dest = dest1;
    airlineService.getAirline(new AsyncCallback<Airline>() {
      @Override
      public void onFailure(Throwable throwable) {
        alertOnException(throwable);
      }

      @Override
      public void onSuccess(Airline airline) {
        Collection<Flight> tempFlight = airline.getFlights();
        StringBuilder flightSB = new StringBuilder();
        flightSB.append("Flights between " + src + " and " + dest + "\n");
        for(Flight flight : tempFlight){
          if(flight.getSource().matches(src) && flight.getDestination().matches(dest)){
            flightSB.append(flight);
            flightSB.append("\n");
          }
        }
        if(displayFlights.getText() != null){
          displayFlights.setText("");
        }
        displayFlights.setText(flightSB.toString());
      }
    });
  }

  private Flight makeFlight(String flightnum, String src, String srcDate, String srcTime, String dest, String destDate, String destTime){
    Flight flight = new Flight(flightnum, src, srcDate, srcTime, dest, destDate, destTime, departDate, arriveDate);
    return flight;
  }

  private void addAirline(String nameofAirline){
    logger.info("Calling getAirline");
    airlineService.addAirline(nameofAirline, new AsyncCallback<Airline>() {
      @Override
      public void onFailure(Throwable throwable) {
        alertOnException(throwable);
      }

      @Override
      public void onSuccess(Airline airline) {
        StringBuilder sb = new StringBuilder(airline.toString());
        Collection<Flight> flights = airline.getFlights();
        for (Flight flight : flights) {
          sb.append(flight);
          sb.append("\n");
        }
        alerter.alert("Successfully added Airline");
      }
    });

  }

  private void addFlight(Flight flight){
    final Flight tempflight = flight;
    airlineService.addFlight(flight, new AsyncCallback<Airline>() {
      @Override
      public void onFailure(Throwable throwable) {
        alertOnException(throwable);
      }

      @Override
      public void onSuccess(Airline airline) {
        StringBuilder sb = new StringBuilder(airline.toString());
        Collection<Flight> flights = airline.getFlights();
        for (Flight flight : flights) {
          sb.append("\n");
          sb.append(flight);
          sb.append("\n");
        }
        if(displayFlights.getText() != null){
          displayFlights.setText("");
        }
        displayFlights.setText(sb.toString());
 //       alerter.alert(sb.toString());
      }
    });
  }

  private void showAirline() {
    logger.info("Calling getAirline");
    airlineService.getAirline(new AsyncCallback<Airline>() {

      @Override
      public void onFailure(Throwable ex) {
        alertOnException(ex);
      }

      @Override
      public void onSuccess(Airline airline) {
        StringBuilder sb = new StringBuilder(airline.toString());
        Collection<Flight> flights = airline.getFlights();
        for (Flight flight : flights) {
          sb.append("\n");

          sb.append(flight);
          sb.append("\n");
        }
        if(displayFlights.getText() != null){
          displayFlights.setText("");
        }
        displayFlights.setText(sb.toString());
      }
    });
  }

  private void showPrettyAirline() {
    logger.info("Calling getAirline");
    airlineService.getAirline(new AsyncCallback<Airline>() {

      @Override
      public void onFailure(Throwable ex) {
        alertOnException(ex);
      }

      @Override
      public void onSuccess(Airline airline) {
        prettyPrint pUrTy = new prettyPrint();
        String prettyStringOfAirlines = pUrTy.prettyItUp(airline);

        if(displayFlights.getText() != null){
          displayFlights.setText("");
        }
        displayFlights.setText(prettyStringOfAirlines);
      }
    });
  }

  @Override
  public void onModuleLoad() {
    setUpUncaughtExceptionHandler();
    // The UncaughtExceptionHandler won't catch exceptions during module load
    // So, you have to set up the UI after module load...
    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
      @Override
      public void execute() {
        setupUI();
      }
    });
  }

  private void setWidgetsGrid(){
    Grid grid = new Grid(30, 75);

    Label AirlineName = new Label("Airline Name");
    Label flightnum = new Label("Flight Number");
    Label flightsrc = new Label("Departure Airport");
    Label flightDateD = new Label("Date of Departure");
    Label flightTimeD = new Label("Time of Departure:");
    Label colon = new Label(":");
    Label flightdest = new Label("Arrival Airport");
    Label flightDateA = new Label("Date of Arrival:");
    Label flightTimeA = new Label("Time of Arrival:");
    Label findSourceLabel = new Label("Flight source fo find");
    Label findDestinLabel = new Label("Flight desination to find");

    AirlineName.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    flightnum.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    flightsrc.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    flightdest.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    flightDateD.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    flightSourceDepartureDate.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    flightTimeD.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    flightDateA.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    findSourceLabel.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    findDestinLabel.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);


    grid.setWidget(1, 13, AirlineName.asWidget());
    grid.setWidget(2, 13, this.AirlineNameBox.asWidget());
    grid.setWidget(3, 13, flightnum.asWidget());
    grid.setWidget(4, 13, this.flightNumber.asWidget());

    grid.setWidget(6, 13, flightsrc.asWidget());
    grid.setWidget(7, 13, this.flightSource.asWidget());

    grid.setWidget(12, 13, flightdest.asWidget());
    grid.setWidget(13, 13, this.flightDestination.asWidget());

    grid.setWidget(14, 13, flightDateA.asWidget());
    grid.setWidget(15, 13, this.dayPickerArrive.asWidget());
    grid.setWidget(16, 13, flightDestinationArrivalDate.asWidget());

    grid.setWidget(12, 14, flightTimeA.asWidget());
    grid.setWidget(12, 15, listBoxHourArrive.asWidget());
    grid.setWidget(12, 16, colon.asWidget());
    grid.setWidget(12, 17, listBoxMinuteArrive.asWidget());
    grid.setWidget(12, 18, listBoxTimeofDayArrv.asWidget());

    grid.setWidget(9, 13, flightDateD.asWidget());
    grid.setWidget(10, 13, dayPickerDepart.asWidget());
    grid.setWidget(11, 13, flightSourceDepartureDate.asWidget());

    grid.setWidget(9, 14, flightTimeD.asWidget());
    grid.setWidget(9, 15, listBoxHourDepart.asWidget());
    grid.setWidget(9, 16, colon.asWidget());
    grid.setWidget(9, 17, listBoxMinuteDepart.asWidget());
    grid.setWidget(9, 18, listBoxTimeOfDay.asWidget());

    grid.setWidget(17, 14, prettyText.asWidget());
    grid.setWidget(17, 13, Submit.asWidget());
    grid.setWidget(10, 19, this.displayFlights.asWidget());

    grid.setWidget(1, 25, findSourceLabel.asWidget());
    grid.setWidget(2, 25, findSource.asWidget());
    grid.setWidget(1, 30, findDestinLabel.asWidget());
    grid.setWidget(2, 30, findDestin.asWidget());
    grid.setWidget(3, 25, searchFlights.asWidget());

    grid.setWidget(0, 1, help.asWidget());
    grid.setWidget(1, 1, README.asWidget());

    DecoratorPanel decoratorPanel = new DecoratorPanel();
    decoratorPanel.add(grid);
    RootPanel.get().add(decoratorPanel);
  }

  private void setupUI() {
    RootPanel rootPanel = RootPanel.get();
    VerticalPanel panel = new VerticalPanel();

    setWidgetsGrid();
    rootPanel.add(panel);
    addWidgets(panel);
  }

  private void setUpUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(Throwable throwable) {
        alertOnException(throwable);
      }
    });
  }

  @VisibleForTesting
  interface Alerter {
    void alert(String message);
  }
}
