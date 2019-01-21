package example.com.api.pharm;

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

public class PharmViewListFragment extends Fragment
{

    private ArrayList<String> datanum = null;

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String MEDI_CDT = "MEDI_CDT";
    private int mPageNo;
    private int mtype;

    public static PharmViewListFragment newInstance(int pageNo,int type)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        args.putInt(MEDI_CDT,type);
        PharmViewListFragment fragment = new PharmViewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
        mtype = getArguments().getInt(MEDI_CDT);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_list, container, false);

        StrictMode.enableDefaults();

        boolean inlist = false, inAddr = false,inName= false,inSid=false;

        String addr = null, Name = null, dataid=null;

        ArrayList<String> items = new ArrayList<String>();
        datanum = new ArrayList<String>();
        ArrayList<String> addr_item = new ArrayList<String>();

        int totalcount=0;
        if(mtype==1) totalcount=975;
        else if(mtype==2) totalcount=344;

        try{
            URL url = new URL("http://openapi.jeonju.go.kr/rest/medicalnew/getMedicalList?"
                    +"&authApiKey="
                    +"mQKdK9LqJX%2F9Dy6KbM3pL0ecweGtcgKe6bZc%2BU9ndBMSuPiuDhrlNxSNgt07e72jYkdKtkrhESFjDeTqE7V3IQ%3D%3D"
                    +"&mediCdt="+mtype+"&pageSize="+totalcount
            );//검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("mediSid")){
                            inSid = true;
                        }
                        if(parser.getName().equals("mediAddr")){ //title 만나면 내용을 받을수 있게 하자
                            inAddr = true;
                        }
                        if(parser.getName().equals("mediName")){ //mapx 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            //status1.setText(status1.getText()+"에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inSid){
                            dataid = parser.getText();
                            datanum.add(dataid);
                            inSid = false;
                        }
                        if(inAddr){ //isTitle이 true일 때 태그의 내용을 저장.
                            addr = parser.getText();
                            addr_item.add(addr);
                            inAddr = false;
                        }
                        if(inName) { //isMapx이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            items.add(Name);
                            inName = false;
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
        for(int i=0;i<totalcount;i++){
            adapter.addItem(datanum.get(i),items.get(i),addr_item.get(i));
        }

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PharmViewListFragment.this.getActivity(),PharmViewList.class);

                intent.putExtra("id", datanum.get(i));
                intent.putExtra("type", mtype);
                startActivity(intent);
            }
        });

        return view;
    }

}
