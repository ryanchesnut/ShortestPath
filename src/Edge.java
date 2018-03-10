import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

public class Edge<E> implements Comparable<Node<E>> {

	private Node<E> one, two;
	private int distance;
	Point p;
   

    public Edge(Node<E> n1, Node<E> n2) {
          this.one = n1;
          this.two = n2;
        }

	public Edge(Node<E> one, Node<E> two, int distance) {
		this.one = one;
		this.two = two;
		this.distance = distance;
	}

	public void delete(Node<E> one, Node<E> two) {
		this.one = null;
		this.two = null;
	}

	// Get and set
	public Node<E> getOne() {
		return one;
	}

	public Node<E> getTwo() {
		return two;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public boolean equals(Edge<E> edge1, Edge<E> edge2) {
		if (edge1.getOne() == edge2.getOne() && edge1.getTwo() == edge2.getTwo()
				&& distance == edge2.getDistance())
			return true; 
		else
		return false; 
	}

	public void draw(Graphics g) {
		Point p1 = one.getLocation();
		Point p2 = two.getLocation();
		g.setColor(Color.darkGray);
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
		
	}

	public Point getLocation() {
		return p;
	}

	public String toString() {
		return "(" + one.getElement() + ", " + two.getElement() + "): "
				+ distance;
	}

	@Override
	public int compareTo(Node<E> arg0) {
		
		return 0;
	}

}
