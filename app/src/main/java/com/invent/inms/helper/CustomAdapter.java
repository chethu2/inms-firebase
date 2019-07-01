package com.invent.inms.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import inms.invent.com.i_invent_inms.R;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<IngredientsData> ingredients;
    private ViewHolder viewHolder;

    public CustomAdapter(Context context, List<IngredientsData> ingredients){
        this.ingredients = ingredients;
        this.context = context;
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ingredients.indexOf(getItem(position));
    }


    private class ViewHolder{
        ImageView imageView;
        CheckedTextView checkedTextView;
        TextView expiryTextview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        IngredientsData ingredient = ingredients.get(position);
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.ingredients_list_iew,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.list_image);
            viewHolder.imageView.setImageResource(R.drawable.default_image);
            viewHolder.checkedTextView = convertView.findViewById(R.id.simpleCheckedTextView);
            viewHolder.expiryTextview = convertView.findViewById(R.id.expiry);
            viewHolder.checkedTextView.setText(ingredient.getCheckedText());
            viewHolder.checkedTextView.setChecked(false);
            viewHolder.imageView.setImageResource(ingredient.getImageId());
            viewHolder.expiryTextview.setText(ingredient.getExpiry((String) viewHolder.checkedTextView.getText()));
        return convertView;
    }
}
