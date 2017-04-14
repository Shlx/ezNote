package marvin.ezNote.note;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Class to manage writing to and reading from preferences */
class NotePrefs {

    /** String constants */
    private static final String PREFS_NAME  = "marvin.notes.note.NotePrefs";
    private static final String CURRENT_ID  = "current_id";
    private static final String ALL_NOTES   = "notes";
    private static final String ALL_TAGS    = "tags";

    /** References to type tokens for easier JSON deserialization */
    private static final Type stringIntegerHashMapType = new TypeToken<HashMap<String, Integer>>(){}.getType();
    private static final Type integerNoteHashMapType = new TypeToken<HashMap<Integer, Note>>(){}.getType();

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();

    /** Constructor takes a Context argument */
    NotePrefs(Context ctx) {
        if (settings == null) {
            settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
    }

    public void delete() {
        editor = settings.edit();
        if (!editor.clear().commit()) {
            throw new RuntimeException("Could not clear the editor");
        }
    }

    ///////////
    // NOTES //
    ///////////

    /** Load all notes */
    HashMap<Integer, Note> loadAllNotes() {
        String hashMapJson = settings.getString(ALL_NOTES, gson.toJson(new HashMap<>()));
        return gson.fromJson(hashMapJson, integerNoteHashMapType);
    }

    /** Updates the ID, Note HashMap */
    private void updateNotes(HashMap<Integer, Note> idToNote) {
        editor = settings.edit();
        if (!editor.putString(ALL_NOTES, gson.toJson(idToNote)).commit()) {
            throw new RuntimeException("Could not write notes to preferences");
        }
    }

    /** Add a new note */
    void addNote(Note note) {
        if (note.getId() == 0) {
            note.setId(nextId());
        }

        // Add the new note to the HashMap and update
        HashMap<Integer, Note> idToNote = loadAllNotes();
        idToNote.put(note.getId(), note);
        updateNotes(idToNote);
    }

    /** Load a note by it's ID */
    Note loadNote(int id) {
        return loadAllNotes().get(id);
    }

    /** Delete a note by it's ID */
    void deleteNote(int id) {
        HashMap<Integer, Note> idToNote = loadAllNotes();
        idToNote.remove(id);
        updateNotes(idToNote);
    }

    /** Get the total number of notes */
    int getNoteCount() {
        return loadAllNotes().size();
    }

    /////////
    // IDS //
    /////////

    /** Get the current ID */
    private int getCurrentId() {
        String countJson = settings.getString(CURRENT_ID, gson.toJson(0));
        return gson.fromJson(countJson, Integer.class);
    }

    /** Increase the current ID and return it */
    private int nextId() {
        int nextId = getCurrentId() + 1;
        editor = settings.edit();
        if (!editor.putString(CURRENT_ID, gson.toJson(nextId)).commit()) {
            throw new RuntimeException("Could not write current ID to preferences");
        }
        return nextId;
    }

    /** Get a list of active IDs */
    List<Integer> getActiveIds() {
        return new ArrayList<>(loadAllNotes().keySet());
    }

    //////////
    // TAGS //
    //////////

    /** Get the HashMap with all tags and their counts */
    HashMap<String, Integer> loadAllTagCounts() {
        String hashMapJson = settings.getString(ALL_TAGS, gson.toJson(new HashMap<>()));
        return gson.fromJson(hashMapJson, stringIntegerHashMapType);
    }

    /** Save all tags */
    private void updateTagCounts(HashMap<String, Integer> tagToCount) {
        editor = settings.edit();
        if (!editor.putString(ALL_TAGS, gson.toJson(tagToCount)).commit()) {
            throw new RuntimeException("Could not write tag counts");
        }
    }

    /** Increase the count for a tag by 1 */
    void increaseTagCount(String tag) {
        HashMap<String, Integer> tagToCount = loadAllTagCounts();
        changeTagCount(tagToCount, tag, 1);
        updateTagCounts(tagToCount);
    }

    /** Decrease the count for a tag by 1 */
    void decreaseTagCount(String tag) {
        HashMap<String, Integer> tagToCount = loadAllTagCounts();
        changeTagCount(tagToCount, tag, -1);
        updateTagCounts(tagToCount);
    }

    /** Change the count of a single tag, delete the tag if it doesn't occur anymore */
    private void changeTagCount(HashMap<String, Integer> tagToCount, String tag, int change) {
        int tagCount = (tagToCount.get(tag) == null ? 0 : tagToCount.get(tag));
        int newCount = tagCount + change;

        if (newCount > 0) {
            tagToCount.put(tag, newCount);
        } else {
            tagToCount.remove(tag);
        }

    }

}
