import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;


public class Test {
	
	public static void main (String args[] ){
	
	int [][] a = new int [5][5]; 
	ArrayList<Integer>[][] matrix = new ArrayList[5][5];
	ArrayList<ArrayList<Integer>> m = new ArrayList<ArrayList<Integer>>(); 
	ArrayList<Integer> n = new ArrayList<Integer>(); 
	ArrayList<ArrayList<Integer>> c = new ArrayList<ArrayList<Integer>>(); 
	
	for(int i = 0; i < 5; i++){
		for(int j = 0; j< 5; j++){
			//System.out.print(i+" "+j);
			a[i][j] = 3*i+j; 
			n.add(3*i+j); 
			//System.out.print("["+ a[i][j] +"]\t");
		}
		 m.add(n);
		 System.out.println(n.toString()); 
		 n = new ArrayList<Integer>();
		 
		System.out.println(" ");
	 
	}
	System.out.println(m.toString()+"\n");
	System.out.println(c.toString());
	
	//	g.drawRect(xTopLeft, yTopLeft, width, height);
		
	}
		



		/*	
		 * 	 public static void main(String[] args){
	  java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
	      JFrame frame = new JFrame();
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.add(new DrawLine(250,250,250,250));
	      frame.setSize(500,500);
	      frame.setVisible(true);  
	    }
	  });  
	 } 
	}

	class DrawLine extends JPanel implements ActionListener{

	  int x1;
	  int y1;
	  int x2;
	  int y2;
	  int midx;
	  int midy;

	  Timer time = new Timer(5, (ActionListener) this);

	  public DrawLine(int x1, int y1, int x2, int y2){
	    this.x1 = x1;
	    this.y1 = y1;
	    this.x2 = x2;
	    this.y2 = y2;
	    midx = x2;
	    midy = y2;
	    time.start();
	  }

	  public void animateLine(Graphics g){
	    g.drawLine(x1,y1,midx,midy);
	    
	  }

	  public void actionPerformed(ActionEvent arg0) {
	 
		  if(midy == y2 && midx == x2){
			  midy = y2;
			  midx = x2; 
		  }
		  else
	      midy++;
	      midx--;
	      repaint();
	    
	  }

	  public void paintComponent(Graphics newG){
	    super.paintComponent(newG);
	    Graphics2D g2d = (Graphics2D)newG;
	    animateLine(g2d);
	  }
	}		
	String node1 = "X4"; 
	String node2 = "X4"; 
		if(node1.equals(node2));
			System.out.println("Equal");
		 * FileOutputStream outStream = new FileOutputStream("test.txt");
	    String s = "This is a test.";
	    for (int i = 0; i < s.length(); ++i)
	      outStream.write(s.charAt(i));
	    outStream.close();
	    FileInputStream inStream = new FileInputStream("test.txt");
	    int inBytes = inStream.available();
	    System.out.println("inStream has " + inBytes + " available bytes");
	    byte inBuf[] = new byte[inBytes];
	    int bytesRead = inStream.read(inBuf, 0, inBytes);
	    System.out.println(bytesRead + " bytes were read");
	    System.out.println("They are: " + new String(inBuf));
	    inStream.close();
	    File f = new File("test.txt");
		 int j = 12; 
		int k = (int)j/3; 
		
		for (int i = 0; i < j; i++){
			System.out.println(i%6); 
			
		}
		Set<Node> set = new HashSet<Node>(); 
		for(int i = 0; i<10; i++){
			String s = "x"+i; 
			set.add(new Node(s, i));
		}
		Iterator<Node> iterator = set.iterator();
		
		Node min = iterator.next();
		System.out.println(set.toString());
		while (iterator.hasNext()){
			Node n = iterator.next();
			System.out.println(n.toString()); 
			if(min.getWeight() > n.getWeight())
				min = n; 
		}
		System.out.println("Min: "+min.toString());
		*/
	

}
