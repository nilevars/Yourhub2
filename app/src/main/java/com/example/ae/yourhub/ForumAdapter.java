package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by A E on 12-Jul-17.
 */

public class ForumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    View itemView;
    private LayoutInflater inflater = null;
    List<ForumData> forumDatas;
    private final static int FADE_DURATION = 1000;

    private final int QUE = 0, ARTICLE = 1, PHOTO = 2, JOKES = 3;


    public ForumAdapter(Context context, List<ForumData> forumDatas) {
        this.context = context;
        this.forumDatas = forumDatas;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return this.forumDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (forumDatas.get(position).getType() == 0) {
            return QUE;
        } else if (forumDatas.get(position).getType() == 1) {
            return ARTICLE;
        } else if (forumDatas.get(position).getType() == 2) {
            return PHOTO;
        } else if (forumDatas.get(position).getType() == 3) {
            return JOKES;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {

            case ARTICLE:
                View v2 = inflater.inflate(R.layout.article_card, viewGroup, false);
                viewHolder = new ArticleCardHolder(v2);
                break;
            case PHOTO:
                View v3 = inflater.inflate(R.layout.feed_card, viewGroup, false);
                viewHolder = new PhotoCardHolder(v3);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {

            case ARTICLE:
                ArticleCardHolder holder1 = (ArticleCardHolder) viewHolder;
                holder1.title.setText(forumDatas.get(position).getTitle());
                holder1.desc.setText(forumDatas.get(position).getDescription());
                holder1.time.setText(forumDatas.get(position).getTime());
                holder1.post_id.setText(forumDatas.get(position).getId());
                break;
            case PHOTO:
                PhotoCardHolder holder2 = (PhotoCardHolder) viewHolder;
                holder2.title.setText(forumDatas.get(position).getTitle());
                holder2.desc.setText(forumDatas.get(position).getDescription());
                holder2.time.setText(forumDatas.get(position).getTime());
                SharedPreferences hub = context.getSharedPreferences("com.example.ae.yourhub", Context.MODE_PRIVATE);
                holder2.comm.setText(hub.getString("community", ""));
                holder2.post_id.setText(forumDatas.get(position).getId());
                holder2.url = forumDatas.get(position).getImage();
                LikeList likeList = new LikeList(context, forumDatas.get(position).getId());
                if (forumDatas.get(position).getLike() == 1) {
                    holder2.imageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    holder2.imageButton.setColorFilter(context.getResources().getColor(R.color.red_400));
                    holder2.like = true;
                }
                if (forumDatas.get(position).getImage().matches("")) {
                    holder2.desc.setVisibility(View.VISIBLE);
                    holder2.imageView.setVisibility(View.GONE);
                }
                Picasso.with(context).load(forumDatas.get(position).getImage()).resize(400, 400).centerCrop().into(holder2.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("loaded Image");
                    }

                    @Override
                    public void onError() {
                        System.out.println("Unable to load Image");
                    }
                });
                setScaleAnimation(holder2.imageView);
                break;

        }
    }

    class PhotoCardHolder extends RecyclerView.ViewHolder {
        TextView title, desc, comment, post_id, time, comm;
        String url = "";
        ImageView imageView;
        ImageButton imageButton;
        Boolean like = false;
        LinearLayout l;

        public PhotoCardHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.description);
            comment = (TextView) itemView.findViewById(R.id.comment);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            comm = (TextView) itemView.findViewById(R.id.community);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageDisplayActivity.class);
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                }
            });
            imageButton = (ImageButton) itemView.findViewById(R.id.like);
            post_id = (TextView) itemView.findViewById(R.id.post_id);
            time = (TextView) itemView.findViewById(R.id.time);
            l = (LinearLayout) itemView.findViewById(R.id.photo);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ForumDisplayActivity.class);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("desc", desc.getText().toString());
                    String id = post_id.getText().toString();
                    intent.putExtra("id", id);
                    intent.putExtra("url", url);
                    intent.putExtra("type", "2");
                    context.startActivity(intent);
                }
            });
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!like) {
                        LikeList likeList = new LikeList(context, post_id.getText().toString(), "1");
                        likeList.insert();
                        imageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                        imageButton.setColorFilter(context.getResources().getColor(R.color.red_400));
                        like = true;
                    } else {
                        LikeList likeList = new LikeList(context, post_id.getText().toString(), "0");
                        likeList.insert();
                        imageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                        imageButton.setColorFilter(context.getResources().getColor(R.color.black));
                        like = false;
                    }
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    String id = post_id.getText().toString();
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });

        }
    }

    class ArticleCardHolder extends RecyclerView.ViewHolder {
        TextView title, desc, comment, post_id, time;
        ImageView imageView;
        ImageButton imageButton;
        Boolean like = false;
        LinearLayout l;

        public ArticleCardHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.description);
            comment = (TextView) itemView.findViewById(R.id.comment);
            post_id = (TextView) itemView.findViewById(R.id.post_id);
            time = (TextView) itemView.findViewById(R.id.time);
            l = (LinearLayout) itemView.findViewById(R.id.article);
            l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ForumDisplayActivity.class);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("desc", desc.getText().toString());
                    String id = post_id.getText().toString();
                    Log.d("id", id);
                    intent.putExtra("id", id);
                    intent.putExtra("type", "1");
                    context.startActivity(intent);
                }
            });
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            imageButton = (ImageButton) itemView.findViewById(R.id.like);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!like) {
                        imageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                        imageButton.setColorFilter(context.getResources().getColor(R.color.red_400));
                        like = true;
                    } else {
                        imageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                        imageButton.setColorFilter(context.getResources().getColor(R.color.black));
                        like = false;
                    }
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    String id = post_id.getText().toString();
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });

        }
    }
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
}
