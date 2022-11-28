package ui;

import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * The starting class for application.
 */
public class Main {
    // EFFECTS: starts app
    public static void main(String[] args) {
        //new PolynomialTerminalApp();
        JFrame frame = new PolynomialGuiApp();

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                EventLog eventLog = EventLog.getInstance();
                for (Event next : eventLog) {
                    System.out.println(next.toString() + "\n");
                }
            }
        });

    }
}
