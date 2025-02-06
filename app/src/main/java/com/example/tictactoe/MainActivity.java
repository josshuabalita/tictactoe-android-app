package com.example.tictactoe;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private boolean isPlayerOneTurn = true;
    private final int[][] board = new int[3][3];
    private final int PLAYER_ONE = 1;
    private final int PLAYER_TWO = 2;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridLayout = findViewById(R.id.gridLayout);

        TextView turnIndicator = findViewById(R.id.turnIndicator);
        turnIndicator.setText(getString(R.string.player_1_s_turn));
        turnIndicator.setTextColor(getResources().getColor(R.color.squidGreenIdentifier));

        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        turnIndicator.startAnimation(blinkAnimation);
    }

    public void onCellClicked(View view) {
        ImageButton button = (ImageButton) view;
        TextView playerTurnIndicator = findViewById(R.id.turnIndicator);
        int row = Integer.parseInt(view.getTag().toString().split(",")[0]);
        int col = Integer.parseInt(view.getTag().toString().split(",")[1]);

        if (board[row][col] == 0) {
            if (isPlayerOneTurn) {
                button.setImageResource(R.drawable.x_pawn);
                board[row][col] = PLAYER_ONE;
                playerTurnIndicator.setText(getString(R.string.player_2_s_turn));
                playerTurnIndicator.setTextColor(getResources().getColor(R.color.squidRedIdentifier));
            } else {
                button.setImageResource(R.drawable.o_pawn);
                board[row][col] = PLAYER_TWO;
                playerTurnIndicator.setText(getString(R.string.player_1_s_turn));
                playerTurnIndicator.setTextColor(getResources().getColor(R.color.squidGreenIdentifier));
            }

            Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
            button.startAnimation(scaleAnimation);

            playerTurnIndicator.clearAnimation();
            Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
            playerTurnIndicator.startAnimation(blinkAnimation);

            if (checkWinner()) {
                String winner = isPlayerOneTurn ? getString(R.string.player_1_wins) : getString(R.string.player_2_wins);
                Toast.makeText(this, winner, Toast.LENGTH_LONG).show();
                resetBoard();
            } else if (isBoardFull()) {
                Toast.makeText(this, getString(R.string.draw), Toast.LENGTH_LONG).show();
                resetBoard();
            }

            isPlayerOneTurn = !isPlayerOneTurn;
        }
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return true;
            }
        }
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View cell = gridLayout.getChildAt(i);
            if (cell instanceof ImageButton) {
                ((ImageButton) cell).setImageDrawable(null);
            }
        }

        isPlayerOneTurn = true;
        TextView playerTurnIndicator = findViewById(R.id.turnIndicator);
        playerTurnIndicator.setText(getString(R.string.player_1_s_turn));
        playerTurnIndicator.setTextColor(getResources().getColor(R.color.squidGreenIdentifier));
    }
}
