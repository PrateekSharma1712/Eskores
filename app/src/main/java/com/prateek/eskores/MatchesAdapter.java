package com.prateek.eskores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by prateek on 18/3/15.
 */
public class MatchesAdapter extends BaseAdapter {

    private Context context = null;
    private List<ScoreFeed> scores = null;

    public MatchesAdapter(Context context) {
        this.context = context;
    }

    public void updateList(List<ScoreFeed> scores) {
        this.scores = scores;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return scores.size();
    }

    @Override
    public Object getItem(int position) {
        return scores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return scores.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.match_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ScoreFeed score = scores.get(position);
        holder.title.setText(EskoreApp.formatTitle(score.getTitle()));
        holder.desc.setText(EskoreApp.formatDesc(score));
        return view;
    }

    static class ViewHolder {
        private TextView title;
        private TextView desc;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            desc = (TextView) view.findViewById(R.id.desc);
        }
    }
}
