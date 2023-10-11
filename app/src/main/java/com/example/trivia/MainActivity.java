package com.example.trivia;

import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.trivia.Data.AnswerListAsyncResponse;
import com.example.trivia.Data.Repository;
import com.example.trivia.Model.Question;
import com.example.trivia.Model.Score;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.util.Prefs;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
  private int currentQuestionIndex = 0;
  List<Question> questionList;
  private int scoreCounter = 0;
  private Score score;
  Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
         binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        score = new Score();
        prefs = new Prefs(MainActivity.this);

        //Retrieve current question index
        currentQuestionIndex = prefs.getState();

        binding.txtScore.setText(String.format("Current Score: %d", score.getScore()));
        binding.txtHighestScore.setText(String.format("highest score:%d", prefs.getHighestScore()));
      questionList =  new Repository().getQuestions(questionArrayList -> {
          binding.txtQuestionHolder.setText(questionArrayList.get(currentQuestionIndex)
                  .getAnswer());

          questionCounter(questionArrayList);
      });



        binding.nextbtn.setOnClickListener(v -> {
            getNextQuestion();
        });

        binding.truebtn.setOnClickListener(v -> {
            checkAnswer(true);
        updatequestion();
        });

        binding.falsebtn.setOnClickListener(v -> {
            checkAnswer(false);
         updatequestion();
        });


    }

    private void getNextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updatequestion();
    }

    //check answer method
    private void checkAnswer(boolean check) {
        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMassageId;
        if (check == answer){
            snackMassageId = R.string.correct_answer;
             fadeAnimation();
             addPoint();

        } else {
            snackMassageId = R.string.incorrect_answer;
            shakeAnimation();
            deductPoint();
        }
        Snackbar.make(binding.cardView, snackMassageId, Snackbar.ANIMATION_MODE_SLIDE).show();
    }


    private void questionCounter(ArrayList<Question> questionArrayList) {
        binding.txtquestion.setText(String.format("Questions: %d/%d",
                currentQuestionIndex, questionArrayList.size()));
    }

    private void updatequestion() {
        String quest = questionList.get(currentQuestionIndex).getAnswer();
        binding.txtQuestionHolder.setText(quest);
        questionCounter((ArrayList<Question>) questionList);
    }

    private void fadeAnimation(){
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(300);
        alpha.setRepeatCount(1);
        alpha.setRepeatMode(Animation.REVERSE);
        binding.cardView.setAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.txtQuestionHolder.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.txtQuestionHolder.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.txtQuestionHolder.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.txtQuestionHolder.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

  private void addPoint(){
        scoreCounter += 10;
        score.setScore(scoreCounter);
      binding.txtScore.setText(String.valueOf(score.getScore()));
      binding.txtScore.setText(String.format("Current Score: %d", score.getScore()));
 }

 private void deductPoint(){

        if (scoreCounter > 0){
            scoreCounter -= 10;
            score.setScore(scoreCounter);
            binding.txtScore.setText(String.format("Current Score: %d", score.getScore()));
        } else {
            scoreCounter = 0;
            score.setScore(scoreCounter);
        }
 }

    @Override
    protected void onPause() {
        prefs.saveHighestScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }
}