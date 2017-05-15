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

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        this.Icon = icon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public List<Prompt> getPrompts() {
        return Prompts;
    }

    public void setPrompts(List<Prompt> prompts) {
        this.Prompts = prompts;
    }

    public List<String> getResponses() {
        return Responses;
    }

    public void setResponses(List<String> responses) {
        this.Responses = responses;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }


    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(getName() + "\n" + getId() + "\n");
        for (Prompt p : getPrompts()) {
            b.append(p.getText());
        }
        for (String s : getResponses()) {
            b.append(s);
        }
        return b.toString();
    }

    @Override
    public int compareTo(@NonNull Deck deck) {
        return getSort() - deck.getSort();
    }
}
