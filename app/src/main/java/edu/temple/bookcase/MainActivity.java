package edu.temple.bookcase;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BookListFragment.getBookName {

    ViewPager viewPager;
    private boolean isTwoPane;

    BookListFragment bookListFragment;
    BookDetailsFragment bookDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.bookListMainView) == null) {
            isTwoPane = false;
        } else {
            isTwoPane = true;
        }

        if (!isTwoPane) {
            viewPager = findViewById(R.id.viewPager);
            List<BookDetailsFragment> bookDetailsFragments = new ArrayList<>();
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
            viewPager.setAdapter(viewPagerAdapter);
        } else {

            bookListFragment = new BookListFragment();
            bookDetailsFragment = new BookDetailsFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.bookListMainView, bookListFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.bookDetailsMainView, bookDetailsFragment).commit();

        }
    }

    @Override
    public void bookSelected(String bookName) {
        bookDetailsFragment.setBookName(bookName);
    }
}
