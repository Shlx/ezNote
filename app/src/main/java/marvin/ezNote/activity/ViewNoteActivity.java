package marvin.ezNote.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Date;

import marvin.ezNote.R;
import marvin.ezNote.note.Note;
import marvin.ezNote.note.NoteHandler;

/** Activity to view a note */
public class ViewNoteActivity extends AppCompatActivity {

    NoteHandler noteHandler;

    EditText editTextNoteTitle;
    EditText editTextNoteContent;

    Note currentNote;
    boolean newNote;
    boolean changed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.contentview_view_note);
        setTitle(R.string.untitled_note);

        noteHandler = new NoteHandler(this);

        editTextNoteTitle = (EditText) findViewById(R.id.editTextNoteTitle);
        editTextNoteContent = (EditText) findViewById(R.id.editTextNoteContent);

        int id = intent.getIntExtra(NoteListActivity.NOTE_ID, -1);
        if (id >= 0) {
            newNote = false;
            currentNote = noteHandler.getNote(id);
            editTextNoteTitle.setText(currentNote.getTitle());
            editTextNoteContent.setText(currentNote.getContent());
        } else {
            newNote = true;
        }

        addTextWatchers();

    }

    @Override
    public void onBackPressed() {
        if (changed) {
            new AlertDialog.Builder(this)
                    .setTitle("Discard changes?")
                    .setMessage("Are you sure you want to exit without saving?")
                    .setNegativeButton("Discard", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            ViewNoteActivity.super.onBackPressed();
                        }

                    }).setPositiveButton("Save note", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            saveNote();
                        }

                    }).create().show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuCheck:
                saveNote();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addTextWatchers() {

        editTextNoteTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changed = true;
                if (s.length() > 0) {
                    setTitle(s);
                } else {
                    setTitle(R.string.untitled_note);
                }
            }

        });

        editTextNoteContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changed = true;
            }

        });

    }

    public void saveNote() {
        String title = editTextNoteTitle.getText().toString();
        String content = editTextNoteContent.getText().toString();
        if (newNote) {
            Note note = new Note(title, content);
            noteHandler.saveNote(note);
            chooseTags();
        } else {
            currentNote.setTitle(title);
            currentNote.setContent(content);
            currentNote.setDate(new Date());
            noteHandler.saveNote(currentNote);
        }
        super.onBackPressed();
    }

    private void chooseTags() {

    }

}
