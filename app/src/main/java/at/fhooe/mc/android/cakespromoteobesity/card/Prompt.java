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

    /**
     * gets the id of a prompt
     * @return id of prompt
     */
    public String getId() {
        return id;
    }

    /**
     * sets the id of a prompt
     * @param id new id of promt
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * gets the text of a prompt
     * @return text of prompt
     */
    public String getText() {
        return text;
    }

    /**
     * set the text of a prompt
     * @param text text of prompt
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * gets the pick count of a prompt
     * @return pick of prompt
     */
    public int getPick() {
        return pick;
    }

    /**
     * sets the pick count of a prompt
     * @param pick new pick of prompt
     */
    public void setPick(int pick) {
        this.pick = pick;
    }

    /**
     * String representation of a prompt
     * @return string
     */
    @Override
    public String toString() {
        return text;
    }
}
