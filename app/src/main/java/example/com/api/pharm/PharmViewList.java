package example.com.api.pharm;

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

import java.net.URL;

import example.com.jeonjuhealth.R;

public class PharmViewList extends AppCompatActivity implements MapView.MapViewEventListener,MapView.POIItemEventListener{

    TextView name,codename,mediaddr,tel,date,stime,etime,holiday,memo;
    ViewGroup mapViewContainer;
    Button call;
    public static String Tel_call="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21 ){
            getWindow().setStatusBarColor(Color.parseColor("#32CD32"));
        }
        setContentView(R.layout.activity_view_list_pharm);

        Intent intent = getIntent();
        String dataid = intent.getStringExtra("id");
        int mtype = intent.getIntExtra("type",1);

        name = (TextView)findViewById(R.id.name);
        codename = (TextView)findViewById(R.id.codename);
        mediaddr = (TextView)findViewById(R.id.addr);
        tel = (TextView)findViewById(R.id.tel);
        date = (TextView)findViewById(R.id.date);
        stime = (TextView)findViewById(R.id.stime);
        etime = (TextView)findViewById(R.id.etime);
        holiday = (TextView)findViewById(R.id.holiday);
        memo = (TextView)findViewById(R.id.memo);
        call=(Button)findViewById(R.id.go_to_url);
        StrictMode.enableDefaults();

        boolean inlist = false, inAddr = false, inConDate = false, inName= false, inStime = false, inSid=false;
        boolean inEtime = false, inTel = false, inMemo = false, inposx = false, inposy=false;

        String addr = null, ConDate = null, Name = null, Stime = null, Etime = null, Tel=null, Memo=null, posx=null;
        String posy = null, longi = null, statUpdateDatetime = null;


        try{
            URL url = new URL("http://openapi.jeonju.go.kr/rest/medicalnew/getMedicalList?"
                    +"&authApiKey="
                    +"mQKdK9LqJX%2F9Dy6KbM3pL0ecweGtcgKe6bZc%2BU9ndBMSuPiuDhrlNxSNgt07e72jYkdKtkrhESFjDeTqE7V3IQ%3D%3D"
                    +"&mediCdt="+mtype+"&mediSid="
                    + dataid
            );//검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("mediAddr")){ //title 만나면 내용을 받을수 있게 하자
                            inAddr = true;
                        }
                        if(parser.getName().equals("mediConDate")){ //address 만나면 내용을 받을수 있게 하자
                            inConDate = true;
                        }
                        if(parser.getName().equals("mediName")){ //mapx 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if(parser.getName().equals("mediStime")){ //mapy 만나면 내용을 받을수 있게 하자
                            inStime = true;
                        }
                        if(parser.getName().equals("mediEtime")){ //mapy 만나면 내용을 받을수 있게 하자
                            inEtime = true;
                        }
                        if(parser.getName().equals("mediTel")){ //mapy 만나면 내용을 받을수 있게 하자
                            inTel = true;
                        }
                        if(parser.getName().equals("memo")){ //mapy 만나면 내용을 받을수 있게 하자
                            inMemo = true;
                        }
                        if(parser.getName().equals("posx")){ //mapy 만나면 내용을 받을수 있게 하자
                            inposx = true;
                        }
                        if(parser.getName().equals("posy")) { //mapy 만나면 내용을 받을수 있게 하자
                            inposy = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            //status1.setText(status1.getText()+"에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inAddr){ //isTitle이 true일 때 태그의 내용을 저장.
                            addr = parser.getText();
                            inAddr = false;
                        }
                        if(inConDate){ //isAddress이 true일 때 태그의 내용을 저장.
                            ConDate = parser.getText();
                            inConDate = false;
                        }
                        if(inName){ //isMapx이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            inName = false;
                        }
                        if(inStime){ //isMapy이 true일 때 태그의 내용을 저장.
                            Stime = parser.getText();
                            inStime = false;
                        }
                        if(inEtime){ //isMapy이 true일 때 태그의 내용을 저장.
                            Etime = parser.getText();
                            inEtime = false;
                        }
                        if(inTel){ //isMapy이 true일 때 태그의 내용을 저장.
                            Tel = parser.getText();
                            inTel = false;
                        }
                        if(inMemo){ //isMapy이 true일 때 태그의 내용을 저장.
                            Memo = parser.getText();
                            inMemo = false;
                        }
                        if(inposx){ //isMapy이 true일 때 태그의 내용을 저장.
                            posx = parser.getText();
                            inposx = false;
                        }
                        if(inposy) { //isMapy이 true일 때 태그의 내용을 저장.
                            posy = parser.getText();
                            inposy = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("list")){
                          /*  status1.setText(status1.getText()+"주소 : "+ addr +"\n 진료 시간: "+ ConDate +"\n 약국이름 : " + Name
                                    +"\n 시작 시간 : " + Stime +  "\n 끝나는 시간 : " + Etime+ "\n 전화번호 : " + Tel
                                    +"\n 메모 : " +Memo + "\n 경도 : " + posx + "\n 위도 : " +posy +"\n");
                            */
                            inlist = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            //status1.setText("에러가..났습니다...");
        }

        name.setText(Name);
        mediaddr.setText(addr);
        tel.setText(Tel);
        stime.setText(Stime);
        etime.setText(Etime);
        memo.setText(Memo);


        MapView  mapView1 = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        MapPOIItem marker = new MapPOIItem();
        marker.setTag(0);
        marker.setItemName(Name);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(posy),Double.parseDouble(posx)));
        marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView1.addPOIItem(marker);
        mapView1.setZoomLevel(2,true);
        mapView1.setMapCenterPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(posy),Double.parseDouble(posx))),true);
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

