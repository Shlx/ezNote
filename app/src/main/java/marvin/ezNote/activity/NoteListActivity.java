package marvin.ezNote.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import marvin.ezNote.note.NoteAdapter;
import marvin.ezNote.R;
import marvin.ezNote.note.Note;
import marvin.ezNote.note.NoteHandler;

/** Shows a list of notes that can be filtered by tags */
public class NoteListActivity extends AppCompatActivity {

    public static final String NOTE_ID = "note_id";

    NoteHandler noteHandler;

    EditText editTextSearchTags;
    ListView listViewNotes;
    NoteAdapter noteAdapter;

    public List<Note> allNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentview_note_list);

        noteHandler = new NoteHandler(this);
        //noteHandler.delete(); // Delete EVERYTHING

        editTextSearchTags = (EditText) findViewById(R.id.editTextSearchTags);
        addTagTextWatcher();
        listViewNotes = (ListView) findViewById(R.id.listViewNotes);

    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        showAllNotes();
        editTextSearchTags.setText("");
    }

    private void showNotes(List<Note> notes) {
        noteAdapter = new NoteAdapter(this, notes);
        listViewNotes.setAdapter(noteAdapter);
        listViewNotes.setOnItemClickListener(noteAdapter);
        listViewNotes.setOnItemLongClickListener(noteAdapter);
    }

    private void showAllNotes() {
        allNotes = noteHandler.getAllNotes();
        showNotes(allNotes);
    }

    private void showTaggedNotes(String query) {
        List<String> tags = Arrays.asList(query.split(" "));
        showNotes(noteHandler.getNotesTaggedWith(allNotes, tags));
    }

    private void addTagTextWatcher() {

        editTextSearchTags.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    showTaggedNotes(s.toString());
                } else {
                    showAllNotes();
                }
            }

        });

    }

    public void addNewNote(View view) {
        Intent intent = new Intent(this, ViewNoteActivity.class);
        startActivity(intent);
    }

    public void deleteNote(Note note) {
        noteHandler.deleteNote(note.getId());
        noteAdapter.remove(note);
        noteAdapter.notifyDataSetChanged();
    }

    public void openNote(int id) {
        Intent intent = new Intent(this, ViewNoteActivity.class);
        intent.putExtra(NOTE_ID, id);
        startActivity(intent);
    }

}
