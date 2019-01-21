package example.com.api.bicycle_keep;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

import example.com.jeonjuhealth.R;
import example.com.listframe.BasicListViewAdapter;

public class BicycleKeepViewListFragment extends Fragment
{
    private ArrayList<String> datanum;

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    public static BicycleKeepViewListFragment newInstance(int pageNo)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        BicycleKeepViewListFragment fragment = new BicycleKeepViewListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_list, container, false);

        StrictMode.enableDefaults();

        boolean inlist = false, inName = false, inSid=false;
        boolean inGu=false, inType=false,inDong=false;
        String Name = null, Tag=null,Gu=null,Type=null,Dong=null;

        datanum = new ArrayList<String>();
        ArrayList<String> name_item = new ArrayList<String>();
        ArrayList<String> addr_item = new ArrayList<String>();


        try{
            URL url = new URL("http://openapi.jeonju.go.kr/rest/bicycleracks/getBicycleRacks?"
                    +"&authApiKey="
                    +"DYYVCYMSOVBKOJJ"
                    +"&pageSize=190"
            );//검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            String add=null;
            System.out.println("파싱시작합니다.");
            int index=0;

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("addr")){ //mapx 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if(parser.getName().equals("centerNm")){ //mapx 만나면 내용을 받을수 있게 하자
                            inDong = true;
                        }
                        if(parser.getName().equals("gu")){ //mapx 만나면 내용을 받을수 있게 하자
                            inGu = true;
                        }
                        if(parser.getName().equals("centerNm")){ //mapx 만나면 내용을 받을수 있게 하자
                            inDong = true;
                        }
                        if(parser.getName().equals("seq")){
                            inSid = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때

                        if(inName) { //isMapx이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            name_item.add(Name);
                            inName = false;
                        }
                        if(inDong) { //isMapx이 true일 때 태그의 내용을 저장.
                            Dong = parser.getText();
                            inDong= false;
                        }
                        if(inGu) { //isMapx이 true일 때 태그의 내용을 저장.
                            Gu = parser.getText();
                            inGu= false;
                            add=Gu+"구"+" "+Dong;
                            addr_item.add(add);
                        }
                        if(inSid){
                            index++;
                            datanum.add(String.valueOf(index));
                            inSid = false;
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

        final BasicListViewAdapter adapter = new BasicListViewAdapter();
        final ListView listView1 = (ListView)view.findViewById(R.id.listview);
        listView1.setAdapter(adapter);

        Log.e("dfsdfsdf", String.valueOf(datanum.get(1)));
        //String.valueOf()

        for(int i=0;i<190;i++){
            adapter.addItem(datanum.get(i),name_item.get(i),addr_item.get(i));

        }

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BicycleKeepViewListFragment.this.getActivity(),BicycleKeepViewList.class);
                intent.putExtra("id", datanum.get(i));
                startActivity(intent);
            }
        });

        return view;
    }

}
