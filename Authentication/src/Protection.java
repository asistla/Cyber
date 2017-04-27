import java.io.*;
import java.security.*;

public class Protection
{
	public static byte[] makeBytes(long t, double q) 
	{    
		try 
		{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			DataOutputStream dataOut = new DataOutputStream(byteOut);
			dataOut.writeLong(t);
			dataOut.writeDouble(q);
			return byteOut.toByteArray();
		}
		catch (IOException e) 
		{
			return new byte[0];
		}
	}	

	public static byte[] makeDigest(byte[] mush, long t2, double q2) throws NoSuchAlgorithmException 
	{
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(mush);
		md.update(makeBytes(t2, q2));
		return md.digest();
	}
	
	public static byte[] makeDigest(String user, String password,
									long t1, double q1)
									throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		// IMPLEMENT THIS FUNCTION.
		MessageDigest md = MessageDigest.getInstance("SHA");//Returns a MessageDigest object that implements the specified digest algorithm.
		md.update(user.getBytes("UTF-8"));//Updates the digest using the specified array of bytes.
		md.update(password.getBytes("UTF-8"));//Updates the digest using the specified array of bytes.
		md.update(makeBytes(t1,q1));//Updates the digest using the specified array of bytes.
		return md.digest();//Completes the hash computation by performing final operations such as padding
		
	}
}
