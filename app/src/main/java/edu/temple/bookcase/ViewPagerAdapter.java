package edu.temple.bookcase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private ArrayList<Book> bookList;

    BookDetailsFragment bookDetailsFragment;

    public ViewPagerAdapter(FragmentManager fm, Context context, ArrayList<Book> bookList) {
        super(fm);
        this.context = context;
        this.bookList = bookList;
    }

    public void setBookList(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public Fragment getItem(int i) {
        bookDetailsFragment = new BookDetailsFragment();
        bookDetailsFragment.setBook(bookList.get(i));
        return bookDetailsFragment;
    }

    @Override
    public int getCount() {
        bookList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
