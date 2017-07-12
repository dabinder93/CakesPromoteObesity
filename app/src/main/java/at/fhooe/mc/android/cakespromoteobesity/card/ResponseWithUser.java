package at.fhooe.mc.android.cakespromoteobesity.card;

import java.util.List;

/**
 * Created by Bastian on 03.06.2017.
 */
public class ResponseWithUser {
    private int userID;
    private Response response;

    //Firebase
    public ResponseWithUser() {
    }

    /**
     * default constructor
     * @param _resp response
     * @param _userID userID
     */
    public ResponseWithUser(Response _resp, int _userID) {
        response = _resp;
        userID = _userID;
    }

    /**
     * gets the response
     * @return response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * sets the response
     * @param response response
     */
    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * gets the userid
     * @return userid
     */
    public int getUserID() {
        return userID;
    }

    /**
     * sets the userid
     * @param userID userid
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
