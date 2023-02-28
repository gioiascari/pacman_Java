package pacman;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.net.ssl.ExtendedSSLSession;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Model extends JPanel implements ActionListener {

	private Dimension dimension;
	// Font
	private final Font font = new Font("Helvetica", Font.BOLD, 14);
	private boolean inGame = false;
	private boolean dying = false;
	// Block
	private final int blockSize = 24;
	private final int blockNum = 15;
	private final int screenSize = blockNum * blockSize;
	// Ghost and speed
	private int ghostNum = 6;
	private final int maxGhost = 12;
	private final int pacSpeed = 6;
	// Position(ghost) and lives/score
	private int lives, score;
	private int[] dx, dy;
	private int[] ghostX, ghostY, ghostDX, ghostDY, ghostSpeed;
	// Image
	private Image heart, ghost;
	private Image right, left, up, down;
	// Position pacman
	private int pacmanX, pacmanY, pacmanDX, pacmanDY;
	private int reqDX, reqDY;
	// ValidSpeed
	private int validSpeed[] = { 1, 2, 3, 4, 5, 6, 7, 8 };
	private int maxSpeed = 6;
	private int currentSpeed = 3;
	private short[] screenData;
	// Timer
	private Timer timer;

	public Model() {
		loadImg();
		initVariables();
		addKeyListener(new TAdapter());
		setFocusable(true);
		initGame();
	}

	// Level Data
	private final short levelData[] = { 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 17, 18, 18, 22, 17, 16, 16, 16, 17,
			17, 16, 16, 15, 16, 16, 16, 16, 16, 20, 25, 24, 24, 24, 24, 25, 16, 15, 15, 15, 16, 16, 16, 16, 20, 0, 0, 0,
			0, 0, 0, 0, 14, 14, 14, 14, 14, 14, 14, 20, 0, 0, 0, 0, 0, 0, 0, 0, 16, 24, 22, 22, 22, 22, 20, 17, 16, 16,
			16, 16, 0, 0, 0, 0, 0, 0, 0, 18, 18, 21, 18, 22, 23, 12, 12, 12, 0, 0, 0, 0, 0, 13, 17, 17, 21, 17, 17, 18,
			0, 0, 0, 0, 5, 5, 22, 22, 25, 23, 25, 22, 19, 18, 18, 0, 22, 0, 27, 23, 22, 22, 20, 21, 19, 19, 20, 0, 0, 0,
			0, 14, 10, 14, 19, 19, 19, 19, 0, 0, 20, 10, 16, 16, 16, 16, 22, 0, 20, 10, 10, 11, 11, 18, 18, 18, 19, 22,
			22, 26, 25, 22, 0, 0, 0, 0, 0, 20, 20, 15, 15, 20, 10, 10, 15, 14, 14, 0, 0, 19, 19, 19, 19, 0, 0, 20, 10,
			16, 16, 16, 16, 22, 22, 20, 10, 10, 11, 11, 18, 18, 18, 19, 22, 24, 24, 24, 25, 25, 25, 25, 22, 20, 20, 20,
			22, 22, 20, };

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
		dimension = new Dimension(400, 400);
		ghostX = new int[maxGhost];
		ghostY = new int[maxGhost];
		ghostDX = new int[maxGhost];
		ghostDY = new int[maxGhost];
		ghostSpeed = new int[maxGhost];
		dx = new int[4];
		dy = new int[4];

		// timer
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
		for (int i = 0; i < blockNum * blockNum; i++) {
			screenData[i] = levelData[i];

		}

	}

	private void playGame(Graphics2D g2D) {
		if (dying) {
			endGame();
		} else {
			movePac();
			drawPac(g2D);
			moveGhosts(g2D);
			checkMaze();
		}
	}
	
	public void showIntroScreen() {
		
	}

	private void movePac() {
		int pos;
		short ch;
		if (pacmanX % blockSize == 0 && pacmanY % blockSize == 0) {
			pos = pacmanX / blockSize + blockNum * (pacmanY / blockSize);
			ch = screenData[pos];
			// 16 sono i blocchi che può mangiare pacman
			if ((ch & 16) != 0) {
				screenData[pos] = (short) (ch & 15);
				// Se pacman ha mangiato 16 blocchi allora il risultato aumenta di 1
				score++;
			}
			// Reqdx e Reqdy sono i controlli di pacman
			if (reqDX != 0 || reqDY != 0) {
				if (!(reqDX == -1 && reqDY == 0 && (ch & 1) != 0) || (reqDX == 1 && reqDY == 0 && (ch & 4) != 0)
						|| (reqDX == 0 && reqDY == -1 && (ch & 2) != 0)
						|| (reqDX == 0 && reqDY == 0 && (ch & 8) != 0)) {
					pacmanX = reqDX;
					pacmanY = reqDY;
				}
			}
			// Questi sono i controlli per i movimenti
			if ((pacmanDX == -1 && pacmanY == 0 && (ch & 1) != 0) || (pacmanDX == 1 && pacmanDY == 0 && (ch & 4) != 0)
					|| (pacmanDX == 0 && pacmanDY == -1 && (ch & 2) != 0)
					|| (pacmanDX == 0 && pacmanDY == 0 && (ch & 8) != 0)) {
				pacmanDX = 0;
				pacmanDY = 0;
			}
		}
		pacmanX = pacmanX + pacSpeed * pacmanDX;
		pacmanY = pacmanY + pacSpeed * pacmanDY;
	}

	public void drawPac(Graphics2D g2D) {
		if (reqDX == -1) {
			g2D.drawImage(left, pacmanX + 1, pacmanY + 1, this);
		} else if (reqDX == 1) {
			g2D.drawImage(right, pacmanX + 1, pacmanY + 1, this);
		} else if (reqDY == -1) {
			g2D.drawImage(up, pacmanX + 1, pacmanY + 1, this);
		} else {
			g2D.drawImage(down, pacmanX + 1, pacmanY + 1, this);
		}
	}

	// Questo metodo permette ai fantasmi di muoversi automaticamente
	public void moveGhosts(Graphics2D g2D) {
		int pos;
		short count;
		for (int i = 0; i < ghostNum; i++) {
			if (ghostX[i] % blockSize == 0 && ghostY[i] % blockSize == 0) {
				pos = ghostX[i] / blockSize + blockNum * (ghostY[i] / blockSize);
				count = 0;

				// Ripeto questo passaggio per quattro volte(tante quanto sono i lati del
				// rettangolo/quadrato contenitore)
				if ((screenData[pos] & 1) == 0 && ghostDX[i] != 1) {
					dx[count] = -1;
					dy[count] = 0;
					count++;
				}
				if ((screenData[pos] & 2) == 0 && ghostDY[i] != 1) {
					dx[count] = 0;
					dy[count] = -1;
					count++;
				}
				if ((screenData[pos] & 4) == 0 && ghostX[i] != -1) {
					dx[count] = 1;
					dy[count] = 0;
					count++;
				}
				if ((screenData[pos] & 8) == 0 && ghostX[i] != -1) {
					dx[count] = 0;
					dy[count] = 1;
					count++;
				}
				if (count == 0) {
					// Determino dove sono posizionati i fantasmi
					if ((screenData[pos] & 15) == 15) {
						ghostDY[i] = 0;
						ghostDX[i] = 0;
					} else {
						ghostDY[i] = -ghostDY[i];
						ghostDX[i] = -ghostDX[i];
					}
				} else {
					count = (short) (Math.random() * count);
					if (count > 3) {
						count = 3;
					}
					ghostDY[i] = dy[count];
					ghostDX[i] = dx[count];
				}
			}
			ghostX[i] = ghostX[i] + ghostDX[i] * ghostSpeed[i];
			ghostY[i] = ghostY[i] + ghostDY[i] * ghostSpeed[i];

			drawGhost(g2D, ghostX[i] + 1, ghostY[i] + 1);

			// se pacman tocca un fantasma, allora perde una vita
			if (pacmanX > (ghostX[i] - 12) && pacmanX < (ghostX[i] + 12) && pacmanY > (ghostY[i] - 12)
					&& pacmanY < (ghostY[i] + 12) && inGame) {
				dying = true;

			}

		}
	}

	public void drawGhost(Graphics2D g2D, int x, int y) {
		g2D.drawImage(ghost, x, y, this);

	}

	// Con questo metodo verifico quanti punti rimangono da mangiare
	public void checkMaze() {
		boolean finished = true;
		int i = 0;

		while (i < blockNum * blockNum && finished) {
			if ((screenData[i] & 48) != 0) {
				finished = false;
			}
		}
		i++;

		// se tutti i punti sono stati mangiati, si va al livello successivo
		if (finished) {
			score += 50;
			// con il livello nuovo la velocità e la quantità dei fantasmi incrementa di 1
			// livello per livello
			if (ghostNum < maxGhost) {
				ghostNum++;
			}
			if (currentSpeed < maxSpeed) {
				currentSpeed++;
			}

		}
		initLevel();

	}

	private void endGame() {
		lives--;
		if (lives == 0) {
			inGame = false;
		}

		continueLevel();
	}

	private void continueLevel() {
		int dx = 1;
		int random;

		// Init ghost speed x block
		for (int i = 0; i < ghostNum; i++) {
			ghostY[i] = 4 * blockSize;
			ghostX[i] = 4 * blockSize;
			ghostDY[i] = 0;
			ghostDX[i] = dx;
			dx = -dx;
			random = (int) (Math.random() * currentSpeed + 1);
			if (random > currentSpeed) {
				random = currentSpeed;
			}
			ghostSpeed[i] = validSpeed[random];
		}

		pacmanX = 7 * blockSize;
		pacmanY = 11 * blockSize;
		pacmanDX = 0;
		pacmanDY = 0;
		reqDX = 0;
		reqDY = 0;
		dying = false;
	}

	public void drawMaze(Graphics2D g2D) {
		int i = 0;
		int x, y;
		for (y = 0; y < screenSize; y += blockSize) {
			for (x = 0; x < screenSize; x += blockSize) {
				g2D.setColor(new Color(243, 108, 180));
				g2D.setStroke(new BasicStroke(5));

				if (screenData[i] == 0) {
					g2D.fillRect(x, y, blockSize, blockSize);
				}
				if ((screenData[i] & 1) != 0) {
					g2D.drawLine(x, y, x, y + blockSize - 1);
				}
				// top border
				if ((screenData[i] & 2) != 0) {
					g2D.fillRect(x, y, x + blockSize - 1, y);
				}
				// right border
				if ((screenData[i] & 4) != 0) {
					g2D.fillRect(x + blockSize - 1, y, x + blockSize - 1, y + blockSize - 1);
				}
				// buttom border
				if ((screenData[i] & 8) != 0) {
					g2D.fillRect(x, y + blockSize - 1, x + blockSize - 1, y + blockSize - 1);
				}
				// white dots
				if ((screenData[i] & 18) != 0) {
					g2D.setColor(new Color(255, 255, 255));
					// forma
					g2D.fillOval(x + 10, y + 10, 6, 6);
				}
			}
		}

	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2D = (Graphics2D) g;
		// BGColor
		g2D.setColor(Color.black);
		g2D.fillRect(0, 0, dimension.width, dimension.height);

		drawMaze(g2D);
		drawScore(g2D);

		if (inGame) {
			playGame(g2D);
		} else {
			showIntroScreen(g2D);
		}

		Toolkit.getDefaultToolkit().sync();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
