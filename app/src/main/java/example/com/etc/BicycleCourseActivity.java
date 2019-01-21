package example.com.etc;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import example.com.jeonjuhealth.R;

public class BicycleCourseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21 ){
            getWindow().setStatusBarColor(Color.parseColor("#32CD32"));
        }
        setContentView(R.layout.activity_bicycle_course);
    }
}
