package com.ps;

// Import BufferedReader/Writer, along with ArrayList and Scanner
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main
{
    //create static Scanners and ArrayList to be called on
    static ArrayList<Transaction> allTransactions = new ArrayList<>();
    static Scanner inputScanner = new Scanner(System.in);
    static Scanner commandScanner = new Scanner(System.in);

    // Date and time formatters
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args)
    {
        loadTransactions();

        /*
        create a main menu that allows a user to
        1) add deposit,
        2) make payment(debit),
        3) access Ledger,
        4) exit program
        this will be done in a series of break cases/switch statements in nested in a do/while loop
         */

        // Initialize the command variable (int mainMenuCommand;)
        int mainMenuCommand;

        //Do/While loop to run user command through switch statements while they dont want to exit
        do {
            // Display the menu, sout
            System.out.println("Please enter an option: ");
            System.out.println("1) Add Deposit");
            System.out.println("2) Make Payment(Debit)");
            System.out.println("3) Ledger");
            System.out.println("0) Exit");
            System.out.print("Command: ");

            // Use scanner to store the users command input into a variable
            try {
                mainMenuCommand = commandScanner.nextInt();
            } catch (Exception e) {
                //ime.printStackTrace();
                mainMenuCommand = 0;
            }

            // switch statement to match the user command to the provided cases
            switch (mainMenuCommand) {
                case 1:
                    // methods will match the corresponding command
                    addDeposit();
                    break;
                case 2:
                    makePayment();
                    break;
                case 3:
                    accessLedger();
                    break;
                case 0:
                    System.out.println("Thank you for your valuable time, come again");
                    break;
                default:
                    // Handle incorrect commands
                    System.out.println("Command not found, please try again");
            }
        } while (mainMenuCommand != 0);
    }

    //create method to load all transactions
    public static void loadTransactions()
    {
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader("transactions.csv"));
            String firstLine = bufferedReader.readLine(); // Assumes the first line is the header and skips it
            String line;

            // Skip over the header line


            // Process each remaining line in the file
            while ((line = bufferedReader.readLine()) != null) {
                String[] fields = line.split("\\|");
                    String date = fields[0];
                    String time = fields[1];
                    String description = fields[2];
                    String vendor = fields[3];
                    double amount = Double.parseDouble(fields[4]);
                    //allTransactions.add(new Transaction(fields[0], fields[1], fields[2], fields[3], amount));
                allTransactions.add(new Transaction(date, time, description, vendor, amount));
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addDeposit()
    {
        //BufferedWriter method
        System.out.print("Enter description: ");
        String description = inputScanner.nextLine();

        System.out.print("Enter vendor: ");
        String vendor = inputScanner.nextLine();

        System.out.print("Enter deposit amount: ");
        double amount = inputScanner.nextDouble();
        inputScanner.nextLine(); // Consume newline

        // Get current date and time, formatted
        String date = LocalDate.now().format(dateFormatter);
        String time = LocalTime.now().format(timeFormatter);

        // Create the transaction record
        String record = String.format("%s|%s|%s|%s|%.2f\n", date, time, description, vendor, amount);

        // Write the record to a CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            writer.write(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Deposit added successfully!");
    }

    public static void makePayment()
    {
        //BufferedWriter method
        System.out.print("Enter payment amount: ");
        double amount = inputScanner.nextDouble();
        inputScanner.nextLine(); // Consume newline

        System.out.print("Enter description: ");
        String description = inputScanner.nextLine();

        System.out.print("Enter vendor: ");
        String vendor = inputScanner.nextLine();

        // Get current date and time, formatted
        String date = LocalDate.now().format(dateFormatter);
        String time = LocalTime.now().format(timeFormatter);

        // Create the transaction record
        String record = String.format("%s|%s|%s|%s|%.2f\n", date, time, description, vendor, -amount);

        // Write the record to a CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            writer.write(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Payment processed successfully!");
    }

    //method to navigate ledger
    public static void accessLedger()
    {
        //load report
        loadReports();

        //make variable to navigate ledger
        int navLedgerCommand;

        /*
        another do/while to allow the user to navigate ledger till they wish to return to main menu
        while they have access the ledger the user can do several things such as:
        1) Display all transactions
        2) Display Deposits only
        3) Display only payments
        4) Run Reports (do/while loop)
         */
        do {
            // Display the menu, sout
            System.out.println("What would you like to do? : ");
            System.out.println("1) Display All Transactions");
            System.out.println("2) Display All Deposits");
            System.out.println("3) Display All Payments");
            System.out.println("4) Reports");
            System.out.println("0) Exit");
            System.out.print("Command: ");

            // Use scanner to store the users command input into a variable
            try {
                navLedgerCommand = commandScanner.nextInt();
            } catch (Exception e) {
                navLedgerCommand = 0;
            }

            // switch statement to match the user command to the provided cases
            switch (navLedgerCommand) {
                case 1:
                    // methods will match the corresponding command
                    displayAll();
                    break;
                case 2:
                    displayDeposits();
                    break;
                case 3:
                    displayPayments();
                    break;
                case 4:
                    runReports();
                    break;
                case 0:
                    System.out.println("Returning to main menu");
                    break;
                default:
                    // Handle incorrect commands
                    System.out.println("Command not found, please try again");
            }
            commandScanner.nextLine();
        }while (navLedgerCommand != 0);
    }

    //create method to load all transactions
    public static void loadReports()
    {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("transactions.csv"))) {
            String firstLine = bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Split the CSV line by the pipe character '|'
                String[] values = line.split("\\|");
                if (values.length == 5) {
                    String date = values[0];
                    String time = values[1];
                    String description = values[2];
                    String vendor = values[3];
                    double amount = Double.parseDouble(values[4]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayAll()
    {
        //Method uses loop to run through all transactions and prints them out in proper format
        System.out.println("All Transactions:");
        for (Transaction transaction : allTransactions) {
            System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: $%.2f\n",
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
        }
    }

    public static void displayDeposits()
    {
        //Method checks all transactions for those greater than zero and prints them out
        System.out.println("Displaying All Deposits:");
        for (Transaction transaction : allTransactions) {
            if (transaction.getAmount() > 0) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: $%.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
    }

    public static void displayPayments()
    {
        //Method checks all transactions for those less than zero and prints them out
        System.out.println("Displaying All payments:");
        for (Transaction transaction : allTransactions) {
            if (transaction.getAmount() < 0) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: $%.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
    }

    public static void runReports()
    {
        //make variable to navigate ledger
        int navReportCommand;

        /*
        another do/while to allow the user to navigate ledger till they wish to return to main menu
        while they have access the ledger the user can do several things such as:
        1) Display all transactions
        2) Display Deposits only
        3) Display only payments
        4) Run Reports (do/while loop)
         */
        do {
            // Display the menu, sout
            System.out.println("How would you like to search the reports?: ");
            System.out.println("1) Month to Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year to Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Exit");
            System.out.print("Command: ");

            // Use scanner to store the users command input into a variable
            try {
                navReportCommand = commandScanner.nextInt();
            } catch (Exception e) {
                navReportCommand = 0;
            }

            // switch statement to match the user command to the provided cases
            switch (navReportCommand) {
                case 1:
                    // methods will match the corresponding command
                    searchMTD();
                    break;
                case 2:
                    searchPMs();
                    break;
                case 3:
                    searchYTD();
                    break;
                case 4:
                    searchPY();
                    break;
                case 5:
                    searchVendor();
                    break;
                case 6:
                    searchCustom();
                    break;
                case 0:
                    System.out.println("Returning to Ledger options");
                    break;
                default:
                    // Handle incorrect commands
                    System.out.println("Command not found, please try again");
            }
        }while (navReportCommand != 0);
    }

    public static void searchMTD()
    {
        System.out.println("Search Month to Date:");
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        // DateTimeFormatter for parsing the transaction dates
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Loop through all transactions
        for (Transaction transaction : allTransactions) {
            // Parse the transaction date
            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), dateFormatter);

            // Get the month and year of the transaction date
            int transactionMonth = transactionDate.getMonthValue();
            int transactionYear = transactionDate.getYear();

            // Check if the transaction occurred in the current month and year
            if (transactionMonth == currentMonth && transactionYear == currentYear) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: $%.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
        System.out.println();
    }

    // Search Previous Month
    public static void searchPMs() {
        System.out.println("Search Previous Month:");
        LocalDate today = LocalDate.now();
        LocalDate previousMonth = today.minusMonths(1);
        int prevMonth = previousMonth.getMonthValue();
        int prevYear = previousMonth.getYear();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Transaction transaction : allTransactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), dateFormatter);

            if (transactionDate.getMonthValue() == prevMonth && transactionDate.getYear() == prevYear) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: $%.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
        System.out.println();
    }

    // Search Year to Date
    public static void searchYTD() {
        System.out.println("Search Year to Date:");
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Transaction transaction : allTransactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), dateFormatter);

            if (transactionDate.getYear() == currentYear) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: $%.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
        System.out.println();
    }

    // Search Previous Year
    public static void searchPY() {
        System.out.println("Search Previous Year:");
        LocalDate today = LocalDate.now();
        int prevYear = today.getYear() - 1;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Transaction transaction : allTransactions) {
            LocalDate transactionDate = LocalDate.parse(transaction.getDate(), dateFormatter);

            if (transactionDate.getYear() == prevYear) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: $%.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
        System.out.println();
    }

    // Search by Vendor
    public static void searchVendor() {
        System.out.println("Search by Vendor:");
        inputScanner.nextLine(); // Consume any leftover newline character

        System.out.print("Enter vendor name: ");
        String vendor = inputScanner.nextLine();

        for (Transaction transaction : allTransactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.printf("Date: %s, Time: %s, Description: %s, Vendor: %s, Amount: $%.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
        }
        System.out.println();
    }

    // Search by Custom
    public static void searchCustom()
    {
        System.out.println("Search by Custom Name:");
    }
}