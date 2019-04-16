package edu.temple.bookcase;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ArrayList<BookDetailsFragment> bookDetailsFragmentsList;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<BookDetailsFragment> bookDetailsFragments) {
        super(fm);
        this.bookDetailsFragmentsList = bookDetailsFragments;
    }

    public void addBooks(ArrayList<Book> books) {
        bookDetailsFragmentsList = new ArrayList<>(); // Empty the array list containing the collection of fragments
        for (Book book : books) {
            bookDetailsFragmentsList.add(BookDetailsFragment.newInstance(book));
            Log.d("New Books", book.getTitle()); // Populate
        }
        notifyDataSetChanged(); // FragmentStatePagetAdapter object.
    }

    public BookDetailsFragment getBookFragment(int position) {
        return bookDetailsFragmentsList.get(position);
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
