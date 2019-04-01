package edu.temple.bookcase;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity implements BookListFragment.getBookName {

    private boolean isTwoPane;

    BookListFragment bookListFragment;
    BookDetailsFragmentLandscape bookDetailsFragmentLandscape;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;

    TextView searchBox;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isTwoPane = findViewById(R.id.bookListMainView) != null;



        if (isTwoPane) {
            bookListFragment = new BookListFragment();

            bookDetailsFragmentLandscape = new BookDetailsFragmentLandscape();

            getSupportFragmentManager().beginTransaction().replace(R.id.bookListMainView, bookListFragment).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.bookDetailsMainView, bookDetailsFragmentLandscape).commit();

        } else {
            searchBox = findViewById(R.id.searchBox);
            searchButton = findViewById(R.id.searchButton);

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            viewPager = findViewById(R.id.viewPager);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    @Override
    public void bookSelected(String bookName) {
        bookDetailsFragmentLandscape.displayBookName(bookName);
    }

}
