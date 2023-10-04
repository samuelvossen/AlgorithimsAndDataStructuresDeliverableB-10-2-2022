import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DelivB {

	private File inputFile;
	private File outputFile;
	private PrintWriter output;
	private Graph graph;

	// Constructor - DO NOT MODIFY
	public DelivB(File in, Graph gr) {
		inputFile = in;
		graph = gr;

		// Set up for writing to a file
		try {
			// Use input file name to create output file in the same location
			String inputFileName = inputFile.toString();
			String outputFileName = inputFileName.substring(0, inputFileName.length() - 4).concat("_out.txt");
			outputFile = new File(outputFileName);

			// A Printwriter is an object that can write to a file
			output = new PrintWriter(outputFile);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}

		// Calls the method that will do the work of deliverable B
		runDelivB();

		output.flush();
	}

	// *********************************************************************************
	// This is where your work starts
	private static int time;

	private void runDelivB() {
		// Delete these lines when you add functionality

		System.out.println("DFS of graph");
		System.out.println("");
		System.out.println("Node	Disc	Finish");

		output.println("DFS of graph");
		output.println("");
		output.println("Node	Disc	Finish");
		ArrayList<Node> m = graph.getNodeList();
		setToWhite(m);
		time = 0;
		dfsVisit(findStart(m));
		Collections.sort(m, new compareAbbrev());
		while (HasWhites(m)) {
			dfsVisit(nextWhiteNode(m));
		}

		ArrayList<Node> g = graph.getNodeList();
		Collections.sort(g, new compareAbbrev());
		for (Node e : g) {
			System.out.println(e.getAbbrev() + "\t" + e.getDescoveryTime() + "\t" + e.getFinishTime());
			output.println(e.getAbbrev() + "\t" + e.getDescoveryTime() + "\t" + e.getFinishTime());
		}

		System.out.println("");
		System.out.println("Edge Classification:");
		System.out.println("");
		System.out.println("Edge	Type");

		output.println("");
		output.println("Edge Classification:");
		output.println("");
		output.println("Edge	Type");
		ArrayList<Edge> e = graph.getEdgeList();
		edgeTypeFinder(e);
		for (Edge edge : e) {
			System.out
					.println(edge.getTail().getAbbrev() + "->" + edge.getHead().getAbbrev() + "    " + edge.getType());
			output.println(edge.getTail().getAbbrev() + "->" + edge.getHead().getAbbrev() + "    " + edge.getType());
		}

	}

	public static Node findStart(ArrayList<Node> nodelist) {
		Node start = null;
		String s = "s";
		for (Node d : nodelist) {
			if (d.getValue().compareToIgnoreCase(s) == 0) {
				start = d;
			}
		}
		return start;
	}

	public static void setToWhite(ArrayList<Node> m) {
		for (int i = 0; i < m.size(); i++) {
			m.get(i).setColor("WHITE");
			m.get(i).setParentNode(null);
		}
	}

	public static void dfsVisit(Node node) {
		time++;
		node.setDescoveryTime(time);
		node.setColor("gray");
		ArrayList<Edge> x = node.getOutgoingEdges();
		ArrayList<Edge> v = new ArrayList<Edge>();
		for (int i = 0; i < x.size(); i++) {
			v.add(x.get(i));
		}
		Collections.sort(v, new CompareEdges());
		for (int i = 0; i < v.size(); i++) {
			if (v.get(i).getHead().getColor().equalsIgnoreCase("white")) {
				v.get(i).getHead().setParentNode(node);
				dfsVisit(v.get(i).getHead());
			}
		}
		node.setColor("black");
		time++;
		node.setFinishTime(time);
	}

	public static void edgeTypeFinder(ArrayList<Edge> e) {
		for (Edge edge : e) {
			if (edge.getTail().getDescoveryTime() < edge.getHead().getDescoveryTime()
					&& edge.getTail().getFinishTime() > edge.getHead().getFinishTime()) {
				if (edge.getHead().getParentNode().equals(edge.getTail())) {
					edge.setType("Tree");
				} else {
					edge.setType("Forward");
				}
			}
			if (edge.getTail().getDescoveryTime() > edge.getHead().getDescoveryTime()
					&& edge.getTail().getFinishTime() < edge.getHead().getFinishTime()) {
				edge.setType("Back");
			}
			if (edge.getHead().getDescoveryTime() < edge.getHead().getFinishTime()
					&& edge.getHead().getFinishTime() < edge.getTail().getDescoveryTime()
					&& edge.getTail().getDescoveryTime() < edge.getTail().getFinishTime()) {
				edge.setType("cross");
			}
		}
	}

	public static class CompareEdges implements Comparator<Edge> {

		@Override
		public int compare(Edge o1, Edge o2) {
			// TODO Auto-generated method stub
			return compareTo(o1, o2);
		}

		public static int compareTo(Edge e1, Edge e2) {
			if (e1.getDistance() > e2.getDistance()) {
				return 1;
			}
			if (e1.getDistance() < e2.getDistance()) {
				return -1;
			}
			String a = e1.getHead().getAbbrev();
			String b = e2.getHead().getAbbrev();
			return a.compareToIgnoreCase(b);

		}
	}

	public static class compareAbbrev implements Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) {
			// TODO Auto-generated method stub
			return compareAbbrev(o1, o2);
		}

		public static int compareAbbrev(Node nodeOne, Node nodeTwo) {
			return nodeOne.getAbbrev().compareToIgnoreCase(nodeTwo.getAbbrev());
		}

	}

	public static Node nextWhiteNode(ArrayList<Node> nodeList) {
		Node b = null;
		for (Node a : nodeList) {
			if (a.getColor().compareToIgnoreCase("WHITE") == 0) {
				b = a;
				break;
			}
		}
		return b;
	}

	public static boolean HasWhites(ArrayList<Node> nodeList) {
		boolean b = false;
		for (Node a : nodeList) {
			if (a.getColor().compareToIgnoreCase("WHITE") == 0) {
				b = true;
				break;
			}
		}
		return b;
	}

}
