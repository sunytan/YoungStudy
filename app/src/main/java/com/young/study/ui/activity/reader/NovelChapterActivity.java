package com.young.study.ui.activity.reader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.young.study.R;
import com.young.study.bean.Novel;
import com.young.study.reader.Chapter;
import com.young.study.reader.manager.NovelManager;
import com.young.study.reader.manager.SettingManager;
import com.young.study.ui.activity.base.BaseActivity;
import com.young.study.util.NovelFileUtils;

public class NovelChapterActivity extends BaseActivity {

    private int curPos = 1;
    private Novel mCurrentNovel;
    private MyChapterAdapter myChapterAdapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_chapter);
        mCurrentNovel = NovelManager.getInstance().getCurrentNovel();
        curPos = SettingManager.getInstance().getReadProgress(mCurrentNovel.getId()+"")[0];
        List<Chapter> chapters = NovelManager.getInstance().getChapterList();
        myChapterAdapter = new MyChapterAdapter(chapters);
        listView.setFastScrollEnabled(true);
        listView.setAdapter(myChapterAdapter);
        listView.setDividerHeight(5);
        listView.setSelection(curPos -1);
    }



    @Override
    public View getLoadingView() {
        return null;
    }

    @Override
    public boolean getFirstStart() {
        return false;
    }

    @Override
    public void initViewAndEvents() {
        listView = (ListView) findViewById(R.id.listchapter);
    }


    class MyChapterAdapter extends BaseAdapter {
        private List<Chapter> mData;
        private LayoutInflater mInflater;

        public MyChapterAdapter(List<Chapter> chapters) {
            mData = chapters;
            mInflater = LayoutInflater.from(NovelChapterActivity.this);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return mData.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.chapter_item, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.downloaded);
                holder.title = (TextView) convertView.findViewById(R.id.chapter_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Chapter chapter = mData.get(position);
            holder.title.setText(chapter.getTitle());
            if(curPos == position + 1) {
                holder.icon.setImageResource(R.drawable.red_choose);
            } else if(NovelFileUtils.isChapterExist(chapter)) {
                holder.icon.setImageResource(R.drawable.point_select_green);
            } else {
                holder.icon.setImageResource(R.drawable.point_unselect);
            }

            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView title;
        }

    }
}
