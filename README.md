<img width="405" alt="Screenshot 2026-01-05 at 12 41 03 PM" src="https://github.com/user-attachments/assets/c559ba6c-178f-4355-a9d4-2c785bd6ffa5" />

# **Jerry-Tac-Toe**

Jerry-Tac-Toe is a Java-based desktop game that reimagines classic Tic-Tac-Toe with a creative twist and a randomized computer 
opponent. Built using Java Swing and AWT, the project focuses on object-oriented design, GUI development, and reliable game 
state management.

# **Features**

- Custom graphical user interface built with Java Swing & AWT
- Randomized computer opponent logic for varied gameplay
- Clear visual feedback for player turns, wins, and ties
- Reliable game state management using object-oriented principles
- Smooth user interaction through event-driven programming

# **How to Play**

**Objective:**
- Defeat the computer by getting three of your symbols in a row—horizontally, vertically, or diagonally.

**Gameplay:**
- The player and computer take turns placing their marks on the board.
- The computer opponent selects moves using randomized logic.
- The game automatically detects wins, losses, and ties.
- Players can restart and play multiple rounds without restarting the application.

# **Technologies Used**

- **Language:** Java
- **GUI Frameworks:** Swing, AWT
- **Concepts:**
  - Object-Oriented Programming (OOP)
  - Event handling
  - Game state management
  - Algorithmic logic for win/tie detection
- **Tools:**
  - Git & GitHub
  - Java Development Kit (JDK)

# **Challenges & Solutions**

**Game State Management:**
- Tracking player turns, board updates, and end-game conditions became complex as the game progressed. This was addressed by 
separating responsibilities into dedicated classes and validating the board state after every move to consistently detect wins, 
losses, and ties.

**Computer Opponent Logic:**
- The computer opponent initially risked selecting already-occupied spaces. This was solved by implementing randomized move 
selection constrained to available board positions, ensuring both valid moves and varied gameplay.

**GUI Responsiveness:**
- Synchronizing user input with visual updates was challenging in an event-driven environment. Swing event listeners were used
to handle user actions and immediately update the interface, ensuring the GUI accurately reflected the current game state
without lag or inconsistencies.

# **What I Learned**
- Designing Java applications using object-oriented principles
- Building interactive GUIs with Swing and AWT
- Implementing algorithms for win and tie detection
- Structuring code for readability, maintainability, and scalability

# **What's Next**
- Add difficulty levels with smarter AI logic
- Improve UI visuals and animations
- Add score tracking across multiple rounds

# **Conclusion**
This project was developed as part of an object-oriented programming course in April 2025. It offered practical experience in 
Java, GUI development, and game state management, while reinforcing object-oriented design principles through the creation of a 
complete desktop application.

# **Credits**
- Created by Helena Kazenski (April 2025)
