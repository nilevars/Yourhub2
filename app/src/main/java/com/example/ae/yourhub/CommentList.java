package com.example.ae.yourhub;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A E on 4/25/2017.
 */

public class CommentList extends BaseAdapter
{
    private LayoutInflater inflater=null;
    Context context;
    List<UserComments> userCommentsList=new ArrayList<UserComments>();
    CommentList(Context context,List<UserComments> userCommentsList)
    {
        this.context=context;
        this.userCommentsList=userCommentsList;
        inflater = ( LayoutInflater ) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return userCommentsList.size();
    }

    @Override
    public Object getItem(int i){
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_comment, null);

        holder.name=(TextView) rowView.findViewById(R.id.name);
        holder.desc=(TextView) rowView.findViewById(R.id.comment);
        holder.time=(TextView) rowView.findViewById(R.id.time_comment);

        holder.desc.setMovementMethod(LinkMovementMethod.getInstance());

        String uname=userCommentsList.get(i).getName();
        String first_letter=""+uname.charAt(0);
        String desc=userCommentsList.get(i).getDescription();
        String time=userCommentsList.get(i).getTime();

        holder.name.setText(uname);
        holder.desc.setText(desc);
        holder.time.setText(time);
        Linkify.addLinks(holder.desc, Linkify.WEB_URLS);
        return rowView;
    }
}

class Holder
{
    TextView name;
    TextView desc;
    TextView time;
}