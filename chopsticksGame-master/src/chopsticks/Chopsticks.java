/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chopsticks;

import chopsticks.ai.Ai3;
import chopsticks.ai.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

/**
 * @author Kurt
 */
@SuppressWarnings("serial")
public final class Chopsticks extends JFrame {

	static public void main(String args[]) {
		new Chopsticks();
	}

	static public Player human;
	static public Player computer;
	static boolean pTurn = true;
	static boolean winnerYet = false;
	static boolean playerWon = true;

	JLabel pLeft = new JLabel("1");
	JLabel pRight = new JLabel("1");
	JLabel cLeft = new JLabel("1");
	JLabel cRight = new JLabel("1");
	JButton rHit = new JButton("Hit");
	JButton lHit = new JButton("Hit");
	JPanel swapPanel = new JPanel();
	JLabel computerLabel = new JLabel("Computer");
	JLabel playerLabel = new JLabel("Player");
	JButton pLHit = new JButton("Use");
	JButton pRHit = new JButton("Use");
	JButton cGoButton = new JButton("Computer GO");
	JPanel computerPanel = new JPanel();
	JTextField swapNumField = new JTextField();
	JRadioButton rSwapRadioButton = new JRadioButton("From right hand");
	JRadioButton lSwapRadioButton = new JRadioButton("From left hand");
	JButton swapButton = new JButton("Swap");
	ButtonGroup radioGroup = new ButtonGroup();
	String attackHand;

	public Chopsticks() {
		super("ChopSticks");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 300);
		setLayout(new GridLayout(4, 2));
		pLHit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				lHit.setEnabled(true);
				rHit.setEnabled(true);
				pLHit.setEnabled(false);
				pRHit.setEnabled(false);
				attackHand = "l";
			}
		});
		pRHit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lHit.setEnabled(true);
				rHit.setEnabled(true);
				pLHit.setEnabled(false);
				pRHit.setEnabled(false);
				attackHand = "r";
			}
		});
		lHit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (computer.left > 0) {
					int total = 0;
					total = computer.left;
					total += (attackHand.equals("r") ? human.right : human.left);
					computer.left = total;
				}
				cycle();
				lHit.setEnabled(false);
				rHit.setEnabled(false);
				pLHit.setEnabled(true);
				pRHit.setEnabled(true);
			}
		});
		rHit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (computer.right > 0) {
					int total = 0;
					total = computer.right;
					total += (attackHand.equals("r") ? human.right : human.left);
					computer.right = total;
				}
				cycle();
				lHit.setEnabled(false);
				rHit.setEnabled(false);
				pLHit.setEnabled(true);
				pRHit.setEnabled(true);
			}
		});

		lHit.setEnabled(false);
		rHit.setEnabled(false);
		add(playerLabel);
		add(pLeft);
		add(pRight);
		radioGroup.add(rSwapRadioButton);
		radioGroup.add(lSwapRadioButton);
		swapPanel.setLayout(new BorderLayout());
		swapPanel.add(swapNumField, BorderLayout.CENTER);

		swapPanel.add(rSwapRadioButton, BorderLayout.EAST);
		swapPanel.add(lSwapRadioButton, BorderLayout.WEST);
		swapPanel.add(swapButton, BorderLayout.SOUTH);
		swapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String moveFrom = (rSwapRadioButton.isSelected()) ? "r" : "l";

				// <editor-fold defaultstate="collapsed" desc="checks if any
				// fingers left">
				if (moveFrom.equals("r") && human.right < 1) {
					System.out.println("You dont have any points in your right hand!!!");
					return;

				}
				if (moveFrom.equals("l") && human.left < 1) {
					System.out.println("You dont have any points in your left hand!!!");
					return;

				}
				// </editor-fold>

				int moveAmount = Integer.parseInt(swapNumField.getText());
				if (moveFrom.equals("r")) {
					if (human.right < moveAmount) {
						System.out.println("Not enough!");
						return;
					} else if (human.left + moveAmount > 4) {
						System.out.println("This would kill you hand!");
						return;
					} else {
						human.left += moveAmount;
						human.right -= moveAmount;
						System.out.println("Move complete!");
					}

				}
				if (moveFrom.equals("l")) {
					if (human.left < moveAmount) {
						System.out.println("Not enough!");
						return;
					} else if (human.right + moveAmount > 4) {
						System.out.println("This would kill you hand!");
						return;
					} else {
						human.right += moveAmount;
						human.left -= moveAmount;
						System.out.println("Move complete!");
					}

				}

				cycle();
			}
		});
		add(swapPanel);

		add(pLHit);

		add(pRHit);
		computerPanel.add(computerLabel);
		cGoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				newCompGo();
				validateAndReport();
				refresh();

			}
		});
		computerPanel.add(cGoButton);
		add(computerPanel);

		add(cLeft);

		add(cRight);

		add(new JLabel());
		add(lHit);

		add(rHit);

		setVisible(true);
		initialize();

	}

	public void refresh() {

		pLeft.setText(String.valueOf(human.left));
		pRight.setText(String.valueOf(human.right));
		cLeft.setText(String.valueOf(computer.left));
		cRight.setText(String.valueOf(computer.right));

	}

	public void initialize() {
		human = new Player();
		computer = new Player();
		showHelp();
		validateAndReport();
	}

	static public void validateAndReport() {

		System.out.println("Computer\tPlayer");
		System.out.println("L:" + computer.left + " R:" + computer.right + " \tL:" + human.left + " R:"
				+ human.right);
		boolean changeMade = false;
		if (human.left > 4) {
			human.left = 0;
			System.out.println("Hand out!");
			changeMade = true;
		}

		if (human.right > 4) {
			human.right = 0;
			System.out.println("Hand out!");
			changeMade = true;
		}

		if (computer.left > 4) {
			computer.left = 0;
			System.out.println("Hand out!");
			changeMade = true;
		}

		if (computer.right > 4) {
			computer.right = 0;
			System.out.println("Hand out!");
			changeMade = true;
		}

		if (changeMade) {
			System.out.println("new:");
			System.out.println("Computer\tPlayer");
			System.out.println("L:" + computer.left + " R:" + computer.right + " \tL:" + human.left + " R:"
					+ human.right);
		}
		if (computer.left == 0 && computer.right == 0) {
			winnerYet = true;
			playerWon = true;
		}
		if (human.left == 0 && human.right == 0) {
			winnerYet = true;
			playerWon = false;

		}
		if (winnerYet) {
			if (playerWon) {
				System.out.println("You Won!");
			} else {
				System.out.println("The c won");
			}
			exit(0);
		}
	}

	static public void showHelp() {
		System.out.println("Press left or l to hit the computers left, press right or r to hit the right.");
	}

	static public String askForInput() {
		// String userInputString = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			return br.readLine();

		} catch (IOException ex) {
			Logger.getLogger(Chopsticks.class.getName()).log(Level.SEVERE, null, ex);
			System.err.println("Fatal Error");
			exit(-1);
		}
		return null;

	}

	static public int askForInt() {
		boolean gotNumber = false;
		int number = 0;
		while (!gotNumber) {
			gotNumber = true;
			String userInput = "";
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				userInput = br.readLine();
				number = Integer.parseInt(userInput);
			} catch (NumberFormatException | IOException e) {
				System.out.print("I asked for a number! Try again: ");
				gotNumber = false;
			}

		}
		return number;
	}

	private static void computerGo() {

		System.out.println("The c's going now");

		if (computer.right > 0) {
			// Winning Killer moves for right
			if ((human.right + computer.right > 4) && human.left == 0) {
				human.right += computer.right;
				return;
			}
			if ((human.left + computer.right > 4) && human.right == 0) {
				human.left += computer.right;
				return;
			}

		}
		// Winning Killer moves for left
		if (computer.left > 0) {
			if ((human.right + computer.left > 4) && human.left == 0) {
				human.right += computer.left;
				return;
			}
			if ((human.left + computer.left > 4) && human.right == 0) {
				human.left += computer.left;
				return;
			}
		}
		// check swap
		if (computer.right - computer.left > 3) { // only possible
			// situation is a
			// 4-0 hand set
			computer.right = 2;
			computer.left = 2;
			return;
		}
		if (computer.left - computer.right > 3) { // only possible
			// situation is a
			// 0-4 hand set
			computer.right = 2;
			computer.left = 2;
			return;
		}

		if (computer.left == 0 || computer.right == 1) {
			if (computer.right + human.right > 3) {
				human.left += computer.right;
				return;
			}
		}
		if (computer.left == 1 || computer.right == 0) {
			if (computer.left + human.right > 3) {
				human.right += computer.left;
				return;
			}
		}
		if (computer.left == 0 || computer.right == 3) {
			if (computer.right + human.right > 3) {
				human.left += computer.right;
				return;
			}
		}
		if (computer.left == 3 || computer.right == 0) {
			if (computer.left + human.right > 3) {
				human.right += computer.left;
				return;
			}
		}

		if (computer.right > 0) {
			// Killer moves for right
			if (human.right + computer.right > 4) {
				human.right += computer.right;
				return;
			}
			if (human.left + computer.right > 4) {
				human.left += computer.right;
				return;
			}

		}
		// Killer moves for left
		if (computer.left > 0) {
			if (human.right + computer.left > 4) {
				human.right += computer.left;
				return;
			}
			if (human.left + computer.left > 4) {
				human.left += computer.left;
				return;
			}
		}
		// hit to 4 moves for right
		if (computer.right > 0) {
			if (human.right + computer.right > 3) {
				human.right += computer.right;
				return;
			}
			if (human.left + computer.right > 3) {
				human.left += computer.right;
				return;
			}

		}
		// hit for 4 moves for left
		if (computer.left > 0) {
			if (human.right + computer.left > 3) {
				human.right += computer.left;
				return;
			}
			if (human.left + computer.left > 3) {
				human.left += computer.left;
				return;
			}
		}

		if (computer.left > 0) {
			if (human.right > 0) {
				human.right += computer.left;
				return;
			}
		}
		if (computer.right > 0) {
			if (human.right > 0) {
				human.right += computer.right;
				return;
			}
		}
		if (computer.right > 0) {
			if (human.left > 0) {
				human.left += computer.right;
				return;
			}
		}
		if (computer.left > 0) {
			if (human.left > 0) {
				human.left += computer.left;
				return;
			}
		}
		validateAndReport();
		System.out.println("players turn");
	}

	static String askForHand() {
		String input;
		boolean gotLetter = false;
		while (!gotLetter) {
			gotLetter = true;
			// System.out.print("Move from left or right (r or l) :");
			input = askForInput();
			if (!(input.equals("r") || input.equals("l"))) {
				System.out.println("Please enter r or l");
				gotLetter = false;
			} else {
				return input;
			}
		}
		return null;
	}

	@Deprecated
	public static boolean startAttack(String handFrom) {
		int total = 0;
		if ((handFrom.equals("r") ? human.right : human.left) < 1) {
			System.out.println("You can't attack from this hand!");
			return false;
		} else {
			boolean badHand = true;
			String response = "";
			do {

				System.out.print("What hand to attack?");
				response = askForHand();
				if (((response.equals("r")) ? computer.right : computer.left) > 0) {
					badHand = false;
				}
			} while (badHand);
			total = (handFrom.equals("r") ? human.right : human.left);
			total += (response.equals("r") ? computer.right : computer.left);
			if (response.equals("r")) {
				computer.right = total;
			} else {
				computer.left = total;
			}
		}
		return true;

	}

	public void cycle() {
		System.out.println("cycle called");
		validateAndReport();
		refresh();

		newCompGo();

		System.out.println("players turn");
		//Ai.init(computer, human);
		//Ai.computerGo();
		//computerGo();
		validateAndReport();
		refresh();
	}

	private void newCompGo() {
		Move in = Ai3.computerGo(computer.right, computer.left, human.right, human.left);

		if (in.swapNum == 0) {
			if (in.cRight) {
				if (in.pRight) {
					human.right += computer.right;
					System.out.println("right hit with right");
				} else {
					human.left += computer.right;
					System.out.println("left hit with right");
				}
			} else {
				if (in.pRight) {
					human.right += computer.left;
					System.out.println("right hit with left");
				} else {
					human.left += computer.left;
					System.out.println("left hit with left");
				}
			}
		}else{
			System.out.println("Swapped! "+in.swapNum);
			if (in.cRight) {
				computer.left-=in.swapNum;
				computer.right+=in.swapNum;

			}else{
				computer.right-=in.swapNum;
				computer.left+=in.swapNum;

			}
		}
	}

}
