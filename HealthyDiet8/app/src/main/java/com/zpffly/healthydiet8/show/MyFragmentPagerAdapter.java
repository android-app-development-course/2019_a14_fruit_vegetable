package com.zpffly.healthydiet8.show;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    JSONObject jsonObj;
    JSONArray jsonarry;
    public MyFragmentPagerAdapter(FragmentManager fm,String json) throws JSONException {
        super(fm);
        jsonObj=new JSONObject(json);
        jsonarry=jsonObj.optJSONArray("newslist");
    }
    @Override
    public Fragment getItem(int position) {
        MyFragment fragment=new MyFragment(jsonarry.optJSONObject(position));
        return fragment;
    }
    @Override
    public int getCount() {
        return jsonarry.length();
    }
    //用来设置tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return jsonarry.optJSONObject(position).optString("cp_name");
    }
}
