package com.example.luiz.chuvinhaz.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.luiz.chuvinhaz.R;

import java.util.ArrayList;

/**
 * Created by luiz on 6/1/15.
 */
public class AttributeArrayAdapter extends ArrayAdapter<VariableListItem> {

    private static class ViewHolder {
        TextView name;
        TextView value;
    }

    public AttributeArrayAdapter(Context context, ArrayList<VariableListItem> itemsArrayList) {
        super(context, R.layout.row, itemsArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        VariableListItem item = getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.label);
            viewHolder.value = (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.name.setText(item.getName());
        viewHolder.value.setText(item.getValue());

        // Return the completed view to render on screen
        return convertView;
    }
}
