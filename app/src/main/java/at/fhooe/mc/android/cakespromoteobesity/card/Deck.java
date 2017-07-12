package at.fhooe.mc.android.cakespromoteobesity.card;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Bastian on 28.04.2017.
 */
public class Deck implements Comparable<Deck> {

    private String Name;
    private String Id;
    private String Icon;
    private List<Prompt> Prompts;
    private List<String> Responses;
    private int Sort;

    //Firebase
    public Deck() {}

    /**
     * gets the icon resource of a deck
     * @return icon res
     */
    public String getIcon() {
        return Icon;
    }

    /**
     * sets the icon res of a deck
     * @param icon new icon res
     */
    public void setIcon(String icon) {
        this.Icon = icon;
    }

    /**
     * gets the name of a deck
     * @return name of deck
     */
    public String getName() {
        return Name;
    }

    /**
     * sets the name of a deck
     * @param name new name of deck
     */
    public void setName(String name) {
        this.Name = name;
    }

    /**
     * gets the id of the deck
     * @return id of deck
     */
    public String getId() {
        return Id;
    }

    /**
     * sets the id of the deck
     * @param id new id
     */
    public void setId(String id) {
        this.Id = id;
    }

    /**
     * gets the list of prompts
     * @return list of prompts
     */
    public List<Prompt> getPrompts() {
        return Prompts;
    }

    /**
     * sets the list of prompts
     * @param prompts new list of prompts
     */
    public void setPrompts(List<Prompt> prompts) {
        this.Prompts = prompts;
    }

    /**
     * gets the list of responses
     * @return list of responses
     */
    public List<String> getResponses() {
        return Responses;
    }

    /**
     * sets he list of responses
     * @param responses new list of responses
     */
    public void setResponses(List<String> responses) {
        this.Responses = responses;
    }

    /**
     * gets the sort int
     * @return sort int
     */
    public int getSort() {
        return Sort;
    }

    /**
     * sets the sort int
     * @param sort
     */
    public void setSort(int sort) {
        Sort = sort;
    }

    /**
     * String representation of deck
     * @return deck
     */
    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        /*b.append(getName() + "\n" + getId() + "\n");
        for (Prompt p : getPrompts()) {
            b.append(p.getText());
        }
        for (String s : getResponses()) {
            b.append(s);
        }
        */
        b.append(getName() + "\n" + "Prompts/Responses: " + getPrompts().size() + "/" + getResponses().size());
        return b.toString();
    }

    /**
     * sorting will be done with the sort-integer
     * @param deck other deck
     * @return compare int
     */
    @Override
    public int compareTo(@NonNull Deck deck) {
        return getSort() - deck.getSort();
    }
}
