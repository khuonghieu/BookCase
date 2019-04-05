package edu.temple.bookcase;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    private ArrayList<Book> bookList;

    private ArrayList<BookDetailsFragment> bookDetailsFragmentsList;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Book> bookList, ArrayList<BookDetailsFragment> bookDetailsFragments) {
        super(fm);
        this.bookList = bookList;
        this.bookDetailsFragmentsList = bookDetailsFragments;
    }

    public void setBookList(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    public void addBooks(ArrayList<Book> books) {
        bookDetailsFragmentsList = new ArrayList<BookDetailsFragment>(); // Empty the array list containing the collection of fragments
        for (Book book : books) {
            bookDetailsFragmentsList.add(BookDetailsFragment.newInstance(book));
            Log.d("New Books", book.getTitle()); // Populate
        }
        notifyDataSetChanged(); // FragmentStatePagetAdapter object.
    }

    @Override
    public Fragment getItem(int i) {
        return bookDetailsFragmentsList.get(i);

    }

    @Override
    public int getCount() {
        return bookDetailsFragmentsList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
