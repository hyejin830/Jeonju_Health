package example.com.listframe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import example.com.api.bicycle_keep.BicycleKeepViewListFragment;
import example.com.api.bicycle_keep.BicycleKeepViewMapFragment;
import example.com.api.gym.GymViewListFragment;
import example.com.api.gym.GymViewMapFragment;
import example.com.api.heartattack.HeartAttackViewListFragment;
import example.com.api.heartattack.HeartAttackViewMapFragment;
import example.com.api.park.ParkViewListFragment;
import example.com.api.park.ParkViewMapFragment;
import example.com.api.pharm.PharmViewListFragment;
import example.com.api.pharm.PharmViewMapFragment;
import example.com.data.bicycle_rental.RentalBicycleViewListFragment;
import example.com.data.bicycle_rental.RentalBicycleViewMapFragment;
import example.com.data.bicycle_sell.BicycleSellViewListFragment;
import example.com.data.bicycle_sell.BicycleSellViewMapFragment;
import example.com.data.demen_center.DemencenterViewListFragment;
import example.com.data.demen_center.DemencenterViewMapFragment;
import example.com.data.healthup_center.HealthUpViewListFragment;
import example.com.data.healthup_center.HealthUpViewMapFragment;
import example.com.data.rest_center.RestcenterViewListFragment;
import example.com.data.rest_center.RestcenterViewMapFragment;
import example.com.data.water.WaterViewListFragment;
import example.com.data.water.WaterViewMapFragment;
import example.com.jeonjuhealth.R;

public class ListMainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private int[] mTabsIcons =
            {R.drawable.tab_list, R.drawable.tap_map};
    TextView mAppName;
    int code_type;

    final int PARK = 1;
    final int GYM = 2;
    final int METER = 3;
    final int RENTBC = 4;
    final int KEEPBC = 5;
    final int SELLBC = 6;
    final int WATER = 7;
    final int HOSPITAL = 8;
    final int MEDI = 9;
    final int HELATHUP = 10;
    final int HEARTATTACK = 11;
    final int NOSMOKING = 12;
    final int SHELTER = 13;
    final int DEMENCENTER = 14;
    final int DEMENFREE = 15;
    final int MEDISALE = 16;
    final int COURSEBC = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_main);

        Intent intent = getIntent();
        code_type = intent.getIntExtra("code",0);

        ViewPager viewPager = (ViewPager) findViewById(R.id.list_view_pager);

        ListMainActivity.MyPagerAdapter pagerAdapter = new ListMainActivity.MyPagerAdapter(getSupportFragmentManager());
        if(viewPager != null){
            viewPager.setAdapter(pagerAdapter);
        }


        mTabLayout = (TabLayout) findViewById(R.id.list_tab_layout);
        mAppName = (TextView)findViewById(R.id.app_name);
        String app_name=null;

        if(code_type==PARK) app_name = "공원"; //공원
        else if(code_type==GYM) app_name = "체육관";//체육관
        else if(code_type==RENTBC) app_name = "자전거 대여소"; //자전거 대여소
        else if(code_type==KEEPBC) app_name = "자전거 보관소"; //자전거 보관소
        else if(code_type == SELLBC) app_name = "자전거 판매소";// 자전거 판매소
        //else if(code_type == COURSEBC) app_name ="자전거 코스"// 자전거 코스
        else if(code_type==WATER) app_name = "약수터"; //약수터

        else if(code_type==MEDI) app_name = "약국"; // 약국
        else if(code_type==HOSPITAL) app_name = "병원"; // 병원
        else if(code_type==HELATHUP) app_name = "건강증진센터"; //건강증진센터
        else if(code_type==HEARTATTACK)  app_name = "자동심장충격기";//심장충격기
        else if(code_type==SHELTER) app_name = "무더위 쉼터";//무더위 쉼터
        else if(code_type ==DEMENCENTER ) app_name = "치매 센터";//치매 센터

        mAppName.setText(app_name);

        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }
            mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000")); // 탭
            // 선택
            // 색
            // 설정
            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
            // FirebaseInstanceId.getInstance().getToken().toString()

        }

    }

    class ContentDownlad extends AsyncTask<String,Void,String>{
        ProgressDialog asyncDialog = new ProgressDialog(ListMainActivity.this);

        protected void onPreExecute(){
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중.....");
            asyncDialog.show();
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... strings) {
            for(int i=0;i<5;i++){
                asyncDialog.setProgress(i*30);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String abc = "Parsing OK!";

            return abc;
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            asyncDialog.dismiss();
            Toast.makeText(ListMainActivity.this,s,Toast.LENGTH_SHORT).show();
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT =2;

        private final String[] mTabsTitle =
                {"리스트","지도"};

        public MyPagerAdapter(FragmentManager fm){super(fm);}


        public View getTabView(int position){
            View view = LayoutInflater.from(ListMainActivity.this).inflate(R.layout.activity_custom_tab, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(mTabsTitle[position]);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            return view;
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    if(code_type==PARK) return ParkViewListFragment.newInstance(1); //공원
                    else if(code_type==GYM) return GymViewListFragment.newInstance(1); //체육관
                    else if(code_type==RENTBC) return RentalBicycleViewListFragment.newInstance(1); //자전거 대여소
                    else if(code_type==KEEPBC) return BicycleKeepViewListFragment.newInstance(1); //자전거 보관소
                    else if(code_type == SELLBC) return BicycleSellViewListFragment.newInstance(1);// 자전거 판매소
                    //else if(code_type == COURSEBC) // 자전거 코스
                    else if(code_type==WATER) return WaterViewListFragment.newInstance(1); //약수터

                    else if(code_type==MEDI) return PharmViewListFragment.newInstance(1,2); // 약국
                    else if(code_type==HOSPITAL) return PharmViewListFragment.newInstance(1,1); // 병원
                    else if(code_type==HELATHUP) return HealthUpViewListFragment.newInstance(1); //건강증진센터
                    else if(code_type==HEARTATTACK)  return HeartAttackViewListFragment.newInstance(1);//심장충격기
                    else if(code_type==SHELTER) return RestcenterViewListFragment.newInstance(1);//무더위 쉼터
                    else if(code_type ==DEMENCENTER ) return DemencenterViewListFragment.newInstance(1); //치매 센터

                case 1:
                    if(code_type==PARK) return ParkViewMapFragment.newInstance(2);
                    else if(code_type==GYM) return GymViewMapFragment.newInstance(2);//체육관
                    else if(code_type==RENTBC) return RentalBicycleViewMapFragment.newInstance(2); //자전거 대여소
                    else if(code_type==KEEPBC) return BicycleKeepViewMapFragment.newInstance(2); //자전거 보관소
                    else if(code_type == SELLBC) return BicycleSellViewMapFragment.newInstance(2); // 자전거 판매소
                    // else if(code_type == COURSEBC) // 자전거 코스
                    else if(code_type==WATER) return WaterViewMapFragment.newInstance(2);//약수터

                    else if(code_type==MEDI) return PharmViewMapFragment.newInstance(2,2);
                    else if(code_type==HOSPITAL) return PharmViewMapFragment.newInstance(2,1);
                    else if(code_type==HELATHUP) return HealthUpViewMapFragment.newInstance(2); //건강증진센터
                    else if(code_type==HEARTATTACK) return HeartAttackViewMapFragment.newInstance(2); //심장충격기
                    else if(code_type==SHELTER) return RestcenterViewMapFragment.newInstance(2);//무더위 쉼터
                    else if(code_type==DEMENCENTER) return DemencenterViewMapFragment.newInstance(2);
                    else return ParkViewListFragment.newInstance(2);
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }
    }
}

