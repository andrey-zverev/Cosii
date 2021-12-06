
public class Main 
{
	public static String path = "D:\\Projects\\CosiiLab1\\img\\cat.jpg";
	public static String userPath = "D:\\Projects\\CosiiLab1\\img\\user.jpg";
	public static String userPathResult = "D:\\Projects\\CosiiLab1\\img\\user_result.jpg";
	public static String userPathGray = "D:\\Projects\\CosiiLab1\\img\\user_gray.jpg";
	public static String grayPicturePath = "D:\\Projects\\CosiiLab1\\img\\cat_gray.jpg";
	public static String symbolPicturePath = "D:\\Projects\\CosiiLab1\\img\\cat_symbol.jpg";
	public static String filterPicturePathMin = "D:\\Projects\\CosiiLab1\\img\\cat_filter_min.jpg";
	public static String filterPicturePathMax = "D:\\Projects\\CosiiLab1\\img\\cat_filter_max.jpg";
	public static String filterPicturePathMinMax = "D:\\Projects\\CosiiLab1\\img\\cat_filter_minmax.jpg";
	 public static void main(String[] args)
	 {
		 Image cat = new Image(path);
		 System.out.println(cat);
		 double[] histogram = new double[256];
		 histogram = cat.histogramArray();
		 double sum = 0.0;
		 for(int i = 0; i < 256; i++) {
			 sum+=histogram[i];
		 }
		 System.out.println(Math.random());
		 System.out.println(Math.random());
		 System.out.println(Math.random());
		 
		 //gray
		 System.out.println(sum);
		 System.out.println(1200*900);
		 cat.saveImage(grayPicturePath);
		 Window window = new Window(grayPicturePath, histogram);
		 window.showWindow(grayPicturePath, histogram);
		 //symbol
		 Image catSymbol = new Image(path);
		 catSymbol.image = cat.lowFilter();
		 catSymbol.saveImage(symbolPicturePath);
		 double[] histogram_symbol = new double[256];
		 histogram_symbol = catSymbol.histogramArray();
		 Window window_symbol = new Window(symbolPicturePath, histogram_symbol);
		 window_symbol.showWindow(symbolPicturePath, histogram_symbol);
		 //filterMin
		 Image catFilterMin = new Image(path);
		 catFilterMin.image = cat.filterImageMin();
		 catFilterMin.saveImage(filterPicturePathMin);
		 double[] histogram_filter_min = new double[256];
		 histogram_filter_min = catFilterMin.histogramArray();
		 Window window_filter_min = new Window(filterPicturePathMin, histogram_filter_min);
		 window_filter_min.showWindow(filterPicturePathMin, histogram_filter_min);
		//filterMax
		 Image catFilterMax = new Image(path);
		 catFilterMax.image = cat.filterImageMax();
		 catFilterMax.saveImage(filterPicturePathMax);
		 double[] histogram_filter_max = new double[256];
		 histogram_filter_max = catFilterMax.histogramArray();
		 Window window_filter_max = new Window(filterPicturePathMax, histogram_filter_max);
		 window_filter_max.showWindow(filterPicturePathMax, histogram_filter_max);
		//filterMinMax
		 Image catFilterMinMax = new Image(path);
		 catFilterMinMax.image = cat.filterImageMin();
		 catFilterMinMax.image = catFilterMinMax.filterImageMax();
		 catFilterMinMax.saveImage(filterPicturePathMinMax);
		 double[] histogram_filter_minmax = new double[256];
		 histogram_filter_minmax = catFilterMin.histogramArray();
		 Window window_filter_minmax = new Window(filterPicturePathMinMax, histogram_filter_minmax);
		 window_filter_minmax.showWindow(filterPicturePathMinMax, histogram_filter_minmax);
		 //user
		 Image user = new Image(userPath);
		 user.saveImage(userPathGray);
		 //user histogram
		 histogram = user.histogramArray();
		 Window user_window = new Window(userPathGray, histogram);
		 user_window.showWindow(userPathGray, histogram);
		 //user filter
		 Image userResult = new Image(userPath);
		 userResult.image = user.userFilter();
		 userResult.saveImage(userPathResult);
		 //user filter histogram
		 double[] histogram_filter = new double[256];
		 histogram_filter = userResult.histogramArray();
		 Window user_filter = new Window(userPathResult, histogram_filter);
		 user_filter.showWindow(userPathResult, histogram_filter);
	 }
} 