import java.util.ArrayList;

public class Graph<E> {

	private ArrayList<Node<E>> nodes; 
	
	public Graph(){
		nodes = new ArrayList<Node<E>>();
	}
	
	public boolean addNode(Node<E> vertex){
		if(nodes.contains(vertex))
				return false; 
		nodes.add(vertex); 
		return true; 
	}
	
	public int deleteNode(Node<E> vertex){
		if (nodes == null)
			return -1; 
		else
			nodes.remove(vertex);
		return 0; 
	}
	public boolean contains(Node<E> vertex){
		return nodes.contains(vertex);
	}
	
	public Node<E> get(int index){
		return nodes.get(index); 
	}
	
	public boolean equals(Graph<E> that){
		if(that.nodes.size() != nodes.size())
		return false; 
		ArrayList<Node<E>> temp = new ArrayList<Node<E>>(that.nodes);
		return temp.retainAll(nodes); 
	}

	public int count(){
		return nodes.size();
		}
	}


