/*

Author: Helena Kazenski
email: hkazenski@butler.edu
Class: CS248
Section: 03
File name: JerryTacToe.java
Date: 04/29/2025

Description: Plays Jerry-Tac-Toe with the user. Jerry-Tac-Toe is similar to Tic-Tac-Toe,
but with a different boared. You cannot win if a sequence of three isn't connected by a line
(2, 5, 8, for example) or if it's not in a straght line (1, 4, 7, for example).
The user decides who goes first, the user or the computer. The user makes a move by selecting 
the number that corresponds to the spot they want to play in the dropdown menu. The computer
uses the code given in the textbook to evaluate which move will be the best by simulating the
possible outcomes of future moves.

Honor pledge: I pledge that I have neither given nor received any help on this assignment,
unless stated and cited otherwise. I pledge that all work is my own, and not copied from
any other source.

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class JerryTacToe extends JFrame 
{
   private JComboBox<Integer> moveSelector;
   private JButton submitButton;
   private JLabel moveLabel;
   private JButton meButton, computerButton;
   private JLabel whoGoesFirstLabel;
   private int turn; // 1 for player, 0 for computer
   private boolean[] spotsTaken = new boolean[9]; // Track taken spots
   private ArrayList<Integer> availableSpots = new ArrayList<>();
   private CirclePanel circlePanel;
   private int[] playerMoves = new int[9]; // Player's moves
   private int[] computerMoves = new int[9]; // Computer's moves


   public JerryTacToe() 
   {
       // Make Title
       setTitle("Jerry-Tac-Toe");
       setSize(1000, 1100);
       setLocation(200, 200);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       // Set layout to null for manual positioning
       setLayout(null);

       // "Choose who goes first" panel
       whoGoesFirstLabel = new JLabel("Choose who goes first:");
       whoGoesFirstLabel.setFont(new Font("Arial", Font.PLAIN, 20));
       whoGoesFirstLabel.setBounds(300, 300, 220, 30);
       add(whoGoesFirstLabel);

       // Me button
       meButton = new JButton("Me");
       meButton.setFont(new Font("Arial", Font.PLAIN, 20));
       meButton.setBounds(300, 400, 100, 40);
       meButton.addActionListener(new ActionListener() 
       {
           @Override
           public void actionPerformed(ActionEvent e) 
           {
               turn = 1; // Player goes first
               startGame();
           }
       });
       add(meButton);

       // Computer Button
       computerButton = new JButton("Computer");
       computerButton.setFont(new Font("Arial", Font.PLAIN, 20));
       computerButton.setBounds(450, 400, 150, 40);
       computerButton.addActionListener(new ActionListener() 
       {
           @Override
           public void actionPerformed(ActionEvent e) 
           {
               turn = 0; // Computer goes first
               startGame();
           }
       });
       add(computerButton);


       setVisible(true);
   }


   //============//
   // START GAME //
   //============//
   private void startGame() 
   {
       // Remove the "Choose who goes first" screen
       remove(whoGoesFirstLabel);
       remove(meButton);
       remove(computerButton);

       // Custom panel for the game
       circlePanel = new CirclePanel();
       circlePanel.setBounds(0, 0, 1000, 700); // Adjust height to leave space below if needed
       add(circlePanel);

       // Move Label
       moveLabel = new JLabel("Choose where to move:");
       moveLabel.setFont(new Font("Arial", Font.PLAIN, 20));
       moveLabel.setBounds(300, 800, 220, 30);
       add(moveLabel);

       // Move Selector
       moveSelector = new JComboBox<>();
       for (int i = 1; i <= 9; i++) 
       {
           moveSelector.addItem(i);
       }
       moveSelector.setFont(new Font("Arial", Font.PLAIN, 20));
       moveSelector.setBounds(530, 805, 60, 40);
       add(moveSelector);

       // Set the initial selected item to the first available spot (default)
       updateAvailableSpots();

       // Submit Button
       submitButton = new JButton("Submit");
       submitButton.setFont(new Font("Arial", Font.BOLD, 20));
       submitButton.setBounds(600, 800, 120, 30);
       add(submitButton);

       // Action Listener for Submit Button
       submitButton.addActionListener(new ActionListener() 
       {
           @Override
           public void actionPerformed(ActionEvent e) 
           {
               int choice = (Integer) moveSelector.getSelectedItem();
               if (!spotsTaken[choice - 1]) 
               {
                   // Mark the spot as taken
                   spotsTaken[choice - 1] = true;

                   // Draw the player's move (red circle)
                   circlePanel.makeMove(choice, Color.RED);

                   // Check for winner or tie
                   if (checkWinner()) 
                   {
                       displayWinner();
                   } 
                   else if (isBoardFull()) 
                   {
                       displayTie();
                   } 
                   else 
                   {
                       // Update available spots and refresh dropdown
                       updateAvailableSpots();
                       switchTurn();
                   }
               }
           }
       });

       // Refresh the frame to show the new components
       revalidate();
       repaint();

       // If computer goes first, it will immediately make a move
       if (turn == 0) 
       {
           computerMove();
       }
   }

   // After turn, update available spots
   private void updateAvailableSpots() 
   {
       availableSpots.clear();
       for (int i = 0; i < 9; i++) 
       {
           if (!spotsTaken[i]) {
               availableSpots.add(i + 1);
           }
       }

       // Update the dropdown with available spots
       moveSelector.removeAllItems();
       for (int spot : availableSpots) 
       {
           moveSelector.addItem(spot);
       }

       // Set the first available spot as default
       if (!availableSpots.isEmpty()) 
       {
           moveSelector.setSelectedItem(availableSpots.get(0));
       }
   }

   private void switchTurn() 
   {
       // Switch turn between player (1) and computer (0)
       if (turn == 1) 
       {
           turn = 0; // Switch to computer's turn
           computerMove();
       } 
       else 
       {
           turn = 1; // Switch back to player's turn
       }
   }

   // Stores evaluated moves by computer
   private class Best 
   {
        int val;
        int spot;

        public Best(int val, int spot) 
        {
            this.val = val;
            this.spot = spot;
        }

        public Best(int val) 
        {
            this(val, -1);
        }
    }

    // Imaginary game ran by computer to evaluate best move; Not really happening
    private int evaluateWinner(int[] tempPlayerMoves, int[] tempComputerMoves) 
    {
        int[][] winningSequences = 
        {
            {1, 2, 3},
            {1, 4, 8},
            {1, 5, 9},
            {2, 4, 7},
            {2, 6, 9},
            {3, 5, 7},
            {3, 6, 8},
            {4, 5, 6},
            {7, 8, 9}
        };

        for (int[] seq : winningSequences) 
        {
            if (tempPlayerMoves[seq[0] - 1] == 1 &&
                tempPlayerMoves[seq[1] - 1] == 1 &&
                tempPlayerMoves[seq[2] - 1] == 1) 
            {
                return 1; // Player wins
            }

            if (tempComputerMoves[seq[0] - 1] == 1 &&
                tempComputerMoves[seq[1] - 1] == 1 &&
                tempComputerMoves[seq[2] - 1] == 1) 
            {
                return 0; // Computer wins
            }
        }

        return -1; // No winner yet
    }

    // Code taken from textbook to find the best move
    private Best chooseBestMove(int[] tempPlayerMoves, int[] tempComputerMoves, boolean[] tempSpotsTaken, int side) 
    {
        // Checks the spots that haven't been played yet
        ArrayList<Integer> tempAvailableSpots = new ArrayList<>();
        for (int i = 0; i < 9; i++) 
        {
            if (!tempSpotsTaken[i]) 
            {
                tempAvailableSpots.add(i);
            }
        }

        int winner = evaluateWinner(tempPlayerMoves, tempComputerMoves);
        if (winner == 0) return new Best(1); // Computer wins
        if (winner == 1) return new Best(-1); // Player wins
        if (tempAvailableSpots.isEmpty()) return new Best(0); // Tie

        int bestVal = (side == 0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestSpot = -1; // Stores the best move

        // Simulates the game for each available spot, evaluating the results (win or lose)
        for (int spot : tempAvailableSpots) 
        {
            // Clone arrays
            int[] newPlayerMoves = tempPlayerMoves.clone();
            int[] newComputerMoves = tempComputerMoves.clone();
            boolean[] newSpotsTaken = tempSpotsTaken.clone();

            // Marks the chosen spot as taken
            newSpotsTaken[spot] = true;

            // Marks the move depending on whose turn it is
            if (side == 0) 
            {
                newComputerMoves[spot] = 1;
            } 
            else 
            {
                newPlayerMoves[spot] = 1;
            }

            Best reply = chooseBestMove(newPlayerMoves, newComputerMoves, newSpotsTaken, 1 - side); // Evaluates the result of next turn
            int value = reply.val; // Stores the result after moving

            if ((side == 0 && value >= bestVal) || (side == 1 && value <= bestVal)) 
            {
                bestVal = value; // Update the best value if better
                bestSpot = spot; // Update best move with the current spot
            }
        }

        // Return the best value and the corresponding best move
        return new Best(bestVal, bestSpot);
    }

   private void computerMove() 
   {
        Best best = chooseBestMove(playerMoves.clone(), computerMoves.clone(), spotsTaken.clone(), 0);
        int choice = best.spot + 1;

       // Mark the spot as taken
       spotsTaken[choice - 1] = true;

       // Draw the computer's move (blue circle)
       circlePanel.makeMove(choice, Color.BLUE);

       // Check for winner or tie
       if (checkWinner()) 
       {
           displayWinner();
       } 
       else if (availableSpots.isEmpty()) 
       {
           displayTie();
       } 
       else 
       {
           // Update available spots and refresh dropdown
           updateAvailableSpots();

           // Switch back to player's turn
           switchTurn();
       }
   }

   private boolean isBoardFull() 
   {
        for (boolean taken : spotsTaken) 
        {
            if (!taken) 
            {
                return false;  // There's still an available spot
            }
        }

        return true;  // No spots left, the board is full
    }

   private boolean checkWinner() 
   {
       // Sequences that you can win from
       int[][] winningSequences = 
       {
           {1, 2, 3},
           {1, 4, 8},
           {1, 5, 9},
           {2, 4, 7},
           {2, 6, 9},
           {3, 5, 7},
           {3, 6, 8},
           {4, 5, 6},
           {7, 8, 9}
       };

       // Check for a winning line for player (1) or computer (0)
       for (int[] seq : winningSequences) 
       {
           if (playerMoves[seq[0] - 1] == 1 && playerMoves[seq[1] - 1] == 1 && playerMoves[seq[2] - 1] == 1) 
           {
               return true; // Player wins
           }

           if (computerMoves[seq[0] - 1] == 1 && computerMoves[seq[1] - 1] == 1 && computerMoves[seq[2] - 1] == 1) 
           {
               return true; // Computer wins
           }
       }

       return false; // No winner yet
   }

   private void displayWinner() 
   {
       // See if player or computer wins
       String message = turn == 1 ? "You Win!" : "You Lose!";

       // Final option
       int option = JOptionPane.showOptionDialog(this,
           message + "\nWould you like to play again?",
           "Game Over",
           JOptionPane.YES_NO_CANCEL_OPTION,
           JOptionPane.INFORMATION_MESSAGE,
           null,
           new String[] { "Start Over", "Quit" },
           "Start Over");

       if (option == 0) // Start Over
       {
           resetGame();
       } 
       else if (option == 1) // Quit
       {
           System.exit(0);  // Quit the game
       }
   }

   private void displayTie() 
   {
       int option = JOptionPane.showOptionDialog(this,
           "It's a Tie!\nWould you like to play again?",
           "Game Over",
           JOptionPane.YES_NO_CANCEL_OPTION,
           JOptionPane.INFORMATION_MESSAGE,
           null,
           new String[] { "Start Over", "Quit" },
           "Start Over");

       if (option == 0) // Start Over
       { 
           resetGame();
       } 
       else if (option == 1) // Quit
       {
           System.exit(0);  // Quit the game
       }
   }

   private void resetGame() 
   {
       // Reset game state
       for (int i = 0; i < 9; i++) 
       {
           spotsTaken[i] = false;
           playerMoves[i] = 0; // Reset player moves
           computerMoves[i] = 0; // Reset computer moves
       }

       availableSpots.clear();
       updateAvailableSpots();
       circlePanel.repaint();
       new JerryTacToe();
   }

   private class CirclePanel extends JPanel 
   {
       // Pairs that could have a relation line drawn
       private final int[][] pairs = 
       {
           {1, 2}, {1, 5}, {1, 4}, {2, 3}, {3, 5}, {3, 6},
           {2, 4}, {2, 6}, {4, 5}, {4, 7}, {4, 8}, {5, 6},
           {5, 7}, {5, 9}, {6, 8}, {6, 9}, {7, 8}, {8, 9}
       };

       @Override
       protected void paintComponent(Graphics g) 
       {
           super.paintComponent(g);

           g.setColor(new Color(128, 0, 128)); // Purple
           g.setFont(new Font("Arial", Font.BOLD, 50));
           FontMetrics titleMetrics = g.getFontMetrics();
           String title = "Jerry-Tac-Toe";
           int titleWidth = titleMetrics.stringWidth(title);
           g.drawString(title, (getWidth() - titleWidth) / 2, 100);

           g.setFont(new Font("Arial", Font.BOLD, 30));
           FontMetrics labelMetrics = g.getFontMetrics();

           // Color legend: player
           String playerLabel = "Player:";
           int playerLabelX = 100;
           int playerLabelY = 180;
           g.setColor(Color.BLACK);
           g.drawString(playerLabel, playerLabelX, playerLabelY);

           int playerCircleX = playerLabelX + labelMetrics.stringWidth(playerLabel) + 20;
           int playerCircleY = playerLabelY - 50;
           g.setColor(Color.RED);
           g.fillOval(playerCircleX, playerCircleY, 75, 75);

           // Color legend: computer
           String computerLabel = "Computer:";
           int computerLabelWidth = labelMetrics.stringWidth(computerLabel);
           int computerLabelX = getWidth() - computerLabelWidth - 200;
           int computerLabelY = 180;
           g.setColor(Color.BLACK);
           g.drawString(computerLabel, computerLabelX, computerLabelY);

           int computerCircleX = computerLabelX + computerLabelWidth + 20;
           int computerCircleY = computerLabelY - 50;
           g.setColor(Color.BLUE);
           g.fillOval(computerCircleX, computerCircleY, 75, 75);

           // Draw the grid
           g.setColor(Color.BLACK);
           int circleDiameter = 75;
           int number = 1;

           g.setFont(new Font("Arial", Font.BOLD, 20));
           FontMetrics metrics = g.getFontMetrics();

           Point[] centers = new Point[10];

           // Create playing grid of circles
           for (int row = 0; row < 3; row++) 
           {
               for (int col = 0; col < 3; col++) 
               {
                   int x;
                   int y = row * 175 + 250;

                   if (row == 1) 
                   {
                       if (col == 0) 
                       {
                           x = 270 + 50;
                       } 
                       else if (col == 1) 
                       {
                           x = 270 + 180;
                       } 
                       else 
                       {
                           x = 270 + 175 + 125;
                       }
                   } 
                   else 
                   {
                       x = col * 225 + 225;
                   }

                   g.fillOval(x, y, circleDiameter, circleDiameter);

                   int centerX = x + circleDiameter / 2;
                   int centerY = y + circleDiameter / 2;
                   centers[number] = new Point(centerX, centerY);

                   number++;
               }
           }

           // All lines (black lines)
           int[][] sequences = 
           {
               {1, 2, 3},
               {4, 5, 6},
               {7, 8, 9},
               {1, 4, 8},
               {1, 5, 9},
               {2, 4, 7},
               {2, 6, 9},
               {3, 5, 7},
               {3, 6, 8}
           };

           Graphics2D g2 = (Graphics2D) g;
           g2.setStroke(new BasicStroke(4));

           // Draw black lines
           for (int[] seq : sequences) 
           {
               Point p1 = centers[seq[0]];
               Point p2 = centers[seq[1]];
               Point p3 = centers[seq[2]];

               g2.drawLine(p1.x, p1.y, p2.x, p2.y);
               g2.drawLine(p2.x, p2.y, p3.x, p3.y);
           }

           // Draw numbers on the circles
           for (int i = 1; i <= 9; i++) 
           {
               Point center = centers[i];
               String numStr = String.valueOf(i);
               int textWidth = metrics.stringWidth(numStr);
               int textHeight = metrics.getHeight();
               int textX = center.x - textWidth / 2;
               int textY = center.y + textHeight / 4;

               g.setColor(Color.WHITE);
               g.drawString(numStr, textX, textY);
               g.setColor(Color.BLACK);
           }

           // Draw player's and computer's moves (red and blue circles)
           for (int i = 0; i < 9; i++) 
           {
               if (playerMoves[i] == 1) 
               {
                   g.setColor(Color.RED);
                   g.fillOval(centers[i + 1].x - 30, centers[i + 1].y - 30, 60, 60);
               }
               if (computerMoves[i] == 1) 
               {
                   g.setColor(Color.BLUE);
                   g.fillOval(centers[i + 1].x - 30, centers[i + 1].y - 30, 60, 60);
               }
           }

           // Draw the winning lines based on moves
           g2.setStroke(new BasicStroke(4));

           for (int[] pair : pairs) 
           {
               int spot1 = pair[0] - 1;
               int spot2 = pair[1] - 1;
               if ((playerMoves[spot1] == 1 && playerMoves[spot2] == 1) || (computerMoves[spot1] == 1 && computerMoves[spot2] == 1)) 
               {
                   Point p1 = centers[pair[0]];
                   Point p2 = centers[pair[1]];
                   g2.setColor(playerMoves[spot1] == 1 ? Color.RED : Color.BLUE);
                   g2.drawLine(p1.x, p1.y, p2.x, p2.y);
               }
           }
       }

       public void makeMove(int spot, Color color) 
       {
           int index = spot - 1;
           if (color == Color.RED) 
           {
               playerMoves[index] = 1;
           } 
           else if (color == Color.BLUE) 
           {
               computerMoves[index] = 1;
           }

           repaint();
       }
   }

   //============//
   //    MAIN    //
   //============//
   public static void main(String[] args) 
   {
       new JerryTacToe();
   }
}
