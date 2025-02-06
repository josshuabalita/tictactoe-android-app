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
    private boolean isPlayerOneTurn = true;
    private final String[][] board = new String[3][3];
    private static final int SIZE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        resetBoard();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView turnIndicator = findViewById(R.id.turnIndicator);
        if (turnIndicator != null) {
            turnIndicator.setText(getString(R.string.player_1_s_turn));
            turnIndicator.setTextColor(getResources().getColor(R.color.squidGreenIdentifier));

            Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
            turnIndicator.startAnimation(blinkAnimation);
        }
    }

    public void onCellClicked(View view) {
        ImageButton button = (ImageButton) view;
        TextView playerTurnIndicator = findViewById(R.id.turnIndicator);

        int[] position = getPosition(button.getId());
        int row = position[0];
        int cols = position[1];

        if (button.getDrawable() == null && board[row][cols].isEmpty()) {
            if (isPlayerOneTurn) {
                button.setImageResource(R.drawable.x_pawn);
                board[row][cols] = "X";
            } else {
                button.setImageResource(R.drawable.o_pawn);
                board[row][cols] = "O";
            }

            Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
            button.startAnimation(scaleAnimation);

            String winner = checkWinner();
            if (winner != null) {
                if (winner.equals("Draw")) {
                    playerTurnIndicator.setText(getString(R.string.draw));
                    playerTurnIndicator.setTextColor(getResources().getColor(R.color.drawIdentifier));
                    showPlayAgainOption("Draw");
                } else {
                    String winnerText = winner.equals("X") ? getString(R.string.player_1_s_wins) : getString(R.string.player_2_s_wins);
                    playerTurnIndicator.setText(winnerText);
                    playerTurnIndicator.setTextColor(winner.equals("X") ? getResources().getColor(R.color.squidGreenIdentifier) : getResources().getColor(R.color.squidRedIdentifier));
                    showPlayAgainOption(winner);
                }
                disableBoard();
                return;
            } else {
                isPlayerOneTurn = !isPlayerOneTurn;
                String turnIndicatorText = isPlayerOneTurn ? getString(R.string.player_1_s_turn) : getString(R.string.player_2_s_turn);
                playerTurnIndicator.setText(turnIndicatorText);
                playerTurnIndicator.setTextColor(isPlayerOneTurn ? getResources().getColor(R.color.squidGreenIdentifier) : getResources().getColor(R.color.squidRedIdentifier));
            }

            playerTurnIndicator.clearAnimation();
            Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
            playerTurnIndicator.startAnimation(blinkAnimation);
        }
    }

    private int[] getPosition(int id) {
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
            return new int[]{-1, -1};
        }
    }

    public String checkWinner() {
        for (int i = 0; i < SIZE; i++) {
            if (!board[i][0].isEmpty() && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return board[i][0];
            }
            if (!board[0][i].isEmpty() && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return board[0][i];
            }
        }

        if (!board[0][0].isEmpty() && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return board[0][0];
        }
        if (!board[0][2].isEmpty() && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return board[0][2];
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].isEmpty()) {
                    return null;
                }
            }
        }

        return "Draw";
    }

    private void resetBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = "";
            }
        }

        GridLayout gridLayout = findViewById(R.id.gridBoard);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View v = gridLayout.getChildAt(i);
            if (v instanceof ImageButton) {
                ((ImageButton) v).setImageResource(0);
                v.setEnabled(true);
            }
        }

        TextView playerTurnIndicator = findViewById(R.id.turnIndicator);
        playerTurnIndicator.setText(getString(R.string.player_1_s_turn));
        playerTurnIndicator.setTextColor(getResources().getColor(R.color.squidGreenIdentifier));

        isPlayerOneTurn = true;
    }

    private void resetGame() {
        resetBoard();
        resetPlayerViews();
    }

    private void showPlayAgainOption(String winner) {
        TextView player1 = findViewById(R.id.player1);
        TextView playerSymbol1 = findViewById(R.id.playerSymbol);
        Button playAgainButton1 = findViewById(R.id.playAgainButton_1);

        TextView player2 = findViewById(R.id.player2);
        TextView playerSymbol2 = findViewById(R.id.playerSymbol2);
        Button playAgainButton2 = findViewById(R.id.playAgainButton_2);

        if (winner.equals("X")) {
            playAgainButton2.setVisibility(View.VISIBLE);
            playAgainButton1.setVisibility(View.GONE);
            player2.setVisibility(View.GONE);
            playerSymbol2.setVisibility(View.GONE);
        } else if (winner.equals("O")) {
            playAgainButton2.setVisibility(View.GONE);
            playAgainButton1.setVisibility(View.VISIBLE);
            player1.setVisibility(View.GONE);
            playerSymbol1.setVisibility(View.GONE);
        } else {
            playAgainButton1.setVisibility(View.VISIBLE);
            playAgainButton2.setVisibility(View.VISIBLE);
        }

        playAgainButton1.setOnClickListener(v -> resetGame());
        playAgainButton2.setOnClickListener(v -> resetGame());
    }

    private void resetPlayerViews() {
        TextView player1 = findViewById(R.id.player1);
        TextView playerSymbol1 = findViewById(R.id.playerSymbol);
        Button playAgainButton1 = findViewById(R.id.playAgainButton_1);

        TextView player2 = findViewById(R.id.player2);
        TextView playerSymbol2 = findViewById(R.id.playerSymbol2);
        Button playAgainButton2 = findViewById(R.id.playAgainButton_2);

        player1.setVisibility(View.VISIBLE);
        playerSymbol1.setVisibility(View.VISIBLE);
        player2.setVisibility(View.VISIBLE);
        playerSymbol2.setVisibility(View.VISIBLE);

        playAgainButton1.setVisibility(View.GONE);
        playAgainButton2.setVisibility(View.GONE);
    }

    private void disableBoard() {
        GridLayout gridBoard = findViewById(R.id.gridBoard);
        for (int i = 0; i < gridBoard.getChildCount(); i++) {
            View v = gridBoard.getChildAt(i);
            if (v instanceof ImageButton) {
                v.setEnabled(false);
            }
        }
    }
}