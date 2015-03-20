package com.baeflower.hello.listview.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baeflower.hello.R;

import java.util.List;

/**
 * Created by sol on 2015-03-19.
 */
public class ActivityNameAdapter<ActivityInfo> extends ArrayAdapter<ActivityInfo>{

    // 생성자
    public ActivityNameAdapter(Context context, int resource, List<ActivityInfo> objects) {
        super(context, resource, objects);
    }

    // ViewHolder 패턴
    static class ViewHolder {
        TextView labelText;
    }

    private LayoutInflater inflater;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        View view = convertView;

        if (view == null){
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder();
            TextView textView = (TextView) view.findViewById(R.id.list_item_tv);
            viewHolder.labelText = textView;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        ActivityInfo activityInfo = getItem(position);
        String activityInfoStr = activityInfo.toString();

        if (!TextUtils.isEmpty(activityInfoStr)) {
            String[] activityNameSplited = activityInfoStr.split("[.]");
            String activityName = activityNameSplited[activityNameSplited.length - 1];

            activityName = activityName.substring(0, activityName.length() - 1);

            viewHolder.labelText.setText(activityName);
        }

        return view;
    }

}
