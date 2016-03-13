package com.example.zach.taskwithpomodoro;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

public class TabbedMainActivity extends AppCompatActivity implements TaskFragment.OnListFragmentInteractionListener, TimerFragment.OnFragmentInteractionListener {
    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String TASK_IN_PROGRESS_BOOL = "taskinprogress";
    public static final String TASK_IN_PROGRESS_TITLE = "currenttasktitle";
    public static final String TASK_IN_PROGRESS_ID = "currenttaskid";
    public static final String DESIRED_ACTION = "desiredaction";
    public static final String DESIRED_ACTION_FINISHED = "fininshtask";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    public void taskFinished(View v){
        Log.i("TabbedMainActivity", "taskFinished has been called");
        /*TextView textView = (TextView) v.findViewById(R.id.timerDisplayTextView);
        textView.setText("potato");*/
        timerFragment.finishTask(true);
    }
    public void continueTask(View v){
        timerFragment.finishTask(false);
    }
    public void addPomodoro(View v){
        timerFragment.addPomodoro();
    }
    public void notifyListChange(){
        taskFragment.reassignDB();
    }
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TimerFragment timerFragment;
    TaskFragment taskFragment;


    public void onListFragmentInteraction(Task task){
        Toast.makeText(this,"Pumkindoro started for " + task.getTitle(), Toast.LENGTH_SHORT).show();

        timerFragment.startTimer((long)task.getId());
        mViewPager.setCurrentItem(0, true);


    }

    public void onFragmentInteraction(Uri uri){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivityForResult(new Intent(getApplicationContext(), CreateTaskActivity.class), 0);
            }
        });
        try {
            Log.i("Zach", "try statement ran");
            Intent i = this.getIntent();
            String desiredAction = this.getIntent().getStringExtra(TabbedMainActivity.DESIRED_ACTION);
            Log.i("Zach", desiredAction);
            if (desiredAction.equals(TabbedMainActivity.DESIRED_ACTION_FINISHED)) {
                timerFragment.finishTask(true);
                Log.i("zach", "signal sent");
            }
            Log.i("zach",String.valueOf(desiredAction.equals(TabbedMainActivity.DESIRED_ACTION_FINISHED)));

        }
        catch(Exception e){

        }





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        taskFragment.reassignDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if(position ==1){
                //taskFragment = ShareData.get(getApplicationContext()).getTaskFragment();
                taskFragment = TaskFragment.newInstance(1);
                return taskFragment;
            }
            else if(position == 0){

                //timerFragment = ShareData.get(getApplicationContext()).getTimerFragment();

               //Log.i("zach", timerFragment.getTag() );
                //timerFragment.logOut("test from getItem");
                timerFragment = TimerFragment.newInstance("test", "test");
                return timerFragment;
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Timer";
                case 1:
                    return "Tasks";
                case 2:
                    return "Stats";
            }
            return null;
        }
    }
}
