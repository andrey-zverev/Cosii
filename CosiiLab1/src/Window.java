import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class Window {

	private JFrame frmFirstPicture;
	public void showWindow(String path, double[] histogram) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Window window = new Window(path, histogram);
					window.frmFirstPicture.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	public Window(String path,double[] histogram) {
		initialize(path, histogram);
	}

	private void initialize(String path, double[] histogram) {
		frmFirstPicture = new JFrame();
		frmFirstPicture.getContentPane().setEnabled(false);
		frmFirstPicture.setTitle("First picture");
		frmFirstPicture.setBounds(100, 100, 1200, 900);
		frmFirstPicture.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFirstPicture.getContentPane().add(tabbedPane, BorderLayout.NORTH);
		
		
		JPanel firstPanel = initPanel(path);
		tabbedPane.add("First", firstPanel);
		
		JPanel secondPanel = new JPanel();
		JFreeChart chart = createChart(histogram);
		ChartPanel CP = new ChartPanel(chart);
		CP.setPreferredSize(new Dimension(1200,900));
		secondPanel.add(CP);
		tabbedPane.add("Second", secondPanel);
	}
	
	public JPanel initPanel(String path)
	{
		BufferedImage myPicture = null; //load image
		try {
			myPicture = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JPanel panel = new JPanel(); //new panel
		panel.setBounds(100, 100, myPicture.getWidth(), myPicture.getHeight());
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		panel.add(picLabel);
		return panel;
	}
	
	public JFreeChart createChart(double[] histogram) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int i = 0; i < 256; i++)
			dataset.addValue(histogram[i], "1", String.valueOf(i));
		
        JFreeChart chart = ChartFactory.createBarChart("яркость", "x", "y" , dataset, PlotOrientation.VERTICAL, true, true, false);
 
        return chart;

    }
}
