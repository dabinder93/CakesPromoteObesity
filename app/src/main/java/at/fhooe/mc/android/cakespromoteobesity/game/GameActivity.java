package at.fhooe.mc.android.cakespromoteobesity.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        //Get game passed from Intent
        Lobby game = (Lobby)bundle.getSerializable("LobbyObject");
        Log.i("LobbyOverview", game.getmName());
    }
}
