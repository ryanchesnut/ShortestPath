import java.awt.Color;
import java.util.*; 

public class Dijkstra<E> {
		
	private long endTime;
	private int itterations = 1;
	
	public Dijkstra(){
		
	}       
	
	public long getTestTime(){
		return this.endTime;
	}
	public int getItterations(){
		return this.itterations;
		
	}
	public ArrayList<ArrayList<Node<E>>> dijkstra(Node<E> startNode, List<Node<E>> nodes, List<Edge<E>> edges) {
		 
		 
		ArrayList<Node<E>> nodeList = new ArrayList<Node<E>>();
		ArrayList<Node<E>> connectionList = new ArrayList<Node<E>>();
		ArrayList<ArrayList<Node<E>>> pathList = new ArrayList<ArrayList<Node<E>>>(); 
		ArrayList<Node<E>> path = new ArrayList<Node<E>>();
	
		Set<Node<E>> set = Collections.synchronizedSet(new HashSet<Node<E>>()); 
		Node<E> n = new Node<E>();
		Edge<E> tempEdge = null;
		startNode.setWeight(0);
		nodeList.add(startNode);
		n = startNode;
		int a = 0; 
		int weight;	
		long startTime = System.nanoTime();	
		while(a <= nodes.size()+1){
			//System.out.println("Start Node: " + n.getElement() + " " + n.getConnections() + " " + n.getWeight() + " "); 
			for (int i = 0; i < n.getConnections().size(); i++){
				tempEdge = (Edge<E>) n.getConnections().get(i); // visit connecting node
				set.add(tempEdge.getTwo()); // add node to a list 
				if (tempEdge.getTwo().getWeight() >= tempEdge.getDistance() + n.getWeight()){ //if node weight is less than 
					weight = tempEdge.getDistance() + n.getWeight();                         // dist + connNode weight, update 
					tempEdge.getTwo().setWeight(weight);
					//System.out.println(tempEdge.getTwo().toString()); 
				}
			}				
			//System.out.println("NodeList: "+ nodeList.toString()); 
			set.removeAll(nodeList); 
			//System.out.println("Set: "+ set.toString()); 
			connectionList.clear(); 
			connectionList.addAll((Collection<? extends Node<E>>) set); 
			//System.out.println("Connection List:  " + connectionList.toString());
			Node<E> min;
			if(connectionList.size() == 0){
				break; 
			}
			else{
				min = connectionList.get(0);
			}
			connectionList.remove(min);
			for(int j = 0; j < connectionList.size(); j++){
				Node<E> temp = connectionList.get(j); 
				//System.out.println("Min: " + min.toString() + " Temp: " + temp.toString());  
				if(min.getWeight() >= temp.getWeight()){
					min = temp; 
					//System.out.println("NodeList: "+nodeList.toString()); 
				}
			}
			connectionList.remove(min);
			nodeList.add(min); 
			n = min; 
			//System.out.println("Min: "+min.toString());
			a++;
			itterations++; 
		//System.out.println(path.toString()); 
		}//end while
		endTime = System.nanoTime() - startTime;
		
	 // Find the path to each node 
		
		a = 0; 
		n = startNode;
		//System.out.println("StartNode: " + n.toString()); 
		for (int i = 0; i < n.getConnections().size(); i++){
			tempEdge = (Edge<E>) n.getConnections().get(i);
			path.add(n);
			path.add(tempEdge.getTwo());
			pathList.add(path); 
			path = new ArrayList<Node<E>>(); 
		}
		//System.out.println("PathList : " + pathList.toString()); 
		Node<E> tempNode; 
		Node<E> tempNode2 = null;
	//while(a <= 4){
		for(int i = 0; i < nodes.size() -1; i++){
			tempNode = pathList.get(i).get(pathList.get(i).size()-1);
			//System.out.println("TempNode : " + tempNode.toString() + " " + tempNode.getConnections().toString()); 
			
			for (int j = 0; j < tempNode.getConnections().size(); j++){
				tempEdge = tempNode.getConnections().get(j);
				tempNode2 = tempEdge.getTwo(); 
				//System.out.println("TempNode2 :" + tempNode2.toString());
				if (tempNode2.getWeight() == tempNode.getWeight() + tempEdge.getDistance()){
					path  = new ArrayList<Node<E>>(pathList.get(i));
					//System.out.println("Path before : " + path.toString()); 
					path.add(tempEdge.getTwo()); 
					//System.out.println("Path after : " + path.toString());
					pathList.add(path);
					//System.out.println("PathList -->: " +pathList.toString());
				}	
				 
				}
					
			}
			 
		//System.out.println("PathList -->: " +pathList.toString());
		System.out.println("Dijkstra time: "+ endTime);
		return pathList; 		
	}
	
	// initializes the Graph Nodes to infinity
	public void resetGraph(List<Node<E>> nodes) {
		for (Node<E> n: nodes) {
			n.setWeight(Integer.MAX_VALUE);
		}
	}

}