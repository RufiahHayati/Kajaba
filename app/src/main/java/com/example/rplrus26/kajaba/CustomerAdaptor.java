package com.example.rplrus26.kajaba;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

public class CustomerAdaptor extends ArrayAdapter<siswi> implements View.OnClickListener {
    private ArrayList<siswi> arraylist;
    Context mContext;
    @Override
    public void onClick(View v) {

    }
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtType1;
        ImageView imgLetter;
    }
    public CustomerAdaptor(ArrayList<siswi> arraylist, Context context) {
        super(context, R.layout.row_item, arraylist);
        this.arraylist = arraylist;
        this.mContext=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        siswi siswi = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.txtType1 = (TextView) convertView.findViewById(R.id.type1);
            viewHolder.imgLetter = (ImageView) convertView.findViewById(R.id.imgLetter);




            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.txtName.setText(siswi.getName());
        viewHolder.txtType.setText(siswi.getUsername());
        viewHolder.txtType1.setText(siswi.getAlamat());

        String first = String.valueOf(siswi.getName().charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int drawable = generator.getColor(getItem(position));
        TextDrawable color = TextDrawable.builder()
                .beginConfig()
                .withBorder(4) /* thickness in px */
                .endConfig()
                .buildRoundRect(first, drawable, 30);
                viewHolder.imgLetter.setImageDrawable(color);

        return convertView;
    }
}
