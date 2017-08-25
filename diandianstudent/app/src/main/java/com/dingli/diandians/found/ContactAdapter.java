package com.dingli.diandians.found;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Coursecenter;
import com.dingli.diandians.personcenter.ActionSheetDialog;
import com.dingli.diandians.yichangnv.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/3/17.
 */

public class ContactAdapter extends BaseAdapter{

    List<Coursecenter> arraylist;
    Context context;
    ListContactView.ContactListener listener;
    public ContactAdapter(Context context, ListContactView.ContactListener listener){
        arraylist=new ArrayList<>();
        this.context=context;
        this.listener=listener;
    }
    @Override
    public int getCount() {
        return arraylist.size();
    }
    @Override
    public Coursecenter getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact_view,null);
        }
       final  ListContactView listContactView=(ListContactView) convertView;
        listContactView.setContactView(getItem(position));
        listContactView.setTag(getItem(position));
        listContactView.setContactListener(listener);
        final String phones=arraylist.get(position).phone;
        final String names=arraylist.get(position).name;
        final String stuIds=arraylist.get(position).stuId;
        if (!phones.equals("null")) {
            listContactView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContactListener(listContactView.contactphone.getText().toString(),phones, names, stuIds);
                }
            });
        }
        return listContactView;
    }
    public void refreshListContact(List<Coursecenter> arraylists){
        arraylist.clear();
        arraylist.addAll(arraylists);
    }
    public void addListContact(List<Coursecenter> arraylists){
        arraylist.addAll(arraylists);
    }
}
