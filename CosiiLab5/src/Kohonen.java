import java.util.List;

public class Kohonen{
	
	private double weights[][];
	private double neuronos[];
	private double mistake = 0.01;
	private int m;
	private int n;
	private double beta = 0.10;
	
	public Kohonen(int n, int m){
		
		this.m = m;
		this.n = n;
		weights = new double[n][m];
		neuronos = new double[m];
		mistake = 0.01;
		for(int i = 0; i < n; i++){
			for(int j = 0; j < m; j++)
				weights[i][j] = Math.random();
		}
		
		for(int i = 0; i < m; i++)
			neuronos[i] = 0;
	}
	
	public void findImage(int[] image) {
		double[] newNeurons = countNeurons(image);
		for(int i = 0; i< m; i++){
			System.out.println((i + 1) + " - " + newNeurons[i]); 
		}
	}
	
	public void teachNetwork(List<int[]> teachPic){
		
		for(int[] image : teachPic) {
			int epoch = 1;
			int isTeached = 0;
			do {
				double[] newNeurons = countNeurons(image);
				int maxIndex = max(newNeurons);
				double mistakeWinner = getMistake(maxIndex, image);
				if(mistakeWinner < mistake) isTeached = 1;
				else {
					isTeached = 0;
					correctWeights(maxIndex, image);
					epoch++;
				}
			}while(isTeached == 0);
			System.out.println(epoch);
		}
	}
	
	private double[] countNeurons(int[] vector) {
		double[] newNeurons = new double[m];
		for(int j = 0; j < m; j++) {
			double sum = 0;
			for(int i = 0; i < n; i++) {
				sum += weights[i][j] * vector[i];
			}
			newNeurons[j] = sum;
		}
		return newNeurons;
	}
	
	private int max(double[] mas) {
		double max = 0;
		int index = 0;
		for(int i = 0; i < mas.length; i++) {
			if(mas[i] > max) {
				max = mas[i];
				index = i;
			}
		}
		return index;
	}
	
	private void correctWeights(int j, int[] vector) {
		double absVector = 0;
		for(int i = 0; i < n; i++) {
			absVector += Math.pow(weights[i][j] + beta*((double)vector[i] - weights[i][j]), 2);
		}
		absVector = Math.sqrt(absVector);
		
		for(int i = 0; i < n; i++) {
			weights[i][j] = weights[i][j] + beta*((double)vector[i] - weights[i][j]);
		}
	}
	
	private double getMistake(int index, int[] vector) {
		double mistake = 0;
		for(int i = 0; i < n; i++) {
			mistake += Math.pow(vector[i] - weights[i][index], 2);
		}
		return Math.sqrt(mistake);
	}
}
