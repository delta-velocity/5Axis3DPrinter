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




public class ImportFile extends JFrame implements ActionListener
{
   private JFileChooser jfc = new JFileChooser();
   private JButton jbt_save = new JButton("Save");
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
      add(jbt_save);
      add(jbt_project);
      add(jlb);
   }
   
   public void start()
   {
      jbt_save.addActionListener(this);
      jbt_project.addActionListener(this);
      jfc.setFileFilter(new FileNameExtensionFilter("txt", "txt"));
      jfc.setMultiSelectionEnabled(false);
   }
        
   @Override
       public void actionPerformed(ActionEvent arg0)
   {
     // It updates the saved project file.
      if(arg0.getSource() == jbt_save)
      {
         if(jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
         {
            jlb.setText("Save Path : " + jfc.getSelectedFile().toString() + "." + jfc.getFileFilter().getDescription());
            String path = jfc.getSelectedFile().toString() + "." + jfc.getFileFilter().getDescription();
            jlb.setText("Project File Path :" + path);
            
            try{
               PrintWriter fw = new PrintWriter(path);
               String data = "";
               fw.println(data);
               fw.close();
            }
            catch (Exception exp){
               exp.printStackTrace();
               JOptionPane.showMessageDialog(this, "Failed to create profile", "Error", JOptionPane.ERROR_MESSAGE);
            }
         }
      }
      
      else if(arg0.getSource() == jbt_project)
      {
         if(jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
         {
            String path = jfc.getSelectedFile().toString() + "." + jfc.getFileFilter().getDescription();
            jlb.setText("Project File Path :" + path);
            
            try{
               PrintWriter fw = new PrintWriter(path);
               String data = "";
               fw.println(data);
               fw.close();
            }
            catch (Exception exp){
               exp.printStackTrace();
               JOptionPane.showMessageDialog(this, "Failed to create profile", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
         }
      }
   }
       
}