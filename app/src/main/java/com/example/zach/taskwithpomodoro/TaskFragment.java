package com.example.zach.taskwithpomodoro;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.zach.taskwithpomodoro.dummy.DummyContent;
import com.example.zach.taskwithpomodoro.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TaskFragment extends Fragment {
    TaskDB taskdb;
    View view;
    ListView recyclerView;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TaskFragment newInstance(int columnCount) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_list, container, false);

        taskdb = ShareData.get(view.getContext()).getTaskDB();
        recyclerView = (ListView) view;

        Cursor cursor = taskdb.getTasksAsCurso();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                view.getContext(),
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] {TaskDB.TITLE, TaskDB.NUMPOMODOROS},
                new int[] {android.R.id.text1, android.R.id.text2}
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = taskdb.getTask((int) id);

                reassignDB();
                mListener.onListFragmentInteraction(task);
            }
        });
        recyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                taskdb.deleteTask(id);
                reassignDB();
                return false;
            }
        });

        reassignDB();

        return view;
    }

    public void reassignDB(){
        Cursor cursor = taskdb.getTasksAsCurso();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                view.getContext(),
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] {TaskDB.TITLE, TaskDB.NUMPOMODOROS},
                new int[] {android.R.id.text1, android.R.id.text2}
        );
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(Task item);
    }
}
