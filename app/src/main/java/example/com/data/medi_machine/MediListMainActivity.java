package example.com.data.medi_machine;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

import example.com.jeonjuhealth.R;

public class MediListMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21 ){
            getWindow().setStatusBarColor(Color.parseColor("#32CD32"));
        }
        setContentView(R.layout.activity_list);

        StrictMode.enableDefaults();
        int count_data=0;

        boolean inlist = false, inAddr = false, inName= false, inTel = false;

        String addr = null, Name = null, Tel=null;

        ArrayList<String> items = new ArrayList<String>();
        ArrayList<String> addr_item = new ArrayList<String>();
        ArrayList<String> tel_item = new ArrayList<String>();

        try{
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            InputStream is = getResources().openRawResource(R.raw.medi_machine);
            parser.setInput(is, null) ;

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("상호")){ //title 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if(parser.getName().equals("주소")){ //title 만나면 내용을 받을수 있게 하자
                            inAddr = true;
                        }
                        if(parser.getName().equals("전화번호")){ //title 만나면 내용을 받을수 있게 하자
                            inTel = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inName){ //isTitle이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            items.add(Name);
                            count_data++;
                            inName = false;
                        }
                        if(inAddr){ //isTitle이 true일 때 태그의 내용을 저장.
                            addr = parser.getText();
                            addr_item.add(addr);
                            inAddr = false;
                        }
                        if(inTel){ //isTitle이 true일 때 태그의 내용을 저장.
                            Tel = parser.getText();
                            tel_item.add(Tel);
                            inTel = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("Row")){
                            inlist = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            //status1.setText("에러가..났습니다...");
        }

        final MediListViewAdapter adapter = new MediListViewAdapter();
        final ListView listView1 = (ListView)findViewById(R.id.listview);
        listView1.setAdapter(adapter);
        for(int i=0;i<count_data;i++){
            adapter.addItem(tel_item.get(i),items.get(i),addr_item.get(i));
        }




    }
}

