package at.fhooe.mc.android.cakespromoteobesity.database;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Bastian on 19.01.2017.
 */

public class Database {
    private FirebaseDatabase database;

    public Database() {
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

}
