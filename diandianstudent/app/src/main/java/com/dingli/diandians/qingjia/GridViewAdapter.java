package com.dingli.diandians.qingjia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dingli.diandians.R;
import com.dingli.diandians.common.DianTool;
import com.dingli.diandians.qingjia.picture.GridClose;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
	ArrayList<String> listfile=new ArrayList<String>();
	private Context context;
	private Bitmap bitmap;
	private int count;
    GridClose gridClose;
	Bitmap bm;
	public GridViewAdapter(Context context, ArrayList<String> listfile,
						   int count, Bitmap bitmap,GridClose gridClose) {
		this.context = context;
		this.listfile = listfile;
		this.count = count;
		this.bitmap = bitmap;
		this.gridClose=gridClose;
	}

	public GridViewAdapter(Context context, Bitmap bitmap, int count) {
		this.context = context;
		this.bitmap = bitmap;
		this.count = count;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.gridview_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.gridvieww_image);
			holder.btnclose=(ImageView)convertView.findViewById(R.id.btnclose);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (listfile != null) {
			if (position == count - 1) {
				holder.image.setTag(position);
				holder.image.setImageBitmap(bitmap);
				holder.btnclose.setVisibility(View.GONE);
			} else {
				final String path = listfile.get(position);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				bm = BitmapFactory.decodeFile(path,options);
				holder.image.setImageBitmap(bm);
				holder.btnclose.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						gridClose.close(position);
					}
				});
				holder.image.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(context,VacateDetailActivity.class);
						intent.putExtra("listfile",path);
						context.startActivity(intent);
					}
				});
			}
		} else {
			holder.image.setTag(position);
			holder.image.setImageBitmap(bitmap);
			holder.btnclose.setVisibility(View.GONE);
		}

		return convertView;
	}
	private class ViewHolder {
		public ImageView image,btnclose;
	}
}
