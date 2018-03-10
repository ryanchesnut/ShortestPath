import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.*;

/**
 * @author John B. Matthews; distribution per GPL.
 * @param <E>
 */

// (Done)To Do: Save, Open files
// (Done) Clean up matrix 

// Get shortest path in a list

// Animation (timer)

// Delete button to delete nodes

// Test efficency, Order of magnitude

// Routing table 

public class GraphPanel<E> extends JComponent {
	private static final int WIDE = 1350;
	private static final int HIGH = 650;
	private static final int RADIUS = 30;
	private static final int UPDATE_PERIOD = 10;
	private static final Random rnd = new Random();
	private ControlPanel control = new ControlPanel();
	private int radius = RADIUS;
	private Kind kind = Kind.Circular;
	private List<Node> nodes = new ArrayList<Node>();
	private List<Node> selected = new ArrayList<Node>();
	private List<Edge> edges = new ArrayList<Edge>();
	private List<Node> pathlist = new ArrayList<Node>();
	private List<Point> positionList = new ArrayList<Point>();
	private ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
	private Node startNode;
	private Point mousePt = new Point(WIDE / 3, HIGH / 3);
	private Rectangle mouseRect = new Rectangle();
	private boolean selecting = false;
	private long dTime; 
	private long bfTime; 
	private int dItt; 
	private int bfItt; 
	private double orderObellman; 
	private double orderOdijkstra; 
	// Attributes of moving object
	private int x = 0, y = 0; // top-left (x, y)
	private int size = 50; // width and height
	private int xSpeed = 3, ySpeed = 5; // displacement per step in x, y

	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				JFrame f = new JFrame("GraphPanel");
				JPanel jp1 = new JPanel();
				jp1.setSize(100,100); 
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				GraphPanel gp = new GraphPanel();
				f.add(gp.control, BorderLayout.NORTH);
				f.add(jp1, BorderLayout.SOUTH); 
				f.add(new JScrollPane(gp), BorderLayout.CENTER);
				f.getRootPane().setDefaultButton(gp.control.defaultButton);
				f.pack();
				f.setVisible(true);

			}
		});
	}

	public GraphPanel() {
		this.setOpaque(true);
		this.addMouseListener(new MouseHandler());
		this.addMouseMotionListener(new MouseMotionHandler());
		ActionListener updateTask = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				update(); // update the (x, y) position
				repaint(); // Refresh the JFrame, callback paintComponent()
			}
		};
		// Allocate a Timer to run updateTask's actionPerformed() after every
		// delay msec
		new Timer(UPDATE_PERIOD, updateTask).start();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDE, HIGH);
	}

	@Override
	public void paintComponent(Graphics g) {

		g.setColor(new Color(0x00f0f0f0));
		g.fillRect(0, 0, getWidth(), getHeight());
		for (Edge e : edges) {
			e.draw(g);
		}

		for (Edge e : edges) {
			Node n1 = e.getOne();
			Node n2 = e.getTwo();
			revalidate();
			repaint();
			g.setColor(Color.black);
			String s1 = Integer.toString(e.getDistance());
			g.drawString(s1, (n2.getLocation().x + n1.getLocation().x) / 2,
					(n2.getLocation().y + n1.getLocation().y) / 2);
			revalidate();
			repaint();
		}
		for (Node n : nodes) {
			n.draw(g);
			String s1 = Integer.toString(n.getWeight());
			String s2 = n.getElement().toString();
			g.drawString(s1, n.getLocation().x - 30, n.getLocation().y - 35);
			g.drawString(s2, n.getLocation().x - 5, n.getLocation().y + 2);

		}

		if (selecting) {
			g.setColor(Color.darkGray);
			g.drawRect(mouseRect.x, mouseRect.y, mouseRect.width,
					mouseRect.height);
		}
		int i = 0; 
		int h = 0; 
		
		if(matrix.size() != 0){
						
			g.setColor(Color.black); 
			g.drawString("Order of magnitude = O(" + nodes.size() * edges.size()  + ")", 400, 20);
			//Draw the dijkstra efficiency
			g.drawString("Bellman Ford Matrix ", 27, 15); 
			
			for(Node<E> n : nodes){
				g.drawString(n.getElement().toString(), 27 +h, 30);
				g.drawString("____", 23 +h, 32); 
				g.drawString(n.getElement().toString() + " |", 5, 45 + i); 
				h = h + 30; 
				i = i + 15;
			}	
			h = 0; 
			i = 0; 
			for (int k = 0; k < nodes.size(); k++) {
				for (int j = 0; j <nodes.size(); j++){
				g.drawString("[" + matrix.get(k).get(j).toString() + "]", 27 +h , 45+i);
				 h = h + 30;
			}
				h = 0; 
				i = i + 15;
				repaint(); 
			
		}
	}

		if (startNode != null) {
			x = startNode.getLocation().x;
			y = startNode.getLocation().y;

		}
		//g.setColor(Color.BLACK);
		//g.fillOval(x, y, size, size);
	}
		

	public void update() {
		GeneralPath one = new GeneralPath();

		if (nodes.size() != 0) {
			x += xSpeed;
			y += ySpeed;

			if (x > WIDE - size || x < 0) {
				xSpeed = -xSpeed;
			}
			if (y > HIGH - size || y < 0) {
				ySpeed = -ySpeed;
			}
		}
	}

	private class MouseHandler extends MouseAdapter {

		@Override
		public void mouseReleased(MouseEvent e) {
			selecting = false;
			mouseRect.setBounds(0, 0, 0, 0);
			if (e.isPopupTrigger()) {
				showPopup(e);
			}
			e.getComponent().repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mousePt = e.getPoint();
			if (e.isShiftDown()) {
				Node.selectToggle(nodes, mousePt);
			} else if (e.isPopupTrigger()) {
				Node.selectOne(nodes, mousePt);
				showPopup(e);
			} else if (Node.selectOne(nodes, mousePt)) {
				selecting = false;
			} else {
				Node.selectNone(nodes);
				selecting = true;
			}
			e.getComponent().repaint();
		}

		private void showPopup(MouseEvent e) {
			control.popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private class MouseMotionHandler extends MouseMotionAdapter {

		Point delta = new Point();

		@Override
		public void mouseDragged(MouseEvent e) {
			if (selecting) {
				mouseRect.setBounds(Math.min(mousePt.x, e.getX()),
						Math.min(mousePt.y, e.getY()),
						Math.abs(mousePt.x - e.getX()),
						Math.abs(mousePt.y - e.getY()));
				Node.selectRect(nodes, mouseRect);
			} else {
				delta.setLocation(e.getX() - mousePt.x, e.getY() - mousePt.y);
				Node.updatePosition(nodes, delta);
				mousePt = e.getPoint();
			}
			e.getComponent().repaint();
		}
	}

	public JToolBar getControlPanel() {

		return control;
	}

	private class ControlPanel extends JToolBar {

		private Action newNode = new NewNodeAction("New");
		private Action clearAll = new ClearAction("Clear");
		private Action kind = new KindComboAction("Kind");
		private Action color = new ColorAction("Color");
		private Action connect = new ConnectAction("Connect");
		private Action delete = new DeleteAction("Delete");
		private Action random = new RandomAction("Random");
		private Action findStartNode = new SetStartNodeAction("Start Node");
		private Action resetNode = new ResetNodeAction("Reset Node");
		private Action seqNodes = new SequenceAction("Sequence");
		private Action open = new OpenAction("Open");
		private Action save = new SaveAction("Save");
		

		private Action dijkstra = new DijkstraPathAction("Dijkstra");
		private Action bellmanFord = new BellmanFordPathAction("Bellman/Ford");
		private JButton defaultButton = new JButton(newNode);
		private JComboBox kindCombo = new JComboBox();
		private ColorIcon hueIcon = new ColorIcon(Color.red);
		private JPopupMenu popup = new JPopupMenu();
		
			

		ControlPanel() {
			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			this.setBackground(Color.lightGray);
			this.add(new JButton(open));
			this.add(new JButton(save));
			this.addSeparator();
			this.add(defaultButton);
			this.add(new JButton(clearAll));
			this.add(new JButton(connect));
			this.add(new JButton(random));
			this.add(new JButton(seqNodes));
			this.addSeparator();
			this.add(new JButton(dijkstra));
			this.add(new JButton(bellmanFord));
			this.add(new JButton(color));
			this.add(new JLabel(hueIcon));

			JSpinner js = new JSpinner();
			js.setModel(new SpinnerNumberModel(RADIUS, 5, 100, 5));

			js.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					JSpinner s = (JSpinner) e.getSource();
					radius = (Integer) s.getValue();
					Node.updateRadius(nodes, radius);
					GraphPanel.this.repaint();
				}
			});

			popup.add(new JMenuItem(newNode));
			popup.add(new JMenuItem(delete));
			popup.add(new JMenuItem(connect));
			popup.add(new JMenuItem(findStartNode));
			popup.add(new JMenuItem(resetNode));
			popup.add(new JMenuItem(color));

			JMenu subMenu = new JMenu("Kind");
			for (Kind k : Kind.values()) {
				kindCombo.addItem(k);
				subMenu.add(new JMenuItem(new KindItemAction(k)));
			}
			popup.add(subMenu);
			kindCombo.addActionListener(kind);
		}

	private class KindItemAction extends AbstractAction {

			private Kind k;

			public KindItemAction(Kind k) {
				super(k.toString());
				this.k = k;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				kindCombo.setSelectedItem(k);
			}
		}
	}

	private class ClearAction extends AbstractAction {

		public ClearAction(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {
			nodes.clear();
			edges.clear();
			startNode = null;
			matrix.clear(); 
			repaint();
		}
	}

	private class ColorAction extends AbstractAction {

		public ColorAction(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {
			Color color = control.hueIcon.getColor();
			color = JColorChooser.showDialog(GraphPanel.this, "Choose a color",
					color);
			if (color != null) {
				Node.updateColor(nodes, color);
				control.hueIcon.setColor(color);
				control.repaint();
				repaint();
			}
		}
	}

	private class ConnectAction extends AbstractAction {

		public ConnectAction(String name) {
			super(name);
		}

		public boolean isNumeric(String str) {
			try {
				double d = Double.parseDouble(str);
			} catch (NumberFormatException nfe) {
				return false;
			}
			return true;
		}

		public void actionPerformed(ActionEvent e) {

			Node.getSelected(nodes, selected);
			if (selected.size() > 1) {
				for (int i = 0; i < selected.size() - 1; ++i) {
					Node n1 = selected.get(i);
					Node n2 = selected.get(i + 1);

					int d = 0;
					String s = JOptionPane
							.showInputDialog("Enter the edge distance");

					if (!isNumeric(s)) {
						JOptionPane.showMessageDialog(null, "ErrorMsg",
								"Failure", JOptionPane.ERROR_MESSAGE);
						break;
					}
					d = Integer.parseInt(s);
					n1.connectTo(n2, d);
					n2.connectTo(n1, d);
					Edge newEdge = new Edge(n1, n2, d);
					edges.add(newEdge);

				}//

			}
			repaint();

		}
	}

	private class DeleteAction extends AbstractAction {

		public DeleteAction(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {
			ListIterator<Node> iter = nodes.listIterator();
			while (iter.hasNext()) {
				Node n = iter.next();
				if (n.isSelected()) {
					deleteEdges(n);
					iter.remove();
				}
			}
			repaint();
		}

		private void deleteEdges(Node n) {
			ListIterator<Edge> iter = edges.listIterator();
			while (iter.hasNext()) {
				Edge e = iter.next();
				if (e.getOne() == n || e.getTwo() == n) {
					iter.remove();
				}
			}
		}
	}

	private class KindComboAction extends AbstractAction {

		public KindComboAction(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {
			JComboBox combo = (JComboBox) e.getSource();
			kind = (Kind) combo.getSelectedItem();
			Node.updateKind(nodes, kind);
			repaint();
		}
	}

	private class SaveAction extends AbstractAction {

		public SaveAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			File dir = new File("c:/users/ryan/workspace/DataCommProject");
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(dir);
			fc.showSaveDialog(GraphPanel.this);
			File file = fc.getSelectedFile();
			Writer writer = null;

			try {
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(file + ".txt"), "utf-8"));
				for (int i = 0; i < nodes.size(); i++)
					writer.write("\nNODE " + nodes.get(i).toString() + " "
							+ nodes.get(i).getLocation().x + " "
							+ nodes.get(i).getLocation().y + " ");
				for (int i = 0; i < edges.size(); i++)
					writer.write("\n CONNECTS " + edges.get(i).toString());
			} catch (IOException ex) {
				// report
			} finally {
				try {
					writer.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	private class OpenAction extends AbstractAction {

		public OpenAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			edges.clear();
			nodes.clear();
			File file;
			File dir = new File("c:/users/ryan/workspace/DataCommProject");
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(dir);
			fc.showOpenDialog(GraphPanel.this);
			String nodeName;
			String node1 = null;
			String node2 = null;
			int weight = 0;
			int i = 0, j = 0, k = 0;
			Color color = Color.RED;
			file = fc.getSelectedFile();
			Scanner scan = null;
			try {
				scan = new Scanner(file);
			} catch (FileNotFoundException e1) {

				e1.printStackTrace();
			}

			while (scan.hasNext()) {
				if (scan.hasNext("NODE")) {
					scan.next();
					nodeName = scan.next();
					Point xy = new Point(scan.nextInt(), scan.nextInt());
					nodes.add(new Node(xy, radius, color, kind, weight,
							nodeName));

					// System.out.println(nodes.toString());
				}

				else if (scan.hasNext("CONNECTS")) {
					scan.next();
					node1 = scan.next();
					node2 = scan.next();
					k = scan.nextInt();
					node1 = node1.replaceAll("[^a-zA-Z0-9]", "");
					node2 = node2.replaceAll("[^a-zA-Z0-9]", "");
					Node tempNode1 = null;
					Node tempNode2 = null;
					// System.out.println(node1 + " " +node2);
					for (Node n : nodes) {

						if (node1.toLowerCase().equals(
								n.getElement().toString().toLowerCase())) {
							tempNode1 = n;
						}
						if (node2.toLowerCase().equals(
								n.getElement().toString().toLowerCase())) {
							tempNode2 = n;
						}
						if (tempNode1 != null && tempNode2 != null) {

							Edge edge = new Edge(tempNode1, tempNode2, k);
							// System.out.println(edge.getOne()+" "+edge.getTwo()+" "+edge.getDistance());
							if (!edges.contains(edge)) {
								edges.add(edge);
								tempNode1.connectTo(tempNode2, k);
								tempNode2.connectTo(tempNode1, k);
								tempNode1 = null;
								tempNode2 = null;
							}
						}
					}

				}
			}
			// System.out.println(edges.toString());
			repaint();
		}
	}

	private class SetStartNodeAction extends AbstractAction {
		public SetStartNodeAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (startNode != null)
				JOptionPane.showMessageDialog(null,
						"Only one start node allowed", "Failure",
						JOptionPane.ERROR_MESSAGE);

			Node.getSelected(nodes, selected);
			if (selected.size() == 1 && startNode == null) {

				startNode = selected.get(0);

				startNode.updateColor(nodes, Color.BLUE);

				repaint();

			}

		}

	}

	private class ResetNodeAction extends AbstractAction {
		public ResetNodeAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			for (Node n : nodes) {
				startNode = null;
				n.updateColor(nodes, Color.RED);
				repaint();
			}
		}
	}

	private class DijkstraPathAction extends AbstractAction {

		public DijkstraPathAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Dijkstra d1 = new Dijkstra();
			if (startNode != null) {
				d1.resetGraph(nodes);
				pathlist = d1.dijkstra(startNode, nodes, edges);
				dTime = d1.getTestTime(); 
				System.out.println(dTime +" milliseconds");
				dItt = d1.getItterations(); 
				System.out.println(dItt +" itterations");
				System.out.println("Order of magnitude = O(" + edges.size() +  nodes.size() * nodes.size() + ")"); 
							
				update();
				repaint();
			} else
				JOptionPane.showMessageDialog(null,
						"Please choose a start node", "Failure",
						JOptionPane.ERROR_MESSAGE);

		}
	}

	private class BellmanFordPathAction extends AbstractAction {

		public BellmanFordPathAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (nodes != null) {
				BellmanFord bf = new BellmanFord();
				matrix = bf.bellmanFord(nodes);
				bfTime = bf.getTestTime(); 
				System.out.println(bfTime +" milliseconds");
				bfItt = bf.getItterations(); 
				System.out.println(bfItt +" itterations");
				System.out.println("Order of magnitude = O(" + nodes.size() * edges.size()  + ")"); 
			}
		}
	}

	private class SetNodeNameAction extends AbstractAction {

		public SetNodeNameAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Node.getSelected(nodes, selected);

		}
	}

	private class NewNodeAction extends AbstractAction {

		public NewNodeAction(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {

			Font f = new Font("Dialog", Font.BOLD, 12);
			int weight = 0;
			Node.selectNone(nodes);
			Point c = new Point((int) mousePt.getX(), (int) mousePt.getY());
			Color color = control.hueIcon.getColor();
			String s = JOptionPane.showInputDialog("Give the node a label?");
			if (s == null)
				s = "";
			Node n = new Node(c, radius, color, kind, weight, s);
			n.setSelected(true);
			nodes.add(n);

			repaint();
		}

	}

	private class SequenceAction extends AbstractAction {

		public SequenceAction(String name) {
			super(name);
		}

		public boolean isNumeric(String str) {
			try {
				double d = Double.parseDouble(str);
			} catch (NumberFormatException nfe) {
				return false;
			}
			return true;
		}

		public void actionPerformed(ActionEvent e) {
			String str = JOptionPane
					.showInputDialog("How many nodes to generate?");
			if (!isNumeric(str)) {
				JOptionPane.showMessageDialog(null, "ErrorMsg", "Failure",
						JOptionPane.ERROR_MESSAGE);
			} else {
				int j = Integer.parseInt(str);
				int weight = 0;
				char c1 = (char) (Math.abs(rnd.nextInt() % (90 - 65)) + 65);
				int y = 0;
				int x = 75;
				int k = (int) j / 2;
				int spaceY = 25;
				int spaceX = 0;

				for (int i = 0; i < j; i++) {
					if (i % k == 0) {
						spaceY += 75;
						spaceX = 0;
					}

					Point p = new Point(x + spaceX, y + spaceY);

					spaceX += 75;
					String s = Integer.toString(Math.abs(rnd.nextInt() % 50));
					StringBuilder s1 = new StringBuilder().append(c1).append(i);
					nodes.add(new Node(p, radius, Color.RED, kind, weight, s1));
				}
				repaint();
			}
		}
	}

	private class RandomAction extends AbstractAction {

		public RandomAction(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {
			int weight = 0;
			for (int i = 0; i < 10; i++) {
				Point p = new Point(rnd.nextInt(getWidth() / 2),
						rnd.nextInt(getHeight() / 2));
				String s = Integer.toString(Math.abs(rnd.nextInt() % 50));
				int a = Math.abs(rnd.nextInt() % (90 - 65)) + 65;
				char c1 = (char) (a);
				StringBuilder s1 = new StringBuilder().append(c1).append(s);
				nodes.add(new Node(p, radius, Color.RED, kind, weight, s1));
			}
			repaint();
		}
	}

	private static class ColorIcon implements Icon {

		private static final int WIDE = 20;
		private static final int HIGH = 20;
		private Color color;

		public ColorIcon(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(color);
			g.fillRect(x, y, WIDE, HIGH);
		}

		public int getIconWidth() {
			return WIDE;
		}

		public int getIconHeight() {
			return HIGH;
		}
	}
}