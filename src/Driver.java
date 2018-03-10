import java.util.ArrayList;

//http://algs4.cs.princeton.edu/44sp/
//http://www.dreamincode.net/forums/topic/163842-graphs-tutorial/

public class Driver<E> {

	private Graph<E> graph;

	public static int dijkstra(Node startNode, Node endNode) {
		ArrayList<Node> nodeList = new ArrayList<Node>();
		Node tempNode = new Node();
		Node tempNode2 = new Node();
		Node minNode = new Node();
		Edge tempEdge = null;
		startNode.setWeight(0);
		//
		nodeList.add(startNode);
		tempNode2 = startNode;
		boolean isTrue = true; 
		int cost = 0; 
		
		while(isTrue){
			System.out.println("==================================");
			System.out.println("Node =  "+tempNode2.toString());
			int minWeight = Integer.MAX_VALUE;
			for (int i = 0; i < tempNode2.getConnections().size(); i++) {
				tempEdge = (Edge) tempNode2.getConnections().get(i);
				tempNode = tempEdge.getTwo();
				tempNode.setWeight(tempEdge.getDistance() + tempNode2.getWeight());
				System.out.println();
				System.out.println("Index: " + i + " Node: " + tempNode.toString() + " Distance: " +tempEdge.getDistance());
				System.out.println("Weight " + (tempNode.getWeight()));
			
				if (minWeight > tempNode.getWeight() + tempNode2.getWeight()) {
					minWeight = tempNode.getWeight() + tempNode2.getWeight();
					minNode = tempNode;
					
					System.out.println("Min node -->  "+ minNode.getWeight());
				}
				if (tempNode == endNode){
					isTrue = false;
					cost = tempNode.getWeight(); 
				}
			}
			//
			nodeList.add(minNode);
			tempNode2 = minNode;
			System.out.println("MinNode: " + minNode.toString()+ " minWeight: " + minNode.getWeight());
				System.out.println("==================================");
		
		}
		System.out.println("====================================="); 
		System.out.println("| Minimum cost from " + startNode + " to " + endNode.toString() + " is: "+ cost + "  |");
		System.out.println("=====================================");
		System.out.println("Path");
		for(int i = 0; i<nodeList.size(); i++){
			System.out.print(nodeList.get(i) + " -> ");
		}
		System.out.println("end");
		System.out.println("\n");
		return cost;
	}

	public static void main(String[] args) {

		String st1 = "r1";
		String st2 = "r2";
		String st3 = "r3";
		String st4 = "r4";
		String st5 = "r5";
		String st6 = "r6";

		Node r1 = new Node(st1, 0);
		Node r2 = new Node(st2, 0);
		Node r3 = new Node(st3, 0);
		Node r4 = new Node(st4, 0);
		Node r5 = new Node(st5, 0);
		Node r6 = new Node(st6, 0);

		r1.connectTo(r2, 2);
		r1.connectTo(r4, 1);
		r1.connectTo(r3, 4);

		r2.connectTo(r1, 2);
		r2.connectTo(r6, 4);

		r3.connectTo(r4, 2);
		r3.connectTo(r5, 4);
		r3.connectTo(r1, 4);

		r4.connectTo(r6, 2);
		r4.connectTo(r1, 1);
		r4.connectTo(r3, 2);

		r5.connectTo(r6, 3);
		r5.connectTo(r3, 4);

		r6.connectTo(r5, 3);
		r6.connectTo(r4, 2);
		r6.connectTo(r2, 4);

		Graph g1 = new Graph();
		g1.addNode(r1);
		g1.addNode(r2);
		g1.addNode(r3);
		g1.addNode(r4);
		g1.addNode(r5);
		g1.addNode(r6);

		System.out.println(g1.count());
		System.out.println(r1.toString() + " " + r1.getWeight());
		System.out.println(r2.toString() + " " + r2.getWeight());
		System.out.println(r3.toString() + " " + r3.getWeight());
		System.out.println(r4.toString() + " " + r4.getWeight());
		System.out.println(r5.toString() + " " + r5.getWeight());
		System.out.println(r6.toString() + " " + r6.getWeight());

		resetGraph(g1);

		System.out.println("Number of nodes: " + g1.count());

		System.out.println(r1.toString() + " " + r1.getWeight());
		System.out.println(r2.toString() + " " + r2.getWeight());
		System.out.println(r3.toString() + " " + r3.getWeight());
		System.out.println(r4.toString() + " " + r4.getWeight());
		System.out.println(r5.toString() + " " + r5.getWeight());
		System.out.println(r6.toString() + " " + r6.getWeight());

		r1.getConnections();

		Node startNode = r5;
		Node endNode = r1;
		
		dijkstra(r5, r1);
		dijkstra(r5, r2);
		dijkstra(r5, r3);
		dijkstra(r5, r4);
		dijkstra(r5, r6);

	}

	private static void resetGraph(Graph g) {
		for (int i = 0; i < g.count(); i++) {
			g.get(i).setWeight(Integer.MAX_VALUE);
		}
	}
}
