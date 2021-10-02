package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    public TextView scoreTextView;
    public TextView HighestScoreTextView;
    public Button button;
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        scoreTextView = findViewById(R.id.Score);
        HighestScoreTextView = findViewById(R.id.highest_Score);
        button = findViewById(R.id.button);
        imageButton =findViewById(R.id.share_button);

        Bundle bundle = getIntent().getExtras();

        scoreTextView.setText(new StringBuilder().append("SCORE : ").append(String.valueOf(bundle.getInt("Score"))).toString());
        HighestScoreTextView.setText(new StringBuilder().append("HIGHEST SCORE :").append(String.valueOf(bundle.getInt("highest score"))).toString());

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Intent.ACTION_SEND);

                intent.setType("text/plain");
                String[] add = new String[] {"harshitkumar24112002@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL , add );
                intent.putExtra(Intent.EXTRA_SUBJECT, "INFO FROM TRIVIA APP");
                intent.putExtra(Intent.EXTRA_TEXT , "MY CURRENT SCORE: " + String.valueOf(bundle.getInt("Score"))+ "\n" +
                        "MY HIGHEST SCORE: "+ String.valueOf(bundle.getInt("highest score")));
                startActivity(intent);
            }
        });

        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);

        });
    }
}