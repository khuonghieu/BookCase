package edu.temple.bookcase;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static java.lang.System.in;


public class BookDetailsFragment extends Fragment {

    //Name of book
    private Book book;
    private SeekBar progressBar;
    Button download;
    Button delete;
    String baseDownloadURL = "https://kamorris.com/lab/audlib/download.php?id=";
    SharedPreferences bookDetailPref;

    SharedPreferences.Editor editor;

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

        bookDetailPref = this.getActivity().getSharedPreferences("" + book.getTitle() + " orient", Context.MODE_PRIVATE);
        editor = bookDetailPref.edit();

        TextView bookTitle = v.findViewById(R.id.bookTitle);

        ImageView bookCover = v.findViewById(R.id.bookCover);

        TextView bookAuthor = v.findViewById(R.id.bookAuthor);

        TextView bookPublishDate = v.findViewById(R.id.bookPublishDate);

        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setMax(book.getDuration());

        Button pauseButton = v.findViewById(R.id.pauseButton);
        final Button playButton = v.findViewById(R.id.playButton);
        final Button stopButton = v.findViewById(R.id.stopButton);


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


                File audio = new File(Environment.DIRECTORY_DOWNLOADS, book.getTitle() + ".mp3").getAbsoluteFile();
                if (!audio.exists()) {
                    ((audioControl) getActivity()).playAudio(book.getId(), progressBar.getProgress());
                } else {
                    ((audioControl) getActivity()).playAudio(audio, progressBar.getProgress());
                }


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
                editor.putInt("Progress Bar", 0);
                editor.apply();
                progressBar.setProgress(0);
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

        download = v.findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t2 = new Thread() {
                    @Override
                    public void run() {
                        File check = new File(getContext().getFilesDir(), book.getTitle() + ".mp3");
                        Log.d("check path", check.getAbsolutePath());
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

        delete = v.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
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

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setProgress(bookDetailPref.getInt("Progress Bar", 0));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public SeekBar getProgressBar() {
        return progressBar;
    }

    public interface audioControl {

        void pauseAudio();

        void playAudio(int bookId, int position);

        void playAudio(File audioFile, int position);

        void stopAudio();

        void seekToAudio(int position);


    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
