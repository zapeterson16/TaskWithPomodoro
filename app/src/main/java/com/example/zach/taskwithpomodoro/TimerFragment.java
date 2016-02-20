package com.example.zach.taskwithpomodoro;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView timerDisplayTextView;
    View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TimerFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_timer, container, false);
        timerDisplayTextView = (TextView) view.findViewById(R.id.timerDisplayTextView);
        timerDisplayTextView.setText("test");



        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void logOut(String s){
        Log.i("Zach", s);
        timerDisplayTextView.setText("potato");
    }

    public void startTimer(final Long itemID){
        final long startMillis = System.currentTimeMillis();
        final long totalLength = 1500;
       // Log.i("TimerFragmentStartTimer", itemID.toString());

        final Timer timer = new Timer();

        SharedPreferences savedValues = getActivity().getSharedPreferences("SavedValues", getActivity().MODE_PRIVATE);
        savedValues.edit().putLong("itemID", itemID).commit();





        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                final long elapseMillis = System.currentTimeMillis() - startMillis;
                final long elapseSeconds = elapseMillis / 1000;
                final long timeLeft = totalLength - elapseSeconds;
                String result;
                if (timeLeft >= 0) {
                    if(timeLeft%60<10) {
                        result = String.valueOf(timeLeft / 60) + ":0" + String.valueOf(timeLeft % 60);
                    }
                    else{
                        result = String.valueOf(timeLeft / 60) + ":" + String.valueOf(timeLeft % 60);
                    }
                } else {
                    result = "done";
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(1000);
                    SharedPreferences savedValues = getActivity().getSharedPreferences("SavedValues", getActivity().MODE_PRIVATE);
                    long id = savedValues.getLong("itemID", 0);
                    TaskDB taskdb = ShareData.get(view.getContext()).getTaskDB();
                    Task tempTask = taskdb.getTask((int) id);
                    PomodoroEndNotification nm = new PomodoroEndNotification();
                    //Log.i("fromTimerFragment", " *****************************" + tempTask.getNumPomodoros());

                    PomodoroEndNotification.notify(getActivity().getApplicationContext(), "You are done with your pomodoro",(int) id);
                    timer.cancel();
                    timer.purge();

                }
                final String displayResult = result;
                timerDisplayTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        timerDisplayTextView.setText(displayResult);
                    }

                });
            }


        };


        timer.schedule(task, 0, 1000);

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
