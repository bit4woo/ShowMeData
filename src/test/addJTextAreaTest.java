package test;
/** 
* @author bit4woo
* @github https://github.com/bit4woo 
* @version CreateTime：Jul 18, 2020 7:09:51 AM 
*/
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
//  ww  w . j  a  v a 2  s .  c  o m
public class addJTextAreaTest {
  public static void main(String[] args) {
    JFrame f = new JFrame("Text Area Examples");
    //JScrollPane xxupperPanel = new JScrollPane();
    JScrollPane upperPanel = new JScrollPane();
    //xxupperPanel.add(upperPanel);
    JPanel lowerPanel = new JPanel();
    f.getContentPane().add(upperPanel, "North");
    f.getContentPane().add(lowerPanel, "South");

    upperPanel.add(new JTextArea(content));
    upperPanel.add(new JTextArea(content, 6, 10));
    upperPanel.add(new JTextArea(content, 3, 8));
    upperPanel.add(new JScrollPane(new JTextArea(content, 60, 80)));

    lowerPanel.add(new JScrollPane(new JTextArea(content, 60, 80)));
    JTextArea ta = new JTextArea(content, 6, 8);
    ta.setLineWrap(true);
    lowerPanel.add(new JScrollPane(ta));
    lowerPanel.add(new JScrollPane(ta));
    lowerPanel.add(new JScrollPane(ta));
    lowerPanel.add(new JScrollPane(ta));
    lowerPanel.add(new JScrollPane(ta));
    lowerPanel.add(new JScrollPane(ta));
    lowerPanel.add(new JScrollPane(ta));
    lowerPanel.add(new JScrollPane(ta));
    lowerPanel.add(new JScrollPane(ta));
    

    ta = new JTextArea(content, 6, 8);
    ta.setLineWrap(true);
    ta.setWrapStyleWord(true);
    lowerPanel.add(new JScrollPane(ta));

    f.pack();
    f.setVisible(true);
  }

  static String content = "Here men from the planet Earth\n"
      + "first set foot upon the Moon,\n" + "July 1969, AD.\n"
      + "We came in peace for all mankind.";
}