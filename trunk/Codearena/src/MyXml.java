import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MyXml {
	public MyXml() {

	}

	public File getConnectXml(String userid, String hashid) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();

		Element connect = doc.createElement("connect");
		doc.appendChild(connect);
		connect.setAttribute("userid", userid);
		connect.setAttribute("hashid", hashid);

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("file.xml"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);
		System.out.println("File saved!");

		File f = new File("file.xml");
		return f;
	}

	public File moveFile(String id, String actionType, String direction)
			throws Exception {
		// <unit id="IDJEDNOSTKI"><go [action=""] [rotate=""] [direction=""]
		// /></unit>
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();

		Element unit = doc.createElement("unit");
		doc.appendChild(unit);
		unit.setAttribute("id", id);

		Element go = doc.createElement("go");
		unit.appendChild(go);
		go.setAttribute(actionType, direction);

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("file.xml"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);
		System.out.println("File saved!");

		File f = new File("file.xml");
		return f;
	}

	public String checkResponse(File f) {
		try {
			File fXmlFile = f;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			String rootElement = doc.getDocumentElement().getNodeName();
			if(rootElement.equals("game")){
				return XmlParse(f);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
		
	}

	public String XmlParse(File f) throws Exception {
		Mapa mapa = Mapa.getInstance();
		File fXmlFile = f;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		System.out.println("Root element :"
				+ doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("unit");

		System.out.println("----------------------------");
		String x = "";

		for (int i = 0; i < nList.getLength(); i++) {

			Node nNode = nList.item(i);

			System.out.println("\nCurrent Element :" + nNode.getNodeName());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				System.out.println("id : " + eElement.getAttribute("id"));
				System.out.println("x : " + eElement.getAttribute("x"));
				System.out.println("y : " + eElement.getAttribute("y"));
				System.out.println("hp : " + eElement.getAttribute("hp"));
				System.out.println("status : "
						+ eElement.getAttribute("status"));
				System.out.println("action : "
						+ eElement.getAttribute("action"));
				System.out.println("orientation : "
						+ eElement.getAttribute("orientation"));
				System.out.println("player : "
						+ eElement.getAttribute("player"));

				NodeList seesList = eElement.getElementsByTagName("sees");
				for (int j = 0; j < seesList.getLength(); j++) {
					Node seesNode = seesList.item(j);
					System.out.println("" + seesNode.getNodeName());
					if (seesNode.getNodeType() == Node.ELEMENT_NODE) {
						Element seesElement = (Element) seesNode;
						System.out.println("direction : "
								+ seesElement.getAttribute("direction"));
						System.out.println("background : "
								+ seesElement
										.getElementsByTagName("background")
										.item(0).getTextContent());
					}
				}
			}
		}
		return x;
	}
}
