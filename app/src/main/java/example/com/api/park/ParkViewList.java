package example.com.api.park;

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

public class ParkViewList extends AppCompatActivity  implements MapView.MapViewEventListener,MapView.POIItemEventListener{

    TextView name,codename,addr,tel,area;
    ViewGroup mapViewContainer;
    Button call;
    public static String Tel_call="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21 ){
            getWindow().setStatusBarColor(Color.parseColor("#32CD32"));
        }
        setContentView(R.layout.activity_view_list_park);

        Intent intent = getIntent();
        String dataid = intent.getStringExtra("id");

        name = (TextView)findViewById(R.id.name);
        codename = (TextView)findViewById(R.id.codename);
        addr = (TextView)findViewById(R.id.addr);
        tel = (TextView)findViewById(R.id.tel);
        area = (TextView)findViewById(R.id.size);
        call=(Button)findViewById(R.id.go_to_url);
        StrictMode.enableDefaults();

        boolean inlist = false,inName = false, inCodename = false, inAddr = false, inTel = false, inArea = false, inPosx = false, inPosy=false;

        String Name = null,Codename = null, Addr = null, Tel=null, Area=null, Posx=null, Posy = null;


        try{
            URL url = new URL("http://openapi.jeonju.go.kr/rest/park/getParkView?"
                    +"&ServiceKey="
                    +"DYYVCYMSOVBKOJJ"
                    +"&dataSid="+dataid
            );

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("dataTitle")){ //mapx 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if(parser.getName().equals("parkLoadAddr")){ //title 만나면 내용을 받을수 있게 하자
                            inAddr = true;
                        }
                        if(parser.getName().equals("parkGubun")){ //address 만나면 내용을 받을수 있게 하자
                            inCodename = true;
                        }
                        if(parser.getName().equals("parkTel")){ //mapy 만나면 내용을 받을수 있게 하자
                            inTel = true;
                        }
                        if(parser.getName().equals("parkArea")){ //mapy 만나면 내용을 받을수 있게 하자
                            inArea = true;
                        }
                        if(parser.getName().equals("posx")){ //mapy 만나면 내용을 받을수 있게 하자
                            inPosx = true;
                        }
                        if(parser.getName().equals("posy")) { //mapy 만나면 내용을 받을수 있게 하자
                            inPosy = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inName){ //isMapx이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            inName = false;
                        }
                        if(inAddr){ //isTitle이 true일 때 태그의 내용을 저장.
                            Addr = parser.getText();
                            inAddr = false;
                        }
                        if(inCodename){ //isAddress이 true일 때 태그의 내용을 저장.
                            Codename = parser.getText();
                            inCodename = false;
                        }
                        if(inTel){ //isMapy이 true일 때 태그의 내용을 저장.
                            Tel = parser.getText();
                            inTel = false;
                        }
                        if(inArea){ //isMapy이 true일 때 태그의 내용을 저장.
                            Area = parser.getText();
                            inArea = false;
                        }
                        if(inPosx){ //isMapy이 true일 때 태그의 내용을 저장.
                            Posx = parser.getText();
                            inPosx = false;
                        }
                        if(inPosy) { //isMapy이 true일 때 태그의 내용을 저장.
                            Posy = parser.getText();
                            inPosy = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("list")){
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
        codename.setText(Codename);
        addr.setText(Addr);
        tel.setText(Tel);
        area.setText(Area+"m2");

        MapView  mapView1 = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        MapPOIItem marker = new MapPOIItem();
        marker.setTag(0);
        marker.setItemName(Name);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(Posx),Double.parseDouble(Posy)));
        marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView1.addPOIItem(marker);
        mapView1.setZoomLevel(2,true);
        mapView1.setMapCenterPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(Posx),Double.parseDouble(Posy))),true);
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