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
        System.out.println("Are you a \"host\" or a \"user\"?");
        while (true) {
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("host")) {
                this.isHost = true;
                break;
            } else if (selection.equalsIgnoreCase("user")) {
                break;
            } else {
                System.out.println("Invalid respond! Please enter \"host\" or \"user\"");
            }
        }
    }

    private void systemRegister() {
        if (this.isHost) {
            List<String> allHostUser;
            List<String> allHostEmail;
            List<String> allHostPhone;
            try {
                allHostUser = this.getFieldFromTable("host_username", "host");
                allHostEmail = this.getFieldFromTable("host_email", "host");
                allHostPhone = this.getFieldFromTable("host_phone_number", "host");
            } catch (SQLException e) {
                throw new RuntimeException("Should not reach here! " + e);
            }
            String regUserName, regPassword, regEmail, regPhone, regFirstName, regLastName, regBirth;

            while (true) {
                while (true) {
                    System.out.println("Please enter username");
                    regUserName = scanner.nextLine();
                    if (!allHostUser.contains(regUserName)) {
                        break;
                    } else {
                        System.out.println("Username already exists");
                    }
                }
                System.out.println("Please enter password");
                regPassword = scanner.nextLine();
                while (true) {
                    System.out.println("Please enter email");
                    regEmail = scanner.nextLine();
                    if (!allHostEmail.contains(regEmail)) {
                        break;
                    } else {
                        System.out.println("Email already exists");
                    }
                }
                while (true) {
                    System.out.println("Please enter phone number");
                    regPhone = scanner.nextLine();
                    if (!allHostPhone.contains(regPhone)) {
                        break;
                    } else {
                        System.out.println("Phone number already exists");
                    }
                }
                System.out.println("Please enter first name");
                regFirstName = scanner.nextLine();
                System.out.println("Please enter last name");
                regLastName = scanner.nextLine();
                System.out.println("Please enter birth data in the form yyyy-mm-dd");
                regBirth = scanner.nextLine();
                if (!regUserName.isEmpty() && !regPassword.isEmpty()
                        && !regEmail.isEmpty()
                        && !regPhone.isEmpty()
                        && !regFirstName.isEmpty()
                        && !regLastName.isEmpty()
                        && !regBirth.isEmpty()) {
                    break;
                } else {
                    while (true) {
                        System.out.println("Fields cannot be empty! Please enter \"r\" to retry or enter \"b\" to return to previous page");
                        String input = scanner.nextLine();
                        if (input.equalsIgnoreCase("b")) {
                            return;
                        } else if (input.equalsIgnoreCase("r")) {
                            break;
                        }
                    }
                }
            }

            // Confirmation
            System.out.print("Do you want to create a host account with ");
            System.out.println("username: " + regUserName + ", password: " + regPassword);
            while (true) {
                System.out.println("Enter \"y\" for yes and \"n\" for no");
                String createAccount = scanner.nextLine();
                if (createAccount.equalsIgnoreCase("y")) {
                    try {
                        String sql = "INSERT INTO host VALUES (?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = connnection.prepareStatement(sql);
                        statement.setString(1, regUserName);
                        statement.setString(2, regPassword);
                        statement.setString(3, regEmail);
                        statement.setString(4, regPhone);
                        statement.setString(5, regFirstName);
                        statement.setString(6, regLastName);
                        statement.setString(7, regBirth);
                        int rowsInserted = statement.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Successfully created a new host account, please login!");
                        }
                        break;
                    } catch (SQLException e) {
                        System.out.println("Failed to create a new host account, please try again later!");
                        return;
                    }
                } else if (createAccount.equalsIgnoreCase("n")) {
                    return;
                }
            }
        } else {
            List<String> allUser;
            List<String> allEmail;
            List<String> allPhone;
            try {
                allUser = this.getFieldFromTable("user_name", "user");
                allEmail = this.getFieldFromTable("user_email", "user");
                allPhone = this.getFieldFromTable("user_phone_number", "user");
            } catch (SQLException e) {
                throw new RuntimeException("Should not reach here! " + e);
            }

            String userName, userPassword, userEmail, userPhoneNumber, userBirthYear;
            while (true) {
                // Get username
                while (true) {
                    System.out.println("Please enter username");
                    userName = scanner.nextLine();
                    if (!allUser.contains(userName)) {
                        break;
                    } else {
                        System.out.println("Username already exists");
                    }
                }
                // Get password
                System.out.println("Please enter password");
                userPassword = scanner.nextLine();
                // Get email
                while (true) {
                    System.out.println("Please enter email");
                    userEmail = scanner.nextLine();
                    if (!allEmail.contains(userEmail)) {
                        break;
                    } else {
                        System.out.println("Email already exists");
                    }
                }
                // Get Phone number
                while (true) {
                    System.out.println("Please enter phone number");
                    userPhoneNumber = scanner.nextLine();
                    if (!allPhone.contains(userPhoneNumber)) {
                        break;
                    } else {
                        System.out.println("Phone number already exists");
                    }
                }
                // Get birth year
                System.out.println("Please enter birth year in the form yyyy");
                userBirthYear = scanner.nextLine();

                if (!userName.isEmpty() && !userPassword.isEmpty()
                        && !userEmail.isEmpty()
                        && !userPhoneNumber.isEmpty()
                        && !userBirthYear.isEmpty()) {
                    break;
                } else {
                    System.out.println("Fields cannot be empty!");
                    while (true) {
                        System.out.println("Please enter \"r\" to retry or enter \"b\" to return to previous page");
                        String input = scanner.nextLine();
                        if (input.equalsIgnoreCase("b")) {
                            return;
                        } else if (input.equalsIgnoreCase("r")) {
                            break;
                        }
                    }
                }
            }

            // Confirmation
            System.out.print("Do you want to create a user account with ");
            System.out.println("username: " + userName + ", password: " + userPassword);
            while (true) {
                System.out.println("Enter \"y\" for yes and \"n\" for no");
                String createAccount = scanner.nextLine();
                if (createAccount.equalsIgnoreCase("y")) {
                    try {
                        String sql = "INSERT INTO user VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement statement = connnection.prepareStatement(sql);
                        statement.setString(1, userName);
                        statement.setString(2, userPassword);
                        statement.setString(3, userEmail);
                        statement.setString(4, userPhoneNumber);
                        statement.setString(5, userBirthYear);
                        int rowsInserted = statement.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Successfully created a new user account, please login!");
                        }
                        break;
                    } catch (SQLException e) {
                        System.out.println("Failed to create a new user account, please try again later!");
                        return;
                    }
                } else if (createAccount.equalsIgnoreCase("n")) {
                    return;
                }
            }
        }
    }

    private void systemLogin(boolean back) {
        if (this.isHost) {

        } else {

        }
    }

    /**
     * Returns a list of all elements in a given field of a given table
     *
     * @param field column name
     * @param table table name
     * @return list of all elements in column
     * @throws SQLException
     */
    private List<String> getFieldFromTable(String field, String table) throws SQLException {
        PreparedStatement ps = connnection.prepareStatement("select " + field + " from " + table);
        ResultSet rs = ps.executeQuery();
        List<String> result = new ArrayList<>();

        while (rs.next()) {
            result.add(rs.getString(field));
        }
        rs.close();
        ps.close();
        return result;
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

        // Check if host or user
        this.hostOrUser();

        // Login and Register Procedure
        boolean login = true;
        while (login) {
            System.out.println("Please select \"login\" to an existing account, \"register\" for an account or \"quit\" application");
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("login")) {
                this.systemLogin(login);
            } else if (selection.equalsIgnoreCase("register")) {
                this.systemRegister();
            } else if (selection.equalsIgnoreCase("quit")) {
                this.closeApplication();
                return;
            } else {
                System.out.println("Unknown action");
            }
        }

        // Close and End the application
        this.closeApplication();
    }

    /**
     * Close the database connection and scanner
     */
    public void closeApplication() {
        System.out.println("Closing Application");
        try {
            connnection.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    /**
//     * Get all vendors
//     */
//    public void getVendorList() throws SQLException {
//        PreparedStatement ps = connnection.prepareStatement("select vendor_name from vendors");
//        ResultSet rs = ps.executeQuery();
//        while (rs.next()) {
//            vendorList.add(rs.getString("vendor_name"));
//            System.out.println(rs.getString(1));
//        }
//        rs.close();
//        ps.close();
//    }
//
//    /**
//     * Prompt user for town, state input and validate it
//     */
//    public void validateVendor() {
//        System.out.println("Please enter vendor name");
//
//        while (true) {
//            vendorName = scanner.nextLine();
//            if ((vendorName != "")) {
//                System.out.println(vendorName);
//                if (vendorList.contains(vendorName)) {
//                    break;
//                }
//                System.out.println("Invalid vendor name, please retry");
//            }
//        }
//    }
//
//    /**
//     * Print all the vendors tuples from the  table
//     */
//    public void printTownList() {
//        System.out.println(Arrays.toString(vendorList.toArray()));
//    }
//
//    public void allInvoices(String vendor_p) {
//        String query = "{CALL get_invoices(?)}";
//        try {
//            CallableStatement stmt = connnection.prepareCall(query);
//            stmt.setString(1, vendor_p);
//
//            ResultSet rs = stmt.executeQuery();
//
//            System.out.println("Printing invoice data ");
//            while (rs.next()) {
//                System.out.printf("Invoice id %d vendor id %d date %tB %<te,  %<tY\n",
//                        rs.getInt(1),
//                        rs.getInt(2),
//                        rs.getDate(3)
//                );
//            }
//        } catch (SQLException e) {
//            System.out.println("Could not retrieve invoices");
//            e.printStackTrace();
//        }
//    }
}


