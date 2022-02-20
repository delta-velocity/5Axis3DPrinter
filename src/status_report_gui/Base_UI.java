import javax.swing.event.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;

public class Base_UI {
   public static void main(String[] args) throws Exception {
      //Creating the Frame
      JFrame frame = new JFrame("5 Axis Slicer");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(800, 400);
      
      //test text area for print parms
      JTextArea out = new JTextArea();
      
      JPanel parms_panel = new JPanel(); // main panel for parameter selections
      parms_panel.setLayout(new GridLayout(0, 1));
      
      //file import panel goes here
      
      JPanel n_temp_panel = new JPanel();
      JLabel n_t_label = new JLabel("Nozzle Temperature");
      JTextField n_t_tf = new JTextField(20);
      
      JPanel s_temp_panel = new JPanel();
      JLabel s_t_label = new JLabel("Print Surface Temperature");
      JTextField s_t_tf = new JTextField(20);
      
      JPanel p_speed_panel = new JPanel();
      JLabel p_s_label = new JLabel("Print Speed");
      JTextField p_s_tf = new JTextField(20);
      
      JPanel i_pattern_panel = new JPanel();
      JLabel i_p_label = new JLabel("Infill Pattern");
      String[] i_p_options = {"Cross Lattice", "Square Lattice"};
      JComboBox<String> i_p_cb = new JComboBox<String>(i_p_options);
      
      JPanel i_density_panel = new JPanel();
      JLabel i_d_label = new JLabel("Infill Density");
      JTextField i_d_tf = new JTextField(20);
      
      JButton submit = new JButton("Ready Print");
      
      // Handling Button Click Event
      submit.addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  String theText = "Selected Parameters:"
                     + "\n" + n_t_label.getText() + ": " + n_t_tf.getText()
                     + "\n" + p_s_label.getText() + ": " + p_s_tf.getText()
                     + "\n" + i_p_label.getText() + ": " + i_p_options[i_p_cb.getSelectedIndex()]
                     + "\n" + i_d_label.getText() + ": " + i_d_tf.getText();
                  out.setText(theText);
               }
            });
      
      // Components Added using Flow Layout within each panel...
      
      //file import panel goes here
      
      n_temp_panel.add(n_t_label); 
      n_temp_panel.add(n_t_tf);
      
      s_temp_panel.add(s_t_label); 
      s_temp_panel.add(s_t_tf);
      
      p_speed_panel.add(p_s_label); 
      p_speed_panel.add(p_s_tf);
      
      i_pattern_panel.add(i_p_label); 
      i_pattern_panel.add(i_p_cb);
      
      i_density_panel.add(i_d_label); 
      i_density_panel.add(i_d_tf);
      
      //individual panels added to big parms panel as GridLayout
      
      //file import panel goes here
      parms_panel.add(n_temp_panel);
      parms_panel.add(s_temp_panel);
      parms_panel.add(p_speed_panel);
      parms_panel.add(i_pattern_panel);
      parms_panel.add(i_density_panel);
      parms_panel.add(submit);
      
      //Adding Components to the frame in BorderLayout
      frame.getContentPane().add(BorderLayout.WEST, parms_panel);
      frame.getContentPane().add(BorderLayout.EAST, out);
      frame.setVisible(true);
   }
}