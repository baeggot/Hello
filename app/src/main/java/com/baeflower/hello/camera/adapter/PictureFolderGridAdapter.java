package com.baeflower.hello.camera.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.baeflower.hello.R;
import com.baeflower.hello.camera.builtin.BuiltinCameraActivity;

import java.util.List;

/**
 * Created by sol on 2015-04-24.
 */
public class PictureFolderGridAdapter extends BaseAdapter{

    public static final String TAG = PictureFolderGridAdapter.class.getSimpleName();

    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private BuiltinCameraActivity mContext;

    private List<String> mFolderNameList;

    private String mDirToSave;


    static class ViewHolder {
        Button folderName;
    }

    public PictureFolderGridAdapter(BuiltinCameraActivity context, List<String> folderNameList){
        mContext = context;
        mFolderNameList = folderNameList;
    }

    @Override
    public int getCount() {
        return mFolderNameList.size();
    }

    @Override
    public String getItem(int position) {
        return mFolderNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null){
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.grid_item_picture_folder, null);
            Button folderName = (Button) view.findViewById(R.id.btn_picture_folder_name);

            viewHolder = new ViewHolder();
            viewHolder.folderName = folderName;

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String folderName = mFolderNameList.get(position);
        viewHolder.folderName.setText(folderName);

        final int finalPosition = position;

        viewHolder.folderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDirToSave = ((Button) v).getText().toString();
                Log.d(TAG, mDirToSave);
            }
        });

        viewHolder.folderName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // mContext.showFolderDeleteDialog(finalPosition);
                return false;
            }
        });

        return view;
    }


    public String getmDirToSave() {
        return mDirToSave;
    }
}
