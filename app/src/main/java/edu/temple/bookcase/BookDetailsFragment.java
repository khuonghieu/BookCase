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


public class BookDetailsFragment extends Fragment {

    //Name of book
    private Book book;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BookDetailsFragment() {
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_book_details, container, false);

        TextView bookTitle = v.findViewById(R.id.bookTitle);
        ImageView bookCover = v.findViewById(R.id.bookCover);
        TextView bookAuthor = v.findViewById(R.id.bookAuthor);
        TextView bookPublishDate = v.findViewById(R.id.bookPublishDate);
        bookTitle.setText(book.getTitle());
        Picasso.get().load(book.getCoverURL()).into(bookCover);
        bookAuthor.setText(book.getAuthor());
        bookPublishDate.setText(String.valueOf(book.getPublished()));

        return v;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //Set book name
    public void setBook(Book book) {
        this.book = book;
    }

    //Get book name
    public Book getBook() {
        return this.book;
    }


}
