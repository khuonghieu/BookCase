package edu.temple.bookcase;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BookListFragment.getBookName {

    private boolean isTwoPane;

    BookListFragment bookListFragment;
    BookDetailsFragmentLandscape bookDetailsFragmentLandscape;
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;

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
