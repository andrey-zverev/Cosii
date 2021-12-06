import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class Image {
	public Mat image;
	private int width = 0;
	private int height = 0;
	
	public Image(String path)
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		image = new Mat();
		System.out.println(path);
		image = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
		width = image.width();
		height = image.height();
		if(width != height)
		{
			image = null;
			System.out.println("Exception: IMAGE WIDTH != IMAGE HEIGHT");
		}
	}
	
	public int[][] getMat(){
		int mat[][] = new int[width][width];
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < width; j++) {
				if((int)image.get(i, j)[0] > 128) {
					mat[i][j] = -1;
				}
				else mat[i][j] = 1;
				}
			}
		return mat;
	}
	
	public int[] returnVectorImage(){
		int mat[][] = getMat();
		int vector[] = new int[width * width];
		for(int i = 0; i < width; i++)
			for(int j = 0; j < width; j++)
				vector[i * width + j] = mat[i][j];
		return vector;
	}
	
	public static int[] applayNoises(int[] image, int k){
		
		int size = image.length;
		int noiseCount = 0;
		byte isNoise = 1;
		
		if(k < 50) {
			noiseCount = size * k / 100;
			isNoise = 1;
		}
		else {
			noiseCount = size * (100 - k) / 100;
			isNoise = 0;
		}
		
		int i = 0;
		List<Integer> noiseIndexes = new ArrayList<Integer>();
		
		while(i < noiseCount){
			int rand = (int)(0 + Math.random() * size);
			if(noiseIndexes.contains(rand))
				continue;
			else {
				noiseIndexes.add(rand);
				i++;
			}
		}
		
		int[] noiseImage;
		
		if(isNoise == 1)
		{
			noiseImage = (int[])image.clone();

			for(i = 0; i < noiseCount; i++)
				noiseImage[noiseIndexes.get(i)] *= -1;
		}
		else
		{
			noiseImage = new int[size];
			for(i = 0; i < size; i++)
				noiseImage[i] = image[i] *(-1);
			
			for(i = 0; i<noiseCount; i++)
				noiseImage[noiseIndexes.get(i)] = image[noiseIndexes.get(i)];
		}
		
		return noiseImage;
	}
	
	public void saveImage(int[] arr, String path)
	{
		Mat savedImage = new Mat();
		image.copyTo(savedImage);
		for(int i = 0; i < width; i++)
			for(int j = 0; j < width; j++)
			{
				if(arr[i*width + j] == 1)
					savedImage.put(i, j, 0);
				else
					savedImage.put(i, j, 255);
			}
		Highgui.imwrite(path, savedImage);
	}
}
