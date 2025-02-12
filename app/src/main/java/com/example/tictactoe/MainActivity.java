package com.example.tictactoe;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private boolean isPlayerOneTurn = true; // Player 1 starts
    private final String[][] board = new String[3][3]; // 3x3 board
    private static final int SIZE = 3; // Board size is 3

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge mode
        setContentView(R.layout.activity_main);
        resetBoard(); // Reset board when the app starts

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom); // Apply padding
            return insets;
        });

        TextView turnIndicator = findViewById(R.id.turnIndicator);
        if (turnIndicator != null) {
            turnIndicator.setText(getString(R.string.player_1_s_turn)); // Set Player 1's turn message
            turnIndicator.setTextColor(getResources().getColor(R.color.squidGreenIdentifier)); // Player 1's color

            Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink); // Blinking animation
            turnIndicator.startAnimation(blinkAnimation);
        }
    }

    public void onCellClicked(View view) {
        ImageButton button = (ImageButton) view; // Get clicked button
        TextView playerTurnIndicator = findViewById(R.id.turnIndicator); // Get turn indicator

        int[] position = getPosition(button.getId()); // Get position of clicked cell
        int row = position[0];
        int cols = position[1];

        // Check if the cell is empty
        if (button.getDrawable() == null && board[row][cols].isEmpty()) {
            if (isPlayerOneTurn) {
                button.setImageResource(R.drawable.x_pawn); // Player 1's move
                board[row][cols] = "X";
            } else {
                button.setImageResource(R.drawable.o_pawn); // Player 2's move
                board[row][cols] = "O";
            }

            Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_in); // Button animation
            button.startAnimation(scaleAnimation);

            String winner = checkWinner(); // Check if there's a winner
            if (winner != null) {
                // If there's a winner or a draw
                if (winner.equals("Draw")) {
                    playerTurnIndicator.setText(getString(R.string.draw)); // Draw message
                    playerTurnIndicator.setTextColor(getResources().getColor(R.color.drawIdentifier)); // Draw color
                    showPlayAgainOption("Draw");
                } else {
                    String winnerText = winner.equals("X") ? getString(R.string.player_1_s_wins) : getString(R.string.player_2_s_wins);
                    playerTurnIndicator.setText(winnerText); // Display winner
                    playerTurnIndicator.setTextColor(winner.equals("X") ? getResources().getColor(R.color.squidGreenIdentifier) : getResources().getColor(R.color.squidRedIdentifier));
                    showPlayAgainOption(winner);
                }
                disableBoard(); // Disable board after game ends
                return;
            } else {
                isPlayerOneTurn = !isPlayerOneTurn; // Switch turns
                String turnIndicatorText = isPlayerOneTurn ? getString(R.string.player_1_s_turn) : getString(R.string.player_2_s_turn);
                playerTurnIndicator.setText(turnIndicatorText); // Update turn indicator
                playerTurnIndicator.setTextColor(isPlayerOneTurn ? getResources().getColor(R.color.squidGreenIdentifier) : getResources().getColor(R.color.squidRedIdentifier));
            }

            playerTurnIndicator.clearAnimation(); // Reset the animation
            Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
            playerTurnIndicator.startAnimation(blinkAnimation); // Restart the blinking animation
        }
    }

    private int[] getPosition(int id) {
        // Convert button ID to board position
        if (id == R.id.button1) {
            return new int[]{0, 0};
        } else if (id == R.id.button2) {
            return new int[]{0, 1};
        } else if (id == R.id.button3) {
            return new int[]{0, 2};
        } else if (id == R.id.button4) {
            return new int[]{1, 0};
        } else if (id == R.id.button5) {
            return new int[]{1, 1};
        } else if (id == R.id.button6) {
            return new int[]{1, 2};
        } else if (id == R.id.button7) {
            return new int[]{2, 0};
        } else if (id == R.id.button8) {
            return new int[]{2, 1};
        } else if (id == R.id.button9) {
            return new int[]{2, 2};
        } else {
            return new int[]{-1, -1}; // Invalid ID
        }
    }

    public String checkWinner() {
        // Check rows, columns, and diagonals for winner
        for (int i = 0; i < SIZE; i++) {
            if (!board[i][0].isEmpty() && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return board[i][0]; // Row winner
            }
            if (!board[0][i].isEmpty() && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return board[0][i]; // Column winner
            }
        }

        if (!board[0][0].isEmpty() && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return board[0][0]; // Diagonal winner
        }
        if (!board[0][2].isEmpty() && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return board[0][2]; // Other diagonal winner
        }

        // Check for draw
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].isEmpty()) {
                    return null; // Game still going
                }
            }
        }

        return "Draw"; // Draw if no empty cells
    }

    private void resetBoard() {
        // Reset all cells to empty
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = "";
            }
        }

        GridLayout gridLayout = findViewById(R.id.gridBoard);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View v = gridLayout.getChildAt(i);
            if (v instanceof ImageButton) {
                ((ImageButton) v).setImageResource(0); // Remove image from button
                v.setEnabled(true); // Enable button
            }
        }

        TextView playerTurnIndicator = findViewById(R.id.turnIndicator);
        playerTurnIndicator.setText(getString(R.string.player_1_s_turn)); // Set turn message for Player 1
        playerTurnIndicator.setTextColor(getResources().getColor(R.color.squidGreenIdentifier)); // Set Player 1's color

        isPlayerOneTurn = true; // Set Player 1 to start
    }

    private void resetGame() {
        // Reset board and views
        resetBoard();
        resetPlayerViews();
    }

    private void showPlayAgainOption(String winner) {
        // Show "Play Again" button based on who won
        TextView player1 = findViewById(R.id.player1);
        TextView playerSymbol1 = findViewById(R.id.playerSymbol);
        Button playAgainButton1 = findViewById(R.id.playAgainButton_1);

        TextView player2 = findViewById(R.id.player2);
        TextView playerSymbol2 = findViewById(R.id.playerSymbol2);
        Button playAgainButton2 = findViewById(R.id.playAgainButton_2);

        if (winner.equals("X")) {
            playAgainButton2.setVisibility(View.VISIBLE); // Show Player 2's play again button
            playAgainButton1.setVisibility(View.GONE); // Hide Player 1's play again button
            player2.setVisibility(View.GONE); // Hide Player 2's info
            playerSymbol2.setVisibility(View.GONE); // Hide Player 2's symbol
        } else if (winner.equals("O")) {
            playAgainButton2.setVisibility(View.GONE); // Hide Player 2's button
            playAgainButton1.setVisibility(View.VISIBLE); // Show Player 1's play again button
            player1.setVisibility(View.GONE); // Hide Player 1's info
            playerSymbol1.setVisibility(View.GONE); // Hide Player 1's symbol
        } else {
            playAgainButton1.setVisibility(View.VISIBLE); // Show both buttons for draw
            playAgainButton2.setVisibility(View.VISIBLE);
        }

        playAgainButton1.setOnClickListener(v -> resetGame()); // Reset game if Player 1 clicks
        playAgainButton2.setOnClickListener(v -> resetGame()); // Reset game if Player 2 clicks
    }

    private void resetPlayerViews() {
        // Reset player info visibility
        TextView player1 = findViewById(R.id.player1);
        TextView playerSymbol1 = findViewById(R.id.playerSymbol);
        Button playAgainButton1 = findViewById(R.id.playAgainButton_1);

        TextView player2 = findViewById(R.id.player2);
        TextView playerSymbol2 = findViewById(R.id.playerSymbol2);
        Button playAgainButton2 = findViewById(R.id.playAgainButton_2);

        player1.setVisibility(View.VISIBLE); // Show Player 1 info
        playerSymbol1.setVisibility(View.VISIBLE); // Show Player 1 symbol
        player2.setVisibility(View.VISIBLE); // Show Player 2 info
        playerSymbol2.setVisibility(View.VISIBLE); // Show Player 2 symbol

        playAgainButton1.setVisibility(View.GONE); // Hide Player 1's play again button
        playAgainButton2.setVisibility(View.GONE); // Hide Player 2's play again button
    }

    private void disableBoard() {   
        // Disable all board buttons after game end
        GridLayout gridBoard = findViewById(R.id.gridBoard);
        for (int i = 0; i < gridBoard.getChildCount(); i++) {
            View v = gridBoard.getChildAt(i);
            if (v instanceof ImageButton) {
                v.setEnabled(false); // Disable each button
            }
        }
    }
}
