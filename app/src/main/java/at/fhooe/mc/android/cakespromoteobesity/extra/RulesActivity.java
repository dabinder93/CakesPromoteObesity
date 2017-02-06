package at.fhooe.mc.android.cakespromoteobesity.extra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.w3c.dom.Text;

import at.fhooe.mc.android.cakespromoteobesity.R;

public class RulesActivity extends AppCompatActivity {

    TextView rules;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        rules = (TextView) findViewById(R.id.tv_rules);
        rules.setMovementMethod(new ScrollingMovementMethod());

    }
}
