import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

public class RSAserverA {

	public static void main(String[] args) throws Exception {

		int port = 8999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();


		ObjectInputStream inputStreamFromAlice = new ObjectInputStream(client.getInputStream());
		ObjectOutputStream outputStreamToAlice = new ObjectOutputStream(client.getOutputStream());

		//Generating bob's private and public key
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024, new SecureRandom()); 
		KeyPair keyPair = keyPairGen.genKeyPair();
		RSAPublicKey bobPublicKey_Bob = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey bobPrivateKey = (RSAPrivateKey)keyPair.getPrivate();

		//sending bob's public Key To Alice
		outputStreamToAlice.writeObject(bobPublicKey_Bob);

		//receiving alice public key from bob
		RSAPublicKey AlicePublicKey = (RSAPublicKey) inputStreamFromAlice.readObject();
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");

		int chosenService = inputStreamFromAlice.readInt();

		if(chosenService == 1) {
			//Confidentiality: deciphering using bob's private key 
			byte[] receivedEncryptedMessage = (byte[]) inputStreamFromAlice.readObject();
			cipher.init(Cipher.DECRYPT_MODE, bobPrivateKey);
			byte[] decryptedMessage = cipher.doFinal(receivedEncryptedMessage);
			System.out.println("Confidentiality: The Message has been decrypted with bob's private key"); 
			System.out.println(" The Original Message is: " + new String(decryptedMessage).trim());
		}

		if(chosenService == 2) {
			//Integrity&Authentication: Deciphering with Alice public key  
			byte[] receivedEncryptedMessageI = (byte[]) inputStreamFromAlice.readObject();
			cipher.init(Cipher.DECRYPT_MODE, AlicePublicKey);
			byte[] decryptedMessageI = cipher.doFinal(receivedEncryptedMessageI);
			System.out.println("Integrity & Authetication: The Message has been decrypted with alice's public key "); 
			System.out.println(" The Original Message is: " + new String(decryptedMessageI).trim() );
		}

		if(chosenService == 3) {
			//Both: Deciphering with bob private key first, and then with alice public key.   
			byte[] receivedEncryptedMessageB = (byte[]) inputStreamFromAlice.readObject();
			cipher.init(Cipher.DECRYPT_MODE,bobPrivateKey);
			byte[] decryptedMessageB = cipher.doFinal(receivedEncryptedMessageB);
			cipher.init(Cipher.DECRYPT_MODE,AlicePublicKey);
			byte[] decryptedMessage2 = cipher.doFinal(decryptedMessageB);
			System.out.println("Both: The Message has been first decrypted with bob's private key and then alice public key "); 
			System.out.println(" The Original Message is: " + new String(decryptedMessage2).trim() );
		}
		s.close();
	}

}
