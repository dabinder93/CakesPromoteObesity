package at.fhooe.mc.android.cakespromoteobesity.main;

import android.app.Application;

import com.firebase.client.Firebase;

public class CakesPromotesObesity extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
