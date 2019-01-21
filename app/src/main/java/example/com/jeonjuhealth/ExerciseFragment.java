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

import example.com.etc.BicycleCourseActivity;
import example.com.listframe.ListMainActivity;

public class ExerciseFragment extends Fragment
{
    int code_type;

    final int PARK = 1;
    final int GYM = 2;

    final int RENTBC = 4;
    final int KEEPBC = 5;

    final int SELLBC = 6;
    final int COURSEBC = 20;

    final int WATER = 7;


    //1줄
    Button parkBtn;
    Button gymBtn;
    //2줄
    Button bcrentalBtn;
    Button bckeepBtn;
    //3줄
    Button bcSellBtn;
    Button bcCourseBtn;
    //4줄
    Button waterBtn;

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    public static ExerciseFragment newInstance(int pageNo)
    {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        ExerciseFragment fragment = new ExerciseFragment();
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
        ProgressDialog asyncDialog = new ProgressDialog(ExerciseFragment.this.getActivity());

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
        View view = inflater.inflate(R.layout.activity_exercise_category, container, false);

        parkBtn = (Button) view.findViewById(R.id.park);
        gymBtn = (Button) view.findViewById(R.id.gym);

        bcrentalBtn =(Button)view.findViewById(R.id.rental_bc);
        bckeepBtn  =(Button)view.findViewById(R.id.keep_bc);

        bcSellBtn = (Button) view.findViewById(R.id.sellbc);
        bcCourseBtn  =(Button)view.findViewById(R.id.coursebc);

        waterBtn  =(Button)view.findViewById(R.id.water);

        parkBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ExerciseFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code",PARK);
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
                Intent intent = new Intent(ExerciseFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code",GYM);
                startActivity(intent);
            }
        });

        bcrentalBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ExerciseFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code",RENTBC);
                startActivity(intent);
            }
        });
        bckeepBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ExerciseFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code",KEEPBC);
                startActivity(intent);
            }
        });

        bcSellBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ExerciseFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code", SELLBC);
                startActivity(intent);
            }
        });
        bcCourseBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ExerciseFragment.this.getActivity(), BicycleCourseActivity.class);
                startActivity(intent);
            }
        });
        waterBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ExerciseFragment.this.getActivity(), ListMainActivity.class);
                intent.putExtra("code", WATER);
                startActivity(intent);
            }
        });


        return view;
    }

}


