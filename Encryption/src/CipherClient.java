import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import java.util.Scanner;
//https://docs.oracle.com/javase/7/docs/api/java/security/package-summary.html
//https://docs.oracle.com/javase/7/docs/api/java/security/KeyPairGenerator.html
//http://docs.oracle.com/javase/7/docs/api/javax/crypto/package-summary.html

public class CipherClient
{
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Enter string to encrypt");
		Scanner sc=new Scanner(System.in);
		String message = sc.nextLine();
		sc.close();
		//String host = "paradox.sis.pitt.edu";
		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);

		// YOU NEED TO DO THESE STEPS:
		// -Generate a DES key.
		// -Store it in a file.
		// -Use the key to encrypt the message above and send it over socket s to the server.
		
		//DES key generation
		KeyGenerator keyDES = KeyGenerator.getInstance("DES");
		SecureRandom random = new SecureRandom();
		keyDES.init(random);
		SecretKey secretKey = keyDES.generateKey();//The purpose of this interface is to group (and provide type safety for) all secret key interfaces. 
		
		//saving the key in a file to use it later
		String fileName = "des.txt";
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));
		outputStream.writeObject(secretKey);
		outputStream.close();
		
		//to send the key to the server
		ObjectOutputStream socket = new ObjectOutputStream(s.getOutputStream());
	    socket.writeObject(secretKey);
	    socket.flush();
	    
	    //This class provides the functionality of a cryptographic cipher for encryption and decryption
	    //In order to create a Cipher object, the application calls the Cipher's getInstance method, 
	    //and passes the name of the requested transformation to it. 
	    Cipher desCipher = Cipher.getInstance("DES");
	    desCipher.init(Cipher.ENCRYPT_MODE, secretKey); // making the encryption with the specified key
	    byte[] byteDataToEncrypt = message.getBytes(); // encode the string into bytes
	    
	    // building a cipher output stream from the output stream and the desCipher
	    CipherOutputStream cipherOut = new CipherOutputStream(s.getOutputStream(), desCipher);
	    cipherOut.write(byteDataToEncrypt);  //sending through the socket
	    cipherOut.flush();
	    cipherOut.close();
	    
	    s.close();
	}
}