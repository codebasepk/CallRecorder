package com.byteshatf.callrecorder.contactpicker;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.byteshatf.callrecorder.AppGlobals;
import com.byteshatf.callrecorder.Helpers;
import com.byteshatf.callrecorder.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsPicker extends AppCompatActivity {

    private ArrayAdapter<String> listAdapter;
    private ListView mListView;
    private List<String> mNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006666")));
        setContentView(R.layout.contacts_picker_activity);
        mNames = Helpers.getAllContactNames();
        List<String> numbers = Helpers.getAllContactNumbers();
        ArrayList<String> output = getFormattedListEntries(mNames, numbers);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice,
                output);
        mListView = (ListView) findViewById(R.id.list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setAdapter(listAdapter);

        String noteTitle;
        if (getIntent().getExtras() != null) {
            String checkedContacts = getIntent().getExtras().getString("pre_checked");
            noteTitle = getIntent().getExtras().getString("note");
            boolean revisitingTemporary = getIntent().getExtras().getBoolean("temporary_select");
            if (checkedContacts != null) {
                String[] pre_checked = checkedContacts.split(",");
                for (int i = 0; i < mListView.getCount(); i++) {
                    for (String contact : pre_checked) {
                        if (PhoneNumberUtils.compare(numbers.get(i), contact)) {
                            mListView.setItemChecked(i, true);
                        }
                    }
                }
            } else if (revisitingTemporary) {
                // Just don't do anything
            } else {
                // checkContacts here
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                for (int i = 0; i < mListView.getCount(); i++) {
                    if (mNames.get(i).toLowerCase().startsWith(newText.toLowerCase())) {
                        mListView.setSelection(i);
                        break;
                    } else if (mNames.get(i).toLowerCase().contains(newText.toLowerCase())) {
                        mListView.setSelection(i);
                        break;
                    }
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_all:
                for (int i = 0; i < mListView.getCount(); i++) {
                    mListView.setItemChecked(i, true);
                }
                return true;
            case R.id.unselect_all:
                for (int i = 0; i < mListView.getCount(); i++) {
                    mListView.setItemChecked(i, false);
                }
                return true;
            case R.id.action_done:
                SparseBooleanArray array = mListView.getCheckedItemPositions();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < mListView.getCount(); i++) {
                    if (array.get(i)) {
                        String out = (String) mListView.getAdapter().getItem(i);
                        String lines[] = out.split("\\r?\\n");
                        stringBuilder.append(lines[1]).append(",");
                    }
                }
                Intent intent = new Intent();
                if (stringBuilder.length() != 0) {
                    intent.putExtra("selected_contacts", stringBuilder.toString());
                }
                intent.putExtra("temporary", true);
                setResult(AppGlobals.RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<String> getFormattedListEntries(List<String> names, List<String> numbers) {
        ArrayList<String> entries = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            String formattedName = Html.fromHtml(names.get(i)).toString();
            String formattedNumber = Html.fromHtml(numbers.get(i)).toString();
            String result = formattedName + "\n" + formattedNumber;
            entries.add(result);
        }
        return entries;
    }
}