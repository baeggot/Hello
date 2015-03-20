package com.baeflower.hello.listview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baeflower.hello.R;

import java.util.List;

/**
 * Created by sol on 2015-03-19.
 */
/*
    뷰 하나에 대한 수정은 여기서!
 */
public class CustomAdapter<String> extends ArrayAdapter<String> {
    // alt + enter

    /*
        보일때마다 계속 getView를 호출해서 그 안에 view를 만드는 작업을 한다(getView에서 데이터 넣고 레이아웃 세팅하고~)
        사실 메모리상에 다 올라가있기 때문에 매번 레이아웃 로드하고 데이터 세팅 할 필요가 없음

        ViewHolder 패턴 : 한번 load된 애를 캐싱해 놓고 쓴다(속도가 월등히 좋다)
                            static !!
     */
    static class ViewHolder {
        TextView labelText;
    }

    // layoutInflater : 어떤 특정한 상황에서 특정한 layout을 붙이는 작업을 코드로 하는 것
    // 우리가 만든 layout을 쓰기 위해서 쓴다
    // layout을 가져오기 위한 객체
    private LayoutInflater inflater;


    public CustomAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    /*
        listView에 adapter를 붙여놓으면 ,, View 뿌릴 때 getView(콜백 메서드) 알아서 부름(?)

        getView : view를 실제로 만들어주는 것, 한칸한칸마다 getView가 실행됨
                    몇번째인지를 알아야 그 자리에 맞는 view를 뿌려주겠지
                    view에서 화면이 갱신될때마다 (새로운 item이 보여져야 하는 상황)
                    adapter에서 position으로 Data 찾아와서 view에 뿌림

        스크롤을 빨리하면, view를 순서대로 보여주는게 아니고 스레드로 처리하기 때문에
        중간에 빠지기도 하고 뭐 그렇다네

        convertView : 데이터가 붙어야 하는 view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("CustomAdapter", "position : " + position);

        ViewHolder viewHolder;
        View view = convertView;


        // View 를 재사용하기 위한 처리
        // view가 처음에 불릴 때는 null로 들어옴
        // View를 처음 로딩하고 Data를 처음 세팅할 때
        if (view == null){
            inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // resource : 붙일 리소스, root : 이놈의 부모에 해당하는 놈을 넣어주는 것
            //
            view = inflater.inflate(R.layout.listview_item, null);

            // inflater를 통해서 레이아웃을 붙이고 그 레이아웃 안에서(view) 나오는 것(?)
            TextView textView = (TextView) view.findViewById(R.id.list_item_tv);
            viewHolder = new ViewHolder();
            viewHolder.labelText = textView;

            // 모든 뷰에는 tag를 달 수가 있다(object를 저장할 수 있음)
            // 분류를 나누거나  뭐... 알아서 잘 쓰면 됨
            // 나중에 이걸 까서 viewHolder를 보고 데이터를 가져올것?!
            view.setTag(viewHolder);

        } else {// 한번 호출된 애는 null이 아님
            // View, Data를 재사용
            viewHolder = (ViewHolder) view.getTag();
        }

        // adapter의 getItem 메소드 부름(이 클래스가 adapter기 때문에 그냥 불림)
        // object가 넘어오는데... 뭐지? 바로 이상함 캐스팅이 안됨
        // position 위치의 데이터를 취득
        String name = getItem(position);

        // 처음 실행될 때 convertView는 null
        // 한번 불렸던 view는 convertView로 들어오는데 Data는 새로 set 해줘야된다
        if (!TextUtils.isEmpty(name.toString())) {
            viewHolder.labelText.setText(name.toString());
        }

        if (position % 2 == 0) {
            viewHolder.labelText.setBackgroundColor(Color.parseColor("#d98328"));
        } else {
            viewHolder.labelText.setBackgroundColor(Color.parseColor("#80bd01"));
        }

        // 애니메이션 적용
        // adapter 안에는 getApplicationContext 말고 getContext가 있음
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.translation);
        view.startAnimation(animation);

        // return super.getView(position, convertView, parent);
        // 완성된 View를 리턴
        return view;
    }

//    @Override
//    public String getItem(int position) {
//        return super.getItem(position);
//    }
}
