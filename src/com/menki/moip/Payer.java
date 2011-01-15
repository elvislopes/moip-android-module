package com.menki.moip;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.TextView;

public class Payer extends Activity implements OnClickListener {
	private static final String TAG = "PayerActivity";
	
	private EditText fullName;
	private EditText email;
	private EditText cellPhone;
	private RadioGroup identificationType;
	private EditText identificationNumber;
	private EditText streetAddress;
	private EditText streetNumber;
	private EditText streetComplement;
	private EditText neighborhood;
	private EditText city;
	private Spinner state;
	private EditText zipCode;
	private EditText fixedPhone;
	private Button nextStep;
	private Payment payment;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payer);
        
        payment = (Payment) this.getIntent( ).getSerializableExtra("payment");
        setViews();
        setListeners();  
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case(R.id.payer_next_step):
			// Set payment object attributes and persist it
			setPayment();
			payment.save();
			
			showSummaryDialog( );
			break;
		}
	}
    
	private void setViews() {
		fullName = (EditText) findViewById(R.id.full_name);
		email = (EditText) findViewById(R.id.email);
		cellPhone = (EditText) findViewById(R.id.cell_phone);
		identificationType = (RadioGroup) findViewById(R.id.payer_identification_type);
		identificationNumber = (EditText) findViewById(R.id.payer_identification_number);
		streetAddress = (EditText) findViewById(R.id.street_address);
		streetNumber = (EditText) findViewById(R.id.street_number);
		streetComplement = (EditText) findViewById(R.id.street_complement);
		neighborhood = (EditText) findViewById(R.id.neighborhood);
		city = (EditText) findViewById(R.id.city);
		state = (Spinner) findViewById(R.id.state);
		zipCode = (EditText) findViewById(R.id.zip_code);
		fixedPhone = (EditText) findViewById(R.id.fixed_phone);
		nextStep = (Button) findViewById(R.id.payer_next_step);		
	}
	
	private void setListeners() {
		nextStep.setOnClickListener(this);
	}
	
	private void setPayment() {
		RadioButton checkedItem;
		
		payment.setFullName(fullName.getEditableText().toString());
		payment.setEmail(email.getEditableText().toString());
		payment.setCellPhone(cellPhone.getEditableText().toString());
		
		checkedItem = (RadioButton) findViewById(identificationType.getCheckedRadioButtonId());
		payment.setPayerIdentificationType(checkedItem.getText().toString());
		
		payment.setPayerIdentificationNumber(identificationNumber.getEditableText().toString());
		payment.setStreetAddress(streetAddress.getEditableText().toString());
		
		String streeNumberStr = streetNumber.getEditableText().toString().trim();
		if (streeNumberStr.length() != 0) {
			int n = Integer.parseInt(streeNumberStr);
			payment.setStreetNumber(n);
		}
		
		payment.setStreetComplement(streetComplement.getEditableText().toString());
		payment.setNeighborhood(neighborhood.getEditableText().toString());
		payment.setCity(city.getEditableText().toString());
		
		payment.setState(state.getSelectedItem().toString());
		
		payment.setZipCode(zipCode.getEditableText().toString());
		payment.setFixedPhone(fixedPhone.getEditableText().toString());
		
		PaymentMgr.getInstance( ).setPaymentDetails(payment);
	}

	
	private void showSummaryDialog( )
	{
		//set up dialog
        Dialog summary = new Dialog(this);
        summary.setContentView(R.layout.payment_summary);
        summary.setTitle("Payment Summary");
        summary.setCancelable(true);

        //set up text
        TextView summaryTextView = (TextView) summary.findViewById(R.id.SummaryTextView);
        summaryTextView.setText("SummarySummarySummarySummarySummarySummarySummarySummarySummarySummarySummarySummarySummarySummarySummarySummarySummarySummarySummarySummary");
        
        //set up image view
        ImageView moipImg = (ImageView) summary.findViewById(R.id.SummaryImageView);
        moipImg.setImageResource(R.drawable.moiplabs);
        
        //set up buttons
        Button returnButton = (Button) summary.findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new OnClickListener() 
        {
        	@Override
        	public void onClick(View v) 
        	{
        		
        	}
        });
        
        Button finishButton = (Button) summary.findViewById(R.id.FinishButton);
        finishButton.setOnClickListener(new OnClickListener() 
        {
        	@Override
        	public void onClick(View v) 
        	{
        		Log.w("MENKI [Payer] ", "finishButton - onClick( ):");
        	}
        });
            
        summary.show();
	}

}
