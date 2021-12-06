import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Main {

	public static void main(String[] args) 
	{
		Image e = new Image("D:\\Projects\\CosiiLab4\\img\\eletter.bmp");
		Image k = new Image("D:\\Projects\\CosiiLab4\\img\\kletter.bmp");
		Image o = new Image("D:\\Projects\\CosiiLab4\\img\\oletter.bmp");
		
		Map<String,List<int[]>> classes = new HashMap<String, List<int[]>>();
		List<int[]> eList = new ArrayList<int[]>();
		eList.add(e.returnVectorImage());
		List<int[]> kList = new ArrayList<int[]>();
		kList.add(k.returnVectorImage());
		List<int[]> oList = new ArrayList<int[]>();
		oList.add(o.returnVectorImage());
		
		
		classes.put("e", eList);
		classes.put("k", kList);
		classes.put("o", oList);
		
		double alfa = 1.2;
		double beta = 0.9;
		double maxError = 0.05;
		
		MultilayerPerceptron perceptron = new MultilayerPerceptron(classes, alfa, beta, maxError);
		
		System.out.println("Нейронная сеть обучена");
		System.out.println("Количество итераций: " + perceptron.iterationCount);
		
		Image etest1 = new Image("D:\\Projects\\CosiiLab4\\img\\etest1.bmp");
		Map<String, Double> result = perceptron.classifyImage(etest1.returnVectorImage());
		System.out.println("e - " + result.get("e"));
		System.out.println("k - " + result.get("k"));
		System.out.println("o - " + result.get("o"));
	}

}
