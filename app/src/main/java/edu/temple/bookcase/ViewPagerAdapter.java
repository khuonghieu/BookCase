package edu.temple.bookcase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    private ArrayList<Book> bookList;

    BookDetailsFragment bookDetailsFragment;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Book> bookList) {
        super(fm);
        this.bookList = bookList;
    }

    public void setBookList(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public Fragment getItem(int i) {
        return BookDetailsFragment.newInstance(bookList.get(i));
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
