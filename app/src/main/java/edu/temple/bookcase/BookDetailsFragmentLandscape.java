package edu.temple.bookcase;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class BookDetailsFragmentLandscape extends Fragment {


    TextView bookTitleLandscape;
    ImageView bookCoverLandscape;
    TextView bookAuthorLandscape;
    TextView bookPublishDateLandscape;
    private SharedPreferences bookDetailLandPref;
    String baseDownloadURL = "https://kamorris.com/lab/audlib/download.php?id=";
    final static String KEYPREF = "progress bar";
    private SharedPreferences.Editor editor;

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
        Button downloadLand = v.findViewById(R.id.downloadLand);
        Button deleteLand = v.findViewById(R.id.deleteLand);

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

                File audio = new File(getContext().getFilesDir(), book.getTitle() + ".mp3");
                if (!audio.exists()) {
                    ((BookDetailsFragmentLandscape.audioControlLandscape) getActivity()).playAudioLandscape(book.getId(), seekBar.getProgress());
                    Log.d("playing", "from web");

                } else {
                    ((BookDetailsFragmentLandscape.audioControlLandscape) getActivity()).playAudioLandscape(audio, seekBar.getProgress());
                    Log.d("playing", "from file");
                }
            }
        });

        stopLandscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((audioControlLandscape) getActivity()).stopAudioLandscape();
                editor.putInt(KEYPREF, 0);
                editor.apply();
                seekBar.setProgress(0);
            }
        });

        downloadLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t2 = new Thread() {
                    @Override
                    public void run() {
                        File check = new File(getContext().getFilesDir(), book.getTitle() + ".mp3");
                        Log.d("check exist", String.valueOf(check.exists()));
                        if (!check.exists()) {
                            try {

                                URL bookUrl = new URL(baseDownloadURL + book.getId());

                                URLConnection conn = bookUrl.openConnection();
                                int contentLength = conn.getContentLength();

                                DataInputStream stream = new DataInputStream(bookUrl.openStream());

                                byte[] buffer = new byte[contentLength];
                                stream.readFully(buffer);
                                stream.close();

                                DataOutputStream fos = new DataOutputStream(new FileOutputStream(check));
                                fos.write(buffer);
                                fos.flush();
                                fos.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                };
                t2.start();
            }
        });

        deleteLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File check = new File(getContext().getFilesDir(), book.getTitle() + ".mp3");
                if (check.exists()) {
                    check.delete();
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Does not exist, cant delete", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void onResume() {
        super.onResume();
        if (book != null) {
            seekBar.setProgress(bookDetailLandPref.getInt(KEYPREF, 0));
        }
    }

    public void setBook(Book book) {
        this.book = book;
        if (book != null) {
            bookDetailLandPref = this.getActivity().getSharedPreferences(book.getTitle(), Context.MODE_PRIVATE);
            editor = bookDetailLandPref.edit();
        }
        seekBar.setProgress(0);
        seekBar = getView().findViewById(R.id.seekBarLandscape);
        seekBar.setMax(book.getDuration());
        seekBar.setProgress(bookDetailLandPref.getInt(KEYPREF, 0));
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

        void playAudioLandscape(int bookId, int position);

        void stopAudioLandscape();

        void playAudioLandscape(File audioFile, int timeMark);

        void seekToAudioLandscape(int position);


    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
