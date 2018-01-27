package abp.env.java.net;

public class DatagramPacket {

	public byte[] mBuf;
	public int mLength;
	private int mPort;
	private InetAddress mAddress;

	public DatagramPacket(byte[] buf, int length) {
		mBuf = buf;
		mLength = length;
	}

	public DatagramPacket(byte[] buf, int length, InetAddress address, int port) {
		mBuf = buf;
		mLength = length;
		mAddress = address;
		mPort = port;
	}

	public byte[] getData() {
		return mBuf;
	}

	public int getLength() {
		return mLength;
	}

	public int getPort() {
		return mPort;
	}


}
