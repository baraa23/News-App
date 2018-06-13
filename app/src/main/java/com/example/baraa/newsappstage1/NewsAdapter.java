package com.example.baraa.newsappstage1;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A {@link NewsAdapter} knows how to create a list item layout for each news
 * in the data source (a list of {@link News} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news    is the list of news, which is the data source of the adapter
     */

    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays information about the news at the given position
     * in the list of news.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link News} object located at this position in the list
        News currentNewsObject = getItem(position);

        //  Find the TextView with view ID news_title
        TextView newsTitleTextView = listItemView.findViewById(R.id.news_title);
        assert currentNewsObject != null;
        newsTitleTextView.setText(currentNewsObject.getNewsTitle());

        // Find the TextView with the view id news_category
        TextView newsCategoryTextView = listItemView.findViewById(R.id.news_category);
        newsCategoryTextView.setText(currentNewsObject.getNewsCategory());

        // Find the TextView with the view id news_author
        TextView newsAuthorTextView = listItemView.findViewById(R.id.news_author);
        newsAuthorTextView.setText(currentNewsObject.getNewsAuthor());

        // Find the TextView with the view id news_date
        TextView newsDateTextView = listItemView.findViewById(R.id.news_date);

        // Display the date of the current news in that TextView
        SimpleDateFormat dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EE dd MMM yyyy", Locale.ENGLISH);

        try {
            Date dateNews = dateFormatJSON.parse(currentNewsObject.getNewsDate());

            String date = dateFormat2.format(dateNews);
            newsDateTextView.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Return the whole list item layout (containing 4 TextViews)
        // so that it can be shown in the ListView
        return listItemView;


    }
}