import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
public class TimerListener implements ActionListener{

	public final static int ONE_SECOND = 5000;
	
	Timer timer = new Timer(ONE_SECOND, new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
	        
	    	if (timer.equals(0)) {
	            timer.stop();
	        }
	    }    
	});

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	}
