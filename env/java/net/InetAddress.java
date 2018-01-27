package abp.env.java.net;

public class InetAddress {
	public static InetAddress getByName(String host) throws UnknownHostException
	{
		//if (!host.equals("localhost")) throw new UnknownHostException();
		return new InetAddress();
	}
}
