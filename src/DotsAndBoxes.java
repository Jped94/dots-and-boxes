// CS 201
// Final Project!!!
// Josh Pedowitz and Emily Sarich
// Time taken: 2.5 hours Friday
// 1.75 hours Sunday
// 1 hour Monday
// 2.5 hours Tuesday
// 2 hours Friday
// 2:30


import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class DotsAndBoxes extends Applet implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;

	Button reset;
	GameCanvas c;
	int p1Score = 0;
	int p2Score = 0;
	boolean turn = true; //true = player1, false = player2
	Label playerTurn;
	Label score1;
	Label score2;
	Label gameOver;
	int gameSize = 5;
	Choice boardSize;

	boolean rules;
	Button ruleButton;

	public void init() {
		setLayout(new BorderLayout());


		//creates North panel
		Panel top = new Panel();
		top.setLayout(new FlowLayout());
		top.setBackground(Color.green);
		Label title = new Label("DOTS AND BOXES");
		title.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		top.add(title);

		add("North", top);

		//creates the South panel
		reset = new Button("New Game");
		reset.addActionListener(this);

		Choice boardSize = new Choice();
		boardSize.add("Select game size");
		boardSize.addItem("4");
		boardSize.addItem("5");
		boardSize.addItem("6");
		boardSize.addItem("7");
		boardSize.addItem("8");
		boardSize.addItemListener(this);

		Panel bottom = new Panel();

		bottom.setLayout(new FlowLayout());
		bottom.setBackground(Color.green);
		bottom.add(boardSize);
		bottom.add(reset);
		playerTurn = new Label("Turn: Player 1");
		bottom.add(playerTurn);

		add("South", bottom);

		//creates East panel
		Panel east = new Panel();
		east.setLayout(new GridLayout(4,1));
		east.setBackground(Color.green);
		ruleButton = new Button("Rules");
		ruleButton.addActionListener(this);
		ruleButton.setBackground(Color.gray);
		east.add(ruleButton);

		score1 = new Label("Player 1 Score: " + p1Score + " ");
		score1.setBackground(Color.blue);
		score1.setForeground(Color.white);
		score2 = new Label("Player 2 Score: " + p2Score + " ");
		score2.setBackground(Color.red);
		score2.setForeground(Color.white);
		gameOver = new Label();

		east.add(score1);
		east.add(score2);
		east.add(gameOver);

		add("East", east);


		//creates Center panel (the canvas!)
		c = new GameCanvas(this, gameSize);
		c.setBackground(Color.white);
		c.addMouseListener(c);

		add("Center", c);
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == reset)
			c.reset(); //resets the canvas to restart the game
		else if(arg0.getSource() == ruleButton) {
			rules = true;
			c.repaint();
		}	
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == 1 && !(e.getItem().equals("Select game size"))) {
			gameSize = Integer.parseInt((String) e.getItem());
			c.reset();
		}
	}
}

class GameCanvas extends Canvas implements MouseListener {

	private static final long serialVersionUID = 1L;

	DotsAndBoxes parent;
	int row;
	int column; 
	int total; 
	boolean[] rowClicked;
	boolean[] columnClicked;
	Sides[] vertSides;
	Sides[] horizSides;
	int[] boxFull; // 0 = empty, 1 = player 1, 2 = player 2
	int width; //the default sizes of our boxes
	int height; //space between each box


	public GameCanvas (DotsAndBoxes s, int gameSize) {
		parent = s;
		row = parent.gameSize; //number of rows (boxes across)
		column = parent.gameSize; //number of columns (boxes up/down)
		total = row * (column + 1);
		rowClicked = new boolean[total];
		columnClicked = new boolean[total];
		vertSides = new Sides[total];
		horizSides = new Sides[total];
		boxFull = new int[row*column]; // 0 = empty, 1 = player 1, 2 = player 2

		width = 60; //the default sizes of our boxes
		height = 10; //space between each box
	}


	public void paint(Graphics g) {

		if (parent.rules) {
			dispRules(g);
			parent.rules = false;
		} else {
			int numDrawn = 0;
			int xRows = height;
			int yRows = 0;
			int xCols = 0;
			int yCols = height;
			int xDots = 0;
			int yDots = 0;
			int xBox = height;
			int yBox = height;

			while (numDrawn < row + 1) { //draws the whole game board
				drawRows(xRows, yRows, g, numDrawn);
				drawDots(xDots, yDots, row, g);

				if (numDrawn < row){
					drawBoxes(xBox, yBox, numDrawn, g);
					drawColumns(xCols, yCols, g, numDrawn);
				}
				yRows += width + height;
				yCols += width + height;
				yDots += width + height;
				yBox += width + height;
				numDrawn++;
			}
		}
	}


	public void drawRows(int x, int y, Graphics g, int row) {
		//draws rows
		for (int i = 0; i < this.row; i++){ 

			if (rowClicked[i + (row)*this.row]){
				g.setColor(Color.black);
				g.fillRect(x + (i*height), y, width, height);
			} else {
				g.setColor(Color.black);
				g.drawRect(x+ (i*height), y, width, height);
			}
			horizSides[i + (row)*this.row] = 
					new Sides(x + (i*height), y, x + (i*height) + width, y + height);
				//adds each drawn rectangle to the array of horizontal rectangles
			x += width;
		}
	}


	public void drawColumns(int x, int y, Graphics g, int cols) {
		//draws columns
		for (int i = 0; i <= this.column; i++){ 

			if (columnClicked[i + (cols)*(this.column + 1)]){
				g.setColor(Color.black);
				g.fillRect(x + (i*width), y, height, width);
			} else {
				g.setColor(Color.black);
				g.drawRect(x+ (i*width), y, height, width);
			}
			vertSides[i + (cols)*(this.column + 1)] = 
					new Sides(x + (i*width), y, x + (i*width) + height, y + width);
				//adds each drawn rectangle to the array of horizontal rectangles
			x += height;
		}
	}

	public void drawDots (int x, int y, int numRows, Graphics g) {
		//draws the black boxes (the dots) between each box
		int num = numRows +1;
		g.setColor(Color.black);
		for (int i = 0; i < num; i++) {
			g.fillRect(x + (i * height), y, height, height);
			x = ((i+1) * width);
		}
	}
	
	public void drawBoxes (int x, int y, int row, Graphics g) {
		//draws boxes that appear only when the box is surrounded by lines
		for (int i = 0; i < this.row; i++) {				
			if (boxFull[i + (row)*this.row] == 1) { //if it's player 1's turn
				g.setColor(Color.blue);
				g.fillRect(x + (i*height), y, width, width);
			} else if (boxFull[i + (row)*this.row] == 2){ //if it's player 2's turn
				g.setColor(Color.red);
				g.fillRect(x + (i*height), y, width, width);
			}
			x += width;
		}
	}
	
	public void checkBoxes (boolean turn) {
		// a check that is performed after every turn to determine if
		// a box has been completely surrounded
		// if it's been surrounded, the picture gets updated and
		// the scores are increased accordingly

		for (int i = 0; i < row*column; i++) {
			if (rowClicked[i] && rowClicked[i+row] && columnClicked[i+(i/row)] && 
					columnClicked[i+1+(i/row)] && boxFull[i] == 0) {
				if (turn) {
					boxFull[i] = 1;
					parent.p1Score ++;
					parent.score1.setText("Player 1 Score: " + parent.p1Score);
				} else {
					boxFull[i] = 2;
					parent.p2Score ++;
					parent.score2.setText("Player 2 Score: " + parent.p2Score);
				}
				if (parent.turn != turn)
					parent.turn = !parent.turn;
			}
		}
		checkGameOver();
	}
	public void checkGameOver (){
		// Checks the status of the game
		// if the game is over (all boxes have been surrounded),
		// print which player won the game

		if (parent.p1Score + parent.p2Score == row*column){
			if (parent.p1Score > parent.p2Score) {
				parent.gameOver.setText ("Player 1 Wins!");
			} else {
				parent.gameOver.setText("Player 2 Wins!");
			}
		}	
	}

	public void reset() {
		// resets the game board so players can play again if they so desire
		for (int i = 0; i < total; i++){
			rowClicked[i] = false;
			columnClicked[i] = false;
		}
		for (int j = 0; j < row*column; j++)
			boxFull[j] = 0;

		parent.turn = true;
		parent.p1Score = 0;
		parent.p2Score = 0;
		row = parent.gameSize;
		column = parent.gameSize;
		total = row*(column+1);
		rowClicked = new boolean[total];
		columnClicked = new boolean[total];
		vertSides = new Sides[total];
		horizSides = new Sides[total];
		boxFull = new int[row*column];

		parent.score1.setText("Player 1 Score: " + parent.p1Score);
		parent.score2.setText("Player 2 Score: " + parent.p2Score);
		parent.gameOver.setText("");
		parent.playerTurn.setText("Turn: Player 1");

		repaint();
	}

	public void dispRules(Graphics g) {
		//Displays the rules of the game in the game canvas
		
		g.setColor(Color.white);
		g.fillRect(0,0, (row*width)+(column*(height+1)), row*width+(column*(height+1)));
		g.setColor(Color.black);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		g.drawString("Dots and Boxes: Instruction page", 40, 40);
		g.drawString("Players take joining two horizontally or vertically adjacent", 40, 100);
		g.drawString("dots by a line. A player that completes the fourth side", 40, 140);
		g.drawString("of a square (a box) colors that box and must play again.", 40, 180);
		g.drawString("When all boxes have been colored, the game ends and", 40, 220);
		g.drawString("the player who has colored more boxes wins.", 40, 260);
		g.drawString("Player 1 is blue, and player 2 is red.", 40, 300);
		g.drawString("Click 'New Game' to begin!!", 40, 340);

	}

	public void mousePressed(MouseEvent e) { // Handles mouse clicks

		Point p = e.getPoint();
		for (int i = 0; i < total; i++){
			int x1 = horizSides[i].getx1();
			int y1 = horizSides[i].gety1();
			int x2 = horizSides[i].getx2();
			int y2 = horizSides[i].gety2();

			if (p.x >= x1 && p.x <= x2 && p.y >= y1 && p.y <= y2){
				//if the user clicked within a rectangle
				rowClicked[i] = true;
				parent.turn = !parent.turn;
			}
		}

		for (int i = 0; i < total; i++){
			int x1 = vertSides[i].getx1();
			int y1 = vertSides[i].gety1();
			int x2 = vertSides[i].getx2();
			int y2 = vertSides[i].gety2();

			if (p.x >= x1 && p.x <= x2 && p.y >= y1 && p.y <= y2){
				//if the user clicked within a rectangle
				columnClicked[i] = true;
				parent.turn = !parent.turn;
			}
		}
		checkBoxes(!parent.turn);

		if (parent.turn)
			parent.playerTurn.setText("Turn: Player 1");
		else 
			parent.playerTurn.setText("Turn: Player 2");

		repaint();

	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}	
}