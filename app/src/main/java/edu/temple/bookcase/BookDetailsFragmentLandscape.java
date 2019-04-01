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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BookDetailsFragmentLandscape() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fr   agment
        View v = inflater.inflate(R.layout.fragment_book_details_fragment_landscape, container, false);
        bookTitleLandscape = v.findViewById(R.id.bookTitleLandscape);
        bookCoverLandscape = v.findViewById(R.id.bookCoverLandscape);
        bookAuthorLandscape = v.findViewById(R.id.bookAuthorLandscape);
        bookPublishDateLandscape = v.findViewById(R.id.bookPublishDateLandscape);
        return v;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void displayBookName(Book book) {
        bookTitleLandscape.setText(book.getTitle());
        Picasso.get().load(book.getCoverURL()).into(bookCoverLandscape);
        bookAuthorLandscape.setText(book.getAuthor());
        bookPublishDateLandscape.setText(book.getPublished());
    }
}
