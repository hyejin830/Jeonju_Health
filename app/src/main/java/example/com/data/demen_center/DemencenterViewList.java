package example.com.data.demen_center;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

import example.com.jeonjuhealth.R;

public class DemencenterViewList extends AppCompatActivity implements MapView.MapViewEventListener,MapView.POIItemEventListener{

    TextView name,codename,addr,tel,ceo,program;
    ViewGroup mapViewContainer;
    Button call;
    public static String Tel_call="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21 ){
            getWindow().setStatusBarColor(Color.parseColor("#32CD32"));
        }
        setContentView(R.layout.activity_view_list_demen_center);

        Intent intent = getIntent();
        String demen_name = intent.getStringExtra("name");
        System.out.println("넘어온값 "+demen_name);

        name = (TextView)findViewById(R.id.name);
        codename = (TextView)findViewById(R.id.codename);
        addr = (TextView)findViewById(R.id.addr);
        tel = (TextView)findViewById(R.id.tel);
        ceo = (TextView)findViewById(R.id.ceo);
        program = (TextView)findViewById(R.id.program);
        call=(Button)findViewById(R.id.go_to_url);
        StrictMode.enableDefaults();

        boolean inlist = false,incodename = false, inAddr = false, inCeo = false, inName= false, inProgram = false;
        boolean inTel = false, inposx = false, inposy=false;

        String Addr = null, Ceo = null, Name = null, Program = null, Tel=null, posx=null;
        String posy = null, Codename = null;


        try{

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            InputStream is = getResources().openRawResource(R.raw.dementia_center);
            parser.setInput(is, null) ;

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            int flag=0;
            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("치매센터명")){ //title 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if(parser.getName().equals("치매센터유형")){ //title 만나면 내용을 받을수 있게 하자
                            incodename = true;
                        }
                        if(parser.getName().equals("소재지지번주소")){ //title 만나면 내용을 받을수 있게 하자
                            inAddr = true;
                        }
                        if(parser.getName().equals("운영기관전화번호")){ //title 만나면 내용을 받을수 있게 하자
                            inTel = true;
                        }
                        if(parser.getName().equals("운영기관대표자명")){ //title 만나면 내용을 받을수 있게 하자
                            inCeo = true;
                        }
                        if(parser.getName().equals("주요치매관리프로그램소개")){ //title 만나면 내용을 받을수 있게 하자
                            inProgram = true;
                        }
                        if(parser.getName().equals("위도")){ //title 만나면 내용을 받을수 있게 하자
                            inposx = true;
                        }
                        if(parser.getName().equals("경도")){ //title 만나면 내용을 받을수 있게 하자
                            inposy = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inName){ //isTitle이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            if(Name.equals(demen_name)) {
                                flag = 1;
                            }
                            inName = false;
                        }
                        if(incodename){ //isTitle이 true일 때 태그의 내용을 저장.
                            Codename = parser.getText();
                            incodename = false;
                        }
                        if(inAddr){ //isTitle이 true일 때 태그의 내용을 저장.
                            Addr = parser.getText();
                            inAddr = false;
                        }
                        if(inTel){ //isTitle이 true일 때 태그의 내용을 저장.
                            Tel = parser.getText();
                            inTel = false;
                        }
                        if(inCeo){ //isTitle이 true일 때 태그의 내용을 저장.
                            Ceo = parser.getText();
                            inCeo = false;
                        }
                        if(inProgram){ //isTitle이 true일 때 태그의 내용을 저장.
                            Program = parser.getText();
                            if(flag==1) flag=2;
                            inProgram = false;
                        }
                        if(inposx){ //isTitle이 true일 때 태그의 내용을 저장.
                            posx = parser.getText();
                            inposx = false;
                        }
                        if(inposy){ //isTitle이 true일 때 태그의 내용을 저장.
                            posy = parser.getText();
                            inposy = false;
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("Row")){
                            //status1.setText(status1.getText()+"주소 : "+ Addr +"\n 센터명: "+ Name +"\n");
                            inlist = false;
                        }
                        break;
                }
                if(flag==2) break;
                parserEvent = parser.next();
            }
        } catch(Exception e){
            //status1.setText("에러가..났습니다...");
        }

        name.setText(Name);
        codename.setText(Codename);
        addr.setText(Addr);
        tel.setText(Tel);
        ceo.setText(Ceo);
        program.setText(Program);

        MapView  mapView1 = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        MapPOIItem marker = new MapPOIItem();
        marker.setTag(0);
        marker.setItemName(Name);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(posx),Double.parseDouble(posy)));
        marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView1.addPOIItem(marker);
        mapView1.setZoomLevel(2,true);
        mapView1.setMapCenterPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(posx),Double.parseDouble(posy))),true);
        mapViewContainer.addView(mapView1);
        Tel_call="tel:"+Tel;
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse((Tel_call)));
                startActivity(intent);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        mapViewContainer.removeAllViews();

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}

