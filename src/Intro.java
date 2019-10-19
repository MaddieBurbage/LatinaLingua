import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Intro
{
    /** The title for the GUI */
    private static final String name = "Start Latina";
    /** The Dimension for each space-filling Rigid Area */
    private static final Dimension spacer = new Dimension(450, 10);
    /** The intro screen JFrame created */
    private static JFrame introScreen;

    /**
     * Starts the program by running the intro screen
     * @param args The command line args
     */
    public static void main(String[] args)
    {
        new Intro();
    }

    /**
     * Creates an intro screen
     */
    public Intro()
    {
        JComboBox choosePOS = new JComboBox(POS.values());
        choosePOS.setMaximumSize(choosePOS.getPreferredSize());
        choosePOS.setAlignmentX(Component.CENTER_ALIGNMENT);
        choosePOS.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Director.play( (POS) choosePOS.getSelectedItem());
                introScreen.dispose();
            }
        });
        introScreen = new JFrame(name);
        introScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        introScreen.setLayout(new BoxLayout(introScreen.getContentPane(), BoxLayout.Y_AXIS));
        JLabel first = new JLabel("Welcome to Latina!");
        first.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel second = new JLabel("To play, choose what to study. If this is your first time, then press Help.");
        second.setAlignmentX(Component.CENTER_ALIGNMENT);
        introScreen.add(Box.createRigidArea(spacer));
        introScreen.add(first);
        introScreen.add(Box.createRigidArea(spacer));
        introScreen.add(second);
        introScreen.add(Box.createRigidArea(spacer));
        introScreen.add(choosePOS);
        introScreen.add(Box.createRigidArea(spacer));
        introScreen.pack();
        introScreen.setLocationRelativeTo(null); //http://stackoverflow.com/questions/2442599/how-to-set-jframe-to-appear-centered-regardless-of-monitor-resolution
        introScreen.setVisible(true);
    }
}
