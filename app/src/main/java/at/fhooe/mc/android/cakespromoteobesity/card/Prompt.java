package at.fhooe.mc.android.cakespromoteobesity.card;

/**
 * Created by Bastian on 28.04.2017.
 */

public class Prompt {
    private String text;
    private String id;
    private int pick;

    //Firebase
    public Prompt() {
        text = "_____";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPick() {
        return pick;
    }

    public void setPick(int pick) {
        this.pick = pick;
    }
}
