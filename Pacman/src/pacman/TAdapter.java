package pacman;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

public class TAdapter extends KeyAdapter {
	
	private boolean inGame = false;
	private boolean dying = false;
	private Timer timer;
	private int pacmanX, pacmanY, pacmanDX, pacmanDY;
	private int reqDX, reqDY;
	
	private int lives, score;
	
	Model mainPacman;
	
	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		
		if(inGame) {
			
			switch (key) {
			case KeyEvent.VK_LEFT:
				reqDX = -1;
				reqDY = 0;
				break;
			case KeyEvent.VK_RIGHT:
				reqDX = 1;
				reqDY = 0;
				break;
			case KeyEvent.VK_UP:
				reqDX = 0;
				reqDY = 1;
				break;
			case KeyEvent.VK_DOWN:
				reqDX = 0;
				reqDY = -1;
				break;
			case KeyEvent.VK_ESCAPE:
				timer.isRunning();
				inGame = false;
				break;
			}
			
		} else {
			if(key == KeyEvent.VK_SPACE) {
				inGame = true;
				mainPacman.initGame();
				
			}
		}
		
	
		
	}

}
