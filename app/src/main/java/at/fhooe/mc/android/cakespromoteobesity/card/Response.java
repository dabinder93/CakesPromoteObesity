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

    public Response(String _text, String _id) {
        text = _text;
        id = _id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
