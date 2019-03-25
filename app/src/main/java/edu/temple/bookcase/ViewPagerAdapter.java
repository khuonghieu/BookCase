package edu.temple.bookcase;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private String[] bookList;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        bookList = context.getResources().getStringArray(R.array.books);
        BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();
        bookDetailsFragment.setBookName(bookList[i]);
        return bookDetailsFragment;
    }

    @Override
    public int getCount() {

        bookList = context.getResources().getStringArray(R.array.books);
        return bookList.length;
    }
}
