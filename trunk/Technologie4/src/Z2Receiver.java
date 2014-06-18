import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Z2Receiver {
	static final int datagramSize = 50;
	InetAddress localHost;
	int destinationPort;
	List<Z2Packet> pakiety = new ArrayList<Z2Packet>();
	int index = 0;
	DatagramSocket socket;
	Z2Packet last_packet = null;
	long time = 0;

	ReceiverThread receiver;

	public Z2Receiver(int myPort, int destPort) throws Exception {
		localHost = InetAddress.getByName("127.0.0.1");
		destinationPort = destPort;
		socket = new DatagramSocket(myPort);
		receiver = new ReceiverThread();
	}

	public void wypisz() {
		for (int i = 0; i < pakiety.size(); i++) {
			Z2Packet p = pakiety.get(i);
			if (p.getIntAt(0) == index) {
				System.out.println(" R :" + p.getIntAt(0) + ": "
						+ (char) p.data[4]);
				pakiety.remove(i);
				if (time == 0)
					time = System.currentTimeMillis();
				last_packet = p;
				index++;
			}
		}
	}

	class ReceiverThread extends Thread {

		public void run() {
			try {
				while (true) {
					byte[] data = new byte[datagramSize];
					DatagramPacket packet = new DatagramPacket(data,
							datagramSize);
					socket.receive(packet);
					Z2Packet p = new Z2Packet(packet.getData());
					pakiety.add(p);
					wypisz();
					// WYSLANIE POTWIERDZENIA
					packet.setPort(destinationPort);
					if ((System.currentTimeMillis() - time > 10000)
							&& last_packet != null) {
						packet.setData(last_packet.data);
						socket.send(packet);
						time = 0;
					}
				}
			} catch (Exception e) {
				System.out.println("Z2Receiver.ReceiverThread.run: " + e);
			}
		}

	}

	public static void main(String[] args) throws Exception {
		Z2Receiver receiver = new Z2Receiver(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
		receiver.receiver.start();
	}

}