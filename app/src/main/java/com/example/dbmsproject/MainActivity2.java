package com.example.dbmsproject;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    TextView etMessage;
    EditText etTelNr;
    int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    String SENT="SMS_SENT";
    String DELIVERED="SMS_DELIVERED";
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        etMessage=findViewById(R.id.EtMessage);
        String name=getIntent().getStringExtra("name");
        String cgpa=getIntent().getStringExtra("cgpa");
        etMessage.setText("Your ward " + name + " has " + cgpa +" GPA this semester");
        etTelNr = (EditText) findViewById(R.id.etTelNr);

        sentPI=PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        deliveredPI=PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);
    }
    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        smsSentReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity2.this,"SMS Sent!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity2.this, "Generic Failure!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity2.this, "No Service!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(MainActivity2.this, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(MainActivity2.this, "Radio Off!", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };
        smsDeliveredReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered!", Toast.LENGTH_SHORT).show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }


    public void btn_SendSMS_OnClick(View v)
    {
        String message = etMessage.getText().toString();
        String telNr= etTelNr.getText().toString();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else
        {
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(telNr,null,message,sentPI,deliveredPI);
        }
    }

}

