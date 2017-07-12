package at.fhooe.mc.android.cakespromoteobesity.card;

import java.util.List;

/**
 * Created by Bastian on 04.06.2017.
 */

public class ResponseWithUserList {
    private int userID;
    private List<Response> responsesList;

    //Firebase
    public ResponseWithUserList() {
    }

    /**
     * default contstructor
     * @param _resp response
     * @param _userID userID
     */
    public ResponseWithUserList(List<Response> _resp, int _userID) {
        responsesList = _resp;
        userID = _userID;
    }

    /**
     * gets the list of responses
     * @return list of responses
     */
    public List<Response> getResponsesList() {
        return responsesList;
    }

    /**
     * sets the list of responses
     * @param responsesList new list of responses
     */
    public void setResponsesList(List<Response> responsesList) {
        this.responsesList = responsesList;
    }

    /**
     * gets the userID
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * sets the userID
     * @param userID new userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
