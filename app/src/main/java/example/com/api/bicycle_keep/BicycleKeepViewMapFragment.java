package example.com.api.bicycle_keep;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.net.URL;
import java.util.ArrayList;

import example.com.jeonjuhealth.R;

public class BicycleKeepViewMapFragment extends Fragment implements MapView.POIItemEventListener, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String MEDI_CDT = "MEDI_CDT";
    private int mPageNo;
    private int mtype;

    ViewGroup mapViewContainer;
    MapView mapView;
    MapPOIItem marker;

    TextView button_name;
    TextView button_addr;
    TextView button_tel;
    TextView only_show_tel;

    double longitude;
    double latitude;

    public static String Tel_call = "";

    ArrayList<String> items = new ArrayList<String>();
    ArrayList<String> posx_item = new ArrayList<String>();
    ArrayList<String> posy_item = new ArrayList<String>();
    ArrayList<Integer> tag_item = new ArrayList<Integer>();
    Button bt;
    Button phone;
    Button go_to_url;


    public static BicycleKeepViewMapFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        BicycleKeepViewMapFragment fragment = new BicycleKeepViewMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
        mtype = getArguments().getInt(MEDI_CDT);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_view_map_pharm, container, false);
        phone = (Button) view.findViewById(R.id.phone_call);
        go_to_url=(Button)view.findViewById(R.id.go_to_mapurl);
        StrictMode.enableDefaults();

        //TextView status1 = (TextView)findViewById(R.id.result); //파싱된 결과확인!

        boolean inlist = false, inName = false, inPosx = false, inPosy = false, inMedisid = false, inSid = false;
        boolean inGu = false, inType = false, inDong = false;
        String Name = null, Posx = null, Posy = null, MediSid = null, Tag = null, Gu = null, Type = null, Dong = null;

        button_name = (TextView) view.findViewById(R.id.Pharm_name);
        button_addr = (TextView) view.findViewById(R.id.Pharm_addr);
        button_tel = (TextView) view.findViewById(R.id.Pharm_tel);
        only_show_tel = (TextView) view.findViewById(R.id.Pharm_tel_only);

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
                        if (parser.getName().equals("seq")) {
                            inSid = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때

                        if (inName) { //isMapx이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            items.add(Name);
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
                        if (inPosx) {
                            Posx = parser.getText();
                            if (Posx.equals(" ")) {
                                Posx = "0.0";
                            }
                            posx_item.add(Posx);
                            inPosx = false;
                        }
                        if (inPosy) {
                            Posy = parser.getText();
                            if (Posy.equals(" ")) {
                                Posy = "0.0";
                            }
                            posy_item.add(Posy);
                            inPosy = false;
                        }
                        if (inSid) {
                            tag_item.add(index++); //
                            inSid = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("list")) {
                            inlist = false;
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
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
            marker.setTag(tag_item.get(i));
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
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.e("위치",mapPointGeo.latitude+" " +mapPointGeo.longitude);
        longitude = mapPointGeo.longitude;
        latitude = mapPointGeo.latitude;
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
        boolean inlist = false, inName = false, inTel = false, inAddr = false, inGu = false, inDong = false, inSid = false;
        String Name = null, Addr = null, Gu = null, Dong = null;
        String Tel = null;

        int dataid_1 = mapPOIItem.getTag();
        String dataid = String.valueOf(dataid_1);


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

            int index_list = 0;
            int flag = 0;
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("addr")) { //mapx 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if (parser.getName().equals("gu")) { //title 만나면 내용을 받을수 있게 하자
                            inGu = true;
                        }
                        if (parser.getName().equals("centerNm")) { //title 만나면 내용을 받을수 있게 하자
                            inDong = true;
                        }
                        if (parser.getName().equals("seq")) {
                            inSid = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (inName) { //isMapx이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            inName = false;
                        }
                        if (inGu) { //isTitle이 true일 때 태그의 내용을 저장.
                            Gu = parser.getText();
                            inGu = false;
                        }
                        if (inDong) { //isMapy이 true일 때 태그의 내용을 저장.
                            Dong = parser.getText();
                            inDong = false;
                        }
                        if (inSid) {
                            if (dataid_1 == index_list) flag = 1;
                            index_list++;
                            if (flag == 1) flag = 2;
                            inSid = false;
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

        String guplusdong = Gu + "구" + " " + Dong;

        button_addr.setText(guplusdong);
        button_name.setText(Name);
        //button_tel.setText(Tel);
        button_tel.setVisibility(View.GONE);
        phone.setVisibility(View.GONE);
        only_show_tel.setVisibility(View.GONE);

        go_to_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(("http://map.daum.net/link/search/"+mapPOIItem.getItemName()
                )));
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

