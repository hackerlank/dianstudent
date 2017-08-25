package com.dingli.diandians.information.instructor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dingli.diandians.R;
import com.dingli.diandians.common.Constant;
import com.dingli.diandians.common.ResultInfoCall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingliyuangong on 2017/4/13.
 */

public class SignNoteAdapter extends BaseAdapter{
    Context context;
    List<ResultInfoCall> listresult;
    public SignNoteAdapter(Context context){
        this.context=context;
        listresult=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return listresult.size();
    }

    @Override
    public ResultInfoCall getItem(int position) {
        return listresult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note_view,null);
        }
        ListNoteView listNoteView=(ListNoteView) convertView;
        listNoteView.setNote(getItem(position));
        listNoteView.setTag(getItem(position));
        final int id=listresult.get(position).id;
        listNoteView.vtjujuew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,InsLocationActivity.class);
                intent.putExtra(Constant.SUISIGN,id);
                intent.putExtra(Constant.SUISICI,"2");
                context.startActivity(intent);
            }
        });
        return listNoteView;
    }
    public void refresh(List<ResultInfoCall> list){
        listresult.clear();
        listresult.addAll(list);
    }
}
