package example.com.jeonjuhealth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import example.com.data.medi_machine.MediListMainActivity;
import example.com.listframe.ListMainActivity;

public class FitnessFragment extends Fragment
{
    final int HOSPITAL = 8;
    final int MEDI = 9;
    final int HELATHUP = 10;
    final int HEARTATTACK = 11;
    final int SHELTER = 13;
    final int DEMENCENTER = 14;

    //1줄
    Button hospBtn;
    Button pharmBtn;
    //2줄
    Button healthupBtn;
    Button heartBtn;
    //3줄
    Button medisellBtn;
    Button restBtn;
    //4줄
    Button demenctBtn;


    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    public static FitnessFragment newInstance(int pageNo)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        FitnessFragment fragment = new FitnessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);

    }

    class ContentDownlad extends AsyncTask<String,Void,String> {
        ProgressDialog asyncDialog = new ProgressDialog(FitnessFragment.this.getActivity());

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
        View view = inflater.inflate(R.layout.activity_fitness_category, container, false);


        hospBtn =(Button) view.findViewById(R.id.hospital);
        pharmBtn = (Button) view.findViewById(R.id.pharmarcy);

        healthupBtn = (Button) view.findViewById(R.id.healthup);
        heartBtn = (Button)view.findViewById(R.id.heart);

        medisellBtn = (Button) view.findViewById(R.id.medisell);
        restBtn = (Button) view.findViewById(R.id.rest);

        demenctBtn = (Button) view.findViewById(R.id.demenct);

        hospBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(FitnessFragment.this.getActivity(), ListMainActivity.class);
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
                Intent intent = new Intent(FitnessFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code", MEDI);
                startActivity(intent);
                ContentDownlad task = new ContentDownlad();
                task.execute();
            }
        });

        healthupBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(FitnessFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code", HELATHUP);
                startActivity(intent);
            }
        });

        heartBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(FitnessFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code", HEARTATTACK);
                startActivity(intent);
            }
        });

        medisellBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(FitnessFragment.this.getActivity(), MediListMainActivity.class);
                startActivity(intent);
            }
        });

        restBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(FitnessFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code", SHELTER);
                startActivity(intent);
                ContentDownlad task = new ContentDownlad();
                task.execute();
            }
        });

        demenctBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(FitnessFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code", DEMENCENTER);
                startActivity(intent);
            }
        });


        return view;
    }

}


