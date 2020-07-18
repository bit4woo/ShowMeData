package test;
/** 
* @author bit4woo
* @github https://github.com/bit4woo 
* @version CreateTime：Jul 18, 2020 10:01:05 AM 
*/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TextPanel());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TextPanel extends JPanel {

        private InputPane input;
        private JScrollPane scrollPane;

        public TextPanel() {

            setLayout(new GridBagLayout());

            input = new InputPane();
            scrollPane = new JScrollPane(input);

            GridBagConstraints gc = new GridBagConstraints();

            gc.gridx = 0;
            gc.gridy = 0;
            gc.weightx = 0.25;

            add(scrollPane, gc);

        }
    }

    public class InputPane extends JPanel {

        private JTextArea inputText;
        private JTextArea lineNumber;

        public InputPane() {

            setLayout(new BorderLayout());

            setBorder(BorderFactory.createTitledBorder("Original File"));

            inputText = new JTextArea(10, 20);
            lineNumber = new JTextArea(10, 4);

            lineNumber.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.gray));

            add(inputText);
            add(lineNumber, BorderLayout.WEST);

        }
    }
}