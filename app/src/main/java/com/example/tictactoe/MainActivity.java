package com.example.tictactoe;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private boolean isPlayerOneTurn = true;

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

        if (button.getDrawable() == null) {
            if (isPlayerOneTurn) {
                button.setImageResource(R.drawable.x_pawn);
                playerTurnIndicator.setText(getString(R.string.player_2_s_turn));
                playerTurnIndicator.setTextColor(getResources().getColor(R.color.squidRedIdentifier));
            } else {
                button.setImageResource(R.drawable.o_pawn);
                playerTurnIndicator.setText(getString(R.string.player_1_s_turn));
                playerTurnIndicator.setTextColor(getResources().getColor(R.color.squidGreenIdentifier));
            }

            Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_in);
            button.startAnimation(scaleAnimation);

            playerTurnIndicator.clearAnimation();
            Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
            playerTurnIndicator.startAnimation(blinkAnimation);

            isPlayerOneTurn = !isPlayerOneTurn;
        }
    }

}