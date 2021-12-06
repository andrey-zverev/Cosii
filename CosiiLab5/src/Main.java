import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		List<int[]> letters = new ArrayList<int[]>();
		
		letters.add(Data.TWO);
		letters.add(Data.THREE);
		letters.add(Data.FOUR);
		letters.add(Data.FIVE);
		letters.add(Data.SEVEN);
		
		Kohonen network = new Kohonen(36, 5);
		network.teachNetwork(letters);
		
		
		network.findImage(Data.TWO);
		System.out.println();
		network.findImage(Data.THREE);
		System.out.println();
		network.findImage(Data.FOUR);
		System.out.println();
		network.findImage(Data.FIVE);
		System.out.println();
		network.findImage(Data.SEVEN);

		System.out.println("-------------------10% - 2------------------");
		network.findImage(crashImage(Data.TWO, 10));
		
		System.out.println("-------------------20% - 3------------------");
		network.findImage(crashImage(Data.THREE, 20));
		
		System.out.println("-------------------30% - 4------------------");
		network.findImage(crashImage(Data.FOUR, 30));
		
		System.out.println("-------------------70% - 5------------------");
		network.findImage(crashImage(Data.FIVE, 70));
		
		System.out.println("-------------------90% - 7------------------");
		network.findImage(crashImage(Data.SEVEN, 90));
		
		
	}
	
	 private static int[] crashImage(int[] inputImage, int percentage) {
	        if (percentage < 0 || percentage > 100) {
	            throw new IllegalArgumentException("Percentage cannot be more than 100% or less than 0%");
	        }

	        List<Integer> indexes = new ArrayList<>(inputImage.length);
	        Set<Integer> indexesForChange = new HashSet<>(inputImage.length);

	        Random random = new Random();
	        int[] crashedImage = inputImage.clone();

	        for (int i = 0; i < inputImage.length; i++) {
	            indexes.add(i);
	        }

	        int pixelForChange = (inputImage.length * percentage) / 100;
	        for (int i = 0; i < pixelForChange; i++) {
	            int randomIndex = random.nextInt(indexes.size());
	            indexesForChange.add(indexes.get(randomIndex));
	            indexes.remove(randomIndex);
	        }

	        for (Integer index : indexesForChange) {
	            if (inputImage[index] == 1)
	                crashedImage[index] = 0;
	            else
	                crashedImage[index] = 1;
	        }

	        return crashedImage;
	    }

}
