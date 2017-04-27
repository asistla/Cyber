//Signature Verification Class

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalBob
{
	private static boolean verifySignature(	BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message)
	{
		// IMPLEMENT THIS FUNCTION;
		BigInteger leftHS = (y.modPow(a, p).multiply(a.modPow(b, p))).mod(p);
		//System.out.println("sol1: "+leftHS);
		BigInteger m = new BigInteger(message.getBytes());
		BigInteger rightHS = g.modPow(m, p);		// computing g^m mod p
		//System.out.println("sol2: "+rightHS);
		
		return leftHS.equals(rightHS);
	}

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());
		
		// read public key
		BigInteger y = (BigInteger)is.readObject();
		BigInteger g = (BigInteger)is.readObject();
		BigInteger p = (BigInteger)is.readObject();		
		
		// read message
		String message = (String)is.readObject();

		// read signature
		BigInteger a = (BigInteger)is.readObject();
		BigInteger b = (BigInteger)is.readObject();
		
		
		boolean result = verifySignature(y, g, p, a, b, message);

		System.out.println(message);

		if (result == true)
			System.out.println("Verification of signature :: Successfull.");
		else
			System.out.println("Verification of signature :: Failed.");
		
		s.close();
	}
}
