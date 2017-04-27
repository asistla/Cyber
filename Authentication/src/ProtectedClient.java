import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;
import java.util.Scanner;

//https://docs.oracle.com/javase/7/docs/api/java/sql/Timestamp.html

public class ProtectedClient
{
	static String user;
	static String password;
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{
		DataOutputStream out = new DataOutputStream(outStream);

		// IMPLEMENT THIS FUNCTION.
		
		long timeStamp1, timeStamp2;
		double randInt1,randInt2;
		
		Date date = new Date();
		timeStamp1 = date.getTime();
		timeStamp2 = date.getTime();
		
		randInt1 = Math.random();
		randInt2 = Math.random();
		
		byte[] digest1 = Protection.makeDigest(user, password, timeStamp1, randInt1);
		byte[] digest2 = Protection.makeDigest(digest1, timeStamp2, randInt2);
		
		System.out.println("SHA Digest1 " + digest1 + " length " + digest1.length);
		System.out.println("SHA Digest1 " + digest2 + " length " + digest2.length);
		
		out.writeUTF(user);
		out.writeLong(timeStamp1);
		out.writeLong(timeStamp2);
		out.writeDouble(randInt1);
		out.writeDouble(randInt2);
		out.writeInt(digest2.length);
		out.write(digest2);
		

		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		System.out.println("Enter username");
		Scanner sc=new Scanner(System.in);
		String host = "localhost";
		int port = 7999;
		user = sc.nextLine();
		System.out.println("Enter password");
		password = sc.nextLine();
		Socket s = new Socket(host, port);
		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}