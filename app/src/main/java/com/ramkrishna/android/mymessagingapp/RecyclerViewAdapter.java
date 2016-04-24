package com.ramkrishna.android.mymessagingapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ramkr on 23-Apr-16.
 * Custom Adapter used for showing the message data in the RecyclerView
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    OnItemClickListener mItemClickListener;
    private List<MessageItem> list = Collections.emptyList();
    private Context context;
    private String searchString;

    //Adapter Constructor
    public RecyclerViewAdapter(List<MessageItem> list, Context context, String searchString)
    {
        this.list = list;
        this.context = context;
        this.searchString = searchString;
    }

    /**
     * The ViewHolder that stores the references to the views present in the items of the RecyclerView
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row, parent, false);
        ViewHolder holder = new ViewHolder(v);
        /**
         * If the activity is MessagesBySenderActivity, then hide the Sender Textview and the first seperator bar
         */
        if (((Activity) context).getClass().getName().equals(MessagesBySenderActivity.class.getName()))
        {
            holder.message_sender.setVisibility(View.GONE);
            holder.seperator_bar_one.setVisibility(View.GONE);
        }
        return holder;
    }

    /**
     * populate each item using ViewHolder and the message list
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        //If there is a search keyword, then highlight the matched substrings present in the sender and message text views
        if (searchString != null && !searchString.isEmpty())
        {
            String bodyString = list.get(position).getMessageBody();
            holder.message_body.setText(highlightText(bodyString, searchString));
            String senderString = list.get(position).getMessageSender();
            holder.message_sender.setText(highlightText(senderString, searchString));
        } else
        {
            holder.message_sender.setText(list.get(position).getMessageSender());
            holder.message_body.setText(list.get(position).getMessageBody());
        }
        Date date = new Date(Long.parseLong(list.get(position).getMessageDate()));
        holder.message_date.setText(date.toString());
    }

    //returns the number of elements the RecyclerView will display
    @Override
    public int getItemCount() { return list.size(); }

    //Called by RecyclerView when it starts observing this Adapter.
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
        //notify if the data that the RecyclerView displays is changed
        notifyDataSetChanged();
    }

    //Sets the ItemClickListener
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener)
    {
        this.mItemClickListener = mItemClickListener;
    }


    //Interface to provide the capability to listen to touch event
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //Returns a Spannable that highlights the selected text by changing its background color
    private Spannable highlightText(String originalString, String searchString)
    {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(originalString);
        Pattern p = Pattern.compile(searchString, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(originalString);
        while (m.find())
        {
            spannableString.setSpan(new BackgroundColorSpan(context.getResources().getColor(R.color.colorHighlightYellow)), m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannableString;
    }

    /**
     * ViewHolder class that stores the references the views in the RecyclerVeiw items
     * Also implement the OnClickListener interface of Views that allows to listen to touch events
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView message_sender;
        TextView message_date;
        TextView message_body;
        View seperator_bar_one;

        ViewHolder(View itemView)
        {
            super(itemView);
            message_sender = (TextView) itemView.findViewById(R.id.message_title);
            message_date = (TextView) itemView.findViewById(R.id.message_date);
            message_body = (TextView) itemView.findViewById(R.id.message_body);
            seperator_bar_one = itemView.findViewById(R.id.seperator_bar_one);
            itemView.setOnClickListener(this);
        }

        //Action to take when item touched
        @Override
        public void onClick(View v)
        {
            if (mItemClickListener != null)
            {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
