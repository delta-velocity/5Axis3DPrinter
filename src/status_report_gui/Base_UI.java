import javax.swing.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.Vector;
import java.util.HashMap;

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
      
      JPanel model_panel = new JPanel();
      JButton model_button = new JButton("Choose Model");
      JLabel model_label = new JLabel("None");
      
      model_panel.add(model_button);
      model_panel.add(model_label);
      
      FileHolder chosen_model = new FileHolder();
      
      model_button.addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  JFileChooser jfc = new JFileChooser();
                  jfc.setFileFilter(new FileNameExtensionFilter("stl", "stl"));
                  jfc.setMultiSelectionEnabled(false);
               
                  int choice = jfc.showOpenDialog(null);
               
                  if (choice == JFileChooser.APPROVE_OPTION) {
                     chosen_model.setFile(jfc.getSelectedFile());
                     model_label.setText(chosen_model.getFile().toString());
                  }
               }
            });
            
      Path parm_path = Path.of("parameters.txt");
      String parm_string = Files.readString(parm_path);
      
      Vector<JPanel> panels = new Vector<JPanel>();
      HashMap<String, double[]> constraints = new HashMap<String, double[]>();
      
      //read parameters from file
      Scanner parm_scan = new Scanner(parm_string);
      while (parm_scan.hasNextLine()) {
         JPanel new_panel = new JPanel();
         
         String label_text = parm_scan.nextLine();
         JLabel new_label = new JLabel(label_text);
         new_panel.add(new_label);
         
         String type_text = parm_scan.nextLine();
         
         if (type_text.equalsIgnoreCase("Number")) {
            JTextField new_tf = new JTextField(20);
            new_panel.add(new_tf);
            
            double[] bounds = new double[2];
            String lower = parm_scan.nextLine();
            bounds[0] = Double.parseDouble(lower);
            String upper = parm_scan.nextLine();
            bounds[1] = Double.parseDouble(upper);
            
            constraints.put(label_text, bounds);
         }
         else if (type_text.equalsIgnoreCase("List")) {
            Vector<String> options = new Vector<String>();
            options.add("Select"); //default, always first
            
            do {
               String list_item = parm_scan.nextLine();
               if (list_item.equals("#")) {
                  break;
               }
            //else
               options.add(list_item);
            }
            while (parm_scan.hasNextLine());
            
            JComboBox<String> new_cb = new JComboBox<String>(options);
            new_panel.add(new_cb);
         }
         
         panels.add(new_panel);
      }
      parm_scan.close();
      
      JButton submit = new JButton("Ready Print");
      
      // Handling Button Click Event for submission
      submit.addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  String theText = "Selected Parameters:\nModel: ";
                  
                  if (chosen_model.getFile() != null) {
                     theText += chosen_model.getFile().getAbsolutePath();
                  }
                  else {
                     theText += "None";
                  }
                  
                  HashMap<String, String> inputs = getBoundedInputsFromPanelsAndConstraints(panels, constraints);
                  
                  for (String label_text : inputs.keySet()) {
                     String choice_text = inputs.get(label_text);
                     
                     theText += "\n" + label_text + ": " + choice_text;
                  }
                  
                  out.setText(theText);
               }
            });
      
      //individual panels added to big parms panel as GridLayout
      parms_panel.add(model_panel);
      
      for (JPanel panel : panels) {
         parms_panel.add(panel);
      }  
      
      parms_panel.add(submit);
      JButton test = new JButton("test");
      
      test.addActionListener(
            new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  HashMap<String, String> test_hash = new HashMap<String, String>();
                  test_hash.put("Nozzle Temperature", "400");
                  test_hash.put("Model", "C:\\Users\\fowle\\Desktop\\spring_2022\\senior_design\\ASCII-Type1.STL");
               
                  setPanelsFromHashMap(panels, model_label, chosen_model, test_hash);
               }
            });
      
      parms_panel.add(test);
      
      //Adding Components to the frame in BorderLayout
      frame.getContentPane().add(BorderLayout.WEST, parms_panel);
      frame.getContentPane().add(BorderLayout.EAST, out);
      frame.setVisible(true);
   }
   
   public static void setPanelsFromHashMap(Vector<JPanel> panels, JLabel model_label, FileHolder chosen_model, HashMap<String, String> inputs) {
      for (JPanel panel : panels) {
         Component[] comps = panel.getComponents();
         
         String label_text = "";
                     
         for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JLabel) {
               JLabel the_label = (JLabel) comps[i];
               label_text = the_label.getText();
            }
         }
         
         String choice_text = inputs.get(label_text);
                     
         for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JTextField) {
               JTextField the_tf = (JTextField) comps[i];
               the_tf.setText(choice_text);
            }
            else if (comps[i] instanceof JComboBox) {
               JComboBox the_cb = (JComboBox) comps[i];
               
               //set selection to given
               for (int index = 0; index < the_cb.getItemCount(); index++) {
                  String test_str = the_cb.getItemAt(index).toString();
                  if (test_str.equals(choice_text)) {
                     the_cb.setSelectedIndex(index);
                     break;
                  }
                  else {
                     the_cb.setSelectedIndex(0); //defualt, "Select"
                  }
               }
            }
         }
         
         String file_path_as_string = inputs.get("Model");
         if (file_path_as_string != null) {
            String label_string = "";
            
            File the_file = new File(file_path_as_string);
         
            if (the_file.exists()) {
               chosen_model.setFile(the_file);
               label_string = file_path_as_string;
            }
            else {
               label_string = "None";
            }
         
            model_label.setText(label_string);
         }
      }
   }
   
   public static HashMap<String, String> getBoundedInputsFromPanelsAndConstraints(Vector<JPanel> panels, HashMap<String, double[]> constraints) {
      HashMap<String, String> inputs = new HashMap<String, String>();
      
      for (JPanel panel : panels) {
         Component[] comps = panel.getComponents();
                     
         String label_text = "";
         String choice_text = "";
                     
         for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JLabel) {
               JLabel the_label = (JLabel) comps[i];
               label_text = the_label.getText();
            }
         }
                     
         for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JTextField) {
               JTextField the_tf = (JTextField) comps[i];
               choice_text = the_tf.getText();
               
               double[] bounds = constraints.get(label_text);
               double entered_number;
               
               try {
                  entered_number = Double.parseDouble(choice_text);
                  if (entered_number < bounds[0]) {
                     entered_number = bounds[0];
                  }
                  else if (entered_number > bounds[1]) {
                     entered_number = bounds[1];
                  }
               }
               catch (NumberFormatException e) {
                  //not a number
                  entered_number = bounds[0];
               }
               the_tf.setText(Double.toString(entered_number));
               choice_text = the_tf.getText();
            }
            else if (comps[i] instanceof JComboBox) {
               JComboBox the_cb = (JComboBox) comps[i];
               choice_text = the_cb.getSelectedItem().toString();
            }
         }
         
         inputs.put(label_text, choice_text);
      }
   
      return inputs;
   }
}