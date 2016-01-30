package com.example.zach.taskwithpomodoro;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PomodoroRecapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PomodoroRecapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PomodoroRecapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    TaskDB db;
    Task currentTask;
    View view;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PomodoroRecapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PomodoroRecapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PomodoroRecapFragment newInstance(String param1, String param2) {
        PomodoroRecapFragment fragment = new PomodoroRecapFragment();
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
        // Inflate the layout for this fragment
        SharedPreferences savedValues = super.getActivity().getSharedPreferences("SavedValues", super.getActivity().MODE_PRIVATE);
        long id = savedValues.getLong("itemID", 0);

        db = ShareData.get(super.getActivity()).getTaskDB();
        currentTask = db.getTask((int)id);
        if(currentTask.getNumPomodoros()!=0) {
            currentTask.setNumPomodoros(currentTask.getNumPomodoros() - 1);
        }
        view = inflater.inflate(R.layout.fragment_pomodoro_recap, container, false);
        db.updateTask(currentTask);
        drawScreen(currentTask);

        return view;
        //return inflater.inflate(R.layout.fragment_pomodoro_recap, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onButtonClick(3);
        }
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

    public void drawScreen(Task task){
        TextView taskNumPomodorosTextView = (TextView) view.findViewById(R.id.numPomodorosTextView);
        taskNumPomodorosTextView.setText("Pomodoros left: " + task.getNumPomodoros());
        TextView taskTitle = (TextView) view.findViewById(R.id.taskTitleTextView);
        taskTitle.setText("Did you finish " + task.getTitle() + "?");
    }

    public void taskFinished(View v){

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onButtonClick(int i);
    }


}
