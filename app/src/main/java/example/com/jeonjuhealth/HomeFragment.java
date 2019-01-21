package example.com.jeonjuhealth;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import example.com.etc.walk_step.StepCheckService;
import example.com.listframe.ListMainActivity;

public class HomeFragment extends Fragment
{
    //1줄
    final int PARK = 1;
    final int GYM = 2;
    //2줄
    final int HOSPITAL = 8;
    final int MEDI = 9;

    //1줄
    Button parkBtn;
    Button gymBtn;
    //2줄
    Button hospBtn;
    Button pharmBtn;

    Intent manboService;
    BroadcastReceiver receiver;
    boolean flag = true;
    String serviceData;
    TextView countText;
    Button playingBtn;


    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    public static HomeFragment newInstance(int pageNo)
    {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);


    }

    class ContentDownlad extends AsyncTask<String,Void,String> {
        ProgressDialog asyncDialog = new ProgressDialog(HomeFragment.this.getActivity());

        protected void onPreExecute(){
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중...");
            asyncDialog.show();
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... strings) {
            for(int i=0;i<5;i++){
                asyncDialog.setProgress(i*30);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String abc = "Parsing OK!";

            return abc;
        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            asyncDialog.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_home_page, container, false);


        Button ytbBtn=(Button)view.findViewById(R.id.button_youtube);
        ytbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEARCH);
                intent.setPackage("com.google.android.youtube");
                intent.putExtra("query", "홈트레이닝");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        manboService = new Intent(this.getActivity(), StepCheckService.class);
        receiver = new PlayingReceiver();
        countText = (TextView) view.findViewById(R.id.stepText);

        parkBtn = (Button) view.findViewById(R.id.park);
        gymBtn = (Button) view.findViewById(R.id.gym);

        hospBtn =(Button) view.findViewById(R.id.hospital);
        pharmBtn = (Button) view.findViewById(R.id.pharmarcy);

        parkBtn.setOnClickListener(new View.OnClickListener()
        {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(HomeFragment.this.getActivity(), ListMainActivity.class);
            intent.putExtra("code", PARK);
            startActivity(intent);
            ContentDownlad task = new ContentDownlad();
            task.execute();
        }
        });

        gymBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code", GYM);
                startActivity(intent);
            }
        });

        hospBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeFragment.this.getActivity(), ListMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("code", HOSPITAL);
                startActivity(intent);
                ContentDownlad task = new ContentDownlad();
                task.execute();
            }
        });

        pharmBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(HomeFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code", MEDI);
                startActivity(intent);
                ContentDownlad task = new ContentDownlad();
                task.execute();

            }
        });

        if (flag) {
            // TODO Auto-generated method stub
            try {
                IntentFilter mainFilter = new IntentFilter("make.a.yong.manbo");
                getActivity().registerReceiver(receiver, mainFilter);
                getActivity().startService(manboService);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            try {
                getActivity().unregisterReceiver(receiver);
                getActivity().stopService(manboService);
            } catch (Exception e) {
            }
        }


        return view;
    }

    class PlayingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            serviceData = intent.getStringExtra("stepService");
            countText.setText(serviceData+"걸음");
        }
    }


}

