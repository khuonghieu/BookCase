package edu.temple.bookcase;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;


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
        Log.d("pref", bookDetailPref.toString());
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
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        Log.d("pref check", String.valueOf(bookDetailPref.getInt("Progress Bar", 0)));
                        File audio = new File(Environment.DIRECTORY_DOWNLOADS, book.getTitle() + ".mp3").getAbsoluteFile();
                        if (!audio.exists()) {
                            ((audioControl) getActivity()).playAudio(book.getId(), progressBar.getProgress());
                            Log.d("prog", String.valueOf(progressBar.getProgress()));
                        } else {
                            ((audioControl) getActivity()).playAudio(audio, progressBar.getProgress());
                        }
                    }
                };
                t.start();

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

                File check = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), book.getTitle() + ".mp3");

                Log.d("check", check.getAbsolutePath());
                Log.d("exists", String.valueOf(check.exists()));

                if (!check.exists()) {

                    DownloadManager downloadmanager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(baseDownloadURL + book.getId());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setDescription("Downloading");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalFilesDir(getContext(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), book.getTitle() + ".mp3");
                    downloadmanager.enqueue(request);
                } else {
                    Toast.makeText(getContext(), "Already have", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete = v.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File check = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), book.getTitle() + ".mp3");
                Log.d("check", check.getAbsolutePath());
                if (check.exists()) {
                    check.delete();
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
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
