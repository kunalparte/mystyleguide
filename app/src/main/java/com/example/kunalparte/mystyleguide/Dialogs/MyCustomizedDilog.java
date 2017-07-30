package com.example.kunalparte.mystyleguide.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.kunalparte.mystyleguide.R;

/**
 * Created by kunalparte on 06/07/17.
 */

public class MyCustomizedDilog extends Dialog {
    Context context;
    TextView messageTV;
    TextView positiveButton;
    TextView negativeButton;
    public MyCustomizedDilog(@NonNull Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.my_dialog_layout);
        init();
    }
    public void init(){
        messageTV = (TextView) findViewById(R.id.dialogTitle);
        positiveButton = (TextView) findViewById(R.id.positiveButton);
        negativeButton = (TextView) findViewById(R.id.negativeButton);
    }

    public MyCustomizedDilog setDialogMessage(String message){
        messageTV.setText(message);
        return this;
    }

    public MyCustomizedDilog setPositivBtnText(String text){
        positiveButton.setText(text);
        return this;
    }

    public MyCustomizedDilog setNegativBtnText(String text){
        negativeButton.setText(text);
        return this;
    }

    public MyCustomizedDilog setPositivBtnOnClick(View.OnClickListener positivBtnOnClick){
        if (positivBtnOnClick == null){
            positivBtnOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            };
        }
        positiveButton.setOnClickListener(positivBtnOnClick);
        return this;
    }

    public MyCustomizedDilog setNegativBtnOnClick(View.OnClickListener negativBtnOnClick){
        if (negativBtnOnClick == null){
            negativBtnOnClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            };
        }
        negativeButton.setOnClickListener(negativBtnOnClick);
        return this;
    }

}
