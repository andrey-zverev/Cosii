
public class main {
	
	public static void main(String[] args) 
	{
		String path = "D://Projects/CosiiLab2/img/P0001465.jpg";
		Image image = new Image(path);
		System.out.println(image.l);
		for(int i = 0; i < image.figures.size(); i++)
			System.out.println(image.figures.get(i));
	}
}
