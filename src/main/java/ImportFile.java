import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.io.IOException;




public class ImportFile extends JFrame implements ActionListener
{
   private JFileChooser jfc = new JFileChooser();
   private JButton jbt_project = new JButton("Create a New Project");
   private JLabel jlb = new JLabel(" "); 
  
          
   public ImportFile()
   {
      super("Open");
      this.init();
      this.start();
      this.setSize(400,100);
      this.setVisible(true);
   }
   
   public void init()
   {
      getContentPane().setLayout(new FlowLayout());
      add(jbt_project);
      add(jlb);
   }
   
   public void start()
   {
      jbt_project.addActionListener(this);
      jfc.setFileFilter(new FileNameExtensionFilter("txt", "txt"));
      jfc.setMultiSelectionEnabled(false);
   }
   
   
   public void configure(HashMap<String, String> inputs, String path)
   {
      try{
         PrintWriter fw = new PrintWriter(path);
         String theText = "";
         for (String label_text : inputs.keySet()) 
         {
            String choice_text = inputs.get(label_text);   
            theText += label_text + "\n" + choice_text + "\n";
         }
         fw.println(theText);
         fw.close();
      }
      catch (Exception exp){
         exp.printStackTrace();
         
      }
   }
   
   public HashMap<String, String> load(String path) throws IOException
   {
      Scanner scanner = new Scanner(new File(path));
      int i = 0;
      String label = "";
      String value = "";
      HashMap<String, String> inputs = new HashMap<String, String>();
      while (scanner.hasNext()) 
      {  
         if (i % 2 == 0)
         {
            label = scanner.next();
         }
         else
         {
            value = scanner.next();
            inputs.put(label, value);
         }
      }
      return inputs;
   }
   
  
   @Override
       public void actionPerformed(ActionEvent arg0)
   {
     // It saves a project file.
              
      if(arg0.getSource() == jbt_project)
      {
         if(jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
         {
            String path = jfc.getSelectedFile().toString() + "." + jfc.getFileFilter().getDescription();
            jlb.setText("Project File Path :" + path);
            //This is a test HashMap
            HashMap<String, String> inputs = new HashMap<String, String>();
            inputs.put("Song about Christmas is Chrismas Song", "Folwer Scott Walker agrees"); 
            inputs.put("Nice", "Finally");
            this.configure(inputs, path);
                           
         }
         
      }
      
      // It loads the saved project file.
   }
   
       
}