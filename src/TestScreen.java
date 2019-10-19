import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class TestScreen
{
    /**
     * The game JFrame created
     */
    private JFrame frame;
    /**
     * The file name for the GUI's icon
     */
    public static final String logoSource = "Logo.png";
    /**
     * The currently used word
     */
    private Word focus;
    /**
     * The array of JLabels, one for each POS tab, that displays the word's base form
     */
    private JLabel[] bases;
    /**
     * The array of text fields for the adjectives pane
     */
    private JTextField[][] adjAnswers;
    /**
     * The array of text fields for the nouns pane
     */
    private JTextField[][] nounAnswers;
    /**
     * The array of text fields for the verbs pane
     */
    private JTextField[][] verbAnswers;
    /**
     * The currently selected array of text fields
     */
    private JTextField[][] answerBoxes;
    /**
     * The amount of times the current word has been graded
     */
    int check;
    /**
     * The dialog label
     */
    JLabel dialog;
    /**
     * The panel containing help information
     */
    JPanel helpfield;

    /**
     * Creates a testScreen GUI
     * @param testType the current POS used
     * @param focus the current Word being tested
     */
    public TestScreen(POS testType, Word focus)
    {
        this.focus = focus;
        bases = new JLabel[POS.values().length];
        for (int i = 0; i < bases.length; i++)
        {
            bases[i] = new JLabel(focus.getBase());
        }
        ImageIcon logo = new ImageIcon(Director.fileStart + logoSource);
        JTabbedPane selectPOS = new JTabbedPane();
        JPanel adjPanel = setupAdj();
        JPanel nounPanel = setupNoun();
        JPanel verbPanel = setupVerb();
        if (testType == POS.ADJECTIVE)
            answerBoxes = adjAnswers;
        else if (testType == POS.NOUN)
            answerBoxes = nounAnswers;
        else
            answerBoxes = verbAnswers;
        selectPOS.addTab(POS.ADJECTIVE.getText(), adjPanel);
        selectPOS.addTab(POS.NOUN.getText(), nounPanel);
        selectPOS.addTab(POS.VERB.getText(), verbPanel);
        selectPOS.setSelectedIndex(testType.ordinal());
        selectPOS.addChangeListener(new ChangeListener()
        {
            /**
             * Reloads the current word and game state when the current tab is switched
             * @param event the tabbedpane's change event
             */
            public void stateChanged(ChangeEvent event)
            {
                Director.loadDictionary(POS.values()[selectPOS.getSelectedIndex()]);
                setFocus(POS.values()[selectPOS.getSelectedIndex()], Director.updateWord());
                if (selectPOS.getSelectedIndex() == POS.ADJECTIVE.ordinal())
                {
                    answerBoxes = adjAnswers;
                }
                else if (selectPOS.getSelectedIndex() == POS.NOUN.ordinal())
                    answerBoxes = nounAnswers;
                else
                    answerBoxes = verbAnswers;
                frame.pack();
                frame.setLocationRelativeTo(null);
                check = 0;
                dialog.setText(" Press Grade when ready ");
            }
        });
        JPanel footer = new JPanel(new BorderLayout());
        dialog = new JLabel(" Press Grade when ready ");
        JButton helper = new JButton("Help");
        helper.addActionListener(new ActionListener()
        {
            /**
             * Opens and closes the help pane when the help button is pressed
             * @param event the button's action event
             */
            public void actionPerformed(ActionEvent event)
            {
                if (helpfield == null)
                {
                    helpfield = new JPanel(new GridLayout(4, 1));
                    helpfield.add(new JLabel("Welcome to Latina, a Latin word study tool"));
                    helpfield.add(new JLabel("Choose the part of speech and type of word to study at the top"));
                    helpfield.add(new JLabel("Type your guesses, and press Grade to check them. " +
                            "If there are wrong answers they will be counted, then highlighted"));
                    helpfield.add(new JLabel("Latina grades using macrons (ā): to type with them press alt + the key"));
                    footer.add(helpfield, BorderLayout.SOUTH);
                    footer.revalidate(); //https://stackoverflow.com/questions/3599474/adding-components-dynamically-in-a-jpanel
                } else if (helpfield.isVisible())
                {
                    helpfield.setVisible(false);
                } else
                {
                    helpfield.setVisible(true);
                }

            }
        });
        JButton grader = new JButton("Grade");
        grader.addActionListener(new ActionListener()
        {
            /**
             * Checks the answers inputted when the Grade button is pressed
             * @param event the Grade button's action event
             */
            public void actionPerformed(ActionEvent event) //http://stackoverflow.com/questions/10472376/can-i-use-multiple-actionlisteners-in-a-single-class
            {
                if (checkAnswers())
                {
                    setFocus(POS.values()[selectPOS.getSelectedIndex()], Director.updateWord());
                }
            }
        });
        footer.add(dialog, BorderLayout.WEST);
        footer.add(grader, BorderLayout.CENTER);
        footer.add(helper, BorderLayout.EAST);
        frame = new JFrame("Latina");
        frame.setIconImage(logo.getImage()); //http://stackoverflow.com/questions/1614772/how-to-change-jframe-icon
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(selectPOS, BorderLayout.CENTER);
        frame.add(footer, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Sets up the noun's jpanel for the GUI
     * @return the noun's jpanel
     */
    private JPanel setupNoun()
    {
        JPanel nounPanel = new JPanel();
        nounPanel.setLayout(new BorderLayout());
        JPanel menuBar = new JPanel();
        JComboBox genders = new JComboBox(Gender.textValues());
        JComboBox declensions = new JComboBox(Declension.textValues());
        JButton selectionButton = new JButton("Select");
        selectionButton.addActionListener(new ActionListener()
        {
            /**
             * Gets a list of Nouns with the user-chosen constraints when the Select button is pressed
             * @param event the Select button's action event
             */
            public void actionPerformed(ActionEvent event)
            {
                Gender chosenGen = Gender.getGen((String) genders.getSelectedItem());
                Declension chosenDec = Declension.getDec((String) declensions.getSelectedItem());
                setFocus(POS.NOUN, Director.chooseNouns(chosenGen, chosenDec));
            }
        });
        menuBar.add(genders);
        menuBar.add(declensions);
        menuBar.add(selectionButton);
        String[] rowHeads = Case.getTexts();
        String[] colHeads = Number.getTexts();
        JLabel base = bases[POS.NOUN.ordinal()];
        nounAnswers = fillPanel(nounPanel, Case.values().length, Number.values().length, base, rowHeads, colHeads);
        nounPanel.add(menuBar, BorderLayout.NORTH);
        return nounPanel;
    }

    /**
     * Sets up the adjective's jpanel for the GUI
     * @return the adjective's jpanel
     */
    private JPanel setupAdj()
    {
        JPanel adjPanel = new JPanel();
        adjPanel.setLayout(new BorderLayout());
        JPanel menuBar = new JPanel();
        JComboBox declensions = new JComboBox();
        declensions.addItem(Declension.FIRST.getText());
        declensions.addItem(Declension.THIRDI.getText());
        JButton selectionButton = new JButton("Select");
        selectionButton.addActionListener(new ActionListener()
        {
            /**
             * Gets a list of Adjectives with the user-chosen constraints when the Select button is pressed
             * @param event the Select button's action event
             */
            public void actionPerformed(ActionEvent event)
            {
                Declension chosenDec = Declension.getDec((String) declensions.getSelectedItem());
                setFocus(POS.ADJECTIVE, Director.chooseAdjs(chosenDec));
            }
        });
        menuBar.add(declensions);
        menuBar.add(selectionButton);
        String[] rowHeads = Case.getTexts();
        String[] colHeads = Number.getTexts();
        adjAnswers = new JTextField[Gender.ANY.ordinal() * Case.values().length][Number.values().length];
        adjPanel.setLayout(new BorderLayout());
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new FlowLayout());
        for (int i = 0; i < Gender.ANY.ordinal(); i++)
        {
            JLabel header = (i == 0) ? bases[POS.ADJECTIVE.ordinal()] : new JLabel(Gender.values()[i].getText());
            JTextField[][] boxes = fillPanel(answerPanel, Case.values().length, Number.values().length, header, rowHeads, colHeads);
            System.arraycopy(boxes, 0, adjAnswers, i * boxes.length, boxes.length);
        }
        adjPanel.add(answerPanel, BorderLayout.CENTER);
        adjPanel.add(menuBar, BorderLayout.NORTH);
        return adjPanel;
    }

    /**
     * Makes a verb's jpanel for the GUI !!! DOESN"T WORK, VERBS AREN'T IMPLEMENTED YET !!!
     * @return the verb's jpanel
     */
    private JPanel setupVerb()
    {
        return new JPanel();
    }

    /**
     * Makes a jpanel of jtextfields for the user-inputted word forms
     * @param parent the parent to add this jpanel to
     * @param row how many rows of jtextfields
     * @param col how many columns of jtextfields
     * @param header the header for the array, displayed in the top right corner
     * @param rowHeads the headers for the rows
     * @param colHeads the headers for the columns
     * @return an array containing the jtextfields created
     */
    private JTextField[][] fillPanel(JPanel parent, int row, int col, JLabel header, String[] rowHeads, String[] colHeads)
    {
        JPanel boxRegion = new JPanel(new GridLayout(row + 1, col + 1));
        JTextField[][] boxes = new JTextField[Case.values().length][Number.values().length];
        for (int i = 0; i <= row; i++)//replace better later
        {
            for (int j = 0; j <= col; j++)
            {
                if (i == 0)
                {
                    if (j == 0)
                    {
                        boxRegion.add(header);
                    } else
                        boxRegion.add(new JLabel(colHeads[j - 1]));
                } else if (j == 0)
                {
                    boxRegion.add(new JLabel(rowHeads[i - 1]));
                } else
                {
                    JTextField newField = new JTextField(focus.getBase().length() * 2);
                    newField.addKeyListener(macrons);
                    boxes[i - 1][j - 1] = newField;
                    boxRegion.add(newField);
                }
            }
        }
        parent.add(boxRegion);
        return boxes;
    }

    /**
     * Reloads the jtextfields when a new word has been selected or displays an error message if no words are left
     * @param selected the current POS to use
     * @param newFocus the new Word
     */
    public void setFocus(POS selected, Word newFocus)
    {
        if (newFocus == null)
        {
            bases[selected.ordinal()].setText("Choose wider constraints");
            return;
        }
        focus = newFocus;
        bases[selected.ordinal()].setText(focus.getBase());
        for (JTextField[] a : answerBoxes)
        {
            for (JTextField j : a)
            {
                j.setText("");
                j.setBackground(Color.WHITE);
                j.setColumns(focus.getBase().length() * 2);
            }
        }
    }

    /**
     * Returns the current Word focus
     * @return the currently used Word
     */
    public Word getFocus()
    {
        return focus;
    }

    /**
     * A keylistener to detect when the alt key and a vowel are pressed, which types a macron over the vowel (ā vs a)
     */
    KeyListener macrons = new KeyListener()
    {
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            JTextField source = (JTextField) e.getComponent();
            if (e.isAltDown()) //http://stackoverflow.com/questions/10876491/how-to-use-keylistener
            {
                if (key == KeyEvent.VK_A)
                {
                    source.setText(source.getText() + "ā");
                } else if (key == KeyEvent.VK_E)
                {
                    source.setText(source.getText() + "ē");
                } else if (key == KeyEvent.VK_I)
                {
                    source.setText(source.getText() + "ī");
                } else if (key == KeyEvent.VK_O)
                {
                    source.setText(source.getText() + "ō");
                } else if (key == KeyEvent.VK_U)
                {
                    source.setText(source.getText() + "ū");
                }
            }
        }

        public void keyTyped(KeyEvent e)
        {
        }

        public void keyReleased(KeyEvent e)
        {
        }
    };

    /**
     * Checks the answers against the Word's actual forms. If they don't match, the errors are displayed in different
     * ways based on how many times they've been checked.
     * @return whether the answers match
     */
    public boolean checkAnswers()
    {
        String[][] answers = new String[answerBoxes.length][answerBoxes[0].length];
        for (int row = 0; row < answerBoxes.length; row++)
        {
            for (int col = 0; col < answerBoxes[row].length; col++)
            {
                answers[row][col] = answerBoxes[row][col].getText();
            }
        }
        if (getFocus().checkForms(answers))
        {
            check = 0;
            dialog.setText(" Press Grade when ready ");
            return true;
        } else
        {
            String[][] errors = getFocus().wrongForms(answers);
            if (check == 0) //Only the first try gives the amount of wrong answers
            {
                int wrong = 0;
                for (String[] a : errors)
                {
                    for (String s : a)
                    {
                        if (s != null)
                            wrong++;
                    }
                }
                dialog.setText(" " + wrong + " wrong answers ");
            } else if (check > 0) //After first try the wrong answers are highlighted, and the wrong count is still updated
            {
                int wrong = 0;
                for (int row = 0; row < answerBoxes.length; row++)
                {
                    for (int col = 0; col < answerBoxes[row].length; col++)
                    {
                        if (errors[row][col] != null)
                        {
                            wrong++;
                            answerBoxes[row][col].setBackground(Color.PINK);
                        }
                        else
                            answerBoxes[row][col].setBackground(Color.WHITE);
                        dialog.setText(" " + wrong + " wrong answers ");
                    }
                }
            }
            if (check > 4) //After the fourth try the wrong answers are corrected
            {
                for (int row = 0; row < answerBoxes.length; row++)
                {
                    for (int col = 0; col < answerBoxes[row].length; col++)
                    {
                        if (errors[row][col] != null)
                        {
                            answerBoxes[row][col].setBackground(Color.YELLOW);
                            answerBoxes[row][col].setText(errors[row][col]);
                        }
                    }
                }
            }
            check++;
        }
        return false;
    }
}