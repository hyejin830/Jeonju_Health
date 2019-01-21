package example.com.api.heartattack;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
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

public class HeartAttackViewListFragment extends Fragment
{
    private ArrayList<String> datanum = null;

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    public static HeartAttackViewListFragment newInstance(int pageNo)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        HeartAttackViewListFragment fragment = new HeartAttackViewListFragment();
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

        boolean inlist = false, inSid=false, inName= false, inAddr = false;
        String Dataid = null, Name = null, Addr = null;

        datanum = new ArrayList<String>();
        ArrayList<String> name_item = new ArrayList<String>();
        ArrayList<String> addr_item = new ArrayList<String>();

        try{
            URL url = new URL("http://openapi.jeonju.go.kr/rest/heartimpactor/getHeartimpactorList?"
                    +"&ServiceKey="
                    +"DPLOCEWHWNCVWJC"
                    +"&numOfRows=57"
            );//검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("seq")){
                            inSid = true;
                        }
                        if(parser.getName().equals("facilityNm")){ //mapx 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if(parser.getName().equals("loadAddr")){ //title 만나면 내용을 받을수 있게 하자
                            inAddr = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inSid){
                            Dataid = parser.getText();
                            datanum.add(Dataid);
                            inSid = false;
                        }
                        if(inName) { //isMapx이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            name_item.add(Name);
                            inName = false;
                        }
                        if(inAddr){ //isTitle이 true일 때 태그의 내용을 저장.
                            Addr = parser.getText();
                            addr_item.add(Addr);
                            inAddr = false;
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
        for(int i=0;i<57;i++){
            adapter.addItem(datanum.get(i),name_item.get(i),addr_item.get(i));
        }

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HeartAttackViewListFragment.this.getActivity(),HeartAttackViewList.class);
                intent.putExtra("id", datanum.get(i));
                startActivity(intent);
            }
        });

        return view;
    }

}
