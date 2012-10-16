package com.social.qna.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.social.qna.R;
import com.social.qna.fragments.callbacks.RemoveQuestionsCallback;
import com.social.qna.model.QuestionModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/15/12
 * Time: 1:49 PM
 */
public class QuestionAdapter extends ArrayAdapter<QuestionModel> implements CompoundButton.OnCheckedChangeListener {

    public SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    private Context context;
    private int textViewId;
    private List<QuestionModel> items;
    private RemoveQuestionsCallback callback;

    public QuestionAdapter(Context context, int textViewId, List<QuestionModel> items, RemoveQuestionsCallback callback) {
        super(context, textViewId, items);
        this.context = context;
        this.textViewId = textViewId;
        this.items = items;
        this.callback = callback;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionViewHolder viewHolder;
        QuestionModel model = this.items.get(position);

        if (convertView == null) {
            viewHolder = new QuestionViewHolder();
            convertView = LayoutInflater.from(context).inflate(this.textViewId, null, false);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            viewHolder.questionText = (TextView) convertView.findViewById(R.id.questionText);
            viewHolder.userInfo = (TextView) convertView.findViewById(R.id.userInfo);
            viewHolder.createdDate = (TextView) convertView.findViewById(R.id.dateCreated);
            viewHolder.checkBox.setTag(position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (QuestionViewHolder) convertView.getTag();
            viewHolder.checkBox.setTag(position);
        }

        viewHolder.questionText.setText(model.getQuestionText());
        viewHolder.userInfo.setText(String.format(context.getResources().getString(R.string.created_by), model.getCreatedBy()));
        viewHolder.createdDate.setText(String.format(context.getResources().getString(R.string.create_at), model.getCreated()));

        viewHolder.checkBox.setChecked(sparseBooleanArray.get(position, false));
        viewHolder.checkBox.setOnCheckedChangeListener(this);

        return convertView;
    }

    static class QuestionViewHolder {
        private CheckBox checkBox;
        private TextView questionText;
        private TextView userInfo;
        private TextView createdDate;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        sparseBooleanArray.put((Integer)compoundButton.getTag(), b);
        LinearLayout ll = (LinearLayout) compoundButton.getParent();
        int[] diems = getDims(ll);
        if (b) {
            this.callback.itemSelected(sparseBooleanArray);
            ll.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.list_pressed_holo_light));
            ll.setPadding(diems[0], diems[1], diems[2], diems[3]);
        } else {
            this.callback.itemDeSelected(sparseBooleanArray);
            ll.setBackgroundDrawable(null);
            ll.setPadding(diems[0], diems[1], diems[2], diems[3]);
        }
    }

    public boolean isChecked(int position) {
        return sparseBooleanArray.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        sparseBooleanArray.put(position, isChecked);
        notifyDataSetChanged();
    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));
    }

    public SparseBooleanArray getSparseBooleanArray() {
        return sparseBooleanArray;
    }

    private int[] getDims(LinearLayout ll) {
        int[] diems = new int[4];
        diems[0] = ll.getPaddingLeft();
        diems[1] = ll.getPaddingTop();
        diems[2] = ll.getPaddingRight();
        diems[3] = ll.getPaddingBottom();
        return diems;
    }

}