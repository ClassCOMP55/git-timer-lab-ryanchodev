import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Timer;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class DodgeBall extends GraphicsProgram implements ActionListener {
	private ArrayList<GOval> balls;
	private ArrayList<GRect> enemies;
	private GLabel enemyCountLabel;
	private GLabel destroyedLabel;
	private GLabel survivalLabel;
	private Timer movement;
	private RandomGenerator rgen;

	public static final int SIZE = 25;
	public static final int SPEED = 2;
	public static final int MS = 50;
	public static final int MAX_ENEMIES = 10;
	public static final int WINDOW_HEIGHT = 600;
	public static final int WINDOW_WIDTH = 300;

	private int numTimes = 0;
	private int destroyed = 0;

	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	public void run() {
		rgen = RandomGenerator.getInstance();
		balls = new ArrayList<GOval>();
		enemies = new ArrayList<GRect>();

		enemyCountLabel = new GLabel("Enemies: 0", 0, WINDOW_HEIGHT - 30);
		add(enemyCountLabel);

		destroyedLabel = new GLabel("Destroyed: 0", 0, WINDOW_HEIGHT - 15);
		add(destroyedLabel);

		survivalLabel = new GLabel("Survival time: 0", 80, WINDOW_HEIGHT - 15);
		add(survivalLabel);

		movement = new Timer(MS, this);
		movement.start();
		addMouseListeners();
	}

	public void actionPerformed(ActionEvent e) {
		numTimes++;

		// Update survival time label
		survivalLabel.setLabel("Survival time: " + numTimes);

		// Add enemy every 40 ticks
		if (numTimes % 40 == 0) {
			addAnEnemy();
		}

		moveAllBallsOnce();
		moveAllEnemiesOnce();
		checkCollisions();

		// Check if MAX_ENEMIES exceeded.
		if (enemies.size() > MAX_ENEMIES) {
			movement.stop();
			removeAll();
			GLabel lostLabel = new GLabel("You lost! Survival time: " + numTimes, 20, WINDOW_HEIGHT / 2);
			add(lostLabel);
		}
	}

	public void mousePressed(MouseEvent e) {
		for (GOval b : balls) {
			if (b.getX() < SIZE * 2.5) {
				return;
			}
		}
		addABall(e.getY());
	}

	private void addABall(double y) {
		GOval ball = makeBall(SIZE / 2, y);
		add(ball);
		balls.add(ball);
	}

	public GOval makeBall(double x, double y) {
		GOval temp = new GOval(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
		temp.setColor(Color.RED);
		temp.setFilled(true);
		return temp;
	}

	private void addAnEnemy() {
		GRect e = makeEnemy(rgen.nextInt(0, WINDOW_HEIGHT - SIZE / 2));
		enemies.add(e);
		enemyCountLabel.setLabel("Enemies: " + enemies.size());
		add(e);
	}

	public GRect makeEnemy(double y) {
		GRect temp = new GRect(WINDOW_WIDTH - SIZE, y - SIZE / 2, SIZE, SIZE);
		temp.setColor(Color.GREEN);
		temp.setFilled(true);
		return temp;
	}

	private void moveAllBallsOnce() {
		for (GOval ball : balls) {
			ball.move(SPEED, 0);
		}
	}

	private void moveAllEnemiesOnce() {
		for (GRect enemy : enemies) {
			enemy.move(0, rgen.nextInt(-2, 2));
		}
	}

	private void checkCollisions() {
		ArrayList<GRect> toRemove = new ArrayList<GRect>();

		for (GOval ball : balls) {
			// Check the point just in front of the ball
			double checkX = ball.getX() + ball.getWidth() + 1;
			double checkY = ball.getY() + ball.getHeight() / 2;
			GObject obj = getElementAt(checkX, checkY);

			if (obj instanceof GRect) {
				GRect hit = (GRect) obj;
				if (!toRemove.contains(hit)) {
					toRemove.add(hit);
				}
			}
		}

		for (GRect enemy : toRemove) {
			remove(enemy);
			enemies.remove(enemy);
			destroyed++;
			destroyedLabel.setLabel("Destroyed: " + destroyed);
			enemyCountLabel.setLabel("Enemies: " + enemies.size());
		}
	}

	public static void main(String args[]) {
		new DodgeBall().start();
	}
}
