package at.fhooe.mc.android.cakespromoteobesity.game;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by Bastian on 30.01.2017.
 */

public interface IOnGetDataListener {
    public void onStartDB();
    public void onSuccessDB(DataSnapshot data);
    public void onFailedDB(DatabaseError databaseError);
}
