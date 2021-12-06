import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Image 
{
	public Mat img;
	public Mat image;
	public int l;
	public int newL;
	public int[][] labels;
	public int[][] newLabels;
	public int[] comparing;
	public int[][] arr;
	int width;
	int height;
	public ArrayList<ArrayList<Integer>> cluster;
	public List<Figure> figures;
	
	public Image(String path)
	{
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
    	img = new Mat();
    	image = new Mat();
    	Imgproc.cvtColor(Highgui.imread(path), img, Imgproc.COLOR_BGR2GRAY);
    	Highgui.imwrite("D://Projects/CosiiLab2/img/gray.jpg", img);
    	img.copyTo(image);
    	width = image.width();
    	height = image.height();
    	int k;
    	if(path == "D://Projects/CosiiLab2/img/P0001467.jpg")
    		k = 215;
    	else if(path == "D://Projects/CosiiLab2/img/P0001465.jpg")
    		k = 223;
    	else k = 200;
		binare(k);
		
		arr = createArr(image);
    	labeling(image);
    	labels = arr;

    	l = newIndexFigure();
    	mathFigures();
    	figures.get(3).printAllParams(image);
    	
    	System.out.println("¬ведите уже кол-во кластеров");
    	try (
		Scanner scanner = new Scanner(System.in)) {
			int noch = scanner.nextInt();
			clusterisation(noch);
		}
    	for(int i = 0; i < cluster.size(); i++)
    	{
    		for(int j = 0; j < cluster.get(i).size(); j++)
    			img = figures.get(cluster.get(i).get(j)).print(img, cluster.size(), i);
    	}
		Highgui.imwrite("D://Projects/CosiiLab2/img/lastone.jpg", img);
    }
	
	public int newIndexFigure()
	{
		int n = 0;
		Map<Integer, Integer> keys = new HashMap<Integer, Integer>();
		keys.put(0,0);
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(!keys.containsKey(labels[i][j])) {
					n++;
					keys.put(labels[i][j], n);
				}
			}
		}
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				labels[i][j] = keys.get(labels[i][j]);
			}
		}
		System.out.println("------------------------" + n + "-------------------------");
		return n;
	}
	
	public void mathFigures() 
	{
		int[] squares = findSquares();
		int[] perimetres = findPerimetres();
		int[][] centrOfMass = findCentrOfMass(squares);
		double[] compact = findCompactness(perimetres, squares);
    	List<int[][]> coord= coordFigures(squares);
    	figures = new ArrayList<>();
    	for(int i = 0; i < l; i++)
    	{
    		figures.add(new Figure(i, squares[i], perimetres[i], centrOfMass[i], compact[i], coord.get(i)));
    	}
	}
	
	public void binare(int k)
	{
    	Highgui.imwrite("D://Projects/CosiiLab2/img/high.jpg", image);
    	Imgproc.GaussianBlur(image, image, new Size(17, 17), 0);
    	highFilter();
   
      	Imgproc.threshold(image, image, k, 255, Imgproc.THRESH_BINARY);
      	
    	Highgui.imwrite("D://Projects/CosiiLab2/img/binary.jpg", image);
	}
	
	public void highFilter()
	{
		Mat buffer = new Mat();
		buffer = image;
		for(int i = 1; i < buffer.height() - 1; i++)
			{
				for(int j = 1; j < buffer.width() - 1; j++)
				{
					int a = (int)image.get(i, j)[0] + ((8 * ((int)image.get(i, j)[0]))- ((int)image.get(i - 1, j)[0])- ((int)image.get(i, j - 1)[0])- ((int)image.get(i, j + 1)[0])- ((int)image.get(i + 1, j)[0])
							- ((int)image.get(i - 1, j - 1)[0])- ((int)image.get(i - 1, j + 1)[0])- ((int)image.get(i + 1, j - 1)[0])- ((int)image.get(i + 1, j + 1)[0])) / 9;
					if(a > 255) a = 255;
					if(a < 1) a = 0;
					buffer.put(i, j, a);
				}
			}
    	image = buffer;
	}
	
	public void lowFilter()
	{
		Mat buffer = new Mat();
		buffer = image;
		for(int i = 1; i < buffer.height() - 1; i++)
		{
			for(int j = 1; j < buffer.width() - 1; j++)
			{
				int a = ((int)image.get(i - 1, j - 1)[0]) + ((int)image.get(i - 1, j)[0]) + ((int)image.get(i - 1, j + 1)[0])+
						((int)image.get(i, j - 1)[0]) +  ((int)image.get(i, j)[0]) + ((int)image.get(i, j + 1)[0])+
						((int)image.get(i + 1, j - 1)[0]) + ((int)image.get(i + 1, j)[0]) + ((int)image.get(i + 1, j + 1)[0]);
				a = a / 9;
				if(a > 255) a = 255;
				if(a < 1) a = 0;
				buffer.put(i, j, a);
			}
		}
	image = buffer;
	}
	
	public int[][] createArr(Mat image) {
		int arr[][] = new int[image.height()][image.width()];
		for(int i = 0; i < image.height(); i++) {
			for(int j = 0; j < image.width(); j++) {
				if((int)image.get(i, j)[0] < 128) arr[i][j] = 0;
				else arr[i][j] = 1;
			}
		}
		return arr;
	}
	
	public void labeling(Mat image) {
        int count = 0, i = 0 , j = 0;
        int B = 0, C = 0;
            for (i = 0; i < image.rows(); i++) {
                for (j = 0; j < image.cols(); j++) {
                    int q = 0;
                    if (i == 0)
                        B = arr[i][j];
                    else
                        B = arr[i - 1][j];
                    if (j == 0)
                        C = arr[i][j];
                    else
                        C = arr[i][j - 1];

                    if (arr[i][j] == 1) {
                        if (B == 0 && C == 0) {
                            count++;
                            arr[i][j] = count;
                        }
                        if ((B != 0 && C != 0) && B == C){
                            do {
                                arr[i][j-q] = B;
                                q++;
                            }while(arr[i][j-q] != 0);
                        }
                        if ((B != 0 && C != 0) && B != C)
                        {
                            do {
                                arr[i][j-q] = B;
                                q++;
                            }while(arr[i][j-q] != 0);
                        }
                        if (B != 0 && C == 0) {
                            arr[i][j] = B;
                        }
                        if (B == 0 && C != 0) {
                            arr[i][j] = C;
                        }
                    }
                }
            }
    }
	

	public int[] findSquares()
	{
		int[] squares = new int[l];
		int n = l;
		squares = new int[n];
		for(int i = 0; i < n; i++)
		{
			squares[i] = 0;
		}
		
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
			{
				if(labels[i][j] != 0)
					squares[labels[i][j] - 1] += 1;
			}
		return squares;
	}

	public int[] findPerimetres()
	{
		int[] perimetres = new int[l];
		int n = l;
		perimetres = new int[n];
		for(int i = 0; i < n; i++)
		{
			perimetres[i] = 0;
		}
		
		for(int i = 1; i < height - 1; i++)
			for(int j = 1; j < width - 1; j++)
			{
				if((labels[i][j] != 0)&&((labels[i-1][j-1] == 0)||(labels[i - 1][j] == 0)||(labels[i - 1][j + 1] == 0)
						||(labels[i][j - 1] == 0)||(labels[i][j + 1] == 0)
						||(labels[i + 1][j - 1] == 0)||(labels[i + 1][j] == 0)||(labels[i + 1][j + 1] == 0)))
					perimetres[labels[i][j] - 1] += 1;
			}
		return perimetres;
	}

	public int[][] findCentrOfMass(int[] squares)
	{
		int n = l;
		int[][] centrOfMass = new int[n][2];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < 2; j++)
				centrOfMass[i][j] = 0;
		
		int[] sumX = new int[n];
		int[] sumY = new int[n];
		
		for(int i = 0; i < n; i++)
		{
			sumX[i] = 0;
			sumY[i] = 0;
		}
		
		for(int i = 0; i<height; i++)
			for(int j = 0; j<width; j++)
			{
				if(labels[i][j] != 0)
				{
					sumX[labels[i][j] - 1] +=i;
					sumY[labels[i][j] - 1] +=j;
				}
			}
		
		for(int i = 0; i < n; i++)
		{
			centrOfMass[i][0] = sumX[i] /  squares[i];
			centrOfMass[i][1] = sumY[i] /  squares[i];
		}
		return centrOfMass;
	}

	public double[] findCompactness(int[] perimetres, int[] squares)
	{
		double[] compact = new double[l];
		for(int i = 0; i < l; i++)
		{
			compact[i] = (double)(perimetres[i] * perimetres[i]) / squares[i];
		}
		return compact;
	}

	public List<int[][]> coordFigures(int[] squares) 
	{
		List<int[][]>coord = new ArrayList<>();
		int[] k = new int[l];
		for(int i = 0; i < l; i++)
		{
			coord.add(new int[squares[i]][2]);
			k[i] = 0;
		}
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
			{
				int a = labels[i][j];
				if(a!=0)
				{
					int[][]mas = coord.get(a - 1);
					mas[k[a - 1]][0] = i;
					mas[k[a - 1]][1] = j;
					k[a - 1]++;
					coord.set(a - 1, mas);
				}
			}
		return coord;		
	}
	
	public void clusterisation(int n)
	{
		cluster = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < n; i++)//init cluster :0
		{
			cluster.add(new ArrayList<Integer>());
		}
		int[] centries = new int[n];
		int j = 0;
		
		while(j < n)//create random centries :)
			{
			int a = rnd(l);
			if (j ==0)
				{
					centries[j] = a;
					j++;
				}
			else if(check(centries, a) == 1)
				{
					centries[j] = a;
					j++;
				}
			}
		//hell is here
		int iterator = 0;
		while(iterator<10)
		{
			for(int i = 0; i < l; i++)
			{
				double min = Double.MAX_VALUE;
				int imin = -1;
				for(j = 0; j < n; j++)
				{
					double a = mathS(figures.get(i).perimetr, figures.get(centries[j]).perimetr,
							figures.get(i).square, figures.get(centries[j]).square,
							figures.get(i).compact, figures.get(centries[j]).compact,
							figures.get(i).elongation, figures.get(centries[j]).elongation);
					if(Double.compare(a , min ) < 0)
						{
							min = a;
							imin = j;
						}
				}
				cluster.get(imin).add(i);
			}
			
			int[] newCentries = newCentries(cluster);
			System.out.println(cluster);
			if(compare(centries, newCentries) == 0)
				iterator = 10000;
			else {
					iterator++;
					for(int jk = 0; jk < cluster.size(); jk++)
						cluster.get(jk).removeAll(cluster.get(jk));
				                              
			}
		}
	}
	public byte compare(int[] masA, int[] masB)
	{
		byte fl = 0;
		for(int i = 0; i < masA.length; i++)
			if(masA[i]!=masB[i])fl = 1;
		return fl;
	}
	
	public int[] newCentries(ArrayList<ArrayList<Integer>> list)
	{
		int n = list.size();
		int[] centries = new int[n];
		for(int j = 0; j < n; j++)
		{
			ArrayList<Integer> clusters = list.get(j);
			int m = clusters.size();
			double min = Double.MAX_VALUE;
			int imin = -1;

			for(int i = 0; i< m; i++)
			{
				double a = 0.0;
				for(int k = 0; k< m; k++)
				{
					a += mathS(figures.get(clusters.get(i)).perimetr, figures.get(clusters.get(k)).perimetr,
							figures.get(clusters.get(i)).square, figures.get(clusters.get(k)).square,
							figures.get(clusters.get(i)).compact, figures.get(clusters.get(k)).compact,
							figures.get(clusters.get(i)).elongation, figures.get(clusters.get(k)).elongation);
				}
				if(Double.compare(a, min) < 0)
				{
					min = a;
					imin = clusters.get(i);
				}
			}
			centries[j] = imin;
		}
		return centries;
	}
	
	public double mathS(int p1, int p2, int s1, int s2, double c1, double c2, double e1, double e2)
	{
		double s = 0.0;
		s = Math.sqrt(Math.pow(p1 - p2, 2) + Math.pow(s1 - s2, 2) + Math.pow(c1 - c2, 2) + Math.pow(e1 - e2, 2));
		return s;
	}
	
	public byte check(int[] mas, int a)
	{
		byte fl = 1;
		for(int i = 0; i < mas.length; i++)
		{
			if (mas[i] == a) fl = 0;
		}
		return fl;
	}
	
	public int rnd(int max)
	{
		return (int) (Math.random() *max);
	}
}