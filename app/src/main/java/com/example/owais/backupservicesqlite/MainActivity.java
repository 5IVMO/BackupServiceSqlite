package com.example.owais.backupservicesqlite;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupservice.Backup;
import com.example.backupservice.Params;
import com.example.owais.backupservicesqlite.DB.DAL;
import com.example.owais.backupservicesqlite.DB.DBConnect;
import com.example.owais.backupservicesqlite.DB.DataBean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICKFILE_RESULT_CODE = 1;
    RadioGroup radioGroup;
    RadioButton radioButton;
    CheckBox checkbox_monthly, checkBox_encrypt;
    Button buttonExport, buttonImport;
    EditText editTextExpiryDays, editTextPassWord;
    TextView textViewPassword;
    Boolean requiredPassword = false;
    public String TAG = "Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        // generateData();

        checkBox_encrypt.setOnClickListener(this);
        buttonExport.setOnClickListener(this);
        buttonImport.setOnClickListener(this);

    }

    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        checkbox_monthly = (CheckBox) findViewById(R.id.checkBox_monthly);
        checkBox_encrypt = (CheckBox) findViewById(R.id.checkBox_encrypt);
        buttonExport = (Button) findViewById(R.id.mybutton_export);
        buttonImport = (Button) findViewById(R.id.mybutton_import);
        editTextExpiryDays = (EditText) findViewById(R.id.editText_noOfExpiryDays);
        editTextPassWord = (EditText) findViewById(R.id.editText_password);
        textViewPassword = (TextView) findViewById(R.id.password);
    }

    private void generateData() {
        DAL dal = new DAL(this);
        DataBean dataBean = new DataBean();
        dataBean.setCity("Lahore");//get value from edit text
        dataBean.setProvince("Punjab"); //get value from spinner.
        long id = dal.insertCountry(dataBean);


        DataBean dataBean1 = new DataBean();
        dataBean1.setCity("Peshawar");//get value from edit text
        dataBean1.setProvince("KPK"); //get value from spinner.
        long id1 = dal.insertCountry(dataBean1);

        DataBean dataBean2 = new DataBean();
        dataBean2.setCity("Karachi");//get value from edit text
        dataBean2.setProvince("Sindh"); //get value from spinner.
        long id2 = dal.insertCountry(dataBean2);


        DataBean dataBean3 = new DataBean();
        dataBean3.setCity("Quetta");//get value from edit text
        dataBean3.setProvince("Balochistan"); //get value from spinner.
        long id3 = dal.insertCountry(dataBean3);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Fix no activity available
        if (data == null)
            return;
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    //FilePath is your file as a string
                    final String FilePath = data.getData().getPath();
                    Backup backup = new Backup(MainActivity.this);
                    backup.importDB(FilePath, DBConnect.DB_NAME);
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mybutton_import:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                try {
                    startActivityForResult(intent, PICKFILE_RESULT_CODE);
                } catch (ActivityNotFoundException e) {
                    Log.d(TAG, e.toString());
                }
                break;

            case R.id.mybutton_export:
                String Password = editTextPassWord.getText().toString();
                String expiry = editTextExpiryDays.getText().toString();

                if (!(expiry.equals(""))) {
                    if (requiredPassword && Password.equals("")) {
                        Toast.makeText(MainActivity.this, "Please enter Password!", Toast.LENGTH_LONG).show();
                    } else {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(selectedId);
                        String radioButtonText = (String) radioButton.getText();
                        int expiryDays = Integer.parseInt(expiry);

                        Params params = new Params();
                        params.setDbName("Practice");
                        params.setStoragePath("//DCIM");
                        params.setNoOfExpiryDays(expiryDays);

                        if (radioButtonText.equals("Daily")) {
                            params.setSchedule(Params.Schedule.DAILY);
                        } else if (radioButtonText.equals("Weekly")) {
                            params.setSchedule(Params.Schedule.WEEKLY);
                        } else if (radioButtonText.equals("Monthly")) {
                            params.setSchedule(Params.Schedule.MONTHLY);
                        }
                        if (checkbox_monthly.isChecked()) {
                            params.setKeepMonthlyBackup(true);
                        } else {
                            params.setKeepMonthlyBackup(false);
                        }
                        if (checkBox_encrypt.isChecked()) {
                            params.setEncryptDB(true);
                            params.setPassword(Password);
                        } else {
                            params.setEncryptDB(false);
                            params.setPassword("");
                        }
                        Backup backup = new Backup(MainActivity.this);
                        backup.setupService(params);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter expiry days!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.checkBox_encrypt:
                if (checkBox_encrypt.isChecked()) {
                    textViewPassword.setVisibility(View.VISIBLE);
                    editTextPassWord.setVisibility(View.VISIBLE);
                    requiredPassword = true;
                } else {
                    textViewPassword.setVisibility(View.INVISIBLE);
                    editTextPassWord.setVisibility(View.INVISIBLE);
                    requiredPassword = false;
                }
                break;
        }
    }
}
