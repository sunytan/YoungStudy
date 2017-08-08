package ty.youngstudy.com.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ty.youngstudy.com.R;
import ty.youngstudy.com.bean.Novel;
import ty.youngstudy.com.bean.ShelftBook;
import ty.youngstudy.com.ui.widget.DownLoadText;

/**
 * Created by edz on 2017/7/26.
 */

public class BookItemAdapter extends BaseAdapter {

    private List<Novel> mNovelList;
    private LayoutInflater inflater;
    private List<Integer> mShowMore = new ArrayList<Integer>();
    private ViewHolder viewHolder = null;
    public BookItemAdapter(List<? extends Novel> book,LayoutInflater inflater){
        mNovelList = (List<Novel>) book;
        this.inflater = inflater;
    }
    public BookItemAdapter(LayoutInflater inflater) {
        mNovelList = new ArrayList<Novel>();
        this.inflater = inflater;
    }

    public List<Novel> getNovels(){
        return mNovelList;
    }

    public void changeData(List<? extends  Novel> novel) {
        mNovelList.clear();
        mNovelList.addAll(novel);
        notifyDataSetChanged();
    }

    public View bind(View view,int layoutid,int position){
        viewHolder = null;
        final Novel novel = mNovelList.get(position);
        if (view == null) {
            view = inflater.inflate(layoutid,null);
            viewHolder = new ViewHolder();
            viewHolder.thumb = (ImageView) view.findViewById(R.id.image_novel_img_id);
            viewHolder.bookName = (TextView) view.findViewById(R.id.tv_novel_name_id);
            viewHolder.chapterCount = (TextView) view.findViewById(R.id.tv_novel_chapter_count);
            viewHolder.last_chapter = (TextView) view.findViewById(R.id.tv_novel_chapter);
            viewHolder.hide_menu = view.findViewById(R.id.layout_hide_menu);
            viewHolder.detail = (TextView) view.findViewById(R.id.tv_detail);
            viewHolder.downLoadText = (DownLoadText) view.findViewById(R.id.tv_download);
            viewHolder.delete = (TextView) view.findViewById(R.id.tv_delete);
            viewHolder.cardView = view.findViewById(R.id.cv_novel_item_id);
            viewHolder.lastUpdateTime = (TextView) view.findViewById(R.id.tv_novel_update_time);
            viewHolder.unReadCount = (TextView) view.findViewById(R.id.tv_novel_unread_id);
            viewHolder.toggleButton = (ToggleButton) view.findViewById(R.id.btn_toggleBtn);
            view.setTag(layoutid,viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag(layoutid);
        }
        viewHolder.detail.setOnClickListener(new ClickListener(novel));
        viewHolder.delete.setOnClickListener(new ClickListener(novel));
        viewHolder.bookName.setText(novel.getName());
        //viewHolder.thumb.setImageDrawable(novel.getThumb());
        viewHolder.last_chapter.setText(novel.getLastUpdateChapter());
        if (novel instanceof ShelftBook) {
            ShelftBook sb = (ShelftBook) novel;
            viewHolder.chapterCount.setText(sb.getCurrentChapterPosition() + "/" + sb.getChapterCount());
            int a = sb.chapterCount - sb.getCurrentChapterPosition();
            if (a > 0) {
                viewHolder.unReadCount.setVisibility(View.VISIBLE);
                viewHolder.unReadCount.setText(inflater.getContext().getString(R.string.unread_count,a));
            }else {
                viewHolder.unReadCount.setVisibility(View.INVISIBLE);
            }
        }
        if (viewHolder.cardView != null) {
            viewHolder.cardView.setOnClickListener(new ClickListener(novel));
        }
        //Glide.with(view.getContext()).load(novel.getThumb()).load(R.mipmap.default_cover).into(viewHolder.thumb);
        Log.i("tanyang","down = "+viewHolder.downLoadText);
        if (viewHolder.downLoadText != null)
        viewHolder.downLoadText.bintText(novel);
        view.findViewById(R.id.fl_toggleBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.toggleButton.setChecked(!viewHolder.toggleButton.isChecked());
            }
        });

        viewHolder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    viewHolder.hide_menu.setVisibility(View.VISIBLE);
                    if (!mShowMore.contains(novel.getId())){
                        mShowMore.add(novel.getId());
                    }
                } else {
                    viewHolder.hide_menu.setVisibility(View.GONE);
                    mShowMore.remove(Integer.valueOf(novel.getId()));
                }
            }
        });

        if (mShowMore.contains(novel.getId())) {
            viewHolder.toggleButton.setChecked(true);
        } else {
            viewHolder.toggleButton.setChecked(false);
        }

        return view;
    }

    @Override
    public int getCount() {
        return mNovelList.size();
    }

    @Override
    public Object getItem(int i) {
        return mNovelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return bind(view, R.layout.novel_item_layout,i);
    }

    class ClickListener implements View.OnClickListener {

        private Novel mNovel;
        public ClickListener(Novel novel){
            mNovel = novel;
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.tv_delete:
                    break;
                case R.id.tv_download:
                    break;
                case R.id.tv_detail:
                    break;
                case R.id.cv_novel_item_id:
                    break;
                default:
                    break;
            
            }
        }
    }

    public class ViewHolder{
        ImageView thumb;
        TextView bookName;
        TextView last_chapter;
        View hide_menu;
        ToggleButton toggleButton;
        TextView lastUpdateTime;
        TextView chapterCount;
        TextView unReadCount;
        DownLoadText downLoadText;
        TextView detail;
        TextView delete;
        View cardView;
    }
}
