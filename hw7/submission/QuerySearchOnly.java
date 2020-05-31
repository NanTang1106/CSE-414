import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import java.util.ArrayList;

/**
 * Runs queries against a back-end database.
 * This class is responsible for searching for flights.
 */
public class QuerySearchOnly
{
  // `dbconn.properties` config file
  private String configFilename;

  // DB Connection
  protected Connection conn;

  // Canned queries
  private static final String CHECK_FLIGHT_CAPACITY = "SELECT capacity FROM Flights WHERE fid = ?";
  protected PreparedStatement checkFlightCapacityStatement;

  // statement for one hop flights
  private static final String ONE_HOP_ITINERARY = "SELECT TOP (?) fid, day_of_month,carrier_id, flight_num, " +
          "origin_city, dest_city, actual_time, capacity, price " +
          "From Flights " +
          "WHERE origin_city = ? AND dest_city = ? AND day_of_month = ? AND canceled = 0 " +
          "ORDER BY actual_time, fid ASC";
  private PreparedStatement searchOneHopItineraryStatement;

  // statement for two hop flights
  private static final String TWO_HOP_ITINERARY = "SELECT TOP (?) f1.fid AS fid1, f1.day_of_month AS day_of_month1, " +
          "f1.carrier_id AS carrier_id1, f1.flight_num AS flight_num1, f1.origin_city AS origin_city1, " +
          "f1.dest_city AS dest_city1, f1.actual_time AS actual_time1, f1.capacity AS capacity1, " +
          "f1.price AS price1, f2.fid AS fid2, f2.day_of_month AS day_of_month2, f2.carrier_id AS carrier_id2, " +
          "f2.flight_num AS flight_num2, f2.origin_city AS origin_city2, f2.dest_city AS dest_city2, " +
          "f2.actual_time AS actual_time2, f2.capacity AS capacity2, f2.price AS price2 " +
          "FROM Flights f1, Flights f2 " +
          "WHERE f1.origin_city = ? AND f2.dest_city = ? AND f1.dest_city = f2.origin_city AND f1.day_of_month = ? " +
          "AND f1.day_of_month = f2.day_of_month AND f1.canceled = 0 AND f2.canceled = 0 " +
          "ORDER BY (f1.actual_time + f2.actual_time) ASC";
  private PreparedStatement searchTwoHopItineraryStatement;

  // store result to be returned
  // result contains an array of Flight
  private ArrayList<Flight[]> searchResult = new ArrayList<Flight[]>();

  class Flight
  {
    public int fid;
    public int dayOfMonth;
    public String carrierId;
    public String flightNum;
    public String originCity;
    public String destCity;
    public int time;
    public int capacity;
    public int price;

    @Override
    public String toString()
    {
      return "ID: " + fid + " Day: " + dayOfMonth + " Carrier: " + carrierId +
              " Number: " + flightNum + " Origin: " + originCity + " Dest: " + destCity + " Duration: " + time +
              " Capacity: " + capacity + " Price: " + price;
    }
  }

  public QuerySearchOnly(String configFilename)
  {
    this.configFilename = configFilename;
  }

  /** Open a connection to SQL Server in Microsoft Azure.  */
  public void openConnection() throws Exception
  {
    Properties configProps = new Properties();
    configProps.load(new FileInputStream(configFilename));

    String jSQLDriver = configProps.getProperty("flightservice.jdbc_driver");
    String jSQLUrl = configProps.getProperty("flightservice.url");
    String jSQLUser = configProps.getProperty("flightservice.sqlazure_username");
    String jSQLPassword = configProps.getProperty("flightservice.sqlazure_password");

    /* load jdbc drivers */
    Class.forName(jSQLDriver).newInstance();

    /* open connections to the flights database */
    conn = DriverManager.getConnection(jSQLUrl, // database
            jSQLUser, // user
            jSQLPassword); // password

    conn.setAutoCommit(true); //by default automatically commit after each statement
    /* In the full Query class, you will also want to appropriately set the transaction's isolation level:
          conn.setTransactionIsolation(...)
       See Connection class's JavaDoc for details.
    */
  }

  public void closeConnection() throws Exception
  {
    conn.close();
  }

  /**
   * prepare all the SQL statements in this method.
   * "preparing" a statement is almost like compiling it.
   * Note that the parameters (with ?) are still not filled in
   */
  public void prepareStatements() throws Exception
  {
    checkFlightCapacityStatement = conn.prepareStatement(CHECK_FLIGHT_CAPACITY);

    searchOneHopItineraryStatement = conn.prepareStatement(ONE_HOP_ITINERARY);

    searchTwoHopItineraryStatement = conn.prepareStatement(TWO_HOP_ITINERARY);

    /* add here more prepare statements for all the other queries you need */
    /* . . . . . . */
  }



  /**
   * Implement the search function.
   *
   * Searches for flights from the given origin city to the given destination
   * city, on the given day of the month. If {@code directFlight} is true, it only
   * searches for direct flights, otherwise it searches for direct flights
   * and flights with two "hops." Only searches for up to the number of
   * itineraries given by {@code numberOfItineraries}.
   *
   * The results are sorted based on total flight time.
   *
   * @param originCity
   * @param destinationCity
   * @param directFlight if true, then only search for direct flights, otherwise include indirect flights as well
   * @param dayOfMonth
   * @param numberOfItineraries number of itineraries to return
   *
   * @return If no itineraries were found, return "No flights match your selection\n".
   * If an error occurs, then return "Failed to search\n".
   *
   * Otherwise, the sorted itineraries printed in the following format:
   *
   * Itinerary [itinerary number]: [number of flights] flight(s), [total flight time] minutes\n
   * [first flight in itinerary]\n
   * ...
   * [last flight in itinerary]\n
   *
   * Each flight should be printed using the same format as in the {@code Flight} class. Itinerary numbers
   * in each search should always start from 0 and increase by 1.
   *
   * @see Flight#toString()
   */
  public String transaction_search(String originCity, String destinationCity, boolean directFlight, int dayOfMonth,
                                   int numberOfItineraries)
  {
    // Please implement your own (safe) version that uses prepared statements rather than string concatenation.
    // You may use the `Flight` class (defined above).
    StringBuffer sb = new StringBuffer();

    try {
      // search one-hop itinerary first
      searchOneHopItineraryStatement.clearParameters();
      searchOneHopItineraryStatement.setInt(1, numberOfItineraries);
      searchOneHopItineraryStatement.setString(2, originCity);
      searchOneHopItineraryStatement.setString(3, destinationCity);
      searchOneHopItineraryStatement.setInt(4, dayOfMonth);
      ResultSet rs1 = searchOneHopItineraryStatement.executeQuery();

      // add server returns to search result list
      searchResult.clear();
      while (rs1.next()) {
        Flight[] directResult = new Flight[1];
        Flight temp = new Flight();
        temp.fid = rs1.getInt("fid");
        temp.dayOfMonth = rs1.getInt("day_of_month");
        temp.carrierId = rs1.getString("carrier_id");
        temp.flightNum = rs1.getString("flight_num");
        temp.originCity = rs1.getString("origin_city");
        temp.destCity = rs1.getString("dest_city");
        temp.time = rs1.getInt("actual_time");
        temp.capacity = rs1.getInt("capacity");
        temp.price = rs1.getInt("price");
        directResult[0] = temp;
        searchResult.add(directResult);
      }
      rs1.close();

      // accept only one-hop flights
      if (directFlight) {
        int count = 0;
        while (count < searchResult.size()) {
          Flight[] temp = searchResult.get(count);
          sb.append("Itinerary ").append(count).append(": 1 flight(s), ").append(temp[0].time)
              .append(" minutes").append("\n").append(temp[0].toString()).append("\n");
          count++;
        }
      } else {
        // accept two-hop flights
        // when one-hop flights is less than wanted number, replenish with two-hop flights
        if (searchResult.size() < numberOfItineraries) {
          int refillNum = numberOfItineraries - searchResult.size();
          searchTwoHopItineraryStatement.clearParameters();
          searchTwoHopItineraryStatement.setInt(1, refillNum);
          searchTwoHopItineraryStatement.setString(2, originCity);
          searchTwoHopItineraryStatement.setString(3, destinationCity);
          searchTwoHopItineraryStatement.setString(4, dayOfMonth);
          ResultSet rs2 = searchTwoHopItineraryStatement.executeQuery();

          // add server returns to search result list
          while (rs2.next()) {
            Flight[] inDirectResult = new Flight[2];
            Flight f1 = new Flight();
            Flight f2 = new Flight();
            f1.fid = rs2.getInt("fid1");
            f1.dayOfMonth = rs2.getInt("day_of_month1");
            f1.carrierId = rs2.getString("carrier_id1");
            f1.flightNum = rs2.getString("flight_num1");
            f1.originCity = rs2.getString("origin_city1");
            f1.destCity = rs2.getString("dest_city1");
            f1.time = rs2.getInt("actual_time1");
            f1.capacity = rs2.getInt("capacity1");
            f1.price = rs2.getInt("price1")
            f2.fid = rs2.getInt("fid2");
            f2.dayOfMonth = rs2.getInt("day_of_month2");
            f2.carrierId = rs2.getString("carrier_id2");
            f2.flightNum = rs2.getString("flight_num2");
            f2.originCity = rs2.getString("origin_city2");
            f2.destCity = rs2.getString("dest_city2");
            f2.time = rs2.getInt("actual_time2");
            f2.capacity = rs2.getInt("capacity2");
            f2.price = rs2.getInt("price2")
            inDirectResult[0] = f1;
            inDirectResult[1] = f2;
            searchResult.add(inDirectResult);
          }
          rs2.close();
        }
        // sort search result
        Collections.sort(searchResult, new FlightsComparator());
        // add search result to output
        int count = 0;
        while (count < searchResult.size()) {
          Flight[] tempItny = searchResult.get(count);
          int hop = tempItny.length;
          int totalTime = 0;
          if (hop == 1) {
            totalTime = tempItny[0].time;
            sb.append("Itinerary ").append(count).append(": 1 flight(s), ").append(totalTime)
                .append(" minutes").append("\n").append(tempItny[0].toString()).append("\n");
          } else {
            totalTime = tempItny[0].time + tempItny[1].time;
            sb.append("Itinerary ").append(count).append(": 2 flights(s), ").append(totalTime)
                .append(" minutes").append("\n")
                .append(tempItny[0].toString()).append("\n")
                .append(tempItny[1].toString()).append("\n");
          }
          count++;
        }
      }
      if (sb.length() == 0) {
        return "No flights match your selection\n";
      }
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return "Failed to search\n";
    }
  }

  // implement ArrayList Comparator
  public class FlightsComparator implements Comparator<Flight[]> {
    @Override
    public int compare(Flight[] f1, Flight[] f2) {
      int f1Time = 0;
      int f2Time = 0;
      for (i = 0; i < f1.length, i++) {
        f1Time += f1[i].time;
      }
      for (j = 0; j < f2.length, j++) {
        f2Time += f2[j].time;
      }
      if (f1Time != f2Time) {
        return f1Time - f2Time;
      } else {
        // if total times equal, sort by fid
        if (f1[0].fid != f2[0].fid) {
          return f1[0].fid - f2[0].fid;
        } else {
          return f1[1].fid - f2[1].fid;
        }
      }
    }
  }


  /**
   * Same as {@code transaction_search} except that it only performs single hop search and
   * do it in an unsafe manner.
   *
   * @param originCity
   * @param destinationCity
   * @param directFlight
   * @param dayOfMonth
   * @param numberOfItineraries
   *
   * @return The search results. Note that this implementation *does not conform* to the format required by
   * {@code transaction_search}.
   */
  private String transaction_search_unsafe(String originCity, String destinationCity, boolean directFlight,
                                          int dayOfMonth, int numberOfItineraries)
  {
    StringBuffer sb = new StringBuffer();

    try
    {
      // one hop itineraries
      String unsafeSearchSQL =
              "SELECT TOP (" + numberOfItineraries + ") day_of_month,carrier_id,flight_num,origin_city,dest_city,actual_time,capacity,price "
                      + "FROM Flights "
                      + "WHERE origin_city = \'" + originCity + "\' AND dest_city = \'" + destinationCity + "\' AND day_of_month =  " + dayOfMonth + " "
                      + "ORDER BY actual_time ASC";

      Statement searchStatement = conn.createStatement();
      ResultSet oneHopResults = searchStatement.executeQuery(unsafeSearchSQL);

      while (oneHopResults.next())
      {
        int result_dayOfMonth = oneHopResults.getInt("day_of_month");
        String result_carrierId = oneHopResults.getString("carrier_id");
        String result_flightNum = oneHopResults.getString("flight_num");
        String result_originCity = oneHopResults.getString("origin_city");
        String result_destCity = oneHopResults.getString("dest_city");
        int result_time = oneHopResults.getInt("actual_time");
        int result_capacity = oneHopResults.getInt("capacity");
        int result_price = oneHopResults.getInt("price");

        sb.append("Day: ").append(result_dayOfMonth)
                .append(" Carrier: ").append(result_carrierId)
                .append(" Number: ").append(result_flightNum)
                .append(" Origin: ").append(result_originCity)
                .append(" Destination: ").append(result_destCity)
                .append(" Duration: ").append(result_time)
                .append(" Capacity: ").append(result_capacity)
                .append(" Price: ").append(result_price)
                .append('\n');
      }
      oneHopResults.close();
    } catch (SQLException e) { e.printStackTrace(); }

    return sb.toString();
  }

  /**
   * Shows an example of using PreparedStatements after setting arguments.
   * You don't need to use this method if you don't want to.
   */
  private int checkFlightCapacity(int fid) throws SQLException
  {
    checkFlightCapacityStatement.clearParameters();
    checkFlightCapacityStatement.setInt(1, fid);
    ResultSet results = checkFlightCapacityStatement.executeQuery();
    results.next();
    int capacity = results.getInt("capacity");
    results.close();

    return capacity;
  }
}
