import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;




public class ImportFile extends JFrame implements ActionListener
{
   private JFileChooser jfc = new JFileChooser();
   private JButton jbt_open = new JButton("Open");
   private JButton jbt_save = new JButton("Save");
   private JLabel jlb = new JLabel(" "); 
          
   public ImportFile()
   {
      super("Open");
      this.init();
      this.start();
      this.setSize(400,200);
      this.setVisible(true);
   }
   
   public void init()
   {
      getContentPane().setLayout(new FlowLayout());
      add(jbt_open);
      add(jbt_save);
      add(jlb);
   }
   
   public void start()
   {
      jbt_open.addActionListener(this);
      jbt_save.addActionListener(this);
      jfc.setFileFilter(new FileNameExtensionFilter("txt", "txt"));
      jfc.setMultiSelectionEnabled(false);
   }
        
   @Override
       public void actionPerformed(ActionEvent arg0)
   {
      if(arg0.getSource() == jbt_open)
      {
         if(jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
         {
            jlb.setText("Open Path : " + jfc.getSelectedFile().toString());
         }
      }
      else if(arg0.getSource() == jbt_save)
      {
         if(jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
         {
            jlb.setText("Save Path : " + jfc.getSelectedFile().toString() + "." + jfc.getFileFilter().getDescription());
         }
      }
   }
       
}