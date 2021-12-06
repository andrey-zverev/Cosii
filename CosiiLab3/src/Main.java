import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
public static String pathLearn1 = "D:/Projects/CosiiLab3/img/first.bmp";
public static String pathLearn2 = "D:/Projects/CosiiLab3/img/second.bmp";
public static String pathLearn3 = "D:/Projects/CosiiLab3/img/third.bmp";
public static String pathNoise = "D:/Projects/CosiiLab3/img/noise";
public static String pathResult = "D:/Projects/CosiiLab3/img/result";

	public static void main(String[] args) {
		System.out.println("Loading images...");
		Image first = new Image(pathLearn1);
		Image second = new Image(pathLearn2);
		Image third = new Image(pathLearn3);
		int[] firstImg = first.returnVectorImage();
		int[] secondImg = second.returnVectorImage();
		int[] thirdImg = third.returnVectorImage();
		List<int[]> images = new ArrayList<int[]>();
		
		images.add(firstImg);
		images.add(secondImg);
		images.add(thirdImg);
		System.out.println("Teaching network...");
		Scanner scanner = new Scanner(System.in);
		Neuron neuron = new Neuron(images);
		int fl = 2;
		int file = 1;
		do
		{
			System.out.println("Choose image: \n1 - E\n2 - K\n3 - O");
			int choosen = scanner.nextInt();
			System.out.println("Insert percent of noises:\n");
			int k = scanner.nextInt();
			int[] noiseImage = new int[first.image.width() * first.image.width()];
			if(choosen == 1)
				noiseImage = Image.applayNoises(firstImg, k);
			else if(choosen == 2)
				noiseImage = Image.applayNoises(secondImg, k);
			else if(choosen == 3)
				noiseImage = Image.applayNoises(thirdImg, k);
			
			first.saveImage(noiseImage, pathNoise + Integer.toString(file) + ".bmp");
			int[] result = neuron.recogniseImage(noiseImage);
			first.saveImage(result, pathResult + Integer.toString(file) + ".bmp");
			file++;
		}
		while(fl>1);
	}
}
