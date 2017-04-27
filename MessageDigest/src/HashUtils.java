import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
	private HashUtils(){
		throw new RuntimeException("Cannot create instance.");
	}
	public final static String genMD5(String userInput){
		return stringToHash(userInput, "MD5");
	}
	public final static String genSHA1(String userInput){
		return stringToHash(userInput, "SHA-1");
	}
	public final static String genSHA256(String userInput){
		return stringToHash(userInput, "SHA-256");
	}
	
	private static String stringToHash(String userInput, String algorithm){
		try{//here we use Exception Handling by applying try and catch method			
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.reset();//resets all the digest for further use
			byte[] hashByte = md.digest(userInput.getBytes("UTF-8"));
			
			
			StringBuffer sb = new StringBuffer();//created a StringBuffer object
			for(int i = 0; i < hashByte.length;i++){//using for loop
				sb.append(Integer.toString((hashByte[i] & 0xff) + 0x100, 16).substring(1));
				
	        }
	        return sb.toString();//returns string
		} catch(NoSuchAlgorithmException | UnsupportedEncodingException ex){
			ex.printStackTrace();
			return "";
		}
	}
	public static void main(String[] args){
		
	}
}
