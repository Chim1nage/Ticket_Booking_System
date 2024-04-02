package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class TerminalController implements IController {
    /**
     * The name of the MySQL account to use
     */
    private String userName;
    /**
     * The password for the MySQL account
     */
    private String password;
    /**
     * The name of the computer running MySQL
     */
    private final String serverName = "localhost";
    /**
     * The port of the MySQL server (default is 3306)
     */
    private final int portNumber = 3306;
    /**
     * The name of the database we are testing with
     */
    private final String dbName = "TicketDB";
    public Connection connnection = null;
    public Scanner scanner = null;
    public boolean isHost = false;

    /**
     * Prompt user to get login details
     */
    private void getDatabaseLogin() {
        System.out.println("Please enter your MySQL username");
        this.userName = scanner.nextLine();

        System.out.println("Please enter your MySQL password");
        this.password = scanner.nextLine();
    }

    /**
     * Get a new database connection
     *
     * @throws SQLException
     */
    private void getDatabaseConnection() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        connnection = DriverManager.getConnection("jdbc:mysql://"
                + this.serverName
                + ":" + this.portNumber
                + "/" + this.dbName
                + "?characterEncoding=UTF-8&useSSL=false", connectionProps);
    }

    private void hostOrUser() {
        System.out.println("Are you a host or a user?");
        while (true) {
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("host")) {
                this.isHost = true;
                break;
            } else if (!selection.equalsIgnoreCase("register")) {
                System.out.println("Invalid respond! Please enter \"host\" or \"user\"");
            }
        }
    }

    private void systemRegister() {
        if (this.isHost) {
            while (true) {
                System.out.println("Please enter username");
                String regUserName = scanner.nextLine();
                System.out.println("Please enter password");
                String regPassword = scanner.nextLine();
                System.out.println("Please enter email");
                String regEmail = scanner.nextLine();
                System.out.println("Please enter phone number");
                String regPhone = scanner.nextLine();
                System.out.println("Please enter first name");
                String regFirstName = scanner.nextLine();
                System.out.println("Please enter last name");
                String regLastName = scanner.nextLine();
                System.out.println("Please enter birth data in the form yyyy-mm-dd");
                String regBirth = scanner.nextLine();
            }
        } else {
            while (true) {

            }
        }
    }

    private void systemLogin() {
        if (this.isHost) {

        } else {

        }
    }

    private List<String> getHostUserName() throws SQLException {
        PreparedStatement ps = connnection.prepareStatement("select host_username from host");
        ResultSet rs = ps.executeQuery();
        List<String> result = new ArrayList<>();

        while (rs.next()) {
            result.add(rs.getString("vendor_name"));
            System.out.println(rs.getString(1));
        }
        rs.close();
        ps.close();
    }

    /**
     * Connect to MySQL and call other functions
     */
    @Override
    public void run() {
        this.scanner = new Scanner(System.in);
        // Connect to MySQL
        while (true) {
            try {
                // Get login details
                this.getDatabaseLogin();
                this.getDatabaseConnection();
                System.out.println("Connected to database");
                break;
            } catch (SQLException e) {
                System.out.println("ERROR: Could not connect to the database.");
                System.out.println("Please enter \"r\" to retry or any to exit system.");
                String temp = scanner.nextLine();
                if (!temp.equals("r")) {
                    System.out.println("Exit System");
                    return;
                }
            }
        }

        this.hostOrUser();

        while (true) {
            System.out.println("Please select \"login\" or \"register\" for new account");
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("login")) {
                this.systemLogin();
            } else if (selection.equalsIgnoreCase("register")) {
                this.systemRegister();
            } else if ()
        }


        // Close the database connection and scanner
        try {
            connnection.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all vendors
     */
    public void getVendorList() throws SQLException {
        PreparedStatement ps = connnection.prepareStatement("select vendor_name from vendors");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            vendorList.add(rs.getString("vendor_name"));
            System.out.println(rs.getString(1));
        }
        rs.close();
        ps.close();
    }

    /**
     * Prompt user for town, state input and validate it
     */
    public void validateVendor() {
        System.out.println("Please enter vendor name");

        while (true) {
            vendorName = scanner.nextLine();
            if ((vendorName != "")) {
                System.out.println(vendorName);
                if (vendorList.contains(vendorName)) {
                    break;
                }
                System.out.println("Invalid vendor name, please retry");
            }
        }
    }

    /**
     * Print all the vendors tuples from the  table
     */
    public void printTownList() {
        System.out.println(Arrays.toString(vendorList.toArray()));
    }

    public void allInvoices(String vendor_p) {
        String query = "{CALL get_invoices(?)}";
        try {
            CallableStatement stmt = connnection.prepareCall(query);
            stmt.setString(1, vendor_p);

            ResultSet rs = stmt.executeQuery();

            System.out.println("Printing invoice data ");
            while (rs.next()) {
                System.out.printf("Invoice id %d vendor id %d date %tB %<te,  %<tY\n",
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDate(3)
                );
            }
        } catch (SQLException e) {
            System.out.println("Could not retrieve invoices");
            e.printStackTrace();
        }
    }
}


