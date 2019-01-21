package example.com.api.bicycle_keep;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class BicycleKeepViewList extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener {

    TextView name, codename, addr, tel, fee, feebigo, time, bicycle_type, guplusdong;
    ViewGroup mapViewContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= 21 ){
            getWindow().setStatusBarColor(Color.parseColor("#32CD32"));
        }

        setContentView(R.layout.activity_view_list_keep_bicycle);

        Intent intent = getIntent();
        String dataid = intent.getStringExtra("id");

        Log.e("datanum", String.valueOf(dataid));

        name = (TextView) findViewById(R.id.name);
        tel = (TextView) findViewById(R.id.tel);
        bicycle_type = (TextView) findViewById(R.id.bicycle_type);
        guplusdong = (TextView) findViewById(R.id.guplusdong);

        StrictMode.enableDefaults();

        boolean inlist = false, inName = false, inAddr = false, inTel = false, inSid = false;
        boolean inPosx = false, inPosy = false, inType = false, inGu = false, inDong = false, inSeq = false;

        String Name = null, Id = null, Addr = null, Tel = null, Type = null, Gu = null, Dong = null, Posx = null, Posy = null;
        String Alltime = "";

        try {
            URL url = new URL("http://openapi.jeonju.go.kr/rest/bicycleracks/getBicycleRacks?"
                    + "&authApiKey="
                    + "DYYVCYMSOVBKOJJ"
                    + "&pageSize=190"
            );//검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");
            int index = 0;
            int flag = 0;
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("addr")) { //mapx 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if (parser.getName().equals("bicType")) { //mapx 만나면 내용을 받을수 있게 하자
                            inType = true;
                        }
                        if (parser.getName().equals("centerNm")) { //mapx 만나면 내용을 받을수 있게 하자
                            inDong = true;
                        }
                        if (parser.getName().equals("gu")) { //mapx 만나면 내용을 받을수 있게 하자
                            inGu = true;
                        }
                        if (parser.getName().equals("posx")) { //mapx 만나면 내용을 받을수 있게 하자
                            inPosx = true;
                        }
                        if (parser.getName().equals("posy")) { //mapx 만나면 내용을 받을수 있게 하자
                            inPosy = true;
                        }
                        if (parser.getName().equals("seq")) { //mapx 만나면 내용을 받을수 있게 하자
                            inSeq = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때

                        if (inName) { //isMapx이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            inName = false;
                        }
                        if (inType) { //isMapx이 true일 때 태그의 내용을 저장.
                            Type = parser.getText();
                            inType = false;
                        }
                        if (inDong) { //isMapx이 true일 때 태그의 내용을 저장.
                            Dong = parser.getText();
                            inDong = false;
                        }
                        if (inGu) { //isMapx이 true일 때 태그의 내용을 저장.
                            Gu = parser.getText();
                            inGu = false;
                        }
                        if (inSeq) { //isMapx이 true일 때 태그의 내용을 저장.
                            if (Integer.parseInt(dataid) - 1 == index) flag = 1;
                            index++;
                            if (flag == 1) flag = 2;
                            inSeq = false;
                        }
                        if (inPosx) {
                            Posx = parser.getText();
                            if (Posx.equals(" ")) {
                                Posx = "0.0";
                            }
                            inPosx = false;
                        }
                        if (inPosy) {
                            Posy = parser.getText();
                            if (Posy.equals(" ")) {
                                Posy = "0.0";
                            }

                            inPosy = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("list")) {
                            inlist = false;
                        }
                        break;
                }
                if (flag == 2) break;
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            //status1.setText("에러가..났습니다...");
        }

        String Guplusdong = Gu + " " + Dong;
        guplusdong.setText(Guplusdong);
        name.setText(Name);
        bicycle_type.setText(Type);


        MapView mapView1 = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        MapPOIItem marker = new MapPOIItem();
        marker.setTag(0);
        marker.setItemName(Name);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(Posy), Double.parseDouble(Posx)));
        marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView1.addPOIItem(marker);
        mapView1.setZoomLevel(2,true);
        mapView1.setMapCenterPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(Posy), Double.parseDouble(Posx))), true);
        mapViewContainer.addView(mapView1);




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

