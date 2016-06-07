package com.example.monalisa.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created on 07/06/16.
 * Added CustomAdapter for listView
 */
public class CustomListAdapter extends BaseAdapter{

    List<DummyData> itemList;
    Context context;
    LayoutInflater inflater;

    public CustomListAdapter(Context context, List<DummyData> itemList){
        this.context = context;
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public DummyData getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if(view == null){
            view = inflater.inflate(R.layout.lv_item, parent, false);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) view.findViewById(R.id.iv_list_image);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.tvDetail = (TextView) view.findViewById(R.id.tv_detail);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        DummyData data = getItem(position);
        String imageUrl = data.getImageUrl();
        holder.tvTitle.setText(data.getTitle());
        holder.tvDetail.setText(data.getDescription());

        // Get the image asynchronously and resize it.
        Picasso.with(context).load(imageUrl)
                .placeholder(R.drawable.ic_download)
                .error(R.drawable.ic_download)
                .resizeDimen(R.dimen.list_image_width, R.dimen.list_image_height)
                .centerInside()
                .tag(context)
                .into(holder.ivImage);

        return view;
    }

    static class ViewHolder{
        ImageView ivImage;
        TextView tvTitle;
        TextView tvDetail;
    }
}
