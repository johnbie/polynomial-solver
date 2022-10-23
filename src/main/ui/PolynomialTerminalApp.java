package ui;

import model.Polynomial;
import model.Term;
import org.json.JSONArray;
import persistence.JsonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PolynomialTerminalApp {
    private Polynomial polynomial;
    private Scanner input;
    private static final String polynomialFilePath = "./data/polynomials.json";

    public PolynomialTerminalApp() {
        init();
        run();
    }

    // initializes variables for polynomial terminal app
    // MODIFIES: this
    // EFFECTS: initializes variables
    private void init() {
        polynomial = new Polynomial();
        File polynomialsFile = new File(polynomialFilePath);
        if (!polynomialsFile.exists()) {
            generateSampleDataToFile();
        }

        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // runs the polynomial terminal app; runs till user quits (q)
    // MODIFIES: this
    // EFFECTS: runs the app
    private void run() {
        boolean keepGoing = true;
        String command;
        System.out.println("\nWelcome to the Rational Polynomial Information App!");
        System.out.println("Fill in a polynomial made up of rational coefficients and natural degrees ");
        System.out.println("to find the x/y intercept, critical points, inflection points, and more!");
        System.out.println("For more information, type 'h'");

        while (keepGoing) {
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // displays options
    // EFFECTS: displays menu of options to user
    private void displayOptions() {
        System.out.println("\nSelect from:");
        System.out.println("\th -> display options aka help");
        System.out.println("\ti -> input from terminal");
        System.out.println("\to -> output to terminal");
        System.out.println("\ts -> save polynomial");
        System.out.println("\tl -> list and load polynomials"); // ability to delete, reset, and order also stored
        System.out.println("\tr -> reset to zero polynomial");
        System.out.println("\te -> evaluate at point (decimal)");
        System.out.println("\td -> get the detailed summary of the polynomial (x-y intercepts, critical points, etc)");
        System.out.println("\tq -> quit");
    }

    // processes user command from string input
    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("h")) {
            displayOptions();
        } else if (command.equals("i")) { // input
            setPolynomialFromInput();
        } else if (command.equals("o")) { // output
            System.out.println("\nPolynomial: " + polynomial);
        } else if (command.equals("s")) { // save
            savePolynomial();
        } else if (command.equals("l")) { // list/load
            loadPolynomial();
        } else if (command.equals("r")) { // reset
            resetPolynomial();
        } else if (command.equals("e")) { // evaluate
            evaluateAtPoint();
        } else if (command.equals("d")) { // detail
            getSummary();
        } else { // quit accounted for in outer function
            System.out.println("\nSelection not valid...");
        }
    }

    // sets polynomial from user input
    // MODIFIES: this
    // EFFECTS: sets polynomial from user input
    private void setPolynomialFromInput() {
        polynomial = createPolynomialFromInput();
    }

    // add term from user input
    // MODIFIES: this
    // EFFECTS: add term from user input
    private void addPolynomialFromInput() {
        polynomial.addTerm(createTermFromInput());
    }

    // resets polynomial to f(x) = 0
    // MODIFIES: this
    // EFFECTS: resets polynomial
    private void resetPolynomial() {
        polynomial = new Polynomial();
        System.out.println("\nReset polynomial.");
    }

    // evaluates function at point
    // EFFECTS: evaluates function at point
    private void evaluateAtPoint() {
        System.out.println("\nFill in a decimal value x to evaluate polynomial at: ");

        while (true) {
            try {
                double x = input.nextDouble();
                System.out.println("The value of the function at " + x + " is " + polynomial.evaluateAtPoint(x));
                return;
            } catch (Exception e) {
                System.out.println("Invalid input! Try again: ");
            }
        }
    }

    // gets summary of the polynomial
    // EFFECTS: gets summary of the polynomial
    private void getSummary() {
        System.out.println("\nSummary for " + polynomial + ": ");
        System.out.println("\tx-intercepts: " + polynomial.getXIntercepts());
        System.out.println("\ty-intercept: " + polynomial.getYIntercept());
        System.out.println("\tcritical points: " + polynomial.getCriticalPoints());
        System.out.println("\tinflection points: " + polynomial.getInflectionPoints());
        System.out.println("\tderivative: " + polynomial.getDerivative());
    }

    // prompts user to fill in a valid polynomial and return it
    // EFFECTS: prompts user to fill in a valid polynomial and return it
    private Polynomial createPolynomialFromInput() {
        System.out.println("\nFill in polynomial in text form (i.e. x^3 + 1/2x^2 + -x): ");

        while (true) {
            try {
                Polynomial polynomial = new Polynomial(input.next());
                System.out.println("Created new polynomial " + polynomial + " from input!");
                return polynomial;
            } catch (Exception e) {
                System.out.println("Invalid input! Try again: ");
            }
        }
    }

    // prompts user to fill in a valid term and return it
    // EFFECTS: prompts user to fill in a valid term and return it
    private Term createTermFromInput() {
        System.out.println("\nAdd a valid term text form (i.e. x^3, 1/2x^2, -x): ");

        while (true) {
            try {
                Term term = new Term(input.next());
                System.out.println("Added new term " + term + " from input!");
                return term;
            } catch (Exception e) {
                System.out.println("Invalid input! Try again: ");
            }
        }
    }

    // generates sample polynomials and writes them to file
    // MODIFIES: polynomials.json
    // EFFECTS: generates sample polynomials and writes them to file
    private void savePolynomial() {
        try {
            JSONArray polynomials = JsonUtil.getArray(polynomialFilePath);
            polynomials.put(polynomial.toString());
            JsonUtil.writeFile(polynomialFilePath, polynomials);
            System.out.println("Saved polynomial!");
        } catch (FileNotFoundException exception) {
            System.out.println("Failed to save file!");
        } catch (IOException e) {
            System.out.println("Failed to get polynomials!");
        }
    }

    private void loadPolynomial() {
        try {
            JSONArray polynomials = JsonUtil.getArray(polynomialFilePath);
            listPolynomials(polynomials);
            displayLoadOptions();

            boolean keepGoing = true;
            String command;
            while (keepGoing) {
                command = input.next();
                command = command.toLowerCase();

                if (command.equals("e")) {
                    keepGoing = false;
                    System.out.println("Exited list-n-load mode.");
                } else {
                    polynomials = processLoadCommands(command, polynomials);
                }
            }

            JsonUtil.writeFile(polynomialFilePath, polynomials);
        } catch (FileNotFoundException exception) {
            System.out.println("Failed to save file!");
        } catch (IOException e) {
            System.out.println("Failed to get polynomials!");
        }
    }

    private void displayLoadOptions() {
        System.out.println("\nSelect from:");
        System.out.println("\th -> get list of commands");
        System.out.println("\ta -> add to index");
        System.out.println("\td -> delete at index");
        System.out.println("\ts -> set to polynomial");
        System.out.println("\tl -> list polynomials");
        System.out.println("\tr -> reset polynomials to default");
        System.out.println("\te -> exit back to main");
    }

    private JSONArray processLoadCommands(String command, JSONArray polynomials) {
        if (command.equals("h")) {
            displayLoadOptions();
        } else if (command.equals("a")) { // add
            polynomials = addPolynomialToList(polynomials);
        } else if (command.equals("d")) { // delete
            polynomials = deletePolynomialFromList(polynomials);
        } else if (command.equals("s")) { // set
            setToPolynomialFromList(polynomials);
        } else if (command.equals("l")) { // list
            listPolynomials(polynomials);
        } else if (command.equals("r")) { // reset
            polynomials = generateSampleData();
            System.out.println("Reset polynomials list!");
        }
        return polynomials;
    }

    private JSONArray addPolynomialToList(JSONArray polynomials) {
        System.out.println("\nFill in a polynomial to add: ");
        while (true) {
            try {
                String polyStr = input.next();
                Polynomial polynomial = new Polynomial(polyStr);
                polynomials.put(polyStr);
                System.out.println("Added new polynomial " + polynomial + "!");
                return polynomials;
            } catch (Exception e) {
                System.out.println("Invalid input! Try again: ");
            }
        }
    }

    private JSONArray deletePolynomialFromList(JSONArray polynomials) {
        System.out.println("\nDelete at index: ");
        while (true) {
            try {
                int index = input.nextInt();
                polynomials.remove(index);
                System.out.println("Removed polynomial at " + index + "!");
                return polynomials;
            } catch (InputMismatchException e) {
                System.out.println("Input type mismatch! escaping command...");
                return polynomials;
            } catch (Exception e) {
                System.out.println("Invalid input! Try again: ");
            }
        }
    }

    private void setToPolynomialFromList(JSONArray polynomials) {
        System.out.println("\nSelect at index: ");
        while (true) {
            try {
                int index = input.nextInt();
                polynomial = new Polynomial(polynomials.get(index).toString());
                System.out.println("Set to polynomial at " + index + "!");
                return;
            } catch (InputMismatchException e) {
                System.out.println("Input type mismatch! escaping command...");
                return;
            } catch (Exception e) {
                System.out.println("Invalid input! Try again: ");
            }
        }
    }

    private void listPolynomials(JSONArray polynomials) {
        System.out.println("\nPolynomials List:");
        polynomials.length();
        for (int i = 0; i < polynomials.length(); i++) {
            System.out.println("\t" + i + " : " + polynomials.getString(i));
        }
    }

    private JSONArray generateSampleData() {
        JSONArray samplePolynomials = new JSONArray();
        samplePolynomials.put("x^2");
        samplePolynomials.put("x^3 + 6x^2 + 11x + 6");
        samplePolynomials.put("x^3 + -11/4x^2 + -27/2x + 45/4");
        samplePolynomials.put("-x^2 + 2x + 1");
        return samplePolynomials;
    }

    // generates sample polynomials and writes them to file
    // MODIFIES: polynomials.json
    // EFFECTS: generates sample polynomials and writes them to file
    private void generateSampleDataToFile() {
        JSONArray samplePolynomials = generateSampleData();
        try {
            JsonUtil.writeFile(polynomialFilePath, samplePolynomials);
        } catch (FileNotFoundException exception) {
            System.out.println("Failed to generate sample file!");
        }
    }
}
