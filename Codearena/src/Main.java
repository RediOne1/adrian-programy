import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

class Connect {

	public Socket socket;
	public String userid = "204";
	public String hashid = "f65ed88e51d55b6e2f187a0b83113f11";
	public Mapa mapa;

	public ReceiverThread receiver;
	public SenderThread sender;
	public ConnectInspector ci;

	public Connect(Socket s) throws Exception {

		this.socket = s;
		receiver = new ReceiverThread();
		sender = new SenderThread();
		ci = new ConnectInspector();
		mapa = new Mapa();
	}

	class ReceiverThread extends Thread {

		public void run() {
			try {
				String temp[] = { "E", "SW", "W" };
				int i = 0;
				while (true) {
					BufferedReader serverReader = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));

					String serverResponse = null;
					while ((serverResponse = serverReader.readLine()) != null) {
						System.out
								.println("Server Response: " + serverResponse);
						File f = new File("response.xml");
						FileOutputStream fos = new FileOutputStream(f);
						fos.write(serverResponse.getBytes());
						try {
							MyXml mXml = new MyXml();
							String x = mXml.checkResponse(f);
							if (x != null) {
								if (i == 3){}
									//System.exit(0);
								else{
								sendFile(mXml.moveFile(x, "direction", temp[i]));
								i++;}
							}
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
				System.out.println("ReceiverThread: " + e);
			}
		}
	}

	class ConnectInspector extends Thread {

		public void run() {
			try {

				while (true) {
					System.out.println("Is connected: " + socket.isConnected()
							+ "  Is closed: " + socket.isClosed());
					sleep(5000);
				}
			} catch (Exception e) {
				System.out.println("ReceiverThread: " + e);
			}
		}
	}

	class SenderThread extends Thread {
		public void run() {
			try {
				MyXml mXml = new MyXml();
				sendFile(mXml.getConnectXml(userid, hashid));
				sleep(2000);
				// sendFile(mXml.moveFile(, direction))
				sleep(2000);
				sleep(2000);
				sleep(2000);

			} catch (Exception e) {
				System.out.println("SenderThread: " + e);
			}
		}
	}

	public void sendFile(File f) throws Exception {
		int num;
		// SENDFILE
		BufferedInputStream bis = new BufferedInputStream(
				new FileInputStream(f));
		BufferedOutputStream bos = new BufferedOutputStream(
				socket.getOutputStream());
		byte byteArray[] = new byte[8192];
		while ((num = bis.read(byteArray)) != -1) {
			bos.write(byteArray, 0, num);
		}

		bos.flush();
		bis.close();
	}

}

public class Main {

	public static void main(String args[]) {
		int portNumber = 7654;
		String host = "codearena.pl";
		try {
			Socket socket = new Socket(host, portNumber);
			Connect connect = new Connect(socket);
			connect.receiver.start();
			connect.sender.start();
			// connect.ci.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
