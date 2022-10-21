package pl.edu.pb.wi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_CURRENT_INDEX = "currentIndex";
    public static final String KEY_EXTRA_ANSWER = "pl.edu.wi.quiz.correctAnswer";
    private static final int REQUEST_CODE_PROMPT = 0;

    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button podpowiedzButton;
    private TextView questionTextView;
    private int currentIndex = 0;
    private static boolean odpowiedz_pokazana;

    private Question[] questions = new Question[] {
            new Question(R.string.q_1, true),
            new Question(R.string.q_2, false),
            new Question(R.string.q_3, false),
            new Question(R.string.q_4, false),
            new Question(R.string.q_5,true)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate - Stworzono aplikacje");
        setContentView((R.layout.activity_main));

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        podpowiedzButton = findViewById(R.id.podpowiedz_button);
        questionTextView = findViewById(R.id.question_text_view);

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(true);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);
            }
        });

        podpowiedzButton.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, Podpoweidz.class);
            boolean correctAnswer = questions[currentIndex].isTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            startActivityForResult(intent,REQUEST_CODE_PROMPT);

        });

        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                currentIndex=(currentIndex+1)%questions.length;
                odpowiedz_pokazana = false;
                setNextQuestion();
            }
        });
        setNextQuestion();
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){return;}
        if(requestCode==REQUEST_CODE_PROMPT){
            if(data==null){return;}
            odpowiedz_pokazana=data.getBooleanExtra(Podpoweidz.KEY_EXTRA_ANSWER_SHOWN,false);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d("onCreate", "Wywoałana została metoda cyklu żytcia onCreate: ");
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null){
            currentIndex=savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }
    }

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resutMessageId = 0;
        if (odpowiedz_pokazana) {
            resutMessageId = R.string.odpowiedz_byla_pokazana;
        } else {
            if(userAnswer == correctAnswer){
                resutMessageId = R.string.correct_answer;
            } else{
                resutMessageId = R.string.incorrect_answer;
            }
        }
        Toast.makeText(this, resutMessageId, Toast.LENGTH_SHORT).show();
    }

    private void  setNextQuestion(){
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity","onStart - Aplikacja uruchomiona");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity","onResume - Aplikacja wznowiona");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause - Aplikacja zapauzowana");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop - Aplikacja zatrzymana");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy - Aplikacja zniszczona");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MainActivity", "onSaveInstanceState - Zapisano stan");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }
}