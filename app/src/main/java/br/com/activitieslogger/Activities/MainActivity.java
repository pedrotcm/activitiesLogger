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

import java.util.List;

import br.com.activitieslogger.R;
import br.com.activitieslogger.adapters.ActivityAdapter;
import br.com.activitieslogger.entities.Activity;
import br.com.activitieslogger.interfaces.RecyclerViewOnClickListenerHack;
import br.com.activitieslogger.repositories.ActivityRepository;

/**
 * Created by PedroTome on 21/08/15.
 */
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

        //Inicia os componentes da view
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
        //Quando abrir a activity e quando voltar da criação do exercício é calculado a quantidade de horas realizadas
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
        //Inicia a criação do exercício
        Intent intent = new Intent(this,AddActivityActivity.class);
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
        //Inicia a activity de criação de exercício para fazer a atualização do exercício
        Intent intent = new Intent(this, AddActivityActivity.class);
        intent.putExtra("idActivity", adapter.getActivity(position).getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClickListener(View view, int position) {
        //Remove o exercício selecionado
        Activity activity = adapter.getActivity(position);
        adapter.removeListItem(position);
        activity.delete();
    }

}
