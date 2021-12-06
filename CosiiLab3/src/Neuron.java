import java.util.List;

public class Neuron {
	int[][] synapticWeights;
	
	public Neuron(List<int[]> images)
	{
		int size = images.get(0).length;
		for(int[] img : images)
		{
			if(img.length != size) {
				System.out.println("All input must be the same size!");
				break;
			}
		}
		synapticWeights = teachNeuron(images);
	}
	
	private int[][] teachNeuron(List<int[]> images)
	{
		int neuronsCount = images.get(0).length;
		int[][] weights = new int[neuronsCount][neuronsCount];
		
		for(int i = 0; i < neuronsCount; i++) {//сумма вектор на транспонированный вектор
			for(int j = 0; j < neuronsCount; j++) {
				if(i==j)
					weights[i][j] = 0;
				else {
					int sum = 0;
					for(int[] image : images)
					{
						sum += image[i]*image[j];
					}
					weights[i][j] = sum;
				}
			}
		}
		return weights;
	}
	
	public int[] recogniseImage(int[] image)
	{
		int length = image.length;
		int[] oldImage = (int[])image.clone();
		int[] newImage = new int[length];
		
		while(true)
		{
			for(int i = 0; i < length; i++)
			{
				int sum = 0;
			for(int j = 0; j < length; j++)
				sum += synapticWeights[i][j] * oldImage[j];
			if(sum > 0)
				newImage[i] = 1;
			else 
				newImage[i] = -1;
			}
			int fl = 1;
			
			for(int x = 0; x < length; x++)
				if(oldImage[x] != newImage[x])
					fl = 0;
			
			if(fl == 1)
				break;
			else
			{
				int[] tmp = newImage;
				newImage = oldImage;
				oldImage = tmp;
			}

		}
		return newImage;
	}
}
