package at.fhooe.mc.android.cakespromoteobesity.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.lobbysettings.CreateLobby;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mJoinLobby, mCreateLobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCreateLobby = (Button) findViewById(R.id.btn_createLobby);
        mJoinLobby = (Button) findViewById(R.id.btn_joinLobby);


        mCreateLobby.setOnClickListener(this);
    }


    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            case R.id.btn_createLobby : {
                Intent i = new Intent(this, CreateLobby.class);
                startActivity(i);
            }break;
            case R.id.btn_joinLobby : {

            }break;
            default: {
                Toast.makeText(this,"Error on onClick",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
