package ui;

import model.Polynomial;
import model.Term;
import org.json.JSONArray;
import persistence.JsonUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * The class for starting the polynomial gui app.
 */
public class PolynomialGuiApp extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private Polynomial polynomial;
    private static final String polynomialFilePath = "./data/polynomials.json";
    private GraphicsPanel graphicsPanel;
    private SummaryPanel summaryPanel;

    // EFFECTS: constructor; initializes and starts the polynomial gui app
    public PolynomialGuiApp() {
        super("Polynomial Root Calculator");
        init();
    }

    // MODIFIES: this
    // EFFECTS: initializes polynomial gui app
    private void init() {
        initPolynomial();
        initFrame();
        initUI();
    }

    // MODIFIES: this, file storage
    // EFFECTS: initializes the polynomial and polynomial file (if not exists)
    private void initPolynomial() {
        polynomial = new Polynomial("0");
        File polynomialsFile = new File(polynomialFilePath);
        if (!polynomialsFile.exists()) {
            generateSampleDataToFile();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the application's frame
    private void initFrame() {
        // main settings
        setTitle("CPSC 210: Alarm System Simulator");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // misc settings
        setBackground(Color.white);
        setUndecorated(false);

        // center on screen
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }

    // MODIFIES: this
    // EFFECTS: initializes the application's ui elements,
    // such as summary and graph
    private void initUI() {
        summaryPanel = new SummaryPanel(this.polynomial);
        add(summaryPanel, BorderLayout.EAST);

        graphicsPanel = new GraphicsPanel(this.polynomial);
        add(graphicsPanel, BorderLayout.WEST);

        addMenuBar();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: adds elements to the application's menu bar
    private void addMenuBar() {
        // set file menu
        JMenu fileMenu = new JMenu("Files");
        fileMenu.setMnemonic('F');
        addMenuItem(fileMenu, new LoadAction(), KeyStroke.getKeyStroke("control L"));
        addMenuItem(fileMenu, new SaveAction(), KeyStroke.getKeyStroke("control S"));

        // set edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        addMenuItem(editMenu, new NewPolynomialAction(), KeyStroke.getKeyStroke("control N"));
        addMenuItem(editMenu, new NewTermAction(), KeyStroke.getKeyStroke("control T"));
        addMenuItem(editMenu, new ResetPolynomialAction(), KeyStroke.getKeyStroke("control R"));
        addMenuItem(editMenu, new DerivePolynomialAction(), KeyStroke.getKeyStroke("control D"));

        // add menus to menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        setJMenuBar(menuBar);
    }

    // EFFECTS: Adds item to menu (which may not be set to app yet)
    // Code copied from AlarmSystem project
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    // MODIFIES: this
    // EFFECTS: prepares action for when user wants to load polynomial from file
    private class LoadAction extends AbstractAction {
        private JList<String> polynomialList;
        private String selectedPolynomial = "";

        // EFFECTS: constructor; initializes action
        LoadAction() {
            super("Load from file");
        }

        @Override
        // MODIFIES: parent (PolynomialGuiApp)
        // EFFECTS: show list of polynomials from file, then allow user to select from it
        public void actionPerformed(ActionEvent evt) {
            // load polynomial as json
            try {
                // create list element
                polynomialList = new JList<>(getPolynomialFromFile());
                polynomialList.addListSelectionListener(new PolynomialListSelectionHandler());

                // create dialog for selecting list
                JPanel inputPanel = new JPanel();
                inputPanel.add(polynomialList);
                int result = JOptionPane.showConfirmDialog(null, inputPanel,
                        "Please select a polynomial from list below:", JOptionPane.OK_CANCEL_OPTION);

                // check for results
                if (result == JOptionPane.OK_OPTION) {
                    polynomial = new Polynomial(selectedPolynomial);
                    summaryPanel.update(polynomial);
                    graphicsPanel.update(polynomial);
                }
            } catch (FileNotFoundException exception) {
                JOptionPane.showMessageDialog(null, "Failed to save file!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to get polynomials!");
            }
        }

        // EFFECTS: gets polynomials from file and returns as an array of strings
        private String[] getPolynomialFromFile() throws IOException {
            // get from file
            JSONArray polynomials = JsonUtil.getArray(polynomialFilePath);

            // create string array
            String[] polynomialStrings = new String[polynomials.length()];
            for (int i = 0; i < polynomials.length(); i++) {
                polynomialStrings[i] = polynomials.getString(i);
            }

            return polynomialStrings;
        }

        // MODIFIES: this (LoadAction)
        // EFFECTS: prepares action for when user wants to load polynomial from file
        private class PolynomialListSelectionHandler implements ListSelectionListener {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && polynomialList.getSelectedIndex() >= 0) {
                    selectedPolynomial = polynomialList.getSelectedValue();
                }
            }
        }
    }


    // MODIFIES: this, file storage
    // EFFECTS: prepares action for when user wants to save polynomial to file
    private class SaveAction extends AbstractAction {

        // EFFECTS: constructor; initializes action
        SaveAction() {
            super("Save to file");
        }

        @Override
        // EFFECTS: save polynomial to file, then alert user
        public void actionPerformed(ActionEvent evt) {
            try {
                JSONArray polynomials = JsonUtil.getArray(polynomialFilePath);
                polynomials.put(polynomial.toString());
                JsonUtil.writeFile(polynomialFilePath, polynomials);
                JOptionPane.showMessageDialog(null, "Saved polynomial " + polynomial + "!");
            } catch (FileNotFoundException exception) {
                JOptionPane.showMessageDialog(null, "Failed to save file!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to get polynomials!");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: prepares action for when user wants to load polynomial from input
    private class NewPolynomialAction extends AbstractAction {

        // EFFECTS: constructor; initializes action
        NewPolynomialAction() {
            super("New polynomial");
        }

        @Override
        // EFFECTS: create input dialog, then load polynomial from said input
        public void actionPerformed(ActionEvent evt) {
            String polyStr = JOptionPane.showInputDialog(null,
                    "Polynomial",
                    "New Polynomial",
                    JOptionPane.QUESTION_MESSAGE);

            try {
                if (polyStr != null) {
                    polynomial = new Polynomial(polyStr);
                    summaryPanel.update(polynomial);
                    graphicsPanel.update(polynomial);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: prepares action for when user wants to add term from input
    private class NewTermAction extends AbstractAction {

        // EFFECTS: constructor; initializes action
        NewTermAction() {
            super("Add new term");
        }

        @Override
        // EFFECTS: create input dialog, then add term to loaded polynomial
        public void actionPerformed(ActionEvent evt) {
            String termStr = JOptionPane.showInputDialog(null,
                    "Term",
                    "Add new term",
                    JOptionPane.QUESTION_MESSAGE);

            try {
                if (termStr != null) {
                    Term term = new Term(termStr);
                    polynomial.addTerm(term);
                    summaryPanel.update(polynomial);
                    graphicsPanel.update(polynomial);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "System Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: prepares action for when user wants to reset polynomial (non-reversible)
    private class ResetPolynomialAction extends AbstractAction {

        // EFFECTS: constructor; initializes action
        ResetPolynomialAction() {
            super("Reset polynomial");
        }

        @Override
        // EFFECTS: create input dialog, then add term to loaded polynomial
        public void actionPerformed(ActionEvent evt) {
            polynomial = new Polynomial();
            summaryPanel.update(polynomial);
            graphicsPanel.update(polynomial);
        }
    }

    // MODIFIES: this
    // EFFECTS: prepares action for when user wants to get derivative of polynomial (non-reversible)
    private class DerivePolynomialAction extends AbstractAction {

        // EFFECTS: constructor; initializes action
        DerivePolynomialAction() {
            super("Get derivative");
        }

        @Override
        // EFFECTS: create input dialog, then add term to loaded polynomial
        public void actionPerformed(ActionEvent evt) {
            polynomial = polynomial.getDerivative();
            summaryPanel.update(polynomial);
            graphicsPanel.update(polynomial);
        }
    }

    // EFFECTS: generates sample polynomials as json array
    private JSONArray generateSampleData() {
        JSONArray samplePolynomials = new JSONArray();
        samplePolynomials.put("x^2");
        samplePolynomials.put("x^3 + 6x^2 + 11x + 6");
        samplePolynomials.put("x^3 + -11/4x^2 + -27/2x + 45/4");
        samplePolynomials.put("-x^2 + 2x + 1");
        return samplePolynomials;
    }

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
