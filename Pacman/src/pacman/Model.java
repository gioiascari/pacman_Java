package pacman;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.net.ssl.ExtendedSSLSession;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Model extends JPanel implements ActionListener {
	
	private Dimension dimension;
	//Font
	private final Font font = new Font("Helvetica", Font.BOLD, 14);
		
	//Block
	private final int blockSize = 24;
	private final int blockNum 	= 15;
	private final int screenSize = blockNum * blockSize;	
	//Ghost and speed
	private int ghostNum = 6;
	private final int maxGhost = 12;	
	private final int pacSpeed = 6;	
	//Position(ghost) and lives/score
	private int lives, score;
	private int [] dx, dy;
	private int [] ghostX, ghostY, ghostDX, ghostDY, ghostSpeed;	
	//Image
	private Image heart, ghost;
	private Image right, left, up, down;
	//Position pacman
//	private int pacmanX, pacmanY, pacmanDX, pacmanDY;
//	private int reqDX, reqDY;
	//ValidSpeed
	private int validSpeed [] = {1,2,3,4,5,6,7,8};
	private int maxSpeed = 6;
	private int currentSpeed = 3;
	private short [] screenData;
	//Timer
	private Timer timer;
	//Level Data
	private final short levelData [] = {
			19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 17, 18, 18, 22,
			17, 16, 16, 16, 17, 17, 16, 16, 15, 16, 16, 16, 16, 16, 20,
			25, 24, 24, 24, 24, 25, 16, 15, 15, 15, 16, 16, 16, 16, 20,
			0,	0,	0,	0,	0,	0, 0, 14, 14, 14, 14, 14, 14, 14, 20,
			0, 0, 0, 0, 0, 0, 0, 0, 16, 24, 22, 22, 22, 22, 20,
			17, 16, 16, 16, 16,	0,	0,	0,	0,	0,	0,	0,	18,	18,	21,
			18,	22, 23,	12,	12, 12,	0,	0,	0,	0,	0,	13,	17,	17,	21,
			17,	17,	18,	0,	0,	0,	0,	5,	5,	22,	22,	25,	23,	25, 22,
			19,	18,	18,	0,	22,	0,	27,	23,	22,	22,	20,	21,	19,	19,	20,
			0,	0,	0,	0,	14,	10,	14,	19,	19,	19,	19,	0,	0,	20,	10,
			16,	16,	16,	16,	22,	0,	20,	10,	10,	11,	11,	18,	18,	18,	19,
			22,	22,	26,	25,	22,	0,	0,	0,	0,	0,	20,	20,	15,	15,	20,
			10,	10,	15,	14,	14,	0,	0,	19,	19,	19,	19,	0,	0,	20,	10,
			16,	16,	16,	16,	22,	22,	20,	10,	10,	11,	11,	18,	18,	18,	19,
			22,	24,	24,	24,	25,	25,	25,	25,	22,	20,	20,	20,	22,	22,	20,
	};
	
	private void loadImg() {
		up = new ImageIcon("/img/up.gif").getImage();
		down = new ImageIcon("/img/down.gif").getImage();
		right = new ImageIcon("/img/right.gif").getImage();
		left = new ImageIcon("/img/left.gif").getImage();
		ghost = new ImageIcon("/img/ghost.gif").getImage();
		heart = new ImageIcon("/img/heart.gif").getImage();
	};
	
	private void initVariables() {
		screenData = new short[blockNum * blockNum];
		dimension = new Dimension(400,400);
		ghostX = new int[maxGhost];
		ghostY = new int[maxGhost];
		ghostDX = new int[maxGhost];
		ghostDY = new int[maxGhost];
		ghostSpeed = new int[maxGhost];
		dx = new int[4];
		dy = new int[4];
		
		//timer
		timer = new Timer(50, this);
		timer.restart();		
	}
	
	public void initGame() {
		lives = 3;
		score = 0;
		ghostNum = 6;
		currentSpeed = 3;
		initLevel();
	}
	
	private void initLevel() {
		
		for(int i = 0; i < blockNum * blockNum; i++ ) {
			screenData[i] = levelData[i];
			
		}
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
