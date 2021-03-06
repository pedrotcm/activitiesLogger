package br.com.activitieslogger.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.activitieslogger.R;
import br.com.activitieslogger.entities.Activity;

public class AddActivityActivity extends AppCompatActivity {

    private static String SELECT_TYPE = "Selecione um exercício...";
    private Toolbar mToolbar;
    private EditText et_timeActivity;
    private Spinner sp_typeActivity;
    private TextView tv_dateCreated;
    private ImageButton ib_date;
    private Button bt_add;
    private int mYear, mMonth, mDay, mTimeActivity;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private String CREATE_UPDATE_MSG = "Exercício criado com sucesso";
    private String TITLE = "Adicionar exercício";


    private Activity activity;

    public void init() {
        activity = new Activity();

        et_timeActivity = (EditText) findViewById(R.id.et_timeActivity);
        tv_dateCreated = (TextView) findViewById(R.id.tv_dateCreated);
        sp_typeActivity = (Spinner) findViewById(R.id.sp_typeActivity);
        ib_date = (ImageButton) findViewById(R.id.ib_date);
        bt_add = (Button) findViewById(R.id.bt_add);

        et_timeActivity.setText("0h");
        setCurrentDate();

        //Cria os tipos de exercicios
        String[] typeActivities = {SELECT_TYPE, "Bicicleta", "Corrida", "Futebol", "Natação", "Vôlei"};
        ArrayAdapter<String> adapterTypeActivity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, typeActivities);
        sp_typeActivity.setAdapter(adapterTypeActivity);

        //Verifica se é uma atualização de exercício quando passa o id do exercicio
        if (getIntent().getLongExtra("idActivity", 0) != 0){
            activity = Model.load(Activity.class, getIntent().getExtras().getLong("idActivity"));
            bt_add.setText("Atualizar");
            CREATE_UPDATE_MSG = "Exercício atualizado com sucesso";
            TITLE = "Atualizar exercício";
            et_timeActivity.setText(activity.getTimeActivity() + "h");
            sp_typeActivity.setSelection(adapterTypeActivity.getPosition(activity.getTypeActivity()));
            tv_dateCreated.setText(dateFormat.format(activity.getDateCreated()));
        }
    }

    private void setCurrentDate() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        tv_dateCreated.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(TITLE);

        init();

        et_timeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                timeActivityPicker();
            }
        });

        ib_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validData()) {
                    saveActivity();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Salva o exercício e volta para a tela principal
    private void saveActivity() {
        activity.setTimeActivity(Integer.valueOf(et_timeActivity.getText().toString().substring(0, et_timeActivity.getText().length() - 1)));
        activity.setTypeActivity((String) sp_typeActivity.getSelectedItem());
        try {
            activity.setDateCreated(dateFormat.parse(tv_dateCreated.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        activity.save();
        Toast.makeText(this, CREATE_UPDATE_MSG, Toast.LENGTH_LONG).show();

        finish();

    }

    //Valida os campos obrigatórios
    private boolean validData() {
        if (et_timeActivity.getText().toString().equalsIgnoreCase("0h")) {
            Toast.makeText(this, "Tempo de exercício deve ser maior que zero", Toast.LENGTH_LONG).show();
            return false;
        } else if (((String) sp_typeActivity.getSelectedItem()).equalsIgnoreCase(SELECT_TYPE)) {
            Toast.makeText(this, "Um tipo de exercício deve ser selecionado", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //Diálogo do tempo de exercício
    private void timeActivityPicker() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.time_activity_picker, null);
        final NumberPicker np_time = (NumberPicker) rootView.findViewById(R.id.np_time);
        np_time.setMinValue(00);
        np_time.setMaxValue(99);
        np_time.setValue(mTimeActivity);
        AlertDialog timePicker = new AlertDialog.Builder(this)
                .setTitle("Tempo de exercício")
                .setView(rootView)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                et_timeActivity.setText(np_time.getValue() + "h");
                                mTimeActivity = np_time.getValue();
                            }
                        })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                .create();
        timePicker.show();
    }

    //Diálogo para selecionar a data do exercício
    private void datePicker() {

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                tv_dateCreated.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                mDay = dayOfMonth;
                mMonth = monthOfYear;
                mYear = year;
            }
        }, mYear, mMonth, mDay);
        dpd.show();
    }

    //Ocultar teclado
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}

