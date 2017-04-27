import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		DataInputStream in = new DataInputStream(inStream);

		// IMPLEMENT THIS FUNCTION.
		String user = in.readUTF();
		String password = lookupPassword(user);

		long timeStamp1 = in.readLong();
		long timeStamp2 = in.readLong();

		double randInt1 = in.readDouble();
		double randInt2 = in.readDouble();

		int shaDigest1Length = in.readInt();


		byte[] digest1Server = Protection.makeDigest(user, password, timeStamp1, randInt1);

		byte[] digest2Server = Protection.makeDigest(digest1Server, timeStamp2, randInt2);

		byte[] digest2Client = new byte[shaDigest1Length];
		in.readFully(digest2Client);

		boolean isSame = MessageDigest.isEqual(digest2Server, digest2Client);
		System.out.println("isSame ::: "+isSame);
		return isSame;
		
	}

	protected String lookupPassword(String user) { return ProtectedClient.password; }

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();
		//System.out.println("Request Received from ::: "+client.getRemoteSocketAddress());	

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
	}
}
