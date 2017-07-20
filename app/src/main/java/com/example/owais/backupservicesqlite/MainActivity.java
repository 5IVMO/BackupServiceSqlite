package com.example.owais.backupservicesqlite;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backupservice.AppPreferences;
import com.example.backupservice.BackupService;
import com.example.backupservice.BackupnRestore;
import com.example.backupservice.Params;
import com.example.owais.backupservicesqlite.DB.DAL;
import com.example.owais.backupservicesqlite.DB.DBConnect;
import com.example.owais.backupservicesqlite.DB.DataBean;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private static final int PICKFILE_RESULT_CODE = 1;
    private AppPreferences appPrefs;
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
        generateData();

        LocalDate dateAfter = LocalDate.now();
        LocalDate dateBefore = dateAfter.plusDays(1);
        int daysBetween = Days.daysBetween(dateAfter, dateBefore).getDays();

        checkBox_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox_encrypt.isChecked()) {
                    textViewPassword.setVisibility(View.VISIBLE);
                    editTextPassWord.setVisibility(View.VISIBLE);
                    requiredPassword = true;
                } else {
                    textViewPassword.setVisibility(View.INVISIBLE);
                    editTextPassWord.setVisibility(View.INVISIBLE);
                    requiredPassword = false;
                }
            }
        });
        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        params.setAppName("Sample Application");
                        params.setDbName("Practice");
                        params.setStoragePath("//DCIM");
                        params.setPackageName("com.example.hii.sqlitedb");
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
                        } else {
                            Log.d(TAG, "UnChecked");
                            params.setKeepMonthlyBackup(false);
                        }
                        if (checkBox_encrypt.isChecked()) {
                            Log.d(TAG, "Checked");
                            params.setEncryptDB(true);
                            params.setPassword(Password);
                        } else {
                            Log.d(TAG, "UnChecked");
                            params.setEncryptDB(false);
                            params.setPassword("");
                        }
                        BackupService backupService = new BackupService(MainActivity.this);
                        backupService.setupAlarm(params);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter expiry days!", Toast.LENGTH_LONG).show();
                }

            }
        });

        buttonImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                try {
                    startActivityForResult(intent, PICKFILE_RESULT_CODE);
                } catch (ActivityNotFoundException e) {
                    Log.d(TAG, e.toString());
                }
            }
        });

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Fix no activity available
        if (data == null)
            return;
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    //FilePath is your file as a string
                    final String FilePath = data.getData().getPath();
                    String extension = "";
                    final String DBPath = getDatabasePath(DBConnect.DB_NAME).getPath();
                    if (FilePath.contains(".")) {
                        extension = FilePath.substring(FilePath.lastIndexOf("."));
                    }

                    if (!TextUtils.isEmpty(extension) && extension.equals(".db")) {
                        Util.ToastShort(MainActivity.this, "DB file");

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setTitle("Remove Old Database");
                        builder1.setIcon(R.drawable.ic_warning_black_24dp);
                        builder1.setMessage("Are you Sure You want to remove old Database ?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            new BackupnRestore().importDB(FilePath, DBPath);
                                        } catch (Exception e) {
                                            Util.ToastLong(MainActivity.this, e.toString());
                                        }
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                        Toast.makeText(MainActivity.this,
                                radioButton.getText(), Toast.LENGTH_SHORT).show();
                    } else {
                        Util.ToastLong(MainActivity.this, "Please select .db file");
                    }

                }
        }
    }
}
