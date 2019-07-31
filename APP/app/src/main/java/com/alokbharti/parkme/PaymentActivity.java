package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {

    private TextView billAmount;
    private EditText couponCode;
    private Button couponCodeSubmitButton;
    private RadioButton googlePayRB;
    private Button finalSubmitButton;
    private String bill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initViews();

        bill = getIntent().getStringExtra("amount");
        billAmount.setText("Amount to be paid: "+bill);

        couponCodeSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coupon = couponCode.getText().toString();
                if(TextUtils.isEmpty(coupon)){
                    couponCode.setError("fill correct coupon code");
                    return;
                }

                if(coupon.equals("ParkMe20")){
                    double billToBePaid = Double.parseDouble(bill);
                    billToBePaid = billToBePaid - billToBePaid*(0.2);
                    billAmount.setText("Amount to be paid: "+ String.valueOf(billToBePaid));
                }
            }
        });

        finalSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(googlePayRB.isChecked()){
                    startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                }else {
                    Toast.makeText(PaymentActivity.this, "Please select at least one payment method", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initViews() {
        billAmount = findViewById(R.id.amount_to_be_paid_tv);
        couponCode = findViewById(R.id.coupon_code_et);
        couponCodeSubmitButton = findViewById(R.id.coupon_code_button);
        googlePayRB = findViewById(R.id.google_pay_radiobutton);
        finalSubmitButton = findViewById(R.id.payment_done_button);
    }
}
