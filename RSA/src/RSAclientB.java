import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

public class RSAclientB {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String message = args[0];

		String host = "LOCALHOST";
		int port = 8999;
		try {
			Socket s = new Socket(host, port);
			
			ObjectOutputStream outStreamToBob = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream inputStreamFromBob = new ObjectInputStream(s.getInputStream());
			
			//Generating a pair of  keys representing Alice public and private key. 
			KeyPairGenerator  keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(1024, new SecureRandom());
			KeyPair keyP = keyPairGen.genKeyPair();
			
			// assigning alice public and private keys
			RSAPublicKey AlicePublicKey = (RSAPublicKey) keyP.getPublic();
			RSAPrivateKey AlicePrivateKey = (RSAPrivateKey) keyP.getPrivate();
			
			//sending the alice public key to bob
			outStreamToBob.writeObject(AlicePublicKey);
			
			//getting bob's public key 
			RSAPublicKey BobPublicKey = (RSAPublicKey) inputStreamFromBob.readObject();
			Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		
		  
		  if(args[0].equals("1")) {
			  System.out.println("Confidentiality: The Message will be encrypted using Bob's public Key");
			  cipher.init(Cipher.ENCRYPT_MODE, BobPublicKey);
		      byte[] encryptedMessage = cipher.doFinal(message.getBytes());
		      outStreamToBob.writeInt(1);
		      outStreamToBob.writeObject(encryptedMessage);
		      outStreamToBob.flush();
		      outStreamToBob.close(); 
		  }
		  
		  if(args[0].equals("2")) {
			  System.out.println("Integrity & Authentication: The Message will be encrypted using Alice's private key");
			  
			  cipher.init(Cipher.ENCRYPT_MODE, AlicePrivateKey);
		      byte[] encryptedMessage = cipher.doFinal(message.getBytes());
		      outStreamToBob.writeInt(2);
		      outStreamToBob.writeObject(encryptedMessage);
		      outStreamToBob.flush();
		      outStreamToBob.close();  
		  }
		  
		  if(args[0].equals("3")) {
				  
			  System.out.println("Both : The Message will be first encrypted using Alice's private key and then using bob's public key");
			  cipher.init(Cipher.ENCRYPT_MODE, AlicePrivateKey);
		       //encryptedMessage = new byte[message.length()];
		      byte[] encryptedMessage = cipher.doFinal(message.getBytes());
		      //String x = new String(encryptedMessage);
		      Cipher cipher2 =  Cipher.getInstance("RSA/ECB/NoPadding");
		      cipher2.init(Cipher.ENCRYPT_MODE, BobPublicKey);
		      byte[] encryptedMessage2 = cipher2.doFinal(encryptedMessage);
		      outStreamToBob.writeInt(3);
		      outStreamToBob.writeObject(encryptedMessage2);
		      outStreamToBob.flush();
		      outStreamToBob.close();
		  }
		  
		  s.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
