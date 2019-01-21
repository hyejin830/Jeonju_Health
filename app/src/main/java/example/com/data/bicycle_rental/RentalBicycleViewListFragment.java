package example.com.data.bicycle_rental;

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

import java.io.InputStream;
import java.util.ArrayList;

import example.com.jeonjuhealth.R;
import example.com.listframe.BasicListViewAdapter;

public class RentalBicycleViewListFragment extends Fragment
{

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    public static RentalBicycleViewListFragment newInstance(int pageNo)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        RentalBicycleViewListFragment fragment = new RentalBicycleViewListFragment();
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

        int count_data=0;
        boolean inlist = false, inAddr = false,inName= false;
        String Addr = null, Name = null;

        final ArrayList<String> name_item = new ArrayList<String>();
        ArrayList<String> addr_item = new ArrayList<String>();

        StrictMode.enableDefaults();

        try{

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            //FileInputStream fis = new FileInputStream(R.raw.dementia_center) ;
            InputStream is = getResources().openRawResource(R.raw.rental_bicycle);
            parser.setInput(is, null) ;

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("자전거대여소명")){ //title 만나면 내용을 받을수 있게 하자
                            inName = true;
                        }
                        if(parser.getName().equals("소재지지번주소")){ //title 만나면 내용을 받을수 있게 하자
                            inAddr = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inName){ //isTitle이 true일 때 태그의 내용을 저장.
                            Name = parser.getText();
                            name_item.add(Name);
                            count_data++;
                            inName = false;
                        }
                        if(inAddr){ //isTitle이 true일 때 태그의 내용을 저장.
                            Addr = parser.getText();
                            addr_item.add(Addr);
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
                parserEvent = parser.next();
            }

        } catch(Exception e){
            //status1.setText("에러가..났습니다...");
        }

        System.out.println("count_data "+count_data);

        final BasicListViewAdapter adapter = new BasicListViewAdapter();
        final ListView listView1 = (ListView)view.findViewById(R.id.listview);
        listView1.setAdapter(adapter);
        for(int i=0;i<count_data;i++){
            adapter.addItem("0",name_item.get(i),addr_item.get(i));
        }

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RentalBicycleViewListFragment.this.getActivity(),RentalBicycleViewList.class);

                intent.putExtra("name", name_item.get(i));
                startActivity(intent);
            }
        });

        return view;
    }

}
