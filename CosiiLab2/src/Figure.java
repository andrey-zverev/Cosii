import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class Figure 
{
	public int square;
	public int perimetr;
	public int[] centrOfMass;
	public double compact;
	public int[][] coord;
	public int number;
	public double elongation;
	
	public Figure(int number, int square, int perimetr, int[] centrOfMass, double compact, int[][] coord)
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		this.number = number;
		this.square = square;
		this.perimetr = perimetr;
		this.centrOfMass = centrOfMass;
		this.compact = compact;
		this.coord = coord;
		elongation = findElongation(); 
	}
	public String toString()
	{
		return "number - " + number +"\nSquare - "+square+"\nPerimetr - " +perimetr
				+"\nCentr of Mass - " + centrOfMass[0] + " ; " + centrOfMass[1] +
				"\nCompact - " + compact + "\nElongation - " + elongation;
	}
	
	public Mat print(Mat image, int n, int k)
	{
		Mat img = new Mat();
		int color = 256 / n;
		color = color * k;
		if(k == n) color = 255;
		image.copyTo(img);
		for(int i = 0; i<square; i++) 
		{
			img.put(coord[i][0], coord[i][1], color);
		}
		return img;
	}
	
	public void printAllParams(Mat image)
	{
		Mat mat = new Mat();
		image.copyTo(mat);
		for(int i = 0; i < mat.height(); i++)
			for(int j = 0; j < mat.width(); j++)
				{
					mat.put(i, j, 0);
				}
		for(int i = 0; i < square; i++)
		{
			if((int)image.get(coord[i][0], coord[i][1])[0] == 255)
				if(((int)image.get(coord[i][0] - 1, coord[i][1])[0] == 0)
						||((int)image.get(coord[i][0] + 1, coord[i][1])[0] == 0)
						||((int)image.get(coord[i][0], coord[i][1]- 1)[0] == 0)
						||((int)image.get(coord[i][0], coord[i][1]+ 1)[0] == 0))
					
					mat.put(coord[i][0], coord[i][1], 128);
		}

		mat.put(centrOfMass[0], centrOfMass[1], 255);
		Highgui.imwrite("D://Projects/CosiiLab2/img/object.jpg", mat);
	}

	public int discretMoment(int n, int m)
	{
		int disM = 0;
		for(int i = 0; i < square; i++)
			disM += Math.pow(coord[i][0] - centrOfMass[0], n)*Math.pow(coord[i][1] - centrOfMass[1], m);
		return disM;
	}

	public double findElongation()
	{
		int m02 = discretMoment(0, 2);
		int m11 = discretMoment(1, 1);
		int m20 = discretMoment(2, 0);
		
		double a = m20 + m02 + Math.sqrt(Math.pow((m20 - m02), 2) + 4*Math.pow(m11, 2));
		double b = m20 + m02 - Math.sqrt(Math.pow((m20 - m02), 2) + 4*Math.pow(m11, 2));
		return a/b;
	}
}
