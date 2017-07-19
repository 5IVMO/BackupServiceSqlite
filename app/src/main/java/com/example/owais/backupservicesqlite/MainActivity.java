package com.example.owais.backupservicesqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.backupservice.AppPreferences;
import com.example.backupservice.BackupService;
import com.example.backupservice.Params;
import com.example.owais.backupservicesqlite.DB.DAL;
import com.example.owais.backupservicesqlite.DB.DataBean;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private AppPreferences appPrefs;
    RadioGroup radioGroup;
    RadioButton radioButton;
    CheckBox checkbox_monthly;
    Button buttonExport;
    EditText editTextExpiryDays,editTextPassWord;
    public String TAG = "Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        generateData();

        LocalDate dateAfter = LocalDate.now();
        LocalDate dateBefore = dateAfter.plusDays(1);
        int daysBetween = Days.daysBetween(dateAfter, dateBefore).getDays();

        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Password=editTextPassWord.getText().toString();
                String expiry=editTextExpiryDays.getText().toString();
                if(!(expiry.equals("")|| Password.equals(""))){
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) findViewById(selectedId);
                    String radioButtonText = (String) radioButton.getText();
                    int expiryDays= Integer.parseInt(expiry);

                    Params params = new Params();
                    params.setDbName("Practice");
                    params.setStoragePath("//DCIM");
                    params.setPackageName("com.example.hii.sqlitedb");
                    params.setPassword(Password);
                    params.setNoOfExpiryDays(expiryDays);

                    if (radioButtonText.equals("Daily")) {
                        Log.d(TAG, "Daily");
                        params.setSchedule(Params.Schedule.DAILY);
                    } else if (radioButtonText.equals("Weekly")) {
                        Log.d(TAG, "Weekly");
                        params.setSchedule(Params.Schedule.WEEKLY);
                    } else if (radioButtonText.equals("Monthly")) {
                        Log.d(TAG, "Monthly");
                        params.setSchedule(Params.Schedule.MONTHLY);
                    }
                    if (checkbox_monthly.isChecked()) {
                        Log.d(TAG, "Checked");
                        params.setKeepMonthlyBackup(true);
                    }
                    else {
                        Log.d(TAG, "UnChecked");
                        params.setKeepMonthlyBackup(false);
                    }
                    BackupService backupService = new BackupService(MainActivity.this);
                    backupService.setupAlarm(params);
                }
                else {
                    Toast.makeText(MainActivity.this,"Please enter expiry days and Password First!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        checkbox_monthly = (CheckBox) findViewById(R.id.checkBox_monthly);
        buttonExport = (Button) findViewById(R.id.mybutton_export);
        editTextExpiryDays = (EditText) findViewById(R.id.editText_noOfExpiryDays);
        editTextPassWord= (EditText) findViewById(R.id.editText_password);
        appPrefs = new AppPreferences(this);
        appPrefs.saveUserID("001");
    }

    private void generateData() {
        DAL dal = new DAL(this);
        DataBean dataBean = new DataBean();
        dataBean.setCity("Karachi");//get value from edit text
        dataBean.setProvince("Sindh"); //get value from spinner.
        long id = dal.insertCountry(dataBean);


        DataBean dataBean1 = new DataBean();
        dataBean1.setCity("Peshawar");//get value from edit text
        dataBean1.setProvince("KPK"); //get value from spinner.
        long id1 = dal.insertCountry(dataBean1);
    }
}
