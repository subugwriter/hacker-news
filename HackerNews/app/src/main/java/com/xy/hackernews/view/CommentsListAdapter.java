package com.xy.hackernews.view;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xy.hackernews.R;
import com.xy.hackernews.model.models.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by xy on 25/11/2017.
 */
public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.ViewHolder> {

    private ArrayList<Comment> mCommentsArray = new ArrayList<>();
    private SparseArray<Comment> mCommentsMap = new SparseArray<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mInfo;
        TextView mContent;

        public ViewHolder(View view) {
            super(view);
            mInfo = (TextView) view.findViewById(R.id.info);
            mContent = (TextView) view.findViewById(R.id.content);
        }
    }

    public CommentsListAdapter() {
    }

    /*
     * reset data of the list
     */
    public void setData(List<Comment> comments) {
        for (Comment currentComment : comments) {
            if (currentComment != null) {
                Comment comment = mCommentsMap.get(currentComment.getId());
                if (comment != null) {
                    mCommentsMap.remove(currentComment.getId());
                    mCommentsArray.remove(comment);
                }

                mCommentsMap.put(currentComment.getId(), currentComment);
                mCommentsArray.add(currentComment);
            }
        }

        notifyDataSetChanged();
    }

    /*
     * add data at the end of list
     */
//    public void addData(List<Comment> comments) {
//        mCommentsMap.clear();
//        mCommentsArray.clear();
//        for (Comment currentComment : comments) {
//            if (currentComment != null) {
//                Comment comment = mCommentsMap.get(currentComment.getId());
//                if (comment == null) {
//                    mCommentsMap.put(currentComment.getId(), currentComment);
//                    mCommentsArray.add(currentComment);
//                }
//
//            }
//        }
//
//        notifyDataSetChanged();
//    }

//    public List<Comment> getData() {
//        return mCommentsArray;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Comment comment = mCommentsArray.get(position);
        String timeSpan = DateUtils.getRelativeTimeSpanString(TimeUnit.SECONDS.toMillis(comment.getTime())).toString();
        holder.mInfo.setText(comment.getBy() + " " + timeSpan);
        if ((comment.getText() != null) && (comment.getText().length() > 0)) {
            holder.mContent.setText(Html.fromHtml(comment.getText()));
        } else {
            holder.mContent.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mCommentsArray.size();
    }
}
