package edu.temple.bookcase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment bdf = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("book", book);
        bdf.setArguments(bundle);

        return bdf;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.book = (Book) getArguments().getParcelable("book");
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

        if (book != null) {

            bookTitle.setText(book.getTitle());
            Picasso.get().load(book.getCoverURL()).into(bookCover);
            bookAuthor.setText(book.getAuthor());
            bookPublishDate.setText(String.valueOf(book.getPublished()));
        }
        return v;
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
