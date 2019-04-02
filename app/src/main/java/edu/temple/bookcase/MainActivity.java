package edu.temple.bookcase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    TextView searchBox;
    Button searchButton;
    TextView searchBoxLandscape;
    Button searchButtonLandscape;
    Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isTwoPane = findViewById(R.id.bookListLandscape) != null;

        if (isTwoPane) {
            //Landscape mode
            searchBoxLandscape = findViewById(R.id.searchBoxLandscape);
            searchButtonLandscape = findViewById(R.id.searchButtonLandscape);
            bookListFragment = new BookListFragment();
            bookDetailsFragmentLandscape = new BookDetailsFragmentLandscape();
            getSupportFragmentManager().beginTransaction().add(R.id.bookListLandscape, bookListFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.bookDetailsLandscape, bookDetailsFragmentLandscape).commit();
            searchButtonLandscape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
        } else {
            //Portrait mode
            viewPager = findViewById(R.id.viewPager);

            viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), this, new ArrayList<Book>()));
            searchBox = findViewById(R.id.searchBox);
            searchButton = findViewById(R.id.searchButton);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

    @Override
    public void bookSelected(Book book) {
        bookDetailsFragmentLandscape.displayBookName(book);

    }

    public void setViewPagerAdapter(ArrayList<Book> bookList) {
        ((ViewPagerAdapter) viewPager.getAdapter()).setBookList(bookList);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    public void updateViews(JSONArray jsonArray) {
        //bookListFragment = new BookListFragment();
        bookListFragment.setJsonArray(jsonArray);

        //bookDetailsFragmentLandscape = new BookDetailsFragmentLandscape();

        //getSupportFragmentManager().beginTransaction().replace(R.id.bookListLandscape, bookListFragment).commit();
        //getSupportFragmentManager().beginTransaction().replace(R.id.bookDetailsLandscape, bookDetailsFragmentLandscape).commit();
    }


}
