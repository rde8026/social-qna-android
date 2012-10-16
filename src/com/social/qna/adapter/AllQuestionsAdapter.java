package com.social.qna.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.social.qna.R;
import com.social.qna.model.QuestionModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/16/12
 * Time: 2:15 PM
 */
public class AllQuestionsAdapter extends ArrayAdapter<QuestionModel> {

    private static final String TAG = AllQuestionsAdapter.class.getSimpleName();

    private Context context;
    private int textViewId;
    private List<QuestionModel> items;

    public AllQuestionsAdapter(Context context, int textViewId, List<QuestionModel> items) {
        super(context, textViewId, items);
        this.context = context;
        this.textViewId = textViewId;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionModel model = this.items.get(position);
        AllQuestionViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(this.context).inflate(this.textViewId, null, false);
            viewHolder = new AllQuestionViewHolder();
            viewHolder.questionText = (TextView) convertView.findViewById(R.id.questionText);
            viewHolder.creator = (TextView) convertView.findViewById(R.id.userInfo);
            viewHolder.createdAt = (TextView) convertView.findViewById(R.id.dateCreated);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (AllQuestionViewHolder) convertView.getTag();
        }

        viewHolder.questionText.setText(model.getQuestionText());
        viewHolder.creator.setText(model.getCreatedBy());
        viewHolder.createdAt.setText(model.getCreated());

        return convertView;
    }

    static class AllQuestionViewHolder {
        TextView questionText;
        TextView creator;
        TextView createdAt;
    }

}
