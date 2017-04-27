import java.util.Scanner;

/*
 * We created HashUtils as another file so as to take the advantage of "Static". 
 * Static reference is of a class rather than the object. Hence we don't have to instantiated or 
 * create an object to use static method
 */

public class MessageDigestClass {
	
	//private String[] algorithm = {"MD5", "SHA-1", "SHA-256"};
	
		
	public static void main(String[] args) {
		@SuppressWarnings("resource")//annotation
		Scanner sc = new Scanner(System.in);//used scanner class to take input from user
		System.out.println("Enter input string");  
		String inputString = sc.nextLine();
		
		System.out.println("Input String: " + inputString);
		
		String Hashmd5 = HashUtils.genMD5(inputString); //no need of creating object due to static
        System.out.println("MD5 Hash: " + Hashmd5);
        String HashSHA1 = HashUtils.genSHA1(inputString); //no need of creating object
        System.out.println("SHA1 Hash: " + HashSHA1);
        String HashSHA256 = HashUtils.genSHA256(inputString); //no need of creating object
        System.out.println("SHA256 Hash: " + HashSHA256);
	}

}
