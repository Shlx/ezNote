package marvin.ezNote.note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Note object class, has some basic functionality */
public class Note {

    /** Member variables */
    private String title;
    private String content;
    private int id;
    private Date date;
    private List<String> tags;

    /** Constructor */
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.id = 0;
        this.date = new Date();
        this.tags = new ArrayList<>();
    }

    /** Check if a note contains a list of tags */
    public boolean isTaggedWith(List<String> tags) {
        return this.tags.containsAll(tags);
    }

    /** Get the formatted date of a note */
    public String getFormattedDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
        return dateFormat.format(date);
    }

    public String getTagString() {
        String tagString = "";
        boolean comma = false;
        for (String tag : tags) {
            tagString += (comma ? ", " : "") + tag;
            comma = true;
        }

        return tagString;
    }

    public int getLength() {
        return content.length();
    }

    @Override
    public String toString() {
        return "Note {" +
                "title = \"" + title + '"' +
                ", content [" + content.length() + "] = \"" + content + '"' +
                ", id = " + id +
                ", date = " + getFormattedDate() +
                ", tags = [" + getTagString() + "]" +
                '}';
    }

    public String shortString() {
        return "Note \"" + title + "\" (id " + id + ")";
    }

    public void print() {
        System.out.println(toString());
    }

    /////////////////////////
    // GETTERS AND SETTERS //
    /////////////////////////

    /** TITLE */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /** CONTENT */
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /** DATE */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /** ID */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** TAGS */
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}
