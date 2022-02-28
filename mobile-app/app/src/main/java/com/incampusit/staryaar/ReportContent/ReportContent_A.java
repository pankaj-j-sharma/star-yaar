package com.incampusit.staryaar.ReportContent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.incampusit.staryaar.Home.Home_Get_Set;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.API_CallBack;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportContent_A extends AppCompatActivity {

    Home_Get_Set item;
    RadioGroup reportoptions;
    Button submit;
    EditText comments;
    Map<String, Boolean> options = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_content);

        options.put("Copyright Infringment", false);
        options.put("Harmful dangerous acts", false);
        options.put("Erotic or vulgar content", false);
        options.put("Infringes my rights", true);
        options.put("Fraud or Spam", false);
        options.put("Violent or repulsive content", false);
        options.put("Unauthorized use of the post", true);
        options.put("Improper behaviour for juveniles", false);
        options.put("Others, please specify below", true);

        Intent intent = getIntent();
        if (intent.hasExtra("data")) {
            item = (Home_Get_Set) intent.getSerializableExtra("data");
        }

        reportoptions = findViewById(R.id.report_options);
        submit = findViewById(R.id.btnsubmitreport);
        submit.setEnabled(false);
        comments = findViewById(R.id.editTextCommentReport);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportContent_A.this, "Submitted yout report", Toast.LENGTH_SHORT).show();
                RadioButton rdreport = (RadioButton) findViewById(reportoptions.getCheckedRadioButtonId());
                new AlertDialog.Builder(ReportContent_A.this)
                        .setTitle("Report content : " + rdreport.getText().toString())
                        .setMessage("Are you sure you want to report content ?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Send_Report_Content(item, comments.getText().toString(), rdreport.getText().toString());
                            }
                        })
                        .show();
            }
        });

        comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(ReportContent_A.this, String.valueOf(count), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(Variables.TAG, "character " + s.toString());
                if (s.toString().trim().length() >= 30) {
                    submit.setEnabled(true);
                    submit.setVisibility(View.VISIBLE);
                } else {
                    submit.setEnabled(false);
                    submit.setVisibility(View.GONE);
                    //Toast.makeText(ReportContent_A.this, "Please enter atleast 30 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reportoptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rdreport = (RadioButton) group.findViewById(checkedId);
                if (comments.getText().toString().trim().length() > 30) {
                    submit.setEnabled(true);
                    submit.setVisibility(View.VISIBLE);
                } else {
                    if (options.get(rdreport.getText().toString())) {
                        submit.setEnabled(false);
                        submit.setVisibility(View.GONE);
                        //Toast.makeText(ReportContent_A.this, "Please specify comments with the option", Toast.LENGTH_SHORT).show();
                    } else {
                        submit.setEnabled(true);
                        submit.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void Send_Report_Content(Home_Get_Set item, String comments, String reportoption) {
        Log.d(Variables.TAG, "Report => " + item.fb_id + " " + item.video_description + " " + comments + " " + reportoption);
        Functions.Call_Api_For_Send_Report_Content(this, item, comments, reportoption, new API_CallBack() {
            @Override
            public void ArrayData(ArrayList arrayList) {
                // show alert builder to confirm the report has been sent
            }

            @Override
            public void OnSuccess(String responce) {
                new AlertDialog.Builder(ReportContent_A.this)
                        .setTitle("Report content Success")
                        .setMessage(responce)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        })
                        .show();
            }

            @Override
            public void OnFail(String responce) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        finish();
    }
}
