package edu.temple.bookcase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class BookDetailsFragmentLandscape extends Fragment {


    TextView bookTitleLandscape;
    ImageView bookCoverLandscape;
    TextView bookAuthorLandscape;
    TextView bookPublishDateLandscape;

    public BookDetailsFragmentLandscape() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_details_fragment_landscape, container, false);
        bookTitleLandscape = v.findViewById(R.id.bookTitleLandscape);
        bookCoverLandscape = v.findViewById(R.id.bookCoverLandscape);
        bookAuthorLandscape = v.findViewById(R.id.bookAuthorLandscape);
        bookPublishDateLandscape = v.findViewById(R.id.bookPublishDateLandscape);
        return v;
    }

    public void displayBookName(Book book) {
        bookTitleLandscape.setText(book.getTitle());
        Picasso.get().load(book.getCoverURL()).into(bookCoverLandscape);
        bookAuthorLandscape.setText(book.getAuthor());
        bookPublishDateLandscape.setText(String.valueOf(book.getPublished()));
    }
}
