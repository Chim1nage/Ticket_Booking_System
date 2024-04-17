package controller;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

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
    private final String dbName = "ticketdb";
    public Connection connection = null;
    public Scanner scanner = null;
    private boolean isHost = false;
    private boolean loggedIn = false;
    private String loginAccount;

    /**
     * Prompt user to get login details
     */
    private void getDatabaseLogin() {
        System.out.print("Please enter your MySQL username: ");
        this.userName = scanner.nextLine();

        System.out.print("Please enter your MySQL password: ");
        this.password = scanner.nextLine();
    }

    /**
     * Get a new database connection
     *
     * @throws SQLException error if fail to connect to database
     */
    private void getDatabaseConnection() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        connection = DriverManager.getConnection("jdbc:mysql://"
                + this.serverName
                + ":" + this.portNumber
                + "/" + this.dbName
                + "?characterEncoding=UTF-8&useSSL=false", connectionProps);
    }

    /**
     * Checks if the client is a user or a host.
     */
    private void hostOrUser() {
        System.out.print("Are you a \"host\" or a \"user\"? ");
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
                        String sql = "{CALL add_host(?, ?, ?, ?, ?, ?, ?)}";
                        PreparedStatement statement = connection.prepareStatement(sql);
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
            String userName, userPassword, userEmail, userPhoneNumber, userBirthYear, paymentAccount, paymentType;

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
                    System.out.print("Please enter phone number: ");
                    userPhoneNumber = scanner.nextLine();
                    if (!allPhone.contains(userPhoneNumber)) {
                        break;
                    } else {
                        System.out.println("Phone number already exists");
                    }
                }
                // Get birth year
                System.out.print("Please enter birth year in the form yyyy: ");
                userBirthYear = scanner.nextLine();

                // Get Payment Account information
                String pattern = "\\d{16}";
                while (true) {
                    System.out.print("Enter your bank account number (16 digits): ");
                    paymentAccount = scanner.nextLine();

                    // Check if the input matches the pattern
                    if (Pattern.matches(pattern, paymentAccount)) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please try again.");
                    }
                }

                // Get Payment Account information
                while (true) {
                    System.out.print("Enter your bank account type (\"Checking Account\", \"Savings Account\", \"Credit Card Account\", \"Debit Card Account\"): ");
                    paymentType = scanner.nextLine();

                    // Check if the input matches the pattern
                    if (paymentType.equalsIgnoreCase("Checking Account") ||
                            paymentAccount.equalsIgnoreCase("Savings Account") ||
                            paymentAccount.equalsIgnoreCase("Credit Card Account") ||
                            paymentAccount.equalsIgnoreCase("Debit Card Account")) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please try again.");
                    }
                }

                // Validate input
                if (!userName.isEmpty() && !userPassword.isEmpty()
                        && !userEmail.isEmpty()
                        && !userPhoneNumber.isEmpty()
                        && !userBirthYear.isEmpty()
                        && !paymentAccount.isEmpty()
                        && !paymentType.isEmpty()) {
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
                    break;
                } else if (createAccount.equalsIgnoreCase("n")) {
                    return;
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            }

            // create user
            try {
                String sql = "{CALL add_user(?, ?, ?, ?, ?)}";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, userName);
                statement.setString(2, userPassword);
                statement.setString(3, userEmail);
                statement.setString(4, userPhoneNumber);
                statement.setString(5, userBirthYear);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted == 1) {
                    System.out.println("Successfully created a new user account!");
                }
            } catch (SQLException e) {
                System.out.println("Failed to create a new user account, please try again later!");
                return;
            }

            // create account
            try {
                PreparedStatement ps = connection.prepareStatement("{CALL create_account(?, ?)}");
                ps.setString(1, paymentAccount);
                ps.setString(2, paymentType);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 1) {
                    System.out.println("Successfully created a payment account!");
                } else {
                    throw new RuntimeException("Should not reach here.");
                }
            } catch (SQLException e) {
                System.out.println("Failed to create a new account!");
                try {
                    PreparedStatement ps = connection.prepareStatement("{CALL create_account(?, ?)}");
                    ps.setString(1, paymentAccount);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected != 1) {
                        throw new RuntimeException("Should not reach here.");
                    } else {
                        System.out.println("Account deleted");
                    }
                } catch (SQLException e2) {
                    throw new RuntimeException("Should not reach here. Error: " + e2);
                }
                return;
            }

            // create account and user connection
            try {
                PreparedStatement ps = connection.prepareStatement("{CALL create_account_user(?, ?)}");
                ps.setString(1, paymentAccount);
                ps.setString(2, userName);

                int affectedRows = ps.executeUpdate();
                if (affectedRows != 1) {
                    throw new RuntimeException("Should not reach here.");
                } else {
                    System.out.println("Successfully linked a payment account, please login!");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Should not reach here. Error: " + e);
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

            // Get all host usernames, emails, and phone numbers
            List<String> allHostNames = new ArrayList<>();
            List<String> allHostEmails = new ArrayList<>();
            List<String> allHostPhoneNumbers = new ArrayList<>();
            try {
                String query = "{CALL get_all_host()}";
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    allHostNames.add(rs.getString("host_username"));
                    allHostEmails.add(rs.getString("host_email"));
                    allHostPhoneNumbers.add(rs.getString("host_phone_number"));
                }
                ps.close();
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException("Should not reach here. Error: " + e);
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
                        PreparedStatement ps = connection.prepareStatement(sql);
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
                        int index;
                        switch (method) {
                            case "username":
                                this.loginAccount = inputLoginAccount;
                                break;
                            case "email":
                                index = allHostEmails.indexOf(inputLoginAccount);
                                this.loginAccount = allHostNames.get(index);
                                break;
                            case "phone_number":
                                index = allHostPhoneNumbers.indexOf(inputLoginAccount);
                                this.loginAccount = allHostNames.get(index);
                                break;
                            default:
                                throw new RuntimeException("Should not reach here");
                        }
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

            // Get all usernames, emails, and phone numbers
            List<String> allUserNames = new ArrayList<>();
            List<String> allUserEmails = new ArrayList<>();
            List<String> allUserPhoneNumbers = new ArrayList<>();
            try {
                String query = "{CALL get_all_user()}";
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    allUserNames.add(rs.getString("user_username"));
                    allUserEmails.add(rs.getString("user_email"));
                    allUserPhoneNumbers.add(rs.getString("user_phone_number"));
                }
                ps.close();
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException("Should not reach here. Error: " + e);
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
                        PreparedStatement ps = connection.prepareStatement(sql);
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
                        int index;
                        switch (method) {
                            case "username":
                                this.loginAccount = inputLoginAccount;
                                break;
                            case "email":
                                index = allUserEmails.indexOf(inputLoginAccount);
                                this.loginAccount = allUserNames.get(index);
                                break;
                            case "phone_number":
                                index = allUserPhoneNumbers.indexOf(inputLoginAccount);
                                this.loginAccount = allUserNames.get(index);
                                break;
                            default:
                                throw new RuntimeException("Should not reach here");
                        }
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

    /**
     * Main menu of the application
     */
    private void mainMenu() {
        System.out.println("This is the main menu");
        if (this.isHost) {
            while (true) {
                System.out.println("Please choose to \"create/delete event\", \"create/delete stadium\", \"create/delete stores\", or \"quit\" to exit application");
                String selection = scanner.nextLine().toLowerCase();
                switch (selection) {
                    case "create event":
                        this.createEvent();
                        break;
                    case "delete event":
                        this.deleteEvent();
                        break;
                    case "create stadium":
                        this.createStadium();
                        break;
                    case "delete stadium":
                        this.deleteStadium();
                        break;
                    case "create stores":
                        this.createStore();
                        break;
                    case "delete stores":
                        this.deleteStore();
                        break;
                    case "quit":
                        return;
                    default:
                        System.out.println("Invalid input.");
                }
            }
        } else {
            while (true) {
                System.out.println("Please choose to \"buy\" ticket, \"transfer\" ticket, \"modify\" ticket, \"delete\" ticket, \"view\" ticket, or \"exit\" program.");
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
                    case "view":
                        this.viewTicket();
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Invalid selection.");
                }
            }
        }
    }

    private <T> List<T> findUniqueElements(List<T> list) {
        Set<T> set = new HashSet<>(list);
        return new ArrayList<>(set);
    }

    private List<Integer> findIndexesOfValue(List<String> list, String value) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    /**
     * Create a list of elements from the first list where its index are in the second list
     *
     * @param list    list of elements
     * @param indexes list of indexes
     * @param <T>     Type of list
     * @return new list of elements
     */
    private <T> List<T> createListFromIndexes(List<T> list, List<Integer> indexes) {
        List<T> newList = new ArrayList<>();
        for (int index : indexes) {
            if (index >= 0 && index < list.size()) {
                newList.add(list.get(index));
            } else {
                throw new RuntimeException("Index " + index + " is out of bounds.");
            }
        }
        return newList;
    }

    /**
     * Provide user filters and let user choose an event. Get the eventID
     *
     * @return The eventID of the event
     */
    private int findEventID() {
        List<Integer> ticketIdList = new ArrayList<>();
        List<String> eventDateList = new ArrayList<>();
        List<String> stadiumNameList = new ArrayList<>();
        List<String> eventNameList = new ArrayList<>();
        List<Integer> eventIdList = new ArrayList<>();
        String location;
        String date;
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL get_all_ticket_information()}");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ticketIdList.add(rs.getInt("ticket_id"));
                eventDateList.add(rs.getString("event_date"));
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
            System.out.println("Select one of the following locations or \"b\" to return to main menu: ");
            System.out.println(uniqueStadiumNameList + " any");
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
        List<String> uniqueEventDateList;

        if (location.equals("any")) {
            uniqueEventDateList = findUniqueElements(eventDateList);
            for (int i = 0; i < stadiumNameList.size(); i++) {
                indexes.add(i);
            }
        } else {
            indexes = findIndexesOfValue(stadiumNameList, location);
            List<String> tempEventDateList = createListFromIndexes(eventDateList, indexes);
            uniqueEventDateList = findUniqueElements(tempEventDateList);
        }

        while (true) {
            System.out.println("Select one of the following dates or \"b\" to return to main menu:");
            System.out.println(uniqueEventDateList + " any");
            String selection = scanner.nextLine();
            if (selection.equals("any")) {
                date = "any";
                break;
            }
            if (selection.equalsIgnoreCase("b")) {
                return -1;
            }
            if (uniqueEventDateList.contains(selection)) {
                date = selection;
                break;
            }
            System.out.println("Invalid input!");
        }

        List<Integer> indexes2 = new ArrayList<>();
        if (date.equals("any")) {
            for (int i = 0; i < eventDateList.size(); i++) {
                indexes2.add(i);
            }
        } else {
            indexes2 = findIndexesOfValue(eventDateList, date);
        }

        List<Integer> eventIndexes = new ArrayList<>();
        for (int num : indexes) {
            if (indexes2.contains(num)) {
                eventIndexes.add(num);
            }
        }

        List<Integer> eventListId = createListFromIndexes(eventIdList, eventIndexes);
        eventListId = findUniqueElements(eventListId);
        List<String> eventListName = createListFromIndexes(eventNameList, eventIndexes);
        eventListName = findUniqueElements(eventListName);
        System.out.println("The following are all qualifying events. Please select one to proceed.");
        for (int i = 0; i < eventListId.size(); i++) {
            int eId = eventListId.get(i);
            String eName = eventListName.get(i);
            System.out.println("ID: " + eId + " Name: " + eName + " ");
        }

        while (true) {
            System.out.println("Select one of the events or \"b\" to return to main menu:");
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("b")) {
                return -1;
            }

            int t = -1;
            try {
                t = Integer.parseInt(selection);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }

            if (eventListId.contains(t)) {
                return t;
            }
        }
    }

    /**
     * Buy a ticket
     */
    private void buyTicket() {
        List<Integer> seatIdList = new ArrayList<>();
        List<Integer> seatRowList = new ArrayList<>();
        List<Integer> seatNumList = new ArrayList<>();
        List<Integer> ticketPriceList = new ArrayList<>();
        List<String> seatTypeList = new ArrayList<>();

        // Verify eventID
        int eventID = findEventID();
        if (eventID == -1) {
            System.out.println("Invalid event!");
            return;
        }

        // Get Stadium ID
        int stadiumID;
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL get_stadium_id(?)}");
            ps.setInt(1, eventID);
            ResultSet rs = ps.executeQuery();

            rs.next();
            stadiumID = rs.getInt("stadium_id");
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // Get tickets
        try {
            String query = "{CALL get_buy_ticket_info(?)}";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, eventID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                seatIdList.add(rs.getInt("seat_id"));
                seatRowList.add(rs.getInt("seat_row"));
                seatNumList.add(rs.getInt("seat_number"));
                ticketPriceList.add(rs.getInt("price"));
                seatTypeList.add(rs.getString("seat_type"));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // Print all available seats
        if (seatNumList.isEmpty()) {
            System.out.println("Sorry, there are no available seats. Please try again later!");
            return;
        } else {
            System.out.println("These are the available seats");
            for (int i = 0; i < seatNumList.size(); i++) {
                int seatNum = seatNumList.get(i);
                int seatRow = seatRowList.get(i);
                int seatId = seatIdList.get(i);
                int price = ticketPriceList.get(i);
                String type = seatTypeList.get(i);
                System.out.println("Row: " + seatRow + " Number: " + seatNum + " Id: " + seatId
                        + " Price: " + price + " Type: " + type);
            }
        }

        // Get seat ID from user
        int seat;
        while (true) {
            System.out.print("Please enter desired seatID or \"b\" to return to main menu: ");
            String seatId = scanner.nextLine();
            if (seatId.equalsIgnoreCase("b")) {
                return;
            }
            try {
                seat = Integer.parseInt(seatId);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, Please try again!");
            }
        }

        // Get first name from user
        String fname;
        while (true) {
            System.out.print("Please enter the first name for the ticket or \"b\" to return to main menu: ");
            fname = scanner.nextLine();
            if (fname.equalsIgnoreCase("b")) {
                return;
            } else if (fname.isEmpty()) {
                System.out.println("Name cannot be empty");
            } else {
                break;
            }
        }

        // Get last name from user
        String lname;
        while (true) {
            System.out.print("Please enter the last name for the ticket or \"b\" to return to main menu: ");
            lname = scanner.nextLine();
            if (lname.equalsIgnoreCase("b")) {
                return;
            } else if (lname.isEmpty()) {
                System.out.println("Name cannot be empty");
            } else {
                break;
            }
        }

        // Get ticketID
        int TicketId = -1;
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL get_ticket_from_seat_stadium_event(?, ?, ?)}");
            ps.setInt(1, seat);
            ps.setInt(2, stadiumID);
            ps.setInt(3, eventID);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TicketId = rs.getInt("ticket_id");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // Get payment account number
        String account;
        try {
            String query = "{CALL get_account_number(?)}";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, this.loginAccount);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                account = rs.getString("account_number");
            } else {
                throw new RuntimeException("Should not reach here.");
            }


            query = "{CALL update_ticket_buyer(?, ?, ?, ?, ?, ?)}";
            PreparedStatement ps2 = connection.prepareStatement(query);
            ps2.setString(1, fname);
            ps2.setString(2, lname);
            ps2.setString(3, account);
            ps2.setString(4, this.loginAccount);
            ps2.setInt(5, TicketId);
            ps2.setString(6, LocalDate.now().toString());

            // Execute the update
            int rowsAffected = ps2.executeUpdate();

            if (rowsAffected != 1) {
                throw new RuntimeException("update error.");
            }
            System.out.println("Ticket bought.");
            ps.close();
            ps2.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
    }

    /**
     * A user posts a new ticket and seat for an event
     */
    private void transferTicket() {
        int eventID = findEventID();
        if (eventID == -1) {
            System.out.println("Invalid event!");
            return;
        }

        int stadiumID, i, j, price, ticketID;
        String type;
        int seatID = -1;

        // Get stadium ID
        try {
            PreparedStatement ps2 = connection.prepareStatement("{CALL get_stadium_id(?)}");
            ps2.setInt(1, eventID);
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();
            stadiumID = rs2.getInt("stadium_ID");
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // Get seat row
        while (true) {
            System.out.print("Please enter seat row or \"b\" to return to main menu: ");
            String seatrow = scanner.nextLine();
            if (seatrow.equalsIgnoreCase("b")) {
                return;
            }
            try {
                i = Integer.parseInt(seatrow);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }

        // Get seat column
        while (true) {
            System.out.print("Please enter seat column ");
            String seatcolumn = scanner.nextLine();
            try {
                j = Integer.parseInt(seatcolumn);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }

        // Get seat type
        while (true) {
            System.out.print("Please enter one of the following seat type: General Admission, Box Seats, Club Seats, " +
                    "Suites, Accessible Seats, Standing Areas, Other: ");
            type = scanner.nextLine();
            if (type.equalsIgnoreCase("General Admission") ||
                    type.equalsIgnoreCase("Box Seats") ||
                    type.equalsIgnoreCase("Club Seats") ||
                    type.equalsIgnoreCase("Suites") ||
                    type.equalsIgnoreCase("Accessible Seats") ||
                    type.equalsIgnoreCase("Standing Areas") ||
                    type.equalsIgnoreCase("Other")) {
                break;
            } else {
                System.out.println("Invalid input");
            }
        }

        // Get seat price
        while (true) {
            System.out.print("Please enter price: ");
            String p = scanner.nextLine();
            try {
                price = Integer.parseInt(p);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }

        try {
            PreparedStatement ps = connection.prepareStatement("{CALL create_seats(?, ?, ?, ?)}");
            ps.setInt(1, i);
            ps.setInt(2, j);
            ps.setString(3, type);
            ps.setInt(4, stadiumID);
            int affectedRows = ps.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Should not reach here. Error: " + affectedRows);
            }

            PreparedStatement ps2 = connection.prepareStatement("{CALL get_seat_from_row_number_stadium(?, ?, ?)}");
            ps2.setInt(1, i);
            ps2.setInt(2, j);
            ps2.setInt(3, stadiumID);
            ResultSet rs = ps2.executeQuery();
            if (rs.next()) {
                seatID = rs.getInt("seat_id");
            }

            PreparedStatement ps4 = connection.prepareStatement("{CALL create_ticket(?, ?, ?, ?)}");
            ps4.setInt(1, price);
            ps4.setInt(2, seatID);
            ps4.setInt(3, stadiumID);
            ps4.setInt(4, eventID);
            int affectedRow = ps4.executeUpdate();
            if (affectedRow != 1) {
                throw new RuntimeException("update error.");
            }

            ps = connection.prepareStatement("{CALL get_ticket_from_seat_stadium_event(?, ?, ?)}");
            ps.setInt(1, seatID);
            ps.setInt(2, stadiumID);
            ps.setInt(3, eventID);
            rs = ps.executeQuery();
            if (rs.next()) {
                ticketID = rs.getInt("ticket_id");
            } else {
                throw new RuntimeException("Should not reach here.");
            }

            String account;
            String query = "{CALL get_account_number(?)}";
            PreparedStatement ps5 = connection.prepareStatement(query);
            ps5.setString(1, this.loginAccount);

            ResultSet rs5 = ps5.executeQuery();
            if (rs5.next()) {
                account = rs5.getString("account_number");
            } else {
                throw new RuntimeException("Should not reach here.");
            }

            query = "{CALL update_ticket_seller(?, ?, ?)}";
            PreparedStatement ps3 = connection.prepareStatement(query);
            ps3.setString(1, account);
            ps3.setString(2, this.loginAccount);
            ps3.setInt(3, ticketID);
            // Execute the update
            int rowsAffected = ps3.executeUpdate();
            if (rowsAffected != 1) {
                throw new RuntimeException("update error.");
            } else {
                System.out.println("ticket created.");
            }

            rs.close();
            ps.close();
            ps2.close();
            ps3.close();
            ps4.close();
            ps5.close();
            rs5.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
    }

    /**
     * Modify an existing ticket
     */
    private void modifyTicket() {
        // Get user current tickets
        List<Integer> ticketIdList = new ArrayList<>();
        List<String> bookingDateList = new ArrayList<>();
        List<String> firstNameList = new ArrayList<>();
        List<String> lastNameList = new ArrayList<>();
        List<Integer> seatRowList = new ArrayList<>();
        List<Integer> seatNumList = new ArrayList<>();
        List<String> stadiumNameList = new ArrayList<>();
        List<Integer> stadiumIdList = new ArrayList<>();
        List<String> eventNameList = new ArrayList<>();
        List<Integer> eventIdList = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL get_all_ticket_information_related_to_user(?)}");
            ps.setString(1, this.loginAccount);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ticketIdList.add(rs.getInt("ticket_id"));
                bookingDateList.add(rs.getString("booking_date"));
                firstNameList.add(rs.getString("first_name"));
                lastNameList.add(rs.getString("last_name"));
                seatRowList.add(rs.getInt("seat_row"));
                seatNumList.add(rs.getInt("seat_number"));
                stadiumIdList.add(rs.getInt("stadium_id"));
                stadiumNameList.add(rs.getString("stadium_name"));
                eventIdList.add(rs.getInt("event_id"));
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
        int ticket;
        // Choose which ticket to modify
        while (true) {
            System.out.println("Which ticket do you want to modify. Please enter the ticket id or \"b\" to return to main menu.");
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("b")) {
                return;
            }
            try {
                int t = Integer.parseInt(selection);
                if (ticketIdList.contains(t)) {
                    ticket = t;
                    break;
                } else {
                    System.out.println("Invalid input!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }
        }

        // Get corresponding event id
        int index = ticketIdList.indexOf(ticket);
        int eventId = eventIdList.get(index);
        int stadiumId = stadiumIdList.get(index);

        // Choose what to modify for the ticket
        while (true) {
            System.out.println("Do you want to change the \"name\" or \"seat\" of the ticket or \"b\" to return to main menu.");
            String selection = scanner.nextLine().toLowerCase();
            if (selection.equalsIgnoreCase("name")) {
                System.out.println("Please enter the first name");
                String firstName = scanner.nextLine();
                System.out.println("Please enter the last name");
                String lastName = scanner.nextLine();
                try {
                    String query = "{CALL update_ticket_name(?, ?, ?)}";
                    PreparedStatement ps = connection.prepareStatement(query);
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
            } else if (selection.equalsIgnoreCase("seat")) {
                List<Integer> availableSeatsRow = new ArrayList<>();
                List<Integer> availableSeatsNum = new ArrayList<>();
                // Get all available seats
                try {
                    PreparedStatement ps = connection.prepareStatement("{CALL get_all_empty_seat_from_stadium_for_event(?)}");
                    ps.setInt(1, eventId);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        availableSeatsRow.add(rs.getInt("seat_row"));
                        availableSeatsNum.add(rs.getInt("seat_number"));
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
                    System.out.print("Please enter desired seat: ");
                    String seat = scanner.nextLine();
                    int rowNum, seatNum;

                    try {
                        rowNum = Integer.parseInt(row);
                        seatNum = Integer.parseInt(seat);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input, Please try again!");
                        continue;
                    }

                    if (availableSeatsRow.contains(rowNum) && availableSeatsNum.contains(seatNum)) {
                        List<Integer> numOfRows = new ArrayList<>();
                        for (int i = 0; i < availableSeatsRow.size(); i++) {
                            if (availableSeatsRow.get(i).equals(rowNum)) {
                                numOfRows.add(availableSeatsNum.get(i));
                            }
                        }
                        if (numOfRows.contains(seatNum)) {
                            String firstN, lastN, bkDate;
                            // Get old ticket information
                            try {
                                String query = "{CALL get_ticket(?)}";
                                PreparedStatement ps = connection.prepareStatement(query);
                                ps.setInt(1, ticket);
                                ResultSet rs = ps.executeQuery();

                                if (rs.next()) {
                                    bkDate = rs.getString("booking_date");
                                    firstN = rs.getString("first_name");
                                    lastN = rs.getString("last_name");
                                } else {
                                    System.out.println("Fail to fetch old information");
                                    return;
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException("Should not reach here. Error: " + e);
                            }

                            // Reset old ticket information
                            this.resetTicketToDefault(ticket);

                            // Update new seat and ticket information
                            try {
                                // Get new seat information
                                int newSeatId = -1;
                                PreparedStatement ps = connection.prepareStatement("{CALL get_seat_from_row_number_stadium(?, ?, ?)}");
                                ps.setInt(1, rowNum);
                                ps.setInt(2, seatNum);
                                ps.setInt(3, stadiumId);
                                ResultSet rs = ps.executeQuery();
                                if (rs.next()) {
                                    newSeatId = rs.getInt("seat_id");
                                }

                                // Get new ticket id
                                int newTicketId = -1;
                                ps = connection.prepareStatement("{CALL get_ticket_from_seat_stadium_event(?, ?, ?)}");
                                ps.setInt(1, newSeatId);
                                ps.setInt(2, stadiumId);
                                ps.setInt(3, eventId);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    newTicketId = rs.getInt("ticket_id");
                                }

                                // Update ticket information
                                ps = connection.prepareStatement("{CALL update_ticket(?, ?, ?, ?, ?)}");
                                ps.setString(1, bkDate);
                                ps.setString(2, firstN);
                                ps.setString(3, lastN);
                                ps.setString(4, this.loginAccount);
                                ps.setInt(5, newTicketId);

                                int affectedRows = ps.executeUpdate();
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
            } else if (selection.equalsIgnoreCase("b")) {
                return;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Delete an existing ticket
     */
    private void deleteTicket() {
        // Get user current tickets
        List<Integer> ticketIdList = new ArrayList<>();
        List<String> bookingDateList = new ArrayList<>();
        List<Integer> priceList = new ArrayList<>();
        List<String> firstNameList = new ArrayList<>();
        List<String> lastNameList = new ArrayList<>();
        List<Integer> seatRowList = new ArrayList<>();
        List<Integer> seatNumList = new ArrayList<>();
        List<String> stadiumNameList = new ArrayList<>();
        List<String> eventNameList = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL get_all_ticket_information_related_to_user(?)}");
            ps.setString(1, loginAccount);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ticketIdList.add(rs.getInt("ticket_id"));
                bookingDateList.add(rs.getString("booking_date"));
                priceList.add(rs.getInt("price"));
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

        // Print all current tickets
        if (ticketIdList.isEmpty()) {
            System.out.println("You do not have any tickets. Returning to main menu.");
            return;
        }
        for (int i = 0; i < ticketIdList.size(); i++) {
            int ticketId = ticketIdList.get(i);
            String date = bookingDateList.get(i);
            int price = priceList.get(i);
            String firstName = firstNameList.get(i);
            String lastName = lastNameList.get(i);
            int seatRow = seatRowList.get(i);
            int seatNum = seatNumList.get(i);
            String stadiumName = stadiumNameList.get(i);
            String eventName = eventNameList.get(i);
            System.out.println(ticketId + ": " + eventName + " at " + stadiumName + " on " + date
                    + " for " + firstName + " " + lastName + " with seat on row " + seatRow + " seat number "
                    + seatNum + " with price " + price);
        }

        // ticketId to modify
        int ticket;
        // Choose which ticket to modify
        while (true) {
            System.out.println("Which ticket do you want to delete. Please enter the ticket id or \"b\" to return to main menu.");
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("b")) {
                return;
            }
            try {
                int t = Integer.parseInt(selection);
                if (ticketIdList.contains(t)) {
                    ticket = t;
                    break;
                } else {
                    System.out.println("Invalid input!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }
        }

        // Confirm delete ticket
        while (true) {
            System.out.println("Are you sure you want to delete this ticket? y/n");
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("n")) {
                return;
            } else if (selection.equalsIgnoreCase("y")) {
                break;
            }
        }

        // Delete ticket
        this.resetTicketToDefault(ticket);

        System.out.println("Successfully deleted ticket.");
    }

    /**
     * Resets the ticket information to default
     *
     * @param ticket the ticket id
     */
    private void resetTicketToDefault(int ticket) {
        try {
            String query = "{CALL reset_ticket_trading_information(?)}";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, ticket);

            int affectedRows = ps.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Should not reach here.");
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
    }

    /**
     * View all tickets under user
     */
    private void viewTicket() {
        List<Integer> ticketIdList = new ArrayList<>();
        List<String> bookingDateList = new ArrayList<>();
        List<Integer> priceList = new ArrayList<>();
        List<String> firstNameList = new ArrayList<>();
        List<String> lastNameList = new ArrayList<>();
        List<String> stadiumNameList = new ArrayList<>();
        List<String> eventNameList = new ArrayList<>();
        List<Integer> seatRowList = new ArrayList<>();
        List<Integer> seatNumList = new ArrayList<>();

        try {
            PreparedStatement ps = connection.prepareStatement("{CALL get_all_ticket_information_related_to_user(?)}");
            ps.setString(1, this.loginAccount);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ticketIdList.add(rs.getInt("ticket_id"));
                bookingDateList.add(rs.getString("booking_date"));
                priceList.add(rs.getInt("price"));
                firstNameList.add(rs.getString("first_name"));
                lastNameList.add(rs.getString("last_name"));
                stadiumNameList.add(rs.getString("stadium_name"));
                eventNameList.add(rs.getString("event_name"));
                seatRowList.add(rs.getInt("seat_row"));
                seatNumList.add(rs.getInt("seat_number"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        if (ticketIdList.isEmpty()) {
            System.out.println("You do not have any tickets. Returning to main menu.");
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
                int price = priceList.get(i);
                System.out.println(ticketId + ": " + eventName + " at " + stadiumName + " on " + date
                        + " for " + firstName + " " + lastName + " on row: " + seatRow + " number: "
                        + seatNum + " with price: " + price);
            }
        }
    }

    /**
     * Create an event and its tickets
     */
    private void createEvent() {
        System.out.println("Creating a new event");

        // Get all current events
        List<String> eventNameList = new ArrayList<>();
        List<String> eventDateList = new ArrayList<>();
        List<String> eventTypeList = new ArrayList<>();
        try {
            String query = "{CALL get_all_event()}";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                eventNameList.add(rs.getString("event_name"));
                eventDateList.add(rs.getString("event_date"));
                eventTypeList.add(rs.getString("event_type"));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // Get event name, date, and type from user
        String eventName, eventDate, eventType;
        while (true) {
            System.out.println("Please enter event name");
            String en = scanner.nextLine();
            System.out.println("Please enter event date in the form yyyy-mm-dd");
            String ed = scanner.nextLine();
            System.out.println("Please enter event type(\"Sport\", \"Concert\", \"Art & Theater\", \"Family\", \"Other\")");
            String et = scanner.nextLine();
            if (en.isEmpty() || ed.isEmpty() || et.isEmpty()) {
                System.out.println("Event name or event date or type is empty. Please retry.");
            } else {
                eventName = en;
                eventDate = ed;
                eventType = et;
                break;
            }
        }

        // Get all stadiums
        List<Integer> stadiumIdList = new ArrayList<>();
        List<String> stadiumNameList = new ArrayList<>();
        List<Integer> stadiumCapacityList = new ArrayList<>();
        List<String> stadiumAddressList = new ArrayList<>();
        List<String> stadiumCityList = new ArrayList<>();
        List<String> stadiumCountryList = new ArrayList<>();
        try {
            String query = "{CALL get_all_stadium()}";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                stadiumIdList.add(rs.getInt("stadium_id"));
                stadiumNameList.add(rs.getString("stadium_name"));
                stadiumCapacityList.add(rs.getInt("capacity"));
                stadiumAddressList.add(rs.getString("address_line_1"));
                stadiumCityList.add(rs.getString("city"));
                stadiumCountryList.add(rs.getString("country"));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // output all stadiums
        if (stadiumIdList.isEmpty()) {
            System.out.println("Sorry, there are no available stadiums.");
            return;
        }
        for (int i = 0; i < stadiumIdList.size(); i++) {
            System.out.println("ID: " + stadiumIdList.get(i) + ", Name: " + stadiumNameList.get(i) + ", Capacity: " +
                    stadiumCapacityList.get(i) + ", Address: " + stadiumAddressList.get(i) + ", City: " +
                    stadiumCityList.get(i) + ", Country: " + stadiumCountryList.get(i));
        }

        // select stadium
        int stadiumId;
        while (true) {
            System.out.println("Please enter the ID of the stadium you wish to host the event or \"b\" to return to main menu.");
            String stadium = scanner.nextLine();
            int stadiumTempId;
            if (stadium.equalsIgnoreCase("b")) {
                return;
            }
            try {
                stadiumTempId = Integer.parseInt(stadium);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please retry.");
                continue;
            }

            if (!stadiumIdList.contains(stadiumTempId)) {
                System.out.println("Invalid input. Please retry.");
            } else {
                stadiumId = stadiumTempId;
                break;
            }
        }

        // check if event exists
        if (eventNameList.contains(eventName)) {
            int index = eventNameList.indexOf(eventName);
            if (eventDate.equals(eventDateList.get(index)) &&
                    eventType.equals(eventTypeList.get(index)) &&
                    stadiumId == stadiumIdList.get(index)) {
                System.out.println("Event already exists.");
                return;
            }
        }

        // check if the stadium has another event on that day
        List<String> eventDateInStadium = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL get_events_in_the_same_stadium(?)}");
            ps.setInt(1, stadiumId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                eventDateInStadium.add(rs.getString("event_date"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
        if (eventDateInStadium.contains(eventDate)) {
            System.out.println("Cannot create event. Stadium occupied on the same day.");
            return;
        }

        // confirm crete event
        while (true) {
            System.out.println("Are you sure you want to create the event? y/n");
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("y")) {
                break;
            } else if (selection.equalsIgnoreCase("n")) {
                return;
            } else {
                System.out.print("Invalid input. ");
            }
        }

        // Create Event
        int eventId;
        try {
            String query = "{CALL create_event(?,?,?,?,?)}";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, eventName);
            ps.setString(2, eventDate);
            ps.setString(3, eventType);
            ps.setInt(4, stadiumId);
            ps.setString(5, this.loginAccount);

            int affectedRows = ps.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Should not reach here. Error: " + affectedRows);
            } else {
                System.out.println("Successfully created event.");
            }

            query = "{CALL get_event_id(?, ?, ?, ?, ?)}";
            ps = connection.prepareStatement(query);
            ps.setString(1, eventName);
            ps.setString(2, eventDate);
            ps.setString(3, eventType);
            ps.setInt(4, stadiumId);
            ps.setString(5, this.loginAccount);

            ResultSet rs = ps.executeQuery();
            rs.next();
            eventId = rs.getInt("event_id");

            ps.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Could not create event with given parameters, please try again!");
            return;
        }

        // Get all the seats from the stadium
        List<Integer> seatIdList = new ArrayList<>();
        try {
            String query = "{CALL get_all_seat_id_from_stadium(?)}";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, stadiumId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                seatIdList.add(rs.getInt("seat_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
        if (seatIdList.isEmpty()) {
            System.out.println("Sorry, there are no seats in this stadium.");
            return;
        }

        // Set price of ticket
        int price;
        while (true) {
            System.out.println("Please enter a price for all tickets of the event");
            String selection = scanner.nextLine();
            int inputPrice;
            try {
                inputPrice = Integer.parseInt(selection);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please retry.");
                continue;
            }
            price = inputPrice;
            break;
        }

        // Create tickets for the event
        for (Integer integer : seatIdList) {
            try {
                String query = "{CALL create_ticket(?, ?, ?, ?)}";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, price);
                ps.setInt(2, integer);
                ps.setInt(3, stadiumId);
                ps.setInt(4, eventId);

                int rows_affected = ps.executeUpdate();
                if (rows_affected != 1) {
                    throw new RuntimeException("Should not reach here. Error: " + rows_affected);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Should not reach here. Error: " + e);
            }
        }

        System.out.println("Successfully created all tickets related to the event. Returning to main menu.");
    }

    /**
     * Delete and event and its tickets
     */
    private void deleteEvent() {
        // Get all current events
        List<Integer> eventIdList = new ArrayList<>();
        List<String> eventNameList = new ArrayList<>();
        List<String> eventDateList = new ArrayList<>();
        List<String> eventTypeList = new ArrayList<>();
        List<String> stadiumNameList = new ArrayList<>();
        try {
            String query = "{CALL get_all_event_with_stadium_name()}";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                eventIdList.add(rs.getInt("event_id"));
                eventNameList.add(rs.getString("event_name"));
                eventDateList.add(rs.getString("event_date"));
                eventTypeList.add(rs.getString("event_type"));
                stadiumNameList.add(rs.getString("stadium_name"));
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // Output all current events
        System.out.println("Current events: ");
        for (int i = 0; i < eventIdList.size(); i++) {
            System.out.println("Event ID: " + eventIdList.get(i) + ", Event name: " + eventNameList.get(i) +
                    ", Date: " + eventDateList.get(i) + ", Type: " + eventTypeList.get(i) + ", Stadium: " + stadiumNameList.get(i));
        }

        // Let host select event
        int eventId;
        while (true) {
            System.out.println("Please enter the id of the event you want to delete, or \"b\" to return to main menu.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("b")) {
                return;
            }
            try {
                eventId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please retry.");
                continue;
            }

            if (eventIdList.contains(eventId)) {
                break;
            } else {
                System.out.println("Invalid input. Please retry.");
            }
        }

        // Delete confirmation
        while (true) {
            System.out.println("Are you sure you want to delete the event? y/n");
            String selection = scanner.nextLine();
            if (selection.equalsIgnoreCase("y")) {
                break;
            } else if (selection.equalsIgnoreCase("n")) {
                return;
            } else {
                System.out.print("Invalid input. Please retry.");
            }
        }

        // Delete all tickets related to the event
        try {
            String query = "{CALL delete_all_ticket_related_to_event(?)}";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, eventId);

            int affectedRows = ps.executeUpdate();
            System.out.println(affectedRows + " tickets deleted.");
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // Delete Event
        try {
            String query = "{CALL delete_event(?)}";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, eventId);

            int rowAffected = ps.executeUpdate();
            if (rowAffected != 1) {
                throw new RuntimeException("Should not reach here. Error: " + rowAffected);
            }
            System.out.println("Event deleted.");
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
    }

    /**
     * Create a store in a stadium
     */
    private void createStore() {
        System.out.println("Please enter the name of the store");
        String name = scanner.nextLine();
        System.out.println("What is the type of the store? \"Merchandise\", \"Food and Beverage\", \"Souvenir\", \"Fan Shops\", \"Other\"");
        String type = scanner.nextLine();

        // Get all current stadiums
        List<Integer> stadiumIdList = new ArrayList<>();
        List<String> stadiumNameList = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL get_all_stadium()}");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                stadiumIdList.add(rs.getInt("stadium_id"));
                stadiumNameList.add(rs.getString("stadium_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // Output all stadiums
        if (stadiumIdList.isEmpty()) {
            System.out.println("There are no existing stadiums! Returning to main menu");
            return;
        }
        for (int i = 0; i < stadiumIdList.size(); i++) {
            System.out.println("Stadium ID: " + stadiumIdList.get(i) + ", stadium name: " + stadiumNameList.get(i));
        }

        // Get user select stadium ID
        int stadiumId;
        while (true) {
            System.out.println("Please enter the id of the store, or \"b\" to return to main menu.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("b")) {
                return;
            }
            int stadiumInputId;
            try {
                stadiumInputId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please retry.");
                continue;
            }
            if (stadiumIdList.contains(stadiumInputId)) {
                stadiumId = stadiumInputId;
                break;
            } else {
                System.out.println("Invalid input. Please retry.");
            }
        }

        // create store
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL add_store(?, ?, ?)}");
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setInt(3, stadiumId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Should not reach here. Error: " + affectedRows);
            } else {
                System.out.println("Store added.");
            }
        } catch (SQLException e) {
            System.out.println("Cannot create store with current input.");
        }
    }

    /**
     * Delete a store in a stadium
     */
    private void deleteStore() {
        // Get all stores and their stadiums
        List<Integer> storeIdList = new ArrayList<>();
        List<String> storeNameList = new ArrayList<>();
        List<String> storeTypeList = new ArrayList<>();
        List<String> storeStadiumList = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL get_store_and_stadium_name()}");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                storeIdList.add(rs.getInt("store_id"));
                storeNameList.add(rs.getString("store_name"));
                storeTypeList.add(rs.getString("store_type"));
                storeStadiumList.add(rs.getString("stadium_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // Print all stores
        if (storeIdList.isEmpty()) {
            System.out.println("There are no existing stores. Returning to main menu.");
        } else {
            for (int i = 0; i < storeIdList.size(); i++) {
                int id = storeIdList.get(i);
                String name = storeNameList.get(i);
                String type = storeTypeList.get(i);
                String stadiumname = storeStadiumList.get(i);
                System.out.println("Store ID: " + id + ", store name: " + name + ", store type: " + type + ", stadium: " + stadiumname);
            }
        }

        // select stores
        int storeId;
        while (true) {
            System.out.println("Please enter the id of the store, or \"b\" to return to main menu.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("b")) {
                return;
            }
            int storeInputId;
            try {
                storeInputId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please retry.");
                continue;
            }

            if (storeIdList.contains(storeInputId)) {
                storeId = storeInputId;
                break;
            } else {
                System.out.println("Invalid input. Please retry.");
            }
        }

        // Confirmation
        while (true) {
            System.out.println("Are you sure you want to delete the store with ID: " + storeId + "? y/n");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("y")) {
                break;
            } else if (input.equalsIgnoreCase("n")) {
                return;
            } else {
                System.out.println("Invalid input. Please retry.");
            }
        }

        // Delete store
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL delete_store(?)}");
            ps.setInt(1, storeId);

            int affectedRow = ps.executeUpdate();

            if (affectedRow != 1) {
                throw new RuntimeException("Should not reach here. Error: " + affectedRow);
            } else {
                System.out.println("Store deleted.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
    }

    /**
     * Create a stadium with its seats
     */
    private void createStadium() {
        String name = createStadiumHelper("name");
        int capacity;
        while (true) {
            System.out.println("Please enter the capcity of the stadium");
            String capacityStr = scanner.nextLine();
            try {
                capacity = Integer.parseInt(capacityStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid capacity");
                continue;
            }

            if (capacity < 1) {
                System.out.println("Invalid capacity");
            } else {
                break;
            }
        }

        String address_1 = createStadiumHelper("address line 1");
        System.out.println("Please enter the address line 2 of the stadium or leave it empty");
        String address_2 = scanner.nextLine();
        String city = createStadiumHelper("city");
        String state = createStadiumHelper("state");
        String country = createStadiumHelper("country");

        int zipcode;
        while (true) {
            System.out.println("Please enter the zipcode of the stadium");
            String zipcodeStr = scanner.nextLine();
            try {
                zipcode = Integer.parseInt(zipcodeStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid zipcode");
                continue;
            }

            if (zipcode < 1 || zipcode > 99999) {
                System.out.println("Invalid zipcode");
            } else {
                break;
            }
        }

        int rows;
        while (true) {
            System.out.println("Please enter the number of rows that divide the capacity, or \"b\" to return to main menu.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("b")) {
                return;
            }
            try {
                rows = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid rows");
                continue;
            }

            int seatsPerRow = capacity / rows;
            if (rows > 0 && rows < capacity && seatsPerRow * rows == capacity) {
                break;
            } else {
                System.out.println("Invalid rows");
            }
        }

        // Get number of all types of seats
        int ga, bs, cs, s, as, sa, other;
        while (true) {
            System.out.println("Please enter number of general admission seats, 0 if none");
            String gaStr = scanner.nextLine();
            System.out.println("Please enter number of box seats, 0 if none");
            String bsStr = scanner.nextLine();
            System.out.println("Please enter number of club seats, 0 if none");
            String csStr = scanner.nextLine();
            System.out.println("Please enter number of suites, 0 if none");
            String sStr = scanner.nextLine();
            System.out.println("Please enter number of accessible seats, 0 if none");
            String asStr = scanner.nextLine();
            System.out.println("Please enter number of standing areas, 0 if none");
            String saStr = scanner.nextLine();
            System.out.println("Please enter number of other, 0 if none");
            String otherStr = scanner.nextLine();

            try {
                ga = Integer.parseInt(gaStr);
                bs = Integer.parseInt(bsStr);
                cs = Integer.parseInt(csStr);
                s = Integer.parseInt(sStr);
                as = Integer.parseInt(asStr);
                sa = Integer.parseInt(saStr);
                other = Integer.parseInt(otherStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number");
                continue;
            }

            if (ga + bs + cs + s + as + sa + other != capacity) {
                System.out.println("The total does not add up to the capacity. Please re-enter.");
            } else {
                break;
            }
        }

        // Confirmation
        while (true) {
            System.out.println("Are you sure you want to create a new stadium? y/n");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("y")) {
                break;
            } else if (input.equalsIgnoreCase("n")) {
                return;
            } else {
                System.out.println("Invalid input.");
            }
        }

        // create stadium
        int stadiumID;
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL create_stadium(?, ?, ?, ?, ?, ?, ?, ?)}");
            ps.setString(1, name);
            ps.setInt(2, capacity);
            ps.setString(3, address_1);
            ps.setString(4, address_2);
            ps.setString(5, city);
            ps.setString(6, state);
            ps.setString(7, country);
            ps.setInt(8, zipcode);

            int affectedRows = ps.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Should not reach here. Error: " + affectedRows);
            } else {
                System.out.println("Stadium created.");
            }

            ps = connection.prepareStatement("SELECT LAST_INSERT_ID()");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stadiumID = rs.getInt(1);
            } else {
                throw new RuntimeException("Should not reach here.");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Cannot create stadium with current input. Returning to main menu");
            return;
        }

        // create seats
        int seatsPerRow = capacity / rows;
        int i, j;
        for (i = 0; i < rows; i++) {
            for (j = 0; j < seatsPerRow; j++) {
                String type;
                if (ga > 0) {
                    type = "General Admission";
                    ga--;
                } else if (bs > 0) {
                    type = "Box Seats";
                    bs--;
                } else if (cs > 0) {
                    type = "Club Seats";
                    cs--;
                } else if (s > 0) {
                    type = "Suites";
                    s--;
                } else if (as > 0) {
                    type = "Accessible Seats";
                    as--;
                } else if (sa > 0) {
                    type = "Standing Areas";
                    sa--;
                } else if (other > 0) {
                    type = "Other";
                    other--;
                } else {
                    throw new RuntimeException("Should not reach here.");
                }

                try {
                    PreparedStatement ps = connection.prepareStatement("{CALL create_seats(?, ?, ?, ?)}");
                    ps.setInt(1, i);
                    ps.setInt(2, j);
                    ps.setString(3, type);
                    ps.setInt(4, stadiumID);
                    int affectedRows = ps.executeUpdate();
                    if (affectedRows != 1) {
                        throw new RuntimeException("Should not reach here. Error: " + affectedRows);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Should not reach here. Error: " + e);
                }
            }
        }
        System.out.println("Seats created");
    }

    /**
     * Helps get user input
     *
     * @param field the field the application needs
     * @return the verified input
     */
    private String createStadiumHelper(String field) {
        String result;
        while (true) {
            System.out.println("Please enter the " + field + " of the stadium");
            result = scanner.nextLine();
            if (result.isEmpty()) {
                System.out.println(field + " cannot be empty");
            } else {
                return result;
            }
        }
    }

    /**
     * Delete a stadium with its seats
     */
    private void deleteStadium() {
        // Get all stadiums
        List<Integer> stadiumIdList = new ArrayList<>();
        List<String> stadiumNameList = new ArrayList<>();
        List<Integer> stadiumCapacityList = new ArrayList<>();
        List<String> stadiumCityList = new ArrayList<>();
        List<String> stadiumStateList = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL get_all_stadium()}");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                stadiumIdList.add(rs.getInt("stadium_id"));
                stadiumNameList.add(rs.getString("stadium_name"));
                stadiumCapacityList.add(rs.getInt("capacity"));
                stadiumCityList.add(rs.getString("city"));
                stadiumStateList.add(rs.getString("state"));
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }

        // Output all stadiums
        if (stadiumIdList.isEmpty()) {
            System.out.println("There are no existing stadiums. Returning to main menu.");
            return;
        }
        for (int i = 0; i < stadiumIdList.size(); i++) {
            System.out.println("Stadium ID: " + stadiumIdList.get(i) +
                    " stadium name: " + stadiumNameList.get(i) +
                    " capacity: " + stadiumCapacityList.get(i) +
                    " city: " + stadiumCityList.get(i) +
                    " state: " + stadiumStateList.get(i));
        }

        // Get desired stadium id to delete
        int stadiumId;
        while (true) {
            System.out.println("Please select the stadium ID to delete or \"b\" to return to main menu.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("b")) {
                return;
            }

            try {
                stadiumId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. ");
                continue;
            }

            if (!stadiumIdList.contains(stadiumId)) {
                System.out.print("Invalid input. ");
            } else {
                break;
            }
        }

        // Delete stadium
        try {
            PreparedStatement ps = connection.prepareStatement("{CALL delete_stadium(?)}");
            ps.setInt(1, stadiumId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Should not reach here. Error: " + affectedRows);
            } else {
                System.out.println("Stadium deleted.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Should not reach here. Error: " + e);
        }
    }

    /**
     * Returns a list of all elements in a given field of a given table
     *
     * @param field column name
     * @param table table name
     * @return list of all elements in column
     */
    private List<String> getFieldFromTable(String field, String table) {
        List<String> result = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT " + field + " FROM " + table);
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
            connection.close();
            scanner.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close the application. Error: " + e);
        }
    }
}