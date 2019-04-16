package edu.temple.bookcase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import org.w3c.dom.Text;

public class BookDetailsFragmentLandscape extends Fragment {


    TextView bookTitleLandscape;
    ImageView bookCoverLandscape;
    TextView bookAuthorLandscape;
    TextView bookPublishDateLandscape;

    Book book;
    SeekBar seekBar;

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

        Button pauseLandscape = v.findViewById(R.id.pauseButtonLandscape);
        Button playLandscape = v.findViewById(R.id.playButtonLandscape);
        Button stopLandscape = v.findViewById(R.id.stopButtonLandscape);
        seekBar = v.findViewById(R.id.seekBarLandscape);

        pauseLandscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((audioControlLandscape) getActivity()).pauseAudioLandscape();
            }
        });

        playLandscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((audioControlLandscape) getActivity()).stopAudioLandscape();
                ((audioControlLandscape) getActivity()).playAudioLandscape(book.getId());
            }
        });

        stopLandscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((audioControlLandscape) getActivity()).stopAudioLandscape();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    ((BookDetailsFragmentLandscape.audioControlLandscape) getActivity()).seekToAudioLandscape(progress);
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

    public void setBook(Book book) {
        this.book = book;
        seekBar.setProgress(0);
        seekBar = getView().findViewById(R.id.seekBarLandscape);
        seekBar.setMax(book.getDuration());
    }

    public void displayBookName(Book book) {
        bookTitleLandscape.setText(book.getTitle());
        Picasso.get().load(book.getCoverURL()).into(bookCoverLandscape);
        bookAuthorLandscape.setText(book.getAuthor());
        bookPublishDateLandscape.setText(String.valueOf(book.getPublished()));
    }

    public SeekBar getSeekBarLandscape() {
        return seekBar;
    }

    public interface audioControlLandscape {
        void pauseAudioLandscape();

        void playAudioLandscape(int bookId);

        void stopAudioLandscape();

        void seekToAudioLandscape(int position);

    }
}
