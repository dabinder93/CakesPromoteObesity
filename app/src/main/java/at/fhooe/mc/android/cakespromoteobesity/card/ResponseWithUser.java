package at.fhooe.mc.android.cakespromoteobesity.card;

import java.util.List;

/**
 * Created by Bastian on 03.06.2017.
 */
public class ResponseWithUser {
    private int userID;
    private Response response;

    //Firebase - if needed
    public ResponseWithUser() {
    }

    public ResponseWithUser(Response _resp, int _userID) {
        response = _resp;
        userID = _userID;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
