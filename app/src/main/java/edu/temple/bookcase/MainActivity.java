package edu.temple.bookcase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements BookListFragment.getBook {

    private boolean isTwoPane;

    BookListFragment bookListFragment;
    BookDetailsFragmentLandscape bookDetailsFragmentLandscape;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;

    TextView searchBox;
    Button searchButton;
    TextView searchBoxLandscape;
    Button searchButtonLandscape;
    String baseURL = "https://kamorris.com/lab/audlib/booksearch.php?search=";
    Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isTwoPane = findViewById(R.id.bookListMainView) != null;


        if (isTwoPane) {
            //Landscape mode
            searchBoxLandscape = findViewById(R.id.searchBoxLandscape);
            searchButtonLandscape = findViewById(R.id.searchButtonLandscape);
            bookListFragment = new BookListFragment();
            bookDetailsFragmentLandscape = new BookDetailsFragmentLandscape();
            getSupportFragmentManager().beginTransaction().replace(R.id.bookListMainView, bookListFragment).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.bookDetailsMainView, bookDetailsFragmentLandscape).commit();
            searchButtonLandscape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String searchQueryLandscape = searchBoxLandscape.getText().toString();
                    Thread t2 = new Thread() {
                        @Override
                        public void run() {

                            URL bookUrl;

                            try {
                                bookUrl = new URL("https://kamorris.com/lab/audlib/booksearch.php?search=" + searchQueryLandscape);

                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(bookUrl.openStream()));

                                String response = "", tmpResponse;

                                tmpResponse = reader.readLine();
                                while (tmpResponse != null) {
                                    response = response + tmpResponse;
                                    tmpResponse = reader.readLine();
                                }

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
        } else {
            //Portrait mode
            viewPager = findViewById(R.id.viewPager);
            searchBox = findViewById(R.id.searchBox);
            searchButton = findViewById(R.id.searchButton);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String searchQuery = searchBox.getText().toString();
                    Thread t = new Thread() {
                        @Override
                        public void run() {

                            URL bookUrl;

                            try {
                                bookUrl = new URL("https://kamorris.com/lab/audlib/booksearch.php?search=" + searchQuery);

                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(bookUrl.openStream()));

                                String response = "", tmpResponse;

                                tmpResponse = reader.readLine();
                                while (tmpResponse != null) {
                                    response = response + tmpResponse;
                                    tmpResponse = reader.readLine();
                                }

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
        }

    }

    Handler bookResponseHandler = new Handler(new Handler.Callback() {

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
            setViewPagerAdapter(bookArrayList);

            return false;
        }
    });
    Handler bookResponseHandlerLandscape = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            JSONArray responseArray = (JSONArray) msg.obj;
            updateViews(responseArray);
            return false;
        }
    });
    @Override
    public void bookSelected(Book book) {
        bookDetailsFragmentLandscape.displayBookName(book);

    }

    public void setViewPagerAdapter(ArrayList<Book> bookList) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, bookList);
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void updateViews(JSONArray jsonArray) {
        bookListFragment = new BookListFragment(jsonArray, new BookAdapter(this, jsonArray));
        bookListFragment.setJsonArray(jsonArray);
        bookDetailsFragmentLandscape = new BookDetailsFragmentLandscape();
        getSupportFragmentManager().beginTransaction().replace(R.id.bookListMainView, bookListFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.bookDetailsMainView, bookDetailsFragmentLandscape).commit();
    }


}
