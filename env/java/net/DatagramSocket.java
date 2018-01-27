package abp.env.java.net;

import java.util.LinkedList;

public class DatagramSocket {

	static LinkedList<byte[]> que_0 = null;
	static LinkedList<byte[]> que_1 = null;

	private int inport;
	private int timeout;

	private void createQueue() {
		if(que_0 == null) {
			que_0 = new LinkedList<byte[]>();
		}
		if(que_1 == null) {
			que_1 = new LinkedList<byte[]>();
		}
	}

	private LinkedList<byte[]> GetQueue(int port)
	{
		if (port == 4445)
		{
			return que_0;
		}
		else
		{
			return que_1;
		}
	}

	public DatagramSocket(int port)
	{
		inport = port;
		createQueue();
	}

	public void send(DatagramPacket packet)
	{
		LinkedList<byte[]> que = GetQueue(packet.getPort());
		byte[] buf = new byte[packet.getLength()];
		System.arraycopy(packet.getData(), 0, buf, 0, packet.getLength());
		synchronized (que)
		{

			while (que.size() >= 10)
			{
				try { que.wait(); }
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			que.add(buf);
			que.notifyAll();
		}
	}

	public void close()
	{
	}

	public void setSoTimeout(int t)
	{
		timeout = t;
	}

	public void receive(DatagramPacket packet) throws SocketTimeoutException{

		LinkedList<byte[]> queue = GetQueue(inport);

		synchronized(queue){
			// キューをチェックする
			if(queue.size() >= 1) {
				byte[] data = queue.pop();
				System.arraycopy(data, 0, packet.mBuf, 0, data.length);
				packet.mLength = data.length;
				queue.notifyAll();
				return;
			}

			// wait timeout
			try {
				queue.notifyAll();
				queue.wait(timeout);
			} catch (InterruptedException e) {

			}

			// check agein
			if(queue.size() >= 1) {
				byte[] data = queue.pop();
				System.arraycopy(data, 0, packet.mBuf, 0,  data.length);
				packet.mLength = data.length;
				queue.notifyAll();
				return;
			}else {
				queue.notifyAll();
				throw new SocketTimeoutException();
			}
		}

	}

}
