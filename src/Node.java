import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

import javax.swing.Icon;

public class Node<E> implements Comparator {

	private E element;
	private int id;
	private int weight;
	private LinkedList<Edge<E>> pointers;
	private static int ID = 0;
	private Point p;
	private int r;
	private Color color;
	private Kind kind;
	private boolean selected = false;
	private Rectangle b = new Rectangle();

	public Node() {
		this(null, Integer.MAX_VALUE);
	}

	public Node(E element, int distance) {
		this.element = element;
		id = ID;
		pointers = new LinkedList<Edge<E>>();
		this.weight = distance;
	}

	/**
	 * Construct a new node.
	 */
	public Node(Point p, int r, Color color, Kind kind, int distance, E element) {
		this.element = element;
		this.p = p;
		this.r = r;
		this.color = color;
		this.kind = kind;
		this.weight = distance;
		pointers = new LinkedList<Edge<E>>();
		setBoundary(b);
	}

	// Get and sets
	public E getElement() {
		return element;
	}

	public void setElement(E element) {
		this.element = element;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;		
	}

	// Methods
	public void connectTo(Node<E> that) {
		Edge<E> e1 = new Edge<E>(this, that);
		if (!pointers.contains(e1))
			pointers.add(e1);
		LinkedList<Edge<E>> edge = that.getConnections();
		 
	}

	public void connectTo(Node<E> that, int distance) {
		Edge<E> n1 = new Edge<E>(this, that, distance);
		if (!pointers.contains(n1))
			pointers.add(n1);
	}

	public void disconnect(Edge<E> e1) {
		pointers.remove(e1);
	}

	public LinkedList<Edge<E>> getConnections() {
		return pointers;
	}

	public Iterator<Edge<E>> iterator() {
		return pointers.iterator();
	}
	
	public boolean equals(Node<E> node1, Node<E> node2) {
		if (node1.getElement().equals(node2.getElement()) && node1.getWeight() == node2.getWeight())
			return true;
		else
			return false; 
	}
	

	public String toString() {
		return this.element.toString();
	}

	/**
	 * Calculate this node's rectangular boundary.
	 */
	private void setBoundary(Rectangle b) {
		b.setBounds(p.x - r, p.y - r, 2 * r, 2 * r);
	}

	/**
	 * Draw this node.
	 */
	public void draw(Graphics g) {
		g.setColor(this.color);
		if (this.kind == Kind.Circular) {
			g.fillOval(b.x, b.y, b.width, b.height);
			g.setColor(Color.BLACK);  
			g.drawOval(b.x, b.y, b.width, b.height);
			g.drawOval(b.x+1, b.y+1, b.width-1, b.height-1);
		
			
			//g.drawLine(b.x, b.y, b.width, b.height); 
			g.setColor(this.color);
		} else if (this.kind == Kind.Rounded) {
			g.fillRoundRect(b.x, b.y, b.width, b.height, r, r);
		} else if (this.kind == Kind.Square) {
			g.fillRect(b.x, b.y, b.width, b.height);
		}
		if (selected) {
			g.setColor(Color.darkGray);
			g.drawRect(b.x, b.y, b.width, b.height);
		}
	}
	/**
	 * Return this node's location.
	 */
	public Point getLocation() {
		return p;
	}

	/**
	 * Return true if this node contains p.
	 */
	public boolean contains(Point p) {
		return b.contains(p);
	}

	/**
	 * Return true if this node is selected.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Mark this node as selected.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Collected all the selected nodes in list.
	 */
	public static void getSelected(List<Node> list, List<Node> selected) {
		selected.clear();
		for (Node n : list) {
			if (n.isSelected()) {
				selected.add(n);
			}
		}
	}

	/**
	 * Select no nodes.
	 */
	public static void selectNone(List<Node> list) {
		for (Node n : list) {
			n.setSelected(false);
		}
	}

	/**
	 * Select a single node; return true if not already selected.
	 */
	public static boolean selectOne(List<Node> list, Point p) {
		for (Node n : list) {
			if (n.contains(p)) {
				if (!n.isSelected()) {
					Node.selectNone(list);
					n.setSelected(true);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Select each node in r.
	 */
	public static void selectRect(List<Node> list, Rectangle r) {
		for (Node n : list) {
			n.setSelected(r.contains(n.p));
		}
	}

	/**
	 * Toggle selected state of each node containing p.
	 */
	public static void selectToggle(List<Node> list, Point p) {
		for (Node n : list) {
			if (n.contains(p)) {
				n.setSelected(!n.isSelected());
			}
		}
	}

	/**
	 * Update each node's position by d (delta).
	 */
	public static void updatePosition(List<Node> list, Point d) {
		for (Node n : list) {
			if (n.isSelected()) {
				n.p.x += d.x;
				n.p.y += d.y;
				n.setBoundary(n.b);
			}
		}
	}

	/**
	 * Update each node's radius r.
	 */
	public static void updateRadius(List<Node> list, int r) {
		for (Node n : list) {
			if (n.isSelected()) {
				n.r = r;
				n.setBoundary(n.b);
			}
		}
	}

	/**
	 * Update each node's color.
	 */
	public static void updateColor(List<Node> list, Color color) {
		for (Node n : list) {
			if (n.isSelected()) {
				n.color = color;
			}
		}
	}

	/**
	 * Update each node's kind.
	 */
	public static void updateKind(List<Node> list, Kind kind) {
		for (Node n : list) {
			if (n.isSelected()) {
				n.kind = kind;
			}
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		if (((Node<E>) o1).getWeight() < ((Node<E>) o2).getWeight())
			return 1;
		else
			return -1;
	}
}
