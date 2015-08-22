package br.com.activitieslogger.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.activitieslogger.Fragments.AddActivityFragment;
import br.com.activitieslogger.R;
import br.com.activitieslogger.adapters.ActivityAdapter;
import br.com.activitieslogger.entities.Activity;
import br.com.activitieslogger.interfaces.RecyclerViewOnClickListenerHack;
import br.com.activitieslogger.repositories.ActivityRepository;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private List<Activity> activities;
    private ActivityAdapter adapter;
    private TextView tv_time_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_time_total = (TextView) findViewById(R.id.tv_time_total);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById( R.id.rv_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activities = ActivityRepository.getAllActivities();
        adapter = new ActivityAdapter( this, activities, tv_time_total );
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);

        int totalHours=0;
        for(Activity activity : activities){
            totalHours+=activity.getTimeActivity();
        }

        if(activities == null || activities.isEmpty()){
            tv_time_total.setText("Você não realizou nenhum exercício");
        } else
            tv_time_total.setText("Você já realizou "+ totalHours + " hora(s) de exercício(s)");
    }

    public void addActivity(View v){
        Intent intent = new Intent(this,AddActivityFragment.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickListener(View view, int position) {
        Toast.makeText(this, "onClickListener(): " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClickListener(View view, int position) {
        adapter.removeListItem(position);
    }

}
