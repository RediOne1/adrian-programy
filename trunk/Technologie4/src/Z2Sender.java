import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

class Z2Sender {
	static final int datagramSize = 50;
	static final int sleepTime = 200;
	static final int maxPacket = 50;
	InetAddress localHost;
	int destinationPort;
	DatagramSocket socket;
	SenderThread sender;
	ReceiverThread receiver;
	List<Z2Packet> niepotwierdzone = new ArrayList<Z2Packet>();
	List<Z2Packet> odebrane = new ArrayList<Z2Packet>();
	Potwierdz potwierdz;

	public Z2Sender(int myPort, int destPort) throws Exception {
		localHost = InetAddress.getByName("127.0.0.1");
		destinationPort = destPort;
		socket = new DatagramSocket(myPort);
		sender = new SenderThread();
		receiver = new ReceiverThread();
		potwierdz = new Potwierdz();
	}

	public void update_niepotwierdzone() {
		for (int i = 0; i < odebrane.size(); i++) {

		}
	}

	public void wyslijPakiet(Z2Packet p) {
		try {
			DatagramPacket packet = new DatagramPacket(p.data, p.data.length,
					localHost, destinationPort);
			socket.send(packet);

		} catch (Exception e) {

		}
	}

	public void wypisz() {
		for (int i = 0; i < odebrane.size(); i++) {
			Z2Packet p = odebrane.get(i);
			System.out.println(" Potwierdzono :" + p.getIntAt(0) + ": " + (char) p.data[4]);

			for (int j = 0; j < niepotwierdzone.size(); j++) {
				if (p.getIntAt(0) >= niepotwierdzone.get(j).getIntAt(0)) {
					niepotwierdzone.remove(j);
					break;
				}
			}
			odebrane.remove(i);
		}
	}

	class SenderThread extends Thread {
		public void run() {
			int i, x;
			try {
				for (i = 0; (x = System.in.read()) >= 0; i++) {
					while (niepotwierdzone.size() >= 10)
						sleep(100);
					Z2Packet p = new Z2Packet(4 + 1);
					p.setIntAt(i, 0);
					p.data[4] = (byte) x;
					p.czas = System.currentTimeMillis();
					niepotwierdzone.add(p);
					wyslijPakiet(p);
					sleep(sleepTime);
				}
			} catch (Exception e) {
				System.out.println("Z2Sender.SenderThread.run: " + e);
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
					odebrane.add(p);
					wypisz();
				}
			} catch (Exception e) {
				System.out.println("Z2Sender.ReceiverThread.run: " + e);
			}
		}

	}

	class Potwierdz extends Thread {
		public void run() {
			try {
				while (true) {
					for (int j = 0; j < niepotwierdzone.size(); j++) {
						if (System.currentTimeMillis()
								- niepotwierdzone.get(j).czas >= 10000) {
							// retransmisja

							System.out.println(" Wyslij ponownie "
									+ niepotwierdzone.get(j).getIntAt(0)
									+ "   niepotwierdzonych "
									+ niepotwierdzone.size());

							Z2Packet r = niepotwierdzone.get(j);
							r.czas = System.currentTimeMillis();
							wyslijPakiet(r);
						}
					}
					sleep(10000);
				}
			} catch (Exception e) {
				System.out.println(" Z2Sender . GuardianThread . run : " + e);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Z2Sender sender = new Z2Sender(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
		sender.sender.start();
		sender.receiver.start();
		sender.potwierdz.start();
	}

}