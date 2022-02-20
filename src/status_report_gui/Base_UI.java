import javax.swing.event.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;

public class Base_UI {
   public static void main(String[] args) throws Exception {
      //Creating the Frame
      JFrame frame = new JFrame("5 Axis Slicer");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(800, 400);
      
      //test text area for print parms
      JTextArea out = new JTextArea();
      
      JPanel text_panel = new JPanel(); // the panel is not visible in output
      
      JLabel n_t_label = new JLabel("Nozzle Temperature");
      JTextField n_t_tf = new JTextField(20);
      
      JLabel s_t_label = new JLabel("Print Surface Temperature");
      JTextField s_t_tf = new JTextField(20);
      
      JLabel p_s_label = new JLabel("Print Speed");
      JTextField p_s_tf = new JTextField(20);
      
      JButton submit = new JButton("Submit");
      
      // Components Added using Flow Layout
      text_panel.add(n_t_label); 
      text_panel.add(n_t_tf);
      
      text_panel.add(s_t_label); 
      text_panel.add(s_t_tf);
      
      text_panel.add(p_s_label); 
      text_panel.add(p_s_tf);
      
      text_panel.add(submit);
      
      //Adding Components to the frame.
      frame.getContentPane().add(BorderLayout.WEST, text_panel);
      frame.getContentPane().add(BorderLayout.EAST, out);
      frame.setVisible(true);
   }
}