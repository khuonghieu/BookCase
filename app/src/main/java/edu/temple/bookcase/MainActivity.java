package edu.temple.bookcase;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.getBook, BookDetailsFragment.audioControl, BookDetailsFragmentLandscape.audioControlLandscape {

    private boolean isTwoPane;

    BookListFragment bookListFragment;
    BookDetailsFragmentLandscape bookDetailsFragmentLandscape;
    ViewPager viewPager;

    TextView searchBox;
    Button searchButton;
    TextView searchBoxLandscape;
    Button searchButtonLandscape;
    Book currentBook;

    AudiobookService audiobookService;
    Intent playIntent;
    boolean audioBound = false;
    AudiobookService.MediaControlBinder mediaControlBinder;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getSharedPreferences("MainActivityPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10089);
        isTwoPane = findViewById(R.id.bookListLandscape) != null;

        if (isTwoPane) {

            //Landscape mode
            searchBoxLandscape = findViewById(R.id.searchBoxLandscape);
            searchBoxLandscape.setText(pref.getString("Landscape Search", ""));
            searchButtonLandscape = findViewById(R.id.searchButtonLandscape);
            bookListFragment = new BookListFragment();
            bookDetailsFragmentLandscape = new BookDetailsFragmentLandscape();

            getSupportFragmentManager().beginTransaction().add(R.id.bookListLandscape, bookListFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.bookDetailsLandscape, bookDetailsFragmentLandscape).commit();

            playIntent = new Intent(this, AudiobookService.class);
            startService(playIntent);

            searchButtonLandscape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("Landscape Search", searchBoxLandscape.getText().toString());
                    editor.apply();
                    Thread t2 = new Thread() {
                        @Override
                        public void run() {
                            try {
                                String searchQueryLandscape = searchBoxLandscape.getText().toString();
                                URL bookUrl = new URL("https://kamorris.com/lab/audlib/booksearch.php?search=" + searchQueryLandscape);

                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(bookUrl.openStream()));

                                String tmpResponse;
                                StringBuilder responseBuilder = new StringBuilder();

                                tmpResponse = reader.readLine();
                                while (tmpResponse != null) {
                                    responseBuilder.append(tmpResponse);
                                    tmpResponse = reader.readLine();
                                }
                                reader.close();

                                String response = responseBuilder.toString();
                                JSONArray bookArray = new JSONArray(response);

                                Message msg = Message.obtain();

                                msg.obj = bookArray;
                                bookResponseHandlerLandscape.sendMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t2.start();

                }
            });
            searchButtonLandscape.performClick();

            playIntent = new Intent(this, AudiobookService.class);
            bindService(playIntent, serviceConnection, Context.BIND_AUTO_CREATE);

            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    bookDetailsFragmentLandscape.getSeekBarLandscape().setProgress(msg.what);
                    Log.d("handler", Integer.toString(msg.what));
                    bookDetailsFragmentLandscape.getEditor().putInt("Progress Bar Land", msg.what);
                    bookDetailsFragmentLandscape.getEditor().commit();
                    return false;
                }
            });



        } else {
            //Portrait mode
            viewPager = findViewById(R.id.viewPager);
            viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), new ArrayList<BookDetailsFragment>()));

            searchBox = findViewById(R.id.searchBox);
            searchBox.setText(pref.getString("Orient Search", ""));
            searchButton = findViewById(R.id.searchButton);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("Orient Search", searchBox.getText().toString());
                    editor.apply();
                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            try {
                                String searchQuery = searchBox.getText().toString();
                                URL bookUrl = new URL("https://kamorris.com/lab/audlib/booksearch.php?search=" + searchQuery);

                                Log.d("URL check", bookUrl.toString());

                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(bookUrl.openStream()));

                                String tmpResponse;
                                StringBuilder responseBuilder = new StringBuilder();

                                tmpResponse = reader.readLine();
                                while (tmpResponse != null) {
                                    responseBuilder.append(tmpResponse);
                                    tmpResponse = reader.readLine();
                                }
                                reader.close();

                                String response = responseBuilder.toString();
                                JSONArray bookArray = new JSONArray(response);

                                Message msg = Message.obtain();
                                msg.obj = bookArray;

                                bookResponseHandler.sendMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t.start();
                }
            });

            searchButton.performClick();
            playIntent = new Intent(this, AudiobookService.class);
            bindService(playIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    BookDetailsFragment bookDetailsFragment = (BookDetailsFragment) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(viewPager.getCurrentItem());
                    bookDetailsFragment.getProgressBar().setProgress(msg.what);
                    bookDetailsFragment.getEditor().putInt("Progress Bar", msg.what);
                    bookDetailsFragment.getEditor().commit();
                    Log.d("handler", Integer.toString(msg.what));
                    return false;
                }
            });

        }

    }

    Handler bookResponseHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            ArrayList<Book> bookArrayList = new ArrayList<>();

            JSONArray responseArray = (JSONArray) msg.obj;

            try {
                for (int i = 0; i < responseArray.length(); i++) {
                    JSONObject jsonObject = responseArray.getJSONObject(i);
                    bookArrayList.add(new Book(jsonObject));
                    Log.d("check", jsonObject.getString("title"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setViewPagerAdapter(bookArrayList);

            return false;
        }
    });

    Handler bookResponseHandlerLandscape = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            ArrayList<Book> bookArrayList = new ArrayList<>();

            JSONArray responseArray = (JSONArray) msg.obj;

            try {
                for (int i = 0; i < responseArray.length(); i++) {
                    currentBook = new Book((JSONObject) responseArray.get(i));
                    bookArrayList.add(currentBook);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            updateViews(responseArray);

            return false;
        }
    });

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            audioBound = true;
            mediaControlBinder = (AudiobookService.MediaControlBinder) service;
            mediaControlBinder.setProgressHandler(handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioBound = false;
        }
    };

    @Override
    public void bookSelected(Book book) {
        bookDetailsFragmentLandscape.setBook(book);
        bookDetailsFragmentLandscape.displayBookName(book);

    }

    public void setViewPagerAdapter(ArrayList<Book> bookList) {
        ((ViewPagerAdapter) viewPager.getAdapter()).addBooks(bookList);
    }

    public void updateViews(JSONArray jsonArray) {
        bookListFragment.setJsonArray(jsonArray);
    }


    @Override
    public void pauseAudio() {
        mediaControlBinder.pause();
    }

    @Override
    public void playAudio(int bookId) {
        mediaControlBinder.play(bookId);
    }

    @Override
    public void playAudio(File audioFile) {
        mediaControlBinder.play(audioFile);
    }

    @Override
    public void stopAudio() {
        mediaControlBinder.stop();
    }

    @Override
    public void seekToAudio(int position) {
        mediaControlBinder.seekTo(position);
    }


    @Override
    public void pauseAudioLandscape() {
        mediaControlBinder.pause();
    }

    @Override
    public void playAudioLandscape(int bookId) {
        mediaControlBinder.play(bookId);
    }

    @Override
    public void stopAudioLandscape() {
        mediaControlBinder.stop();
    }

    @Override
    public void seekToAudioLandscape(int position) {
        mediaControlBinder.seekTo(position);
    }


}
