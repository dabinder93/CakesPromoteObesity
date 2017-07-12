package at.fhooe.mc.android.cakespromoteobesity.card;

import java.io.Serializable;

/**
 * Created by Bastian on 28.04.2017.
 */

public class Response implements Serializable {
    String text;
    String id;

    //Firebase
    public Response(){
    }

    /**
     * default constructor
     * @param _text text
     * @param _id id
     */
    public Response(String _text, String _id) {
        text = _text;
        id = _id;
    }

    /**
     * gets the text of a response
     * @return text of response
     */
    public String getText() {
        return text;
    }

    /**
     * sets the text of a response
     * @param text new text of response
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * gets the id of a response
     * @return id of response
     */
    public String getId() {
        return id;
    }

    /**
     * sets the id of a response
     * @param id new id of response
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * string representation
     * @return string
     */
    @Override
    public String toString() {
        return text;
    }
}
