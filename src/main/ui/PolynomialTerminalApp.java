package ui;

import model.Polynomial;
import model.Term;

import java.util.Scanner;

public class PolynomialTerminalApp {
    private Polynomial polynomial;
    private Scanner input;

    public PolynomialTerminalApp() {
        init();
        run();
    }

    // initializes variables for polynomial terminal app
    // MODIFIES: this
    // EFFECTS: initializes variables
    private void init() {
        polynomial = new Polynomial("x^2 + 7/15x + -4/15");
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
        System.out.println("\ta -> add term to polynomial");
        System.out.println("\tr -> reset to zero polynomial");
        System.out.println("\te -> evaluate at point (decimal)");
        System.out.println("\ts -> get the summary of the polynomial (x-y intercepts, critical points, etc)");
        System.out.println("\td -> get derivative");
        System.out.println("\tq -> quit");
    }

    // processes user command from string input
    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("h")) {
            displayOptions();
        } else if (command.equals("i")) {
            setPolynomialFromInput();
        } else if (command.equals("o")) {
            System.out.println("\nPolynomial: " + polynomial);
        } else if (command.equals("a")) {
            addPolynomialFromInput();
        } else if (command.equals("r")) {
            resetPolynomial();
        } else if (command.equals("e")) {
            evaluateAtPoint();
        } else if (command.equals("s")) {
            getSummary();
        } else if (command.equals("d")) {
            System.out.println("\nd/dx Polynomial: " + polynomial.getDerivative());
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
        System.out.println("\nSummary: ");
        System.out.println("\tx-intercepts: " + polynomial.getXIntercepts());
        System.out.println("\ty-intercept: " + polynomial.getYIntercept());
        System.out.println("\tcritical points: " + polynomial.getCriticalPoints());
        System.out.println("\tinflection points: " + polynomial.getInflectionPoints());
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
}
