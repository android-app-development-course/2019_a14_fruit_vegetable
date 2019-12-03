package com.zpffly.healthydiet8.show;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.zpffly.healthydiet8.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Acticity_show extends AppCompatActivity {

    private ViewPager mViewpager;
    private TabLayout mTabLayout;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private Window window;
    private CollapsingToolbarLayout toolbarlayout;
    private Toolbar toolbar;
    private String title;
    private String caipu;
    static int iconflag=0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent thisintent=getIntent();
        title=thisintent.getStringExtra("title");
        caipu=thisintent.getStringExtra("caipu");



        caipu="{\n" +
                "\"code\":200,\n" +
                "\"msg\":\"success\",\n" +
                "\"newslist\":[\n" +
                "{\n" +
                "\"id\":4836,\n" +
                "\"type_id\":439,\n" +
                "\"type_name\":\"凉菜类\",\n" +
                "\"cp_name\":\"蓑衣黄瓜\",\n" +
                "\"zuofa\":\"1.将黄瓜洗净，切成蓑衣花刀，用盐腌10分钟，再用清水冲洗后沥干水分装盘；2.将香菇、胡萝卜、冬笋、葱、姜洗净切丝；3.锅内放油，油烧至六成热时放入葱丝、姜丝，炒出香味后再倒入香菇丝、胡萝卜丝、冬笋丝翻炒，加入白糖、醋、精盐、味精，烧开；4.将糖醋汁放凉后倒入装黄瓜的盘中，浸泡几小时后即可食用。\",\n" +
                "\"texing\":\"清淡爽口，酸甜稍辣。\",\n" +
                "\"tishi\":\"必须等糖醋汁凉透后再浸泡黄瓜，这样黄瓜会更脆爽。\",\n" +
                "\"tiaoliao\":\"食用油30克；香醋1小匙(3克)；精盐3小匙(9克)；白糖1/2小匙(1.5克)；味精1/2小匙(1.5克)\",\n" +
                "\"yuanliao\":\"黄瓜250克；香菇25克；胡萝卜25克；冬笋25克；大葱1根；生姜1小块\"\n" +
                "},\n" +
                "{\n" +
                "\"id\":4649,\n" +
                "\"type_id\":429,\n" +
                "\"type_name\":\"第二道菜\",\n" +
                "\"cp_name\":\"炝黄瓜衣\",\n" +
                "\"zuofa\":\"1.用清水将黄瓜洗净，切成2寸长的段，片下黄瓜皮，卷成卷，放入盆中，撒些精盐，腌10分钟，捞出，挤出水，码在盆中。2.用刀将姜刮去皮，洗净，切成细丝，放在黄瓜皮上。3.锅中注入适量清水，加入醋精、白糖，上火熬化后晾凉，倒在黄瓜皮上，再放入桂花酱，用盘子盖上腌十小时。4.将黄瓜皮卷取出，切成小段，断面朝上，码在盘中即可食用。\",\n" +
                "\"texing\":\"\",\n" +
                "\"tishi\":\"\",\n" +
                "\"tiaoliao\":\"精盐1克，醋精20克，白糖50克，桂花酱10克，姜15克。\",\n" +
                "\"yuanliao\":\"黄瓜750克(约6条)。\"\n" +
                "},\n" +
                "{\n" +
                "\"id\":4630,\n" +
                "\"type_id\":429,\n" +
                "\"type_name\":\"第二道菜\",\n" +
                "\"cp_name\":\"炒黄瓜酱\",\n" +
                "\"zuofa\":\"1.用清水将黄瓜洗净，顺长切成4条，片去黄瓜子，切成3分见方的丁，放入碗中，加入少许精盐，拌匀，腌3分钟，滗去水。2.用刀将猪肉片成3分厚的大片，剞上十字花刀，切成3分见方的丁。3.坐煸锅，注入熟猪油，烧至六成热，放入肉丁煸炒；待肉丁内的水分炒出来时，锅内响声加大，随即放入葱姜末、黄酱继续煸炒；待黄酱裹匀肉丁并散发出酱香味时，加入料酒、精盐继续煸炒均匀，再加入黄瓜丁，淋上香油翻炒均匀，即可出锅。\",\n" +
                "\"texing\":\"\",\n" +
                "\"tishi\":\"\",\n" +
                "\"tiaoliao\":\"料酒10克，精盐1克，黄酱10克，葱姜末各少许，熟猪油15克，香油5克。\",\n" +
                "\"yuanliao\":\"主料：瘦猪肉150克。配料：嫩黄瓜1条(约100克)。\"\n" +
                "}\n" +
                "]\n" +
                "}";


        //获取view对象
        mViewpager =findViewById(R.id.myViewPager);
        mTabLayout=findViewById(R.id.tab_layout);
        toolbarlayout=findViewById(R.id.collapsing_toolbar_layout);
        toolbar=findViewById(R.id.myToolbar);


        //设置stausbar和toolbar颜色
        window=getWindow();
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.example_melon);//获取图片
        paletteBitmap(bmp);//获取图片主色

        //设置toolbarlayout的Title
        setName(title);



        //绑定适配器
        try {
            myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),caipu);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mViewpager.setAdapter(myFragmentPagerAdapter);
        //将TabLayout和ViewPager绑定在一起，相互影响，解放了开发人员对双方变动事件的监听
        mTabLayout.setupWithViewPager(mViewpager);


        //设置toolbar
        setSupportActionBar(toolbar);


    }


    private void paletteBitmap(@NonNull Bitmap bitmap) {
        Palette.from(bitmap)//创建Palette.Builder
                .generate(new Palette.PaletteAsyncListener() {//异步抽取图片色调方法
                    @Override
                    public void onGenerated(@NonNull Palette palette) {
                        //获取到充满活力的这种色调
                        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                        if (vibrantSwatch != null) {
                            //设置toolbar颜色
                            toolbarlayout.setContentScrimColor(vibrantSwatch.getRgb());
                            //设置状态栏的颜色
                            window.setStatusBarColor(vibrantSwatch.getRgb());
                            Log.e("Zorro", "===vibrantSwatch=1=" + vibrantSwatch.getRgb());
                        }
                        //获取充满活力的黑
                        Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                        if (darkVibrantSwatch != null) {


                            Log.e("Zorro", "===darkVibrantSwatch=2=" + darkVibrantSwatch.getRgb());
                        }
                        //获取充满活力的亮
                        Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
                        if (lightVibrantSwatch != null) {
                            Log.e("Zorro", "===lightVibrantSwatch=3=" + lightVibrantSwatch.getRgb());
                        }
                        //获取柔和的色调
                        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                        if (mutedSwatch != null) {
                            Log.e("Zorro", "===mutedSwatch=4=" + mutedSwatch.getRgb());
                        }
                        //获取柔和的黑
                        Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                        if (darkMutedSwatch != null) {
                            Log.e("Zorro", "===darkMutedSwatch=5=" + darkMutedSwatch.getRgb());
                        }
                        //获取柔和的亮
                        Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();
                        if (lightMutedSwatch != null) {

                            //设置tab的颜色
                            mTabLayout.setBackgroundColor(lightMutedSwatch.getRgb());
                            Log.e("Zorro", "===lightMutedSwatch=6=" + lightMutedSwatch.getRgb());
                        }
                    }
                });
    }

    private void setName(String str)
    {
        try {
            JSONObject jsonobj=new JSONObject(str);
            JSONArray jsonarry=jsonobj.optJSONArray("result");
            toolbarlayout.setTitle(jsonarry.optJSONObject(0).optString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show,menu);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_like:
                if(iconflag==0){
                    item.setIcon(R.drawable.icon_like_red);
                    iconflag=1;
                    Toast.makeText(this,"Like",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    item.setIcon(R.drawable.icon_like_empty);
                    iconflag=0;
                    Toast.makeText(this,"Dislike",Toast.LENGTH_SHORT).show();
                }

                break;
            case android.R.id.home:
                Toast.makeText(this,"back",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return true;
    }
}

