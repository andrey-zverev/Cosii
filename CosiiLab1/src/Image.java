import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.*;
import org.opencv.imgproc.*;;

public class Image 
{
    public Mat image;
    public Image(String path)
    {
    	
    	System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
    	image = new Mat();
    	Imgproc.cvtColor(Highgui.imread(path), image, Imgproc.COLOR_BGR2GRAY);
    }
    
    public String toString()
    {
    	return "Width - " + image.width() + "\nHeight - " + image.height() + "\nChannels - " + image.channels();
    }
    
    public void saveImage(String path)
    {
    	Highgui.imwrite(path, image);
    }
    
    public double[] histogramArray()
    {
    	double[] histogram = new double[256];
    	for(int i = 0; i< image.height();i++) 
    	{
    		for(int j = 0; j< image.width();j++) 
    		{
    			histogram[(int)image.get(i, j)[0]] += 1;
    		}
    	}
    	return histogram;
    }
    
    public Mat imageSymbolConvert()
    {
    	Mat convert = new Mat();
    	image.copyTo(convert);
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("¬ведите gMin");
    	int gMin = scanner.nextInt();
    	System.out.println("¬ведите gMax");
    	int gMax = scanner.nextInt();
    	System.out.println("¬ведите fMin");
    	int fMin = scanner.nextInt();
    	System.out.println("¬ведите fMax");
    	int fMax = scanner.nextInt();
    
    	for(int i = 0; i < convert.height(); i++)
    	{
    		for(int j = 0; j< convert.width(); j++)
    		{
    			if((int)image.get(i, j)[0] < fMin)
    				convert.put(i, j, gMin);
    			else if((int)image.get(i, j)[0] > fMax)
    				convert.put(i, j, gMax);
    			else if(((int)image.get(i,j)[0] > fMin)&&((int)image.get(i,j)[0] < fMax))
    			{
    				int b = ((int)image.get(i, j)[0] - fMin)/(fMax - fMin);
    				b = b * (gMax - gMin) + gMin;
    				if(b>255) b = 255;
    				if(b < 0) b = 0;
    				convert.put(i, j, b);
    			}
    		}
    	}
    	return convert;
    }
    
    public Mat lowFilter()
    {
    	Mat resultMat = new Mat();
    	
    	image.copyTo(resultMat);
    	for(int i = 1; i <  resultMat.height() - 1; i++)
    	{
    		for(int j = 1; j< resultMat.width() - 1; j++)
    		{
    			int pixel = (int)image.get(i - 1, j - 1)[0] + (int)image.get(i - 1, j)[0] + (int)image.get(i - 1, j + 1)[0] +
    					(int)image.get(i, j - 1)[0] + (int)image.get(i, j)[0] + (int)image.get(i, j + 1)[0]+
    					(int)image.get(i + 1, j - 1)[0] + (int)image.get(i + 1, j)[0] + (int)image.get(i + 1, j + 1)[0];
    			pixel /= 9;
    			if (pixel>255) pixel = 255;
    			if (pixel <0 ) pixel = 0;
    			resultMat.put(i,j,pixel);
    		}
    	}
    	return resultMat;
    }
    
    public Mat filterImageMin()
    {
    	Mat resultMat = new Mat();
    	image.copyTo(resultMat);
    	Highgui.imwrite("D:\\\\Projects\\\\CosiiLab1\\\\img\\\\test.jpg", resultMat);
    	int k = 0;
    	for(int i = 1; i < resultMat.height() - 1; i++)
    	{
    		for(int j = 1; j< resultMat.width() - 1; j++)
    		{ 		
    			int a1 = (int)image.get(i - 1, j - 1)[0];
    			int a2 = (int)image.get(i - 1, j)[0];
    			int a3 = (int)image.get(i - 1, j + 1)[0];
    			int a4 = (int)image.get(i, j - 1)[0];
    			int a5 = (int)image.get(i, j + 1)[0];
    			int a6 = (int)image.get(i + 1, j - 1)[0];
    			int a7 = (int)image.get(i + 1, j)[0];
    			int a8 = (int)image.get(i + 1, j + 1)[0];
    			
    			int amin = Integer.min(a1, a2);
    			amin = Integer.min(amin, a3);
    			amin = Integer.min(amin, a4);
    			amin = Integer.min(amin, a5);
    			amin = Integer.min(amin, a6);
    			amin = Integer.min(amin, a7);
    			amin = Integer.min(amin, a8);
    			
    			resultMat.put(i, j, amin);
    		}
    	}
    	return resultMat;
    }
    
    public Mat filterImageMax()
    {
    	Mat resultMat = new Mat();
    	image.copyTo(resultMat);
    	Highgui.imwrite("D:\\\\Projects\\\\CosiiLab1\\\\img\\\\test.jpg", resultMat);
    	int k = 0;
    	for(int i = 1; i < resultMat.height() - 1; i++)
    	{
    		for(int j = 1; j< resultMat.width() - 1; j++)
    		{ 		
    			int a1 = (int)image.get(i - 1, j - 1)[0];
    			int a2 = (int)image.get(i - 1, j)[0];
    			int a3 = (int)image.get(i - 1, j + 1)[0];
    			int a4 = (int)image.get(i, j - 1)[0];
    			int a5 = (int)image.get(i, j + 1)[0];
    			int a6 = (int)image.get(i + 1, j - 1)[0];
    			int a7 = (int)image.get(i + 1, j)[0];
    			int a8 = (int)image.get(i + 1, j + 1)[0];
    			
    			int amin = Integer.max(a1, a2);
    			amin = Integer.max(amin, a3);
    			amin = Integer.max(amin, a4);
    			amin = Integer.max(amin, a5);
    			amin = Integer.max(amin, a6);
    			amin = Integer.max(amin, a7);
    			amin = Integer.max(amin, a8);
    			
    			resultMat.put(i, j, amin);
    		}
    	}
    	return resultMat;
    }
    
    public Mat userFilter()
    {
    	Mat user = image;
    	for(int i = 0; i < user.height() - 1; i++)
    	{
    		for(int j = 0; j < user.width() - 1; j++)
    		{
    			int g1 = (int)user.get(i, j)[0] + 60;
    			if(g1>=256) g1=255;
    			user.put(i, j, g1);
    		}
    	}
    	return user;
    }
}
