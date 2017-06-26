package at.fhooe.mc.android.cakespromoteobesity.customize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.lobby.CreateLobby;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

public class CustomizeActivity extends AppCompatActivity
{
    private User mUser;
    private List<Deck> mDecks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        mUser = MainActivity.mUser;
        Button b = (Button)findViewById(R.id.btn_custom_explorer);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomizeActivity.this, DeckExplorer.class);
                //If the user hasn't made an User-Object yet, it gets created and pushed to Firebase
                startActivity(i);
            }
        });


    }
}
