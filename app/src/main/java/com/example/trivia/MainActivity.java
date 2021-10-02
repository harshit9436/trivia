package com.example.trivia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.trivia.Model.Questions;
import com.example.trivia.data.Queslistresponce;
import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
 String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
     private SoundPool soundPool;
     private ActivityMainBinding binding;
     private int correct_sound , incorrect_sound;
     int questionIndex =0;
     List<Questions> questions;
     int score = 0 ;
     int highestScore = 0;
     private static final String MESSAGE_ID = "message";
     private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .build();


        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();
        correct_sound = soundPool.load(this , R.raw.correct, 1);
        incorrect_sound = soundPool.load(this , R.raw.defeat_two, 1);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        questions =   new Repository().getQuestions(new Queslistresponce() {
           @Override
           public void processFinished(List<Questions> questionsList) {
               binding.QuestionTextview.setText(questionsList.get(questionIndex).getAnswer());
               binding.QuestionNumber.setText(String.format(MainActivity.this
                       .getString(R.string.Question_index), questionIndex + 1, questions.size() + 1));
               binding.CurrentScore.setText(new StringBuilder().append("Score = ").append(score).toString());

           }
       });


      binding.NextButton.setOnClickListener(v -> {
          questionIndex = (questionIndex+1) % questions.size();
          updateQuestion();
          binding.QuestionNumber.setText(String.format(getString(R.string.Question_index), questionIndex+1, questions.size()+1));
      });

      binding.TrueButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              iscorrect(true);
             updateQuestion();

          }
      });


      binding.FalseButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              iscorrect(false);
             updateQuestion();
          }
      });


   SharedPreferences getSharedData = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
    int highest_score = getSharedData.getInt("Highest_score" , 0);
    binding.textView3.setText(new StringBuilder().append("Highest Score: ").append(String.valueOf(highest_score)).toString());
        highestScore = highest_score;



 }

    private void iscorrect(boolean choosenOption) {
        boolean answer = questions.get(questionIndex).isAnsTrue();
        if (answer == choosenOption) {
            Toast.makeText(MainActivity.this, R.string.Correct_ans, Toast.LENGTH_SHORT).show();
            score += 4;
            binding.CurrentScore.setText(new StringBuilder().append("Score = ").append(score).toString());
            fadeAnim();


        } else {
            Toast.makeText(MainActivity.this, R.string.Incorrect_ans, Toast.LENGTH_SHORT).show();
            shakeAnim();
            if (score > 0) {
                score = score - 1;
                binding.CurrentScore.setText(new StringBuilder().append("Score = ").append(score).toString());
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences getSharedData = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        int highest_score = getSharedData.getInt("Highest_score" , 0);
        if(score>highest_score){
        editor.putInt("Highest_score", score);
        }
        else{
            editor.putInt("Highest_score" , highest_score);
        }
        editor.apply();
        highestScore = highest_score;

    }

    private void updateQuestion() {
     binding.QuestionTextview.setText(questions.get(questionIndex).getAnswer());
    }


    private void fadeAnim(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.QuestionTextview.setTextColor(Color.GREEN);
                soundPool.play(correct_sound , 1,1,0,0,1);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.QuestionTextview.setTextColor(Color.WHITE);
                questionIndex = (questionIndex+1) % questions.size();
                updateQuestion();
                binding.QuestionNumber.setText(String.format(getString(R.string.Question_index), questionIndex+1, questions.size()+1));

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });





    }

    private void shakeAnim(){
      Animation shake= AnimationUtils.loadAnimation(MainActivity.this , R.anim.shake_anim);
      binding.cardView.setAnimation(shake);

      shake.setAnimationListener(new Animation.AnimationListener() {
          @Override
          public void onAnimationStart(Animation animation) {
              binding.QuestionTextview.setTextColor(Color.RED);
              soundPool.play(incorrect_sound ,1,1,0,0,1);
          }

          @Override
          public void onAnimationEnd(Animation animation) {
              binding.QuestionTextview.setTextColor(Color.WHITE);

              questionIndex = (questionIndex+1) % questions.size();
              updateQuestion();
              binding.QuestionNumber.setText(String.format(getString(R.string.Question_index), questionIndex+1, questions.size()+1));
          }

          @Override
          public void onAnimationRepeat(Animation animation) {

          }
      });

    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences getSharedData = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        int highest_score = getSharedData.getInt("Highest_score" , 0);
        if(score>highest_score){
            editor.putInt("Highest_score", score);
        }
        else{
            editor.putInt("Highest_score" , highest_score);
        }
        editor.apply();
        binding.textView3.setText(new StringBuilder().append("Highest Score: ").append(String.valueOf(highest_score)).toString());

        highestScore = highest_score;
    }

    public void Stop(View view) {
        Intent intent = new Intent(MainActivity.this ,MainActivity2.class);
        intent.putExtra("Score" , score);
        intent.putExtra("highest score" , highestScore);
        startActivity(intent);
    }

}