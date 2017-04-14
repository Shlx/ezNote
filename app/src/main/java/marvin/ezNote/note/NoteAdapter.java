package marvin.ezNote.note;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import marvin.ezNote.R;
import marvin.ezNote.activity.NoteListActivity;
import marvin.ezNote.ezNote;

public class NoteAdapter extends BaseAdapter implements AdapterView.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private NoteListActivity activity;
    private List<Note> notes;
    private HashMap<View, Integer> viewToPosition = new HashMap<>();
    private HashMap<Integer, Boolean> isLongClicked = new HashMap<>();
    private static LayoutInflater inflater = null;

    public NoteAdapter(NoteListActivity activity, List<Note> notes) {
        this.activity = activity;
        this.notes = notes;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return notes.size();
    }

    public Note getItem(int position) {
        return notes.get(position - 1);
    }

    public long getItemId(int position) {
        return position;
    }

    public void remove(Note note) {
        notes.remove(note);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_note, null);
        }

        TextView title = (TextView) view.findViewById(R.id.listItemTitle);
        TextView tags = (TextView) view.findViewById(R.id.listItemTags);
        TextView date = (TextView) view.findViewById((R.id.listItemDate));
        AppCompatImageView deleteButton = (AppCompatImageView) view.findViewById(R.id.listItemDeleteButton);

        Note note = notes.get(position);

        // Setting the title
        String titleString = note.getTitle();
        if (titleString == "") {
            titleString = ezNote.getContext().getText(R.string.untitled_note).toString();
            title.setTypeface(null, Typeface.ITALIC);
        }

        title.setText(titleString);

        // Setting the tags
        String tagString = note.getTagString();
        if (tagString == "") {
            tagString = ezNote.getContext().getText(R.string.no_tags).toString();
            tags.setTypeface(null, Typeface.ITALIC);
            tags.setTextColor(Color.LTGRAY);
        }

        tags.setText(tagString);

        // Setting the date
        String dateString = note.getFormattedDate();
        date.setTextColor(Color.LTGRAY);

        date.setText(dateString);

        viewToPosition.put(deleteButton, position);
        isLongClicked.put(position, false);

        return view;
    }

    @Override
    public void onClick(View view) {
        int position = viewToPosition.get(view);
        activity.deleteNote(notes.get(position));
        //disableDeleteButton(view);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        activity.openNote(activity.allNotes.get(position).getId());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
        if (!isLongClicked.get(position)) {
            isLongClicked.put(position, true);
            enableDeleteButton(view);
            Handler handler = new Handler();

            // Hide the button after five seconds
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    disableDeleteButton(view);
                    isLongClicked.put(position, false);
                }
            }, 3000);
        }
        return true;
    }

    private void enableDeleteButton(View view) {
        AppCompatImageView deleteButton = (AppCompatImageView) view.findViewById(R.id.listItemDeleteButton);
        deleteButton.setOnClickListener(this);
        AlphaAnimation al = new AlphaAnimation(0.0f, 1.0f);
        al.setDuration(500);
        deleteButton.startAnimation(al);
        deleteButton.setVisibility(View.VISIBLE);

        TextView dateTextView = (TextView) view.findViewById(R.id.listItemDate);
        if (dateTextView != null) {
            dateTextView.setVisibility(View.GONE);
        }
    }

    private void disableDeleteButton(View view) {
        AppCompatImageView deleteButton = (AppCompatImageView) view.findViewById(R.id.listItemDeleteButton);
        deleteButton.setOnLongClickListener(null);
        AlphaAnimation al = new AlphaAnimation(1.0f, 0.0f);
        al.setDuration(200);
        deleteButton.startAnimation(al);
        deleteButton.setVisibility(View.GONE);

        TextView dateTextView = (TextView) view.findViewById(R.id.listItemDate);
            dateTextView.setVisibility(View.VISIBLE);

    }

}