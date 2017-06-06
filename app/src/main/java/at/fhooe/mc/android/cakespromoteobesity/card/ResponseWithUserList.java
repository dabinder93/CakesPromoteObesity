package at.fhooe.mc.android.cakespromoteobesity.card;

import java.util.List;

/**
 * Created by Bastian on 04.06.2017.
 */

public class ResponseWithUserList {
    private int userID;
    private List<Response> responsesList;

    //Firebase - if needed
    public ResponseWithUserList() {
    }

    public ResponseWithUserList(List<Response> _resp, int _userID) {
        responsesList = _resp;
        userID = _userID;
    }

    public List<Response> getResponsesList() {
        return responsesList;
    }

    public void setResponsesList(List<Response> responsesList) {
        this.responsesList = responsesList;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
