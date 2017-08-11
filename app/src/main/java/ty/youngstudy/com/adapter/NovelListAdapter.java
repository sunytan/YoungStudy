package ty.youngstudy.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ty.youngstudy.com.R;
import ty.youngstudy.com.bean.Novels;

/**
 * Created by edz on 2017/8/1.
 */

public class NovelListAdapter extends BaseAdapter {

    private List<Novels> novelList;
    private LayoutInflater mInflater;
    private int layoutId;

    public NovelListAdapter(Context context,List<Novels> result) {
        this.novelList = result;
        mInflater = LayoutInflater.from(context);
        layoutId = R.layout.kind_list;
    }

    public NovelListAdapter(Context context,List<Novels> result,int layout) {
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
/*        int c = 0;
        Novel novel = null;
        if(novelList != null) {
            for(Novels n : novelList) {
                int size = n.getNovels().size();
                if(i < c + size) {
                    novel = n.getNovels().get(i - c);
                    break;
                }
                c = c + size;
            }
        }
        return novel;*/
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        NovelItem item = null;
        if(view == null) {
            view = mInflater.inflate(layoutId, null);
            item = new NovelItem(view);
            view.setTag(item);
        } else {
            item = (NovelItem) view.getTag();
        }
        item.bind((Novels)getItem(i));
        return view;
    }


    private class NovelItem{

        private View mView;

        private ImageView thumb;
        private TextView kindName;
        private TextView[] tv1;


        private NovelItem(View view) {
            mView = view;
            thumb = (ImageView) view.findViewById(R.id.img_kind_thumb);
            kindName = (TextView) view.findViewById(R.id.tv_kind_name);
            tv1[0] = (TextView) view.findViewById(R.id.tv_novel_name_1);
            tv1[1] = (TextView) view.findViewById(R.id.tv_novel_name_2);
            tv1[2] = (TextView) view.findViewById(R.id.tv_novel_name_3);
            tv1[3] = (TextView) view.findViewById(R.id.tv_novel_name_4);
            tv1[4] = (TextView) view.findViewById(R.id.tv_novel_name_5);
            tv1[5] = (TextView) view.findViewById(R.id.tv_novel_name_6);
        }

        public void bind(Novels novels) {
            kindName.setText(novels.getKindName());
            if (novels.getNovels().size()>=6){
                for (int i = 0; i <tv1.length ; i++) {
                    tv1[i].setText(novels.getNovels().get(i).getName());
                    tv1[i].setVisibility(View.VISIBLE);
                }
            } else {
                for (int i = 0; i < novels.getNovels().size(); i++) {
                    tv1[i].setText(novels.getNovels().get(i).getName());
                    tv1[i].setVisibility(View.VISIBLE);
                }
            }
            //显示第一本输的图标
            Glide.with(mView.getContext()).load(novels.getNovels().get(0).getThumb()).into(thumb);
//            Glide.with(mView.getContext()).load(novel.getThumb()).placeholder(R.drawable.default_cover).centerCrop()
//                    .into(thumb);
        }
    }
}
