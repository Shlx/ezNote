package marvin.ezNote.note;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/** Handles notes */
public class NoteHandler {

    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(NotePrefs.class.getName());

    private NotePrefs notePrefs;

    public NoteHandler(Context ctx) {
        notePrefs = new NotePrefs(ctx);
    }

    public void delete() {
        notePrefs.delete();
    }

    ///////////
    // NOTES //
    ///////////

    /** Save a note */
    public void saveNote(Note note) {
        notePrefs.addNote(note);

        for (String tag : note.getTags()) {
            notePrefs.increaseTagCount(tag);
        }

        LOGGER.info("Saved " + note.shortString());
    }

    /** Get a note by it's ID */
    public Note getNote(int id) {
        LOGGER.info("getNote(" + id + ")");
        return notePrefs.loadNote(id);
    }

    /** Delete a note by it's ID */
    public void deleteNote(int id) {
        Note note = getNote(id);
        String noteString = note.shortString();

        if (note != null) {
            for (String tag : note.getTags()) {
                notePrefs.decreaseTagCount(tag);
            }
            notePrefs.deleteNote(id);
            LOGGER.info("Deleted " + noteString);
        } else {
            LOGGER.severe("Note with ID " + id + " does not exist");
        }

    }

    /** Get all notes */
    public List<Note> getAllNotes() {
        LOGGER.info("getAllNotes()");
        List<Note> notes = new ArrayList<>(notePrefs.loadAllNotes().values());
        Map<Date, Note> noteMap = new TreeMap<>();
        for (Note note : notes) {
            noteMap.put(note.getDate(), note);
        }
        notes.clear();
        for (Note note : noteMap.values()) {
            notes.add(note);
        }
        Collections.reverse(notes);
        return notes;
    }

    /** Get the total number of notes */
    public int getNoteCount() {
        LOGGER.info("getNoteCount()");
        return notePrefs.getNoteCount();
    }

    /////////
    // IDS //
    /////////

    public List<Integer> getActiveIds() {
        LOGGER.info("getActiveIds()");
        return notePrefs.getActiveIds();
    }

    //////////
    // TAGS //
    //////////

    public List<Note> getNotesTaggedWith(List<Note> notes, List<String> tags) {
        List<Note> taggedNotes = new ArrayList<>();

        for (Note note : notes) {
            if (note.isTaggedWith(tags)) {
                taggedNotes.add(note);
            }
        }

        return taggedNotes;
    }

    /** Get all tag counts */
    public HashMap<String, Integer> getAllTagCounts() {
        LOGGER.info("getAllTagCounts()");
        return notePrefs.loadAllTagCounts();
    }

    /** Add a single tag to a note (and update the count) */
    public void addTag(Note note, String tag) {
        if (!note.getTags().contains(tag)) {
            note.getTags().add(tag);
        } else {
            LOGGER.warning(note.shortString() + " is already tagged with " + tag);
        }

        notePrefs.increaseTagCount(tag);

        LOGGER.info("Added tag " + tag + " to " + note.shortString());
    }

    /** Add multiple tags to a note */
    public void addTags(Note note, List<String> tags) {
        for (String tag : tags) {
            addTag(note, tag);
        }
    }

    /** Remove a single tag from a note (and update the count) */
    public void removeTag(Note note, String tag) {
        if (!note.getTags().remove(tag)) {
            LOGGER.warning(note.shortString() + " is not tagged with " + tag);
        }

        notePrefs.decreaseTagCount(tag);

        LOGGER.info("Removed tag " + tag + " from " + note.shortString());
    }

    /** Remove multiple tags from a note */
    public void removeTags(Note note, List<String> tags) {
        for (String tag : tags) {
            removeTag(note, tag);
        }
    }

}
