package co.edu.unal.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TicTacToeActivity extends AppCompatActivity {

    int selected = -1;
    private TicTacToeGame mGame;
    // Buttons making up the board
    private Button[] mBoardButtons;
    private TextView mHumanScoreTextView;
    private TextView mTiesScoreTextView;
    private TextView mAndroidScoreTextView;
    private TextView mInfoTextView;
    private boolean mGameOver;
    private int mHumanWins;
    private int mComputerWins;
    private int mTies;
    private SharedPreferences mPrefs;
    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences("ttt_prefs", MODE_PRIVATE);

        mHumanWins=mPrefs.getInt("mHumanWins",0);
        mComputerWins=mPrefs.getInt("mComputerWins",0);
        mTies=mPrefs.getInt("mTies",0);

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];

        mBoardButtons[0] = findViewById(R.id.one);
        mBoardButtons[1] = findViewById(R.id.two);
        mBoardButtons[2] = findViewById(R.id.three);
        mBoardButtons[3] = findViewById(R.id.four);
        mBoardButtons[4] = findViewById(R.id.five);
        mBoardButtons[5] = findViewById(R.id.six);
        mBoardButtons[6] = findViewById(R.id.seven);
        mBoardButtons[7] = findViewById(R.id.eight);
        mBoardButtons[8] = findViewById(R.id.nine);
        mInfoTextView = findViewById(R.id.information);

        mHumanScoreTextView = findViewById(R.id.humanW);
        mTiesScoreTextView = findViewById(R.id.tiesW);
        mAndroidScoreTextView = findViewById(R.id.androidW);

// Restore the scores from the persistent preference data source

        /*mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean("sound", true);
        String difficultyLevel = mPrefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_harder));
        if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        else
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);*/

        mGame = new TicTacToeGame();
        selected = mGame.getDifficultyLevel().ordinal();

        startNewGame();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)
                };
                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                builder.setSingleChoiceItems(levels, selected, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss(); // Close dialog
                        // TODO: Set the diff level of mGame based on which item was selected.
                        TicTacToeGame.DifficultyLevel newDiff = TicTacToeGame.DifficultyLevel.valueOf(levels[item].toString());
                        mGame.setDifficultyLevel(newDiff);
                        // Display the selected difficulty level
                        Toast.makeText(getApplicationContext(), levels[item], Toast.LENGTH_SHORT).show();
                        startNewGame();
                    }
                });
                dialog = builder.create();
                break;
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                TicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
            //case R.id.settings:
            //    startActivityForResult(new Intent(this, Settings.class), 0);
                return true;
            case R.id.reset:
                mHumanWins=0;
                mComputerWins=0;
                mTies=0;
                displayScores();
                startNewGame();
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CANCELED) {
// Apply potentially new settings
            /*mSoundOn = mPrefs.getBoolean("sound", true);
            String difficultyLevel = mPrefs.getString("difficulty_level", getResources().getString(R.string.difficulty_harder));
            if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            else
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);*/
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharArray("board", mGame.getBoardState());
        outState.putBoolean("mGameOver", mGameOver);
        outState.putCharSequence("info", mInfoTextView.getText());
        //outState.putChar("mGoFirst", mGoFirst);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        char[] mb = savedInstanceState.getCharArray("board");
        mGame.setBoardState(mb);
        mGameOver = savedInstanceState.getBoolean("mGameOver");
        mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
        //mGoFirst=savedInstanceState.getChar("mGoFirst");
        assert mb != null;
        for (int i = 0; i < mb.length; i++) {
            if (mb[i] != TicTacToeGame.OPEN_SPOT) {
                setMove(mb[i], i);
            }
        }
        displayScores();
    }

    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences.Editor ed=mPrefs.edit();
        ed.putInt("mHumanWins", mHumanWins);
        ed.putInt("mComputerWins", mComputerWins);
        ed.putInt("mTies", mTies);
        ed.apply();
    }

    // Set up the game board.
    private void startNewGame() {

        mGame.clearBoard();
        mGameOver = false;

        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText(" ");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

        // Human goes first
        mInfoTextView.setText(R.string.first_human);
    }


    // Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;

        private ButtonClickListener(int location) {
            this.location = location;
        }

        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled()) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if (winner == 0)
                    mInfoTextView.setText(R.string.turn_human);
                else if (winner == 1) {
                    mInfoTextView.setText(R.string.result_tie);
                    mTies++;
                    mGameOver = true;
                } else if (winner == 2) {
                    mInfoTextView.setText(R.string.result_human_wins);
                    mHumanWins++;
                    //String defaultMessage = getResources().getString(R.string.result_human_wins);
                    //mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage));
                    mGameOver = true;
                } else if (winner == 3) {
                    mInfoTextView.setText(R.string.result_computer_wins);
                    mComputerWins++;
                    mGameOver = true;
                }
                displayScores();
            }
        }
    }

    private void setMove(char player, int location) {

        if (mGameOver) {
            return;
        }
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }

    private void displayScores(){
        mTiesScoreTextView.setText(String.valueOf(mTies));
        mHumanScoreTextView.setText(String.valueOf(mHumanWins));
        mAndroidScoreTextView.setText(String.valueOf(mComputerWins));
    }
}
