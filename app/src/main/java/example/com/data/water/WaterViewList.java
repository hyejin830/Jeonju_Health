package example.com.data.water;

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
import android.widget.Button;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

import example.com.jeonjuhealth.R;

public class WaterViewList extends AppCompatActivity implements MapView.MapViewEventListener,MapView.POIItemEventListener{
    TextView name,addr,tel,use_people,when_test,result,bad_factor;
    ViewGroup mapViewContainer;
    Button call;
    public static String Tel_call="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21 ){
            getWindow().setStatusBarColor(Color.parseColor("#32CD32"));
        }
        setContentView(R.layout.activity_view_list_water);


        Intent intent = getIntent();
        String dataid = intent.getStringExtra("name");

        name = (TextView)findViewById(R.id.name);
        addr = (TextView)findViewById(R.id.addr);
        tel = (TextView)findViewById(R.id.tel);
        use_people = (TextView)findViewById(R.id.use_people);
        when_test = (TextView)findViewById(R.id.when_test);
        result = (TextView)findViewById(R.id.result);
        bad_factor = (TextView)findViewById(R.id.bad_factor);
        call=(Button)findViewById(R.id.go_to_url);
        StrictMode.enableDefaults();

        boolean inlist = false,inId=false,inName = false, inAddr = false, inTel = false, inPosx=false, inPosy=false, inUse=false,inWhen=false,inResult=false,inBad=false;
        String Id =null ,Name = null, Addr = null, Tel=null, Posx=null, Posy = null,Use=null,When=null,Result=null,Bad=null;

        try{

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            //FileInputStream fis = new FileInputStream(R.raw.dementia_center) ;
            InputStream is = getResources().openRawResource(R.raw.water);
            parser.setInput(is, null) ;

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");
            int flag=0;

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("약수터명")){ //title 만나면 내용을 받을수 있게 하자
                            inName= true;
                        }
                        if(parser.getName().equals("소재지도로명주소")){ //title 만나면 내용을 받을수 있게 하자
                            inAddr = true;
                        }
                        if(parser.getName().equals("관리기관전화번호")){ //title 만나면 내용을 받을수 있게 하자
                            inTel = true;
                        }
                        if(parser.getName().equals("경도")){ //title 만나면 내용을 받을수 있게 하자
                            inPosx = true;
                        }
                        if(parser.getName().equals("위도")){ //title 만나면 내용을 받을수 있게 하자
                            inPosy = true;
                        }
                        if(parser.getName().equals("일평균이용인구수")){ //title 만나면 내용을 받을수 있게 하자
                            inUse = true;
                        }
                        if(parser.getName().equals("수질검사일자")){ //title 만나면 내용을 받을수 있게 하자
                            inWhen = true;
                        }
                        if(parser.getName().equals("수질검사결과구분")){ //title 만나면 내용을 받을수 있게 하자
                            inResult = true;
                        }
                        if(parser.getName().equals("부적합항목")){ //title 만나면 내용을 받을수 있게 하자
                            inBad = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inName){ //isTitle이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            if(Name.equals(dataid)) {
                                flag = 1;
                            }

                            inName = false;
                        }
                        if(inTel){ //isTitle이 true일 때 태그의 내용을 저장.
                            Tel= parser.getText();
                            if(flag==1)
                                flag=2;
                            inTel = false;
                        }
                        if(inAddr){ //isTitle이 true일 때 태그의 내용을 저장.
                            Addr = parser.getText();
                            inAddr = false;
                        }
                        if(inPosx){ //isTitle이 true일 때 태그의 내용을 저장.
                            Posx = parser.getText();
                            inPosx = false;
                        }
                        if(inPosy){ //isTitle이 true일 때 태그의 내용을 저장.
                            Posy  = parser.getText();
                            inPosy = false;
                        }
                        if(inUse){ //isTitle이 true일 때 태그의 내용을 저장.
                            Use= parser.getText();
                            inUse = false;
                        }
                        if(inWhen){ //isTitle이 true일 때 태그의 내용을 저장.
                            When = parser.getText();
                            inWhen = false;
                        }
                        if(inResult){ //isTitle이 true일 때 태그의 내용을 저장.
                            Result = parser.getText();
                            inResult = false;
                        }
                        if(inBad){ //isTitle이 true일 때 태그의 내용을 저장.
                            Bad = parser.getText();
                            inBad = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("Row")){
                            //status1.setText(status1.getText()+"주소 : "+ Addr +"\n 센터명: "+ Name +"\n");

                            inlist = false;
                        }
                        break;
                }
                if(flag==2)
                    break;
                parserEvent = parser.next();
            }

        } catch(Exception e){
            //status1.setText("에러가..났습니다...");
        }

        name.setText(Name);
        addr.setText(Addr);
        tel.setText(Tel);
        result.setText(Result);
        use_people.setText(Use);
        bad_factor.setText(Bad);
        when_test.setText(When);



        MapView  mapView1 = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        MapPOIItem marker = new MapPOIItem();
        marker.setTag(0);
        marker.setItemName(Name);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(Posy),Double.parseDouble(Posx)));
        marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView1.addPOIItem(marker);
        mapView1.setZoomLevel(2,true);
        mapView1.setMapCenterPoint((MapPoint.mapPointWithGeoCoord(Double.parseDouble(Posy),Double.parseDouble(Posx))),true);
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

