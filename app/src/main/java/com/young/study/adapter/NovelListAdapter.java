package com.young.study.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.young.study.R;
import com.young.study.bean.Novel;

/**
 * Created by edz on 2017/8/1.
 */

public class NovelListAdapter extends BaseAdapter {

    private List<Novel> novelList;
    private LayoutInflater mInflater;
    private int layoutId;

    public NovelListAdapter(Context context,List<Novel> result) {
        this.novelList = result;
        mInflater = LayoutInflater.from(context);
        layoutId = R.layout.sort_kind_novel_item;
    }

    public NovelListAdapter(Context context,List<Novel> result,int layout) {
        this.novelList = result;
        mInflater = LayoutInflater.from(context);
        layoutId = layout;
    }

    @Override
    public int getCount() {
        return novelList.size();
/*        int c = 0;
        if(novelList != null) {
            for(Novels n : novelList) {
                c += n.getNovels().size();
            }
        }
        return c;*/
    }

    @Override
    public Object getItem(int i) {
        return novelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        NovelOneKindItem item = null;
        if(view == null) {
            view = mInflater.inflate(layoutId, null);
            item = new NovelOneKindItem(view);
            view.setTag(item);
        } else {
            item = (NovelOneKindItem) view.getTag();
        }
        item.bind((Novel)getItem(i));
        return view;
    }


    private class NovelOneKindItem{

        private View mView;

        private ImageView thumb;
        private TextView name;
        private TextView author;
        private TextView brief;


        private NovelOneKindItem(View view) {
            mView = view;
            thumb = (ImageView) view.findViewById(R.id.img_kind_item_thumb);
            name = (TextView) view.findViewById(R.id.tv_kind_item_name);
            author = (TextView) view.findViewById(R.id.tv_kind_item_author);
            brief = (TextView) view.findViewById(R.id.tv_kind_item_brief);
        }

        public void bind(Novel novel) {
            name.setText(novel.getName());
            author.setText(novel.getAuthor());
            brief.setText(novel.getBrief());
            Glide.with(mView.getContext()).load(novel.getThumb()).into(thumb);
        }
    }
}
