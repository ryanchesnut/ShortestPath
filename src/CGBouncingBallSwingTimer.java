import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
/** Bouncing Ball (Animation) via Swing Timer */
@SuppressWarnings("serial")
public class CGBouncingBallSwingTimer extends JFrame {
   // Define named-constants
   private static final int CANVAS_WIDTH = 640;
   private static final int CANVAS_HEIGHT = 480;
   private static final int UPDATE_PERIOD = 10; // milliseconds
 
   
   private DrawCanvas canvas;  // the drawing canvas (extends JPanel)
 
   // Attributes of moving object
   private Point p1 = new Point(10, 10); 
   private Point p2 = new Point(200, 380); 
   private Point p3 = new Point(300, 10); 
   private int x = 0, y = 0;  // top-left (x, y)
   private int size = 50;        // width and height
   private int xSpeed = 3, ySpeed = 3; // displacement per step in x, y
   
 
   public double getAngle(Point po1, Point po2){
	   double dy = po2.y - po1.y; 
	   double dx = po2.x - po1.x;
	   double angle = Math.atan2(dy, dx); 
	   return angle;
   }
   /** Constructor to setup the GUI components */
   public CGBouncingBallSwingTimer() {
      canvas = new DrawCanvas();
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
      this.setContentPane(canvas);
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.pack();
      this.setTitle("Bouncing Ball");
      this.setVisible(true);
 
      // Define an ActionListener to perform update at regular interval
      ActionListener updateTask = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
           
        	 update();   // update the (x, y) position
        	 //updateColor(); 
            repaint();  // Refresh the JFrame, callback paintComponent()
         }
      };
      // Allocate a Timer to run updateTask's actionPerformed() after every delay msec
      new Timer(UPDATE_PERIOD, updateTask).start();
   }
 
   
   
   public void updateColor(){
	  Graphics g = null ;
	  g.setColor(Color.YELLOW); 
	   
   }
   /** Update the (x, y) position of the moving object */
      
   public void update() {
	  double angle1 = getAngle(p1, p2);  
	  double angle2 = getAngle(p2, p3);
	  double angle3 = getAngle(p3, p1);
	  
	  	  x += xSpeed * Math.cos(angle1); 
      	  y += ySpeed * Math.sin(angle1); 
      	  System.out.println(x +" " +y +" " + angle1); 
	  
    if (x > p2.x || y >p2.y ) {
    	
         x += xSpeed * Math.cos(angle2);
         y += ySpeed * Math.sin(angle2);
         System.out.println(x +" " +y +" " + angle2); 
        
    if (x > p3.x || y >p3.y ) {
              x -= xSpeed * Math.cos(angle3);
              y -= ySpeed * Math.sin(angle3);
              System.out.println(x +" " +y +" " + angle3); 
    }
         
    }
   }
 
   /** DrawCanvas (inner class) is a JPanel used for custom drawing */
   private class DrawCanvas extends JPanel {
      @Override
      public void paintComponent(Graphics g) {
         super.paintComponent(g);  // paint parent's background
         setBackground(Color.LIGHT_GRAY);
         g.setColor(Color.BLACK);
         
         g.fillOval(x, y, size, size);  
         g.drawLine(p1.x, p1.y, p2.x, p2.y);  // draw a circle
         g.drawLine(p3.x, p3.y, p2.x, p2.y);
         g.drawLine(p3.x, p3.y, p1.x, p1.y);
      }
   }
 
   /** The entry main method */
   public static void main(String[] args) {
      // Run GUI codes in Event-Dispatching thread for thread safety
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new CGBouncingBallSwingTimer(); // Let the constructor do the job
         }
      });
   }
}