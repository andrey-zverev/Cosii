import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

public class MultilayerPerceptron {
	private double[][] associativeLayer_weights;
	private double[] associativeLayer_thresholds;
	
	private double[][] outputLayer_weights;
    private double[] outputLayer_thresholds;
    
    private int sensorCount;
    private int outputCount;
    private int associativeCount;

    private double alfa;
    private double beta;
    private double maxErrorTreshold;
    public int iterationCount;
    
    private String[] classesNames;
    
    public int getIterationCount(){
    	return iterationCount;
    }
    
    public void setItertionCount(int x){
    	this.iterationCount = x;
    }
    
    public MultilayerPerceptron(Map<String, List<int[]>> classes, double alfa, double beta, double maxError)
    {
    	int size = classes.get("e").get(0).length;
    	System.out.println(size);
    	int i = 0;
    	
    	classesNames = new String[classes.size()];
    	classesNames[0] = "e";
    	classesNames[1] = "k";
    	classesNames[2] = "o";
    	
    	this.alfa = alfa;
    	this.beta = beta;
    	maxErrorTreshold = maxError;
    	
    	iterationCount = teachNetwork(classes);
    }
    
    private int teachNetwork(Map<String, List<int[]>> classes)
    {
    	sensorCount = classes.get(classesNames[0]).get(0).length;//длинна вектора входного
    	outputCount = classes.size();//размер скрытого сло€
    	
    	associativeCount = (int) Math.sqrt((double)sensorCount/outputCount);//кол-во нейронов скрытого сло€
    	
    	associativeLayer_weights = new double[sensorCount][associativeCount];//матрица весов скрытого сло€
    	associativeLayer_thresholds = new double[associativeCount];//пороги скрытого сло€
    	
    	outputLayer_weights = new double[associativeCount][outputCount];//матрица выходного
        outputLayer_thresholds = new double[outputCount];//пороги выходного
        
        randomize();//рандомим матрицы весов и порогов
        
        double[] associativeNeurons = new double[associativeCount];//нейроны скрытого сло€
        double[] outputNeurons = new double[outputCount];//нейроны выходного сло€

        List<Double> maxErrors = new ArrayList<Double>();//список значений ошибок
        
        int iterCount = 0;//число итераций обучени€
        double errorSumPrev = Double.MAX_VALUE;

        double[] outputErrors = new double[outputCount];//выходной слой
        double[] outputErrorsAbs = new double[outputCount];//модули ошибок
        
        while (true)//начало обучени€
        {
        	maxErrors.clear();//очищаем ошибки
        	int classNumber = 0;
        	for(int ind = 0; ind < 3; ind++)//по очереди изображени€
        	{
        		String key = "";
        		if(ind == 0) key = "e";
        		if(ind == 1) key = "k";
        		if(ind == 2) key = "o";
        		List<int[]> images = classes.get(key);
        		for(int[] img : images)//на самом деле 1 изображение, список дл€ Map
        		{
        			for(int j = 0; j < associativeCount; j++)//считаем скрытый слой по формулам
        			{
        				double sum = 0;
        				for(int i = 0; i < sensorCount; i++)
        					sum+= associativeLayer_weights[i][j] * img[i];
        				
        				associativeNeurons[j] = activationFunction(sum + associativeLayer_thresholds[j]);//функци€ экспоненты ниже
        			}
        			
        			for (int k = 0; k < outputCount; k++)//считаем выходной слой
                    {
                        double sum = 0;
                        for (int j = 0; j < associativeCount; j++)
                            sum += outputLayer_weights[j][k] * associativeNeurons[j];

                        outputNeurons[k] = activationFunction(sum + outputLayer_thresholds[k]);
                    }
        			 
        			for(int i = 0; i < outputErrors.length; i++)//занул€ем ошибки
        			{
        				outputErrors[i] = 0;
        			}
        			
        			outputErrors[classNumber] = 1;
        			
        			for(int k = 0; k < outputCount; k++)//считаем ошибку нашего образа
        				outputErrors[k] -= outputNeurons[k];
        			
        			for(int k = 0; k < outputCount; k++)//модули ошибок
        				outputErrorsAbs[k] = Math.abs(outputErrors[k]);
        			
        			DoubleStream doubleStream = Arrays.stream(outputErrorsAbs);
        			maxErrors.add(doubleStream.max().getAsDouble());//находим максимальную ошибку
        			
        			double[] associativeErrors = new double[associativeCount];//ошибки на скрытом слое
        			
        			for (int j = 0; j < associativeCount; j++)//считаем ошибки скрытого сло€
                    {
                        double sum = 0;
                        for (int k = 0; k < outputCount; k++)
                            sum += outputErrors[k] * outputNeurons[k] * (1 - outputNeurons[k]) * outputLayer_weights[j][k];

                        associativeErrors[j] = sum;
                    }
        			
        			//-----------------------------------------------------------------------------
                    // Correct weights and tresholds
        			
        			for (int j = 0; j < associativeCount; j++)//корректировка матрицы весок выходного сло€
                    {
                        for (int k = 0; k < outputCount; k++)
                        {
                            outputLayer_weights[j][k] += 
                                alfa * outputNeurons[k] * (1 - outputNeurons[k]) * outputErrors[k] * associativeNeurons[j];
                        }
                    }
                    for (int k = 0; k < outputCount; k++)//корректировка порогов скрытого сло€
                    {
                        outputLayer_thresholds[k] +=
                            alfa * outputNeurons[k] * (1 - outputNeurons[k]) * outputErrors[k];
                    }

                    for (int i = 0; i < sensorCount; i++)//корректировка матрицы весов скрытого сло€
                    {
                        for (int j = 0; j < associativeCount; j++)
                        {
                            associativeLayer_weights[i][j] +=
                                beta * associativeNeurons[j] * (1 - associativeNeurons[j]) * associativeErrors[j] * img[i];
                        }
                    }
                    for (int j = 0; j < associativeCount; j++)//корректировка порогов скрытого сло€
                    {
                        associativeLayer_thresholds[j] +=
                            beta * associativeNeurons[j] * (1 - associativeNeurons[j]) * associativeErrors[j];
                    }     
        		}
        		classNumber++;
        	}
        	iterCount++;
        	
        	 double maxError = Collections.max(maxErrors);//провер€ем удовлетвор€ет ли нас ошибка
             if (maxError < maxErrorTreshold)
                 break;
             DoubleStream doubleStream = Arrays.stream(outputErrorsAbs);
             
             double errorSum = doubleStream.sum();//провер€ем удовлетвор€ет ли нас сумма ошибок
             if (errorSum >= errorSumPrev)
             {
                 randomize();
                 errorSumPrev = Double.MAX_VALUE;
             }
             else
                 errorSumPrev = errorSum; 
        }
    	return iterCount;
    }
    
    private double activationFunction(double x)//та сама€ экспонента
    {
    	double y = 1.0 / (1.0 + Math.exp(-x));
    	return y;
    }
    
    private void randomize()
    {
    	 for (int i = 0; i < associativeLayer_weights[0].length; i++)
         {
             for (int j = 0; j < associativeLayer_weights[1].length; j++)
                 associativeLayer_weights[i][j] = 2 * Math.random() - 1;
         }

         for (int i = 0; i < associativeLayer_thresholds.length; i++)
             associativeLayer_thresholds[i] = 2 * Math.random() - 1;

         for (int i = 0; i < outputLayer_weights[0].length; i++)
         {
             for (int j = 0; j < outputLayer_weights[1].length; j++)
                 outputLayer_weights[i][j] = 2 * Math.random() - 1;
         }

         for (int i = 0; i < outputLayer_thresholds.length; i++)
             outputLayer_thresholds[i] = 2 * Math.random() - 1;
    }
 
    public Map<String, Double> classifyImage(int[] image)//восстановление картинки
    {
    	if(sensorCount!= image.length)
    		System.out.println("Image has wrong size.");
    	
    	double[] associativeNeurons = new double[associativeCount];
        double[] outputNeurons = new double[outputCount];
        
        for (int j = 0; j < associativeCount; j++)//считаем скрытый слой
        {
            double sum = 0;
            for (int i = 0; i < sensorCount; i++)
                sum += associativeLayer_weights[i][j] * image[i];

            associativeNeurons[j] = activationFunction(sum + associativeLayer_thresholds[j]);
        }

        for (int k = 0; k < outputCount; k++)//считаем скрытый слой
        {
            double sum = 0;
            for (int j = 0; j < associativeCount; j++)
                sum += outputLayer_weights[j][k] * associativeNeurons[j];

            outputNeurons[k] = activationFunction(sum + outputLayer_thresholds[k]);
        }
        
        Map<String, Double> result = new HashMap<String, Double>();//заносим результат
        int t = 0;
        for(String className : classesNames)//заносим в результат
        {
        	result.put(classesNames[t], outputNeurons[t]);
        	t++;
        }
        return result;
    }
}