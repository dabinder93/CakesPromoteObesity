package at.fhooe.mc.android.cakespromoteobesity.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.UserGame;

public class ScoreboardActvity extends AppCompatActivity {
    private List<UserGame> mUserGameList;
    ListView mScoreboard;
    ArrayAdapter<UserGame> mScoreboardAdapter;
    Button mReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        //Get game passed from Intent
        mUserGameList = (List<UserGame>) bundle.getSerializable("Scoreboard");
        Collections.sort(mUserGameList, new Comparator<UserGame>() {
            @Override
            public int compare(UserGame user1, UserGame user2) {
                return Integer.valueOf(user2.getmPoints()).compareTo(user1.getmPoints());
            }
        });

        mScoreboard = (ListView) findViewById(R.id.listView_scoreboard);
        mScoreboardAdapter = new ArrayAdapter<>(ScoreboardActvity.this,android.R.layout.simple_list_item_1,mUserGameList);
        mScoreboard.setAdapter(mScoreboardAdapter);

        mReturn = (Button)findViewById(R.id.btn_scoreboard_return);
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (_view.getId() == R.id.btn_scoreboard_return) {
                    Intent i = new Intent(ScoreboardActvity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        });


    }
}
