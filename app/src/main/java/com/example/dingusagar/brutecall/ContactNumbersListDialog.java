package com.example.dingusagar.brutecall;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.dingusagar.brutecall.interfaces.MyCustomDialog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dingu on 25/11/17.
 */

public class ContactNumbersListDialog implements MyCustomDialog{

    private Context context;
    private String title = "Select a number";
    private int layout = R.layout.contact_list_dialog;

    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View contentView;
    AlertDialog dialog;
    ArrayList<String> numbers;

    public ContactNumbersListDialog(Context context, ArrayList<String> numbers) {
        this.context = context;
        this.numbers = numbers;
    }

    @Override
    public void showDialog() {
        dialog =  builder.create();
        dialog.show();
    }

    @Override
    public void setupDialog() {
        inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(layout,null);


        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(contentView);

        ListView lv = (ListView) contentView.findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,numbers);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)context).updateUIforNumberFromContactList(numbers.get(i));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onNegativeButtonClicked();
            }
        });

    }

    @Override
    public void onPositiveButtonClicked() {

    }

    @Override
    public void onNegativeButtonClicked() {

    }
}
