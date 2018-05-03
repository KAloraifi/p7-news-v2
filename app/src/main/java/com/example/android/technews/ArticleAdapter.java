package com.example.android.technews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aloraifi on 02/05/2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(@NonNull Context context, @NonNull List<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        TextView title = listItemView.findViewById(R.id.article_title);
        title.setText(currentArticle.getTitle());

        TextView section = listItemView.findViewById(R.id.article_section);
        section.setText(currentArticle.getSection());

        TextView author = listItemView.findViewById(R.id.article_author);
        if (currentArticle.getAuthor() == null) {
            author.setText(currentArticle.getNoAuthorResourceId());
        } else {
            author.setText(currentArticle.getAuthor());
        }

        TextView publicationDate = listItemView.findViewById(R.id.article_date);
        String formattedDate = formatDate(currentArticle.getPublicationDate());
        publicationDate.setText(formattedDate);

        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from an already formatted date.
     */
    private String formatDate(String date) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat targetFormat = new SimpleDateFormat("LLL dd, yyyy");
        Date dateObj = null;
        try {
            dateObj = originalFormat.parse(date);
        } catch (ParseException e) {
            Log.e("ArticleAdapter", "Error parsing format.", e);
        }
        String formattedDate = targetFormat.format(dateObj);

        return formattedDate;
    }
}
