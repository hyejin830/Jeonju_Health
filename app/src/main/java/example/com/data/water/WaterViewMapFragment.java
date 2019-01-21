package example.com.data.water;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

import example.com.jeonjuhealth.R;

public class WaterViewMapFragment extends Fragment implements MapView.POIItemEventListener,MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener
{

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    private int mtype;

    ViewGroup mapViewContainer;
    MapView mapView;
    MapPOIItem marker;

    TextView tv_name;
    TextView tv_addr;
    TextView tv_tel;
    public static String Tel_call = "";

    ArrayList<String> items = new ArrayList<String>();
    ArrayList<String> posx_item = new ArrayList<String>();
    ArrayList<String> posy_item = new ArrayList<String>();
    ArrayList<String> tag_item = new ArrayList<String>();
    ArrayList<String> addr_item = new ArrayList<String>();
    Button bt;
    Button phone;
    Button go_to_url;

    public static WaterViewMapFragment newInstance(int pageNo)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        WaterViewMapFragment fragment = new WaterViewMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_view_map_pharm, container, false);
        phone = (Button) view.findViewById(R.id.phone_call);
        go_to_url=(Button)view.findViewById(R.id.go_to_mapurl);
        StrictMode.enableDefaults();

        //TextView status1 = (TextView)findViewById(R.id.result); //파싱된 결과확인!

        boolean inlist = false,inId=false,inName = false, inPosx=false, inPosy=false;
        String Id =null ,Name = null, Posx=null, Posy = null;

        tv_name = (TextView) view.findViewById(R.id.Pharm_name);
        tv_addr = (TextView) view.findViewById(R.id.Pharm_addr);
        tv_tel = (TextView) view.findViewById(R.id.Pharm_tel);

        try{

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            //FileInputStream fis = new FileInputStream(R.raw.dementia_center) ;
            InputStream is = getResources().openRawResource(R.raw.water);
            parser.setInput(is, null) ;

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("약수터명")){ //title 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if(parser.getName().equals("경도")){ //title 만나면 내용을 받을수 있게 하자
                            inPosx = true;
                        }
                        if(parser.getName().equals("위도")){ //title 만나면 내용을 받을수 있게 하자
                            inPosy = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inName){ //isTitle이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            items.add(Name);
                            inName = false;
                        }
                        if(inPosx){ //isTitle이 true일 때 태그의 내용을 저장.
                            Posx = parser.getText();
                            posx_item.add(Posx);
                            inPosx = false;
                        }
                        if(inPosy){ //isTitle이 true일 때 태그의 내용을 저장.
                            Posy  = parser.getText();
                            posy_item.add(Posy);
                            inPosy = false;
                        }
                }
                parserEvent = parser.next();
            }

        } catch(Exception e){
            //status1.setText("에러가..났습니다...");
        }

        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view_pharm_all);


        return view;
    }

    public void onStart() {
        super.onStart();
        mapViewContainer.removeAllViews();
        mapView = new MapView(getActivity());
        mapView.setPOIItemEventListener(this);
        for (int i = 0; i < items.size(); i++) {
            marker = new MapPOIItem();
            marker.setTag(0);
            marker.setItemName(items.get(i));
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(posy_item.get(i)), Double.parseDouble(posx_item.get(i))));
            marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);
            }
        mapView.setZoomLevel(6,true);
        mapView.setMapCenterPoint((MapPoint.mapPointWithGeoCoord(35.8241973,127.1459943)), true);
        mapViewContainer.addView(mapView);
    }
    @Override
    public void onPause() {
        super.onPause();
        mapViewContainer.removeAllViews();

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {

    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, final MapPOIItem mapPOIItem) {

        String dataid= mapPOIItem.getItemName();

        boolean inlist = false,inId=false,inName = false, inAddr = false, inTel = false;
        String Id =null ,Name = null, Addr = null, Tel=null;

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

        tv_addr.setText(Addr);
        tv_name.setText(Name);
        tv_tel.setText(Tel);
        Tel_call=Tel;
        go_to_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(("http://map.daum.net/link/search/"+mapPOIItem.getItemName()
                )));
                startActivity(intent);
            }
        });

        http://map.daum.net/link/search/?
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((Tel_call)));
                startActivity(intent);
            }
        });

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
