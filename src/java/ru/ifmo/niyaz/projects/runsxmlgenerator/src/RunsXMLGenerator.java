import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class RunsXMLGenerator {
	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {
		if (args.length != 1) {
			System.err.println("Usage: java RunsXMLGenerator <xml>");
			return;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new File(args[0]));
		Scanner z = new Scanner(new File("challenges"));
		List<String> P = new ArrayList<String>();
		while (z.hasNext()) {
			P.add(z.next());
		}
		PrintWriter out = new PrintWriter("runs.xml");
		out.println("<runs>");
		NodeList root = doc.getChildNodes();
		for (int i1 = 0; i1 < root.getLength(); i1++) {
			if (!root.item(i1).getNodeName().equals("standings")) {
				continue;
			}
			NodeList standings = root.item(i1).getChildNodes();
			for (int i2 = 0; i2 < standings.getLength(); i2++) {
				if (!standings.item(i2).getNodeName().equals("contest")) {
					continue;
				}
				NodeList contest = standings.item(i2).getChildNodes();
				Map<String, Integer> problems = new HashMap<String, Integer>();
				for (int i = 0; i < contest.getLength(); i++) {
					Node v = contest.item(i);
					if (v.getNodeName().equals("challenge")) {
						NodeList list = v.getChildNodes();
						int cn = 0;
						for (int j = 0; j < list.getLength(); j++) {
							Node p = list.item(j);
							if (!p.getNodeName().equals("problem")) {
								continue;
							}
							problems.put(p.getAttributes()
									.getNamedItem("alias").getNodeValue(), cn++);
						}
						if (problems.size() != P.size()) {
							System.err
									.println("number of challenge ids doesn't equals to the number of problems in the contest");
							return;
						}
					}
					if (!v.getNodeName().equals("session")) {
						continue;
					}
					String sessionID = v.getAttributes().getNamedItem("id")
							.getNodeValue();
					sessionID = sessionID.replace("day1", "day2");
					NodeList list = v.getChildNodes();
					int cn = 1;
					for (int j = 0; j < list.getLength(); j++) {
						Node u = list.item(j);
						if (!u.getNodeName().equals("problem"))
							continue;
						boolean found = false;
						NodeList ll = u.getChildNodes();
						for (int k = 0; k < ll.getLength(); k++) {
							if (ll.item(k).getNodeName().equals("run")) {
								found = true;
								break;
							}
						}
						if (!found)
							continue;
						String score = u.getAttributes()
								.getNamedItem("score").getNodeValue();
						int pID = problems.get(u.getAttributes()
								.getNamedItem("alias").getNodeValue());
						out.println("<run id = '" + sessionID + "." + (++cn)
								+ "' session-id = '" + sessionID
								+ "' problem-id = '" + P.get(pID)
								+ "' accepted = 'yes' score = '" + score
								+ "'/>");
					}
				}
			}
		}
		out.println("</runs>");
		out.close();
	}
}
