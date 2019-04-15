package edu.temple.bookcase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.temple.audiobookplayer.AudiobookService;


public class BookDetailsFragment extends Fragment {

    //Name of book
    private Book book;

    AudiobookService audiobookService;
    boolean audioBound = false;

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
        final SeekBar progressBar = v.findViewById(R.id.progressBar);
        Button pauseButton = v.findViewById(R.id.pauseButton);
        final Button playButton = v.findViewById(R.id.playButton);
        Button stopButton = v.findViewById(R.id.stopButton);

        progressBar.setMax(book.getDuration());
        //((audioControl)getActivity()).setProgress();

        if (book != null) {
            bookTitle.setText(book.getTitle());
            bookTitle.setTextSize(20);
            Picasso.get().load(book.getCoverURL()).into(bookCover);
            bookAuthor.setText(book.getAuthor());
            bookPublishDate.setText(String.valueOf(book.getPublished()));
        }

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((audioControl) getActivity()).playAudio(book.getId());
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((audioControl) getActivity()).pauseAudio();

            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((audioControl) getActivity()).stopAudio();
            }
        });
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    ((audioControl) getActivity()).seekToAudio(progress);
                    Log.d("progress", Integer.toString(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        return v;
    }
    public interface audioControl {
        void pauseAudio();

        void playAudio(int bookId);

        void stopAudio();

        void seekToAudio(int position);

        //void setProgress();
    }
}
