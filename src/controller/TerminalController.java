package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

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
    private boolean isHost = false;
    private boolean loggedIn = false;
    private String loginAccount;
    private String loginPassword;
    private String loginMethod;

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

    /**
     * Checks if the client is a user or a host.
     */
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

    /**
     * The process of register
     */
    private void systemRegister() {
        if (this.isHost) {
            List<String> allHostUser = this.getFieldFromTable("host_username", "host");
            List<String> allHostEmail = this.getFieldFromTable("host_email", "host");
            List<String> allHostPhone = this.getFieldFromTable("host_phone_number", "host");
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
                        if (rowsInserted == 1) {
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
            List<String> allUser = this.getFieldFromTable("user_username", "user");
            List<String> allEmail = this.getFieldFromTable("user_email", "user");
            List<String> allPhone = this.getFieldFromTable("user_phone_number", "user");
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
                        if (rowsInserted == 1) {
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

    /**
     * The process of login. Will change loggedIn to true if successfully logs in.
     */
    private void systemLogin() {
        String inputLoginAccount, inputLoginPassword, method;
        List<String> compareList;

        if (this.isHost) {
            while (true) {
                System.out.println("Please select login method: \"username\", \"email\", \"phone\"");
                method = scanner.nextLine();
                if (method.equalsIgnoreCase("username")) {
                    method = "username";
                    compareList = this.getFieldFromTable("host_username", "host");
                    break;
                } else if (method.equalsIgnoreCase("email")) {
                    method = "email";
                    compareList = this.getFieldFromTable("host_email", "host");
                    break;
                } else if (method.equalsIgnoreCase("phone")) {
                    method = "phone_number";
                    compareList = this.getFieldFromTable("host_phone_number", "host");
                    break;
                } else {
                    System.out.println("Invalid input. Enter any to select again or \"b\" to return to previous page");
                    String option = scanner.nextLine();
                    if (option.equalsIgnoreCase("b")) {
                        return;
                    }
                }
            }

            // Verify username and account
            while (true) {
                System.out.print("Please enter " + method + ": ");
                inputLoginAccount = scanner.nextLine();
                System.out.print("Please enter password: ");
                inputLoginPassword = scanner.nextLine();

                if (compareList.contains(inputLoginAccount)) {
                    try {
                        String sql = "SELECT host_password FROM host WHERE host_" + method + " = ?";
                        PreparedStatement ps = connnection.prepareStatement(sql);
                        ps.setString(1, inputLoginAccount);
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        password = rs.getString("host_password");

                        rs.close();
                        ps.close();
                    } catch (SQLException e) {
                        throw new RuntimeException("Should not reach here. Error: " + e);
                    }

                    // Verify Host input password
                    if (inputLoginPassword.equals(password)) {
                        this.loggedIn = true;
                        System.out.println("Successfully Logged In!");
                        this.loginAccount = inputLoginAccount;
                        this.loginPassword = inputLoginPassword;
                        this.loginMethod = method;
                        return;
                    }
                }
                System.out.println("Incorrect Username or Password! Enter any to retry, or \"b\" to return to previous page.");
                String option = scanner.nextLine();
                if (option.equalsIgnoreCase("b")) {
                    return;
                }
            }
        } else {
            while (true) {
                System.out.println("Please select login method: \"username\", \"email\", \"phone\"");
                method = scanner.nextLine();
                if (method.equalsIgnoreCase("username")) {
                    method = "username";
                    compareList = this.getFieldFromTable("user_username", "user");
                    break;
                } else if (method.equalsIgnoreCase("email")) {
                    method = "email";
                    compareList = this.getFieldFromTable("user_email", "user");
                    break;
                } else if (method.equalsIgnoreCase("phone")) {
                    method = "phone_number";
                    compareList = this.getFieldFromTable("user_phone_number", "user");
                    break;
                } else {
                    System.out.println("Invalid input. Enter any to select again or \"b\" to return to previous page");
                    String option = scanner.nextLine();
                    if (option.equalsIgnoreCase("b")) {
                        return;
                    }
                }
            }

            // Verify username and account
            while (true) {
                System.out.print("Please enter " + method + ": ");
                inputLoginAccount = scanner.nextLine();
                System.out.print("Please enter password: ");
                inputLoginPassword = scanner.nextLine();

                if (compareList.contains(inputLoginAccount)) {
                    try {
                        String sql = "SELECT user_password FROM user WHERE user_" + method + " = ?";
                        PreparedStatement ps = connnection.prepareStatement(sql);
                        ps.setString(1, inputLoginAccount);
                        ResultSet rs = ps.executeQuery();
                        rs.next();
                        password = rs.getString("user_password");

                        rs.close();
                        ps.close();
                    } catch (SQLException e) {
                        throw new RuntimeException("Should not reach here. Error: " + e);
                    }

                    // Verify Host input password
                    if (inputLoginPassword.equals(password)) {
                        loggedIn = true;
                        System.out.println("Successfully Logged In!");
                        this.loginAccount = inputLoginAccount;
                        this.loginPassword = inputLoginPassword;
                        this.loginMethod = method;
                        return;
                    }
                }
                System.out.println("Incorrect Username or Password! Enter any to retry, or \"b\" to return to previous page.");
                String option = scanner.nextLine();
                if (option.equalsIgnoreCase("b")) {
                    return;
                }
            }
        }
    }

    private void mainMenu() {
        if (this.isHost) {

        } else {
            while (true) {
                System.out.println("This is the main menu");
                System.out.println("Please choose to \"buy\" ticket, \"transfer\" ticket, \"modify\" ticket, \"delete\" ticket, or \"exit\" program.");
                String selection = scanner.nextLine().toLowerCase();
                switch (selection) {
                    case "buy":
                        this.buyTicket();
                        break;
                    case "transfer":
                        this.transferTicket();
                        break;
                    case "modify":
                        this.modifyTicket();
                        break;
                    case "delete":
                        this.deleteTicket();
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Invalid selection.");
                }
            }
        }
    }

    private static <T> List<T> findUniqueElements(List<T> list) {
        Set<T> set = new HashSet<>(list);
        return new ArrayList<>(set);
    }

    private static List<Integer> findIndexesOfValue(List<String> list, String value) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    private static List<String> createListFromIndexes(List<String> list, List<Integer> indexes) {
        List<String> newList = new ArrayList<>();
        for (int index : indexes) {
            if (index >= 0 && index < list.size()) {
                newList.add(list.get(index));
            } else {
                // Handle out-of-bounds indexes
                System.out.println("Index " + index + " is out of bounds.");
            }
        }
        return newList;
    }

    public static List<Integer> createListFromIndexesInts(List<Integer> additionalList, List<Integer> indexes) {
        List<Integer> newList = new ArrayList<>();
        for (int index : indexes) {
            if (index >= 0 && index < additionalList.size()) {
                newList.add(additionalList.get(index));
            } else {
                // Handle out-of-bounds indexes
                System.out.println("Index " + index + " is out of bounds.");
            }
        }
        return newList;
    }

    private int findEventID() {
        List<Integer> ticketIdList = new ArrayList<>();
        List<String> bookingDateList = new ArrayList<>();
        List<String> stadiumNameList = new ArrayList<>();
        List<String> eventNameList = new ArrayList<>();
        List<Integer> eventIdList = new ArrayList<>();
        String location;
        String date;
        try {
            PreparedStatement ps = connnection.prepareStatement(
                    "select t.ticket_id, t.booking_date, " +
                            "s.seat_row, s.seat_number, st.stadium_name, e.event_name, e.event_id from ticket as t " +
                            "join seat as s on s.seat_id = t.seat_id " +
                            "join stadium as st on s.stadium_id = st.stadium_id " +
                            "join event as e on e.event_id = t.event_id;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ticketIdList.add(rs.getInt("ticket_id"));
                bookingDateList.add(rs.getString("booking_date"));
                stadiumNameList.add(rs.getString("stadium_name"));
                eventNameList.add(rs.getString("event_name"));
                eventIdList.add(rs.getInt("event_id"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
        if (ticketIdList.isEmpty()) {
            System.out.println("No tickets on sale. Returning to main menu.");
            return -1;
    }
        List<String> uniqueStadiumNameList = findUniqueElements(stadiumNameList);
        while (true) {
            System.out.println("Select one of the following locations or \"b\" to return to main menu:");
            System.out.println(uniqueStadiumNameList + "any");
            String selection = scanner.nextLine();
            if (uniqueStadiumNameList.contains(selection)) {
                location = selection;
                break;
            }
            if (selection.equals("any")) {
                location = "any";
                break;
            }
            if (selection.equalsIgnoreCase("b")) {
                return -1;
            } else {
                System.out.println("Invalid input!");
            }
        }
        List<Integer> indexes = new ArrayList<>();
        List<String> uniqueBookingDateList = new ArrayList<>();;
        if (location.equals("any")) {
            uniqueBookingDateList = findUniqueElements(bookingDateList);
            for (int i=0; i < stadiumNameList.size(); i++) {
                indexes.add(i);
            }
        }
        else {
            indexes = findIndexesOfValue(stadiumNameList, location);
            List<String> tempBookingDateList = createListFromIndexes(bookingDateList, indexes);
            uniqueBookingDateList = findUniqueElements(tempBookingDateList);
        }
        while(true) {
            System.out.println("Select one of the following dates or \"b\" to return to main menu:");
            System.out.println(uniqueBookingDateList + "any");
            String selection = scanner.nextLine();
            if (uniqueBookingDateList.contains(selection)) {
                date = selection;
                break;
            }
            if (selection.equals("any")) {
                date = "any";
                break;
            }
            if (selection.equalsIgnoreCase("b")) {
                return -1;
            } else {
                System.out.println("Invalid input!");
            }
        }
        List<Integer> indexes2  = new ArrayList<>();
        List<Integer> eventList = new ArrayList<>();;
        if (location.equals("any")) {
            for (int i=0; i < bookingDateList.size(); i++) {
                indexes2.add(i);
            }
        }
        else {
            indexes2 = findIndexesOfValue(bookingDateList, date);
        }
        List<Integer> eventIndexes = new ArrayList<>();
        for (int num : indexes) {
            if (indexes2.contains(num)) {
                eventIndexes.add(num);
            }
        }
        eventList = createListFromIndexesInts(eventIdList, eventIndexes);
        while(true) {
            System.out.println("Select one of the following events or \"b\" to return to main menu:");
            System.out.println(eventList);
            String selection = scanner.nextLine();
            int t = Integer.parseInt(selection);
            if (eventList.contains(t)) {
                return t;
            }
            if (selection.equalsIgnoreCase("b")) {
                return -1;
            } else {
                System.out.println("Invalid input!");
            }
        }
    }

    private void buyTicket() {
        List<Integer> ticketIdList = new ArrayList<>();
        List<String> firstNameList = new ArrayList<>();
        List<String> lastNameList = new ArrayList<>();
        List<Integer> seatRowList = new ArrayList<>();
        List<Integer> seatNumList = new ArrayList<>();
        List<Integer> eventIdList = new ArrayList<>();
        int eventID = findEventID();
        if (eventID == -1) {
            return;
        }
        try {
            PreparedStatement ps = connnection.prepareStatement(
                    "select t.ticket_id, t.first_name, t.last_name, " +
                            "s.seat_row, s.seat_number, e.event_id from ticket as t " +
                            "join seat as s on s.seat_id = t.seat_id " +
                            "join event as e on e.event_id = t.event_id;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ticketIdList.add(rs.getInt("ticket_id"));
                firstNameList.add(rs.getString("first_name"));
                lastNameList.add(rs.getString("last_name"));
                seatRowList.add(rs.getInt("seat_row"));
                seatNumList.add(rs.getInt("seat_number"));
                eventIdList.add(rs.getInt("event_id"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
        if (seatNumList.isEmpty()) {
            System.out.println("Sorry, there are no more available seats. Please try again later!");
            return;
        } else {
            System.out.println("These are the available seats");
            for (int i = 0; i < seatNumList.size(); i++) {
                int seatNum = seatNumList.get(i);
                int seatRow = seatNumList.get(i);
                System.out.println("Row: " + seatRow + " number: " + seatNum);
            }
        }

        while (true) {
            System.out.print("Please enter desired row: ");
            String row = scanner.nextLine();
            System.out.println("Please enter desired seat: ");
            String seat = scanner.nextLine();
            int rowNum = -1;
            int seatNum = -1;

            try {
                rowNum = Integer.parseInt(row);
                seatNum = Integer.parseInt(seat);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, Please try again!");
                continue;
            }
            if (seatNumList.isEmpty()) {
                System.out.println("Sorry, there are no more available seats. Please try again later!");
                return;
            } else {
                System.out.println("These are the available seats");
                for (int i = 0; i < seatNumList.size(); i++) {
                    int seatNum = seatNumList.get(i);
                    int seatRow = seatNumList.get(i);
                    System.out.println("Row: " + seatRow + " number: " + seatNum);
                }
            }
        }
    }

    private void transferTicket() {
        int eventID = findEventID();
        if (eventID == -1) {
            return;
        }
    }

    private void modifyTicket() {
        // Get user current tickets
        List<Integer> ticketIdList = new ArrayList<>();
        List<String> bookingDateList = new ArrayList<>();
        List<String> firstNameList = new ArrayList<>();
        List<String> lastNameList = new ArrayList<>();
        List<Integer> seatRowList = new ArrayList<>();
        List<Integer> seatNumList = new ArrayList<>();
        List<String> stadiumNameList = new ArrayList<>();
        List<String> eventNameList = new ArrayList<>();
        try {
            PreparedStatement ps = connnection.prepareStatement(
                    "select t.ticket_id, t.booking_date, t.first_name, t.last_name, " +
                            "s.seat_row, s.seat_number, st.stadium_name, e.event_name from ticket as t " +
                            "join seat as s on s.seat_id = t.seat_id " +
                            "join stadium as st on s.stadium_id = st.stadium_id " +
                            "join event as e on e.event_id = t.event_id;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ticketIdList.add(rs.getInt("ticket_id"));
                bookingDateList.add(rs.getString("booking_date"));
                firstNameList.add(rs.getString("first_name"));
                lastNameList.add(rs.getString("last_name"));
                seatRowList.add(rs.getInt("seat_row"));
                seatNumList.add(rs.getInt("seat_number"));
                stadiumNameList.add(rs.getString("stadium_name"));
                eventNameList.add(rs.getString("event_name"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
        if (ticketIdList.isEmpty()) {
            System.out.println("You do not have any tickets. Returning to main menu.");
            return;
        } else {
            for (int i = 0; i < ticketIdList.size(); i++) {
                int ticketId = ticketIdList.get(i);
                String date = bookingDateList.get(i);
                String firstName = firstNameList.get(i);
                String lastName = lastNameList.get(i);
                int seatRow = seatRowList.get(i);
                int seatNum = seatNumList.get(i);
                String stadiumName = stadiumNameList.get(i);
                String eventName = eventNameList.get(i);
                System.out.println(ticketId + ": " + eventName + " at " + stadiumName + " on " + date
                        + " for " + firstName + " " + lastName + " with seat on row " + seatRow + " seat number "
                        + seatNum + ".");
            }
        }

        // ticketId to modify
        int ticket = -1;
        // Choose which ticket to modify
        while (true) {
            System.out.println("Which ticket do you want to modify. Please enter the ticket id or \"b\" to return to main menu.");
            String selection = scanner.nextLine();
            try {
                int t = Integer.parseInt(selection);
                if (ticketIdList.contains(t)) {
                    ticket = t;
                    break;
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("Should not reach here. Error: " + e);
            }
            if (selection.equalsIgnoreCase("b")) {
                return;
            } else {
                System.out.println("Invalid input!");
            }
        }

        // Choose what to modify for the ticket
        while (true) {
            System.out.println("Do you want to change the \"name\" or \"seat\" of the ticket or \"b\" to return to main menu.");
            String selection = scanner.nextLine().toLowerCase();
            if (selection.equals("name")) {
                System.out.println("Please enter the first name");
                String firstName = scanner.nextLine();
                System.out.println("Please enter the last name");
                String lastName = scanner.nextLine();
                try {
                    String updateSQL = "UPDATE ticket SET first_name = ?, last_name = ? WHERE ticket_id = ?";
                    PreparedStatement ps = connnection.prepareStatement(updateSQL);
                    ps.setString(1, firstName);
                    ps.setString(2, lastName);
                    ps.setInt(3, ticket);

                    // Execute the update
                    int rowsAffected = ps.executeUpdate();
                    ps.close();
                    if (rowsAffected != 1) {
                        throw new RuntimeException("update error.");
                    } else {
                        System.out.println("ticket updated.");
                        return;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Should not reach here. Error: " + e);
                }
            } else if (selection.equals("seat")) {
                List<Integer> availableSeatsRow = new ArrayList<>();
                List<Integer> availableSeatsNum = new ArrayList<>();
                // Get all available seats
                try {
                    PreparedStatement ps = connnection.prepareStatement("SELECT s.* FROM seat s\n" +
                            "JOIN event e ON s.stadium_id = e.stadium_id\n" +
                            "JOIN ticket t ON e.event_id = t.event_id\n" +
                            "WHERE t.ticket_id = " + ticket + ";");
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        if (rs.getBoolean("seat_availability")) {
                            availableSeatsRow.add(rs.getInt("seat_row"));
                            availableSeatsNum.add(rs.getInt("seat_number"));
                        }
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Should not reach here. Error: " + e);
                }

                // Print all available seats
                if (availableSeatsNum.isEmpty()) {
                    System.out.println("Sorry, there are no more available seats. Please try again later!");
                    return;
                } else {
                    System.out.println("These are the available seats");
                    for (int i = 0; i < availableSeatsNum.size(); i++) {
                        int seatNum = availableSeatsNum.get(i);
                        int seatRow = availableSeatsRow.get(i);
                        System.out.println("Row: " + seatRow + " number: " + seatNum);
                    }
                }

                while (true) {
                    System.out.print("Please enter desired row: ");
                    String row = scanner.nextLine();
                    System.out.println("Please enter desired seat: ");
                    String seat = scanner.nextLine();
                    int rowNum = -1;
                    int seatNum = -1;

                    try {
                        rowNum = Integer.parseInt(row);
                        seatNum = Integer.parseInt(seat);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input, Please try again!");
                        continue;
                    }

                    if (availableSeatsRow.contains(rowNum) && availableSeatsNum.contains(seatNum)) {
                        if (availableSeatsRow.indexOf(rowNum) != availableSeatsNum.indexOf(seatNum)) {
                            // Update old seat information
                            try {
                                String updateOldTicket = "UPDATE seat SET seat_availability = FALSE " +
                                        "WHERE seat_id IN (SELECT seat_id FROM ticket WHERE ticket_id = ?);";
                                PreparedStatement ps = connnection.prepareStatement(updateOldTicket);
                                ps.setInt(1, ticket);
                                int affectedRows = ps.executeUpdate();
                                if (affectedRows != 1) {
                                    throw new RuntimeException("Should not reach here.");
                                }
                                ps.close();
                            } catch (SQLException e) {
                                throw new RuntimeException("Should not reach here. Error: " + e);
                            }

                            // Update new seat and ticket information
                            try {
                                // Get new seat information
                                String getNewSeat = "SELECT s.seat_id FROM seat s " +
                                        "JOIN event e ON s.stadium_id = e.stadium_id " +
                                        "JOIN ticket t ON e.event_id = t.event_id " +
                                        "WHERE t.ticket_id = ? " +
                                        "AND s.seat_row = ? " +
                                        "AND s.seat_number = ?";
                                PreparedStatement ps = connnection.prepareStatement(getNewSeat);
                                ps.setInt(1, ticket);
                                ps.setInt(2, rowNum);
                                ps.setInt(3, seatNum);
                                ResultSet rs = ps.executeQuery();
                                rs.next();
                                int newSeatId = rs.getInt("seat_id");

                                // update new seat information
                                String updateNewSeat = "UPDATE seat " +
                                        "SET seat_availability = TRUE " +
                                        "WHERE seat_id = ?;";
                                ps = connnection.prepareStatement(updateNewSeat);
                                ps.setInt(1, newSeatId);
                                int affectedRows = ps.executeUpdate();
                                if (affectedRows != 1) {
                                    throw new RuntimeException("Should not reach here.");
                                }

                                // Update ticket information
                                String updateTicket = "UPDATE ticket " +
                                        "SET seat_id = ? " +
                                        "WHERE ticket_id = ?;";
                                ps = connnection.prepareStatement(updateTicket);
                                ps.setInt(1, newSeatId);
                                ps.setInt(2, ticket);
                                affectedRows = ps.executeUpdate();
                                if (affectedRows != 1) {
                                    throw new RuntimeException("Should not reach here.");
                                }
                                ps.close();
                                rs.close();
                            } catch (SQLException e) {
                                throw new RuntimeException("Should not reach here. Error: " + e);
                            }

                            System.out.println("You have successfully changed your seat.");
                            return;
                        }
                    }
                    System.out.println("Invalid input! Input \"b\" to return to main menu or any to retry");
                    String sel = scanner.nextLine().toLowerCase();
                    if (sel.equals("b")) {
                        return;
                    }
                }
            } else if (selection.equals("b")) {
                return;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    private void deleteTicket() {
    }

    /**
     * Returns a list of all elements in a given field of a given table
     *
     * @param field column name
     * @param table table name
     * @return list of all elements in column
     * @throws SQLException
     */
    private List<String> getFieldFromTable(String field, String table) {
        List<String> result = new ArrayList<>();
        try {
            PreparedStatement ps = connnection.prepareStatement("select " + field + " from " + table);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(rs.getString(field));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
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
        while (!this.loggedIn) {
            System.out.println("Please select \"login\" to an existing account, \"register\" for an account or \"quit\" application");
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("login")) {
                this.systemLogin();
            } else if (selection.equalsIgnoreCase("register")) {
                this.systemRegister();
            } else if (selection.equalsIgnoreCase("quit")) {
                this.closeApplication();
                return;
            } else {
                System.out.println("Unknown action");
            }
        }

        // Main menu
        this.mainMenu();

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


