import acm.graphics.*;
import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class MyFirstTimer extends GraphicsProgram implements ActionListener {
	public static final int PROGRAM_HEIGHT = 600;
	public static final int PROGRAM_WIDTH = 800;
	public static final int MAX_STEPS = 20;
	private GLabel myLabel;
	private int numTimes;
	private Timer t;

	public void init() {
		setSize(PROGRAM_WIDTH, PROGRAM_HEIGHT);
		requestFocus();
	}

	public void run() {
		myLabel = new GLabel("# of times called?", 0, 100);
		add(myLabel);

		numTimes = 0;

		t = new Timer(1000, this);
		t.setInitialDelay(3000); // 3 second delay before starting
		t.start();
	}

	public void actionPerformed(ActionEvent e) {
		myLabel.move(5, 0);
		numTimes++;
		myLabel.setLabel("times called? " + numTimes);

		if (numTimes == 10) {
			t.stop();
		}
	}

	public static void main(String[] args) {
		new MyFirstTimer().start();
	}
}