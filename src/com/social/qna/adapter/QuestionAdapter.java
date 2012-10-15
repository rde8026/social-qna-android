package com.social.qna.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kinvey.KinveyUser;
import com.social.qna.R;
import com.social.qna.model.QuestionModel;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/15/12
 * Time: 1:49 PM
 */
public class QuestionAdapter extends ArrayAdapter<QuestionModel> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Context context;
    private int textViewId;
    private List<QuestionModel> items;

    public QuestionAdapter(Context context, int textViewId, List<QuestionModel> items) {
        super(context, textViewId, items);
        this.context = context;
        this.textViewId = textViewId;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionViewHolder viewHolder;
        QuestionModel model = this.items.get(position);

        if (convertView == null) {
            viewHolder = new QuestionViewHolder();
            convertView = LayoutInflater.from(context).inflate(this.textViewId, null, false);
            viewHolder.questionText = (TextView) convertView.findViewById(R.id.questionText);
            viewHolder.userInfo = (TextView) convertView.findViewById(R.id.userInfo);
            viewHolder.createdDate = (TextView) convertView.findViewById(R.id.dateCreated);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (QuestionViewHolder) convertView.getTag();
        }

        viewHolder.questionText.setText(model.getQuestionText());
        viewHolder.userInfo.setText(String.format(context.getResources().getString(R.string.created_by), model.getCreatedBy()));
        viewHolder.createdDate.setText(String.format(context.getResources().getString(R.string.create_at), model.getCreated()));
        return convertView;
    }

    static class QuestionViewHolder {
        private TextView questionText;
        private TextView userInfo;
        private TextView createdDate;
    }

}