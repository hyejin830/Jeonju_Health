package example.com.jeonjuhealth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import android.support.design.widget.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_STORAGE = 1111;

    private TabLayout mTabLayout;
    private int[] mTabsIcons =
            {R.drawable.tab_home, R.drawable.tab_excercise, R.drawable.tab_health};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        try {
            PackageInfo info = getPackageManager().getPackageInfo("example.com.jeonjuhealth", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        if (viewPager != null)
            viewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
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



        boolean isGrantStorage = grantExternalStoragePermission();
        if (isGrantStorage) {

        }

}

    private boolean grantExternalStoragePermission() {
        for(int i=0;i<2;i++) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE}, 1);
                    return false;
                }
            }
            else {
                Toast.makeText(this, "External Storage Permission is Grant", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return true;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 3;

        private final String[] mTabsTitle =
                {"홈", "운동", "건강"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        public View getTabView(int position) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_custom_tab, null);

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
                    return HomeFragment.newInstance(1); // 메인 페이지 MainFragment.java
                case 1:
                    return ExerciseFragment.newInstance(2);
                case 2:
                    return FitnessFragment.newInstance(3);
                /*case 3:
                    return HomeFragment.newInstance(4);*/

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
