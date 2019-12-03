package com.zpffly.healthydiet8.show;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.zpffly.healthydiet8.R;

import org.json.JSONObject;


public class MyFragment extends Fragment {
    private JSONObject jsonObject;
    private TextView cp_name;
    private TextView yuanliao;
    private TextView tiaoliao;
    private TextView tips;
    private TextView zuofa;

    public MyFragment(JSONObject json){
        super();
        jsonObject=json;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_item, container, false);
        cp_name=view.findViewById(R.id.cp_name);
        yuanliao=view.findViewById(R.id.yuanliao);
        tiaoliao=view.findViewById(R.id.tiaoliao);
        tips=view.findViewById(R.id.tips);
        zuofa=view.findViewById(R.id.zuofa);

        cp_name.setText(jsonObject.optString("cp_name"));
        yuanliao.setText("原料：\n"+jsonObject.optString("yuanliao"));
        tiaoliao.setText("调料：\n"+jsonObject.optString("tiaoliao"));
        tips.setText("提示：\n"+jsonObject.optString("tishi"));
        zuofa.setText("做法：\n"+jsonObject.optString("zuofa"));
        return view;
    }
}