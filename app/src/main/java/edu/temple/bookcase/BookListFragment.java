package edu.temple.bookcase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookListFragment extends Fragment {

    JSONArray jsonArray;
    BookAdapter bookAdapter;
    ListView bookListView;

    public BookListFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_list, container, false);
        bookListView = v.findViewById(R.id.bookListView);
        bookAdapter = new BookAdapter(getContext(), jsonArray);
        bookAdapter.setJsonArray(jsonArray);
        bookListView.setAdapter(bookAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = null;
                try {
                    book = new Book((JSONObject) jsonArray.get(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((getBook) getActivity()).bookSelected(book);
            }
        });


        // Inflate the layout for this fragment
        return v;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public interface getBook {
        void bookSelected(Book book);
    }
}
