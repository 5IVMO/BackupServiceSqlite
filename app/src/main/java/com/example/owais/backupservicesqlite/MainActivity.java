package com.example.owais.backupservicesqlite;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AppPreferences appPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appPrefs = new AppPreferences(this);
        appPrefs.saveUserID("001");
        generateData();
        setupAlarm();
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

    public void setupAlarm(){
        Intent intentService = new Intent(this,TimerReceiver.class);

        Params params = new Params();
        params.setDbName("Practice");
        params.setStoragePath("//DCIM");
        params.setSchedule("Schedule");
        params.setPackageName("com.example.hii.sqlitedb");
        params.setPassword("12345");
        params.setNoOfExpiryDays(1);


        Bundle bundle = new Bundle();
        bundle.putParcelable("params", params);
        intentService.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentService, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 16); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 12);
        calendar.set(Calendar.SECOND, 0);
        final AlarmManager alarmManager = (AlarmManager)(getSystemService( Context.ALARM_SERVICE ));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
       // alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+ 10 * 1000, 55 * 1000, pendingIntent);

    }
}
