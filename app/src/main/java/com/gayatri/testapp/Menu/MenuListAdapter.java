package com.gayatri.testapp.Menu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gayatri.testapp.AllBooksListActivity;
import com.gayatri.testapp.CreateBooksActivity;
import com.gayatri.testapp.R;
import com.gayatri.testapp.TestActivity;
import com.gayatri.testapp.TextActivity;

/**
 * Created by Anthony on 16-01-25.
 */
public class MenuListAdapter extends ArrayAdapter<MenuActionItem> {

    int resource;
    Activity activity;

    public MenuListAdapter(int resource, Activity activity, MenuActionItem[] items) {
        super(activity, resource, items);

        this.resource = resource;
        this.activity = activity;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public View getView (int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if(rowView == null) {
            rowView = activity.getLayoutInflater().inflate(resource, null);

            MenuItemViewHolder viewHolder = new MenuItemViewHolder();

            viewHolder.menuItemImageView = (ImageView)rowView.findViewById(R.id.menu_item_image_view);
            viewHolder.menuItemTextView = (TextView)rowView.findViewById(R.id.menu_item_text_view);




            rowView.setTag(viewHolder);
        }

        final MenuItemViewHolder holder = (MenuItemViewHolder)rowView.getTag();

        if(position == MenuActionItem.ITEM1.ordinal()) {
            holder.menuItemImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.createbook));
            holder.menuItemTextView.setText(activity.getResources().getString(R.string.item1));
        }
        else if(position == MenuActionItem.ITEM2.ordinal()) {
            holder.menuItemImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.mybooks));
            holder.menuItemTextView.setText(activity.getResources().getString(R.string.item2));
        }
        else if(position == MenuActionItem.ITEM3.ordinal()) {
            holder.menuItemImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.file_share));
            holder.menuItemTextView.setText(activity.getResources().getString(R.string.item3));
        }
        else if(position == MenuActionItem.ITEM4.ordinal()) {
            holder.menuItemImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.publication_icon2));
            holder.menuItemTextView.setText(activity.getResources().getString(R.string.item4));
        }
        else if(position == MenuActionItem.ITEM5.ordinal()) {
            holder.menuItemImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.book_lovers1));
            holder.menuItemTextView.setText(activity.getResources().getString(R.string.item5));
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.menuItemTextView.getText().toString().equalsIgnoreCase(activity.getResources().getString(R.string.item1)))

                {
                    Intent i=new Intent(getContext(),TextActivity.class);

                    getContext().startActivity(i);

                }
                if (holder.menuItemTextView.getText().toString().equalsIgnoreCase(activity.getResources().getString(R.string.item2)))

                {
                    Intent i=new Intent(getContext(),AllBooksListActivity.class);

                    getContext().startActivity(i);

                }
            }
        });
        return rowView;
    }

    private static class MenuItemViewHolder {
        public ImageView menuItemImageView;
        public TextView menuItemTextView;

    }
}
