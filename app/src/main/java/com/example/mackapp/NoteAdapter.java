package com.example.mackapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

public class NoteAdapter extends BaseAdapter /*implements Filterable*/ {
    private Context mcontext;
    private List<Note> backList;  //备份note
    private List<Note> noteList;  //暂时的变量note
    private MyFilter myFilter;

    public NoteAdapter(Context mcontext,List<Note> noteList){
        this.mcontext = mcontext;
        this.noteList = noteList;
        backList = noteList;

    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
        mcontext.setTheme(R.style.DayTheme);

        View view = View.inflate(mcontext,R.layout.note_layout,null);
        TextView textView_content = view.findViewById(R.id.tv_content);
        TextView textView_time = view.findViewById(R.id.tv_time);

        String all_text = noteList.get(position).getContent();
        textView_content.setText(all_text);
        textView_time.setText(noteList.get(position).getTime());

        view.setTag(noteList.get(position).getId());
        return view;
    }

    class MyFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Note> list;
            if (TextUtils.isEmpty(constraint)){ //当过滤的关键词为空时则显示所有数据
                list = backList;
            }else { //否则把符合的数据放入集合中
                  list = new ArrayList<>();
                  for (Note note:backList){
                      if (note.getContent().contains(constraint)){
                          list.add(note);
                      }
                  }
            }
            results.values = list; //将得到的集合保存在FilterResult的value变量中
            results.count = list.size(); // 将集合的大小保存到FilterResult的count变量中
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                noteList = (List<Note>)results.values;
                if (results.count>0){
                    notifyDataSetChanged();  //通知数据发生了改变
                }else {
                    notifyDataSetInvalidated();  //通知数据失效
                }
        }
    }

    public Filter getFilter(){
        if (myFilter == null){
            myFilter = new MyFilter();
        }
        return myFilter;
    }

}
