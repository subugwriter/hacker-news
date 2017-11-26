package com.xy.hackernews.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xy.hackernews.R;
import com.xy.hackernews.model.models.Story;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by xy on 25/11/2017.
 */
public class TopStoriesListAdapter extends RecyclerView.Adapter<TopStoriesListAdapter.ViewHolder> {

    private ArrayList<Story> mTopStoriesArray = new ArrayList<>();
    private SparseArray<Story> mTopStoriesMap = new SparseArray<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mNumber;
        View mStory;
        TextView mTitle;
        TextView mDetail;
        TextView mComments;

        public ViewHolder(View view) {
            super(view);
            mNumber = (TextView) view.findViewById(R.id.number);
            mStory = view.findViewById(R.id.story);
            mTitle = (TextView) view.findViewById(R.id.title);
            mDetail = (TextView) view.findViewById(R.id.details);
            mComments = (TextView) view.findViewById(R.id.comments);
        }
    }

    private Context mContext = null;

    public TopStoriesListAdapter(Context context) {
        mContext = context;
    }

    /*
    reset data of the list
     */
    public void setData(List<Story> topStories) {
        mTopStoriesMap.clear();
        mTopStoriesArray.clear();
        for (Story currentStory : topStories) {
            if (currentStory != null) {
                Story story = mTopStoriesMap.get(currentStory.getId());
                if (story != null) {
                    mTopStoriesMap.remove(currentStory.getId());
                    mTopStoriesArray.remove(story);
                }

                mTopStoriesMap.put(currentStory.getId(), currentStory);
                mTopStoriesArray.add(currentStory);
            }
        }

        notifyDataSetChanged();
    }

    /*
    add data at the end of list
     */
    public void addData(List<Story> topStories) {
        for (Story currentStory : topStories) {
            if (currentStory != null) {
                Story story = mTopStoriesMap.get(currentStory.getId());
                if (story == null) {
                    mTopStoriesMap.put(currentStory.getId(), currentStory);
                    mTopStoriesArray.add(currentStory);
                }

            }
        }

        notifyDataSetChanged();
    }

    public List<Story> getData() {
        return mTopStoriesArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_story, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Story story = mTopStoriesArray.get(position);
        holder.mNumber.setText(String.valueOf(position + 1));
        holder.mTitle.setText(story.getTitle());
        String timeSpan = DateUtils.getRelativeTimeSpanString(TimeUnit.SECONDS.toMillis(story.getTime())).toString();
        holder.mDetail.setText(story.getScore() + " points by " + story.getBy() + " " + timeSpan);
        holder.mComments.setText((story.getKids() == null ? 0 : story.getKids().size()) + " comments");

        //open url when story is clicked
        holder.mStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = story.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mContext.startActivity(intent);
            }
        });
        //go to comments page when comments text is clicked
        holder.mComments.setOnClickListener(new View.OnClickListener() {//TODO
            @Override
            public void onClick(View v) {
                if ((story.getKids() != null) && (story.getKids().size() > 0)) {
                    //only go to command page when there are commands
                    CommentsActivity.startWithTitleAndCommentIds(mContext, story.getTitle(), story.getKids());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTopStoriesArray.size();
    }
}
