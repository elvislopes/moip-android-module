/**
 * Menki Mobile Solutions
 * http://www.menkimobile.com.br
 * 
 * @author Augusto Souza
 *
 */

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
//	private static final String TAG = "PayerActivity";
	
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
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payer);
        
        setViews();
        setDefaultValues();
        setListeners();  
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case(R.id.payer_next_step):
			// Set payment objects and persist them
			setPayment();
			
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
	
	private void setDefaultValues() {
		RadioButton itemToCheck;
		PaymentMgr paymentMgr = PaymentMgr.getInstance();
		paymentMgr.restorePaymentDetails(this);
		PaymentDetails paymentDetails = paymentMgr.getPaymentDetails(); 
		
		fullName.setText(paymentDetails.getFullName());
		email.setText(paymentDetails.getEmail());
		cellPhone.setText(paymentDetails.getCellPhone());
		
		for(int i=0; i < identificationType.getChildCount(); i++) {
			itemToCheck = (RadioButton) identificationType.getChildAt(i);
			if (itemToCheck.getText().toString().equals(paymentDetails.getPayerIdentificationType())) {
				itemToCheck.setChecked(true);
				break;
			}
		}
		
		identificationNumber.setText(paymentDetails.getPayerIdentificationNumber());
		streetAddress.setText(paymentDetails.getStreetAddress());
		
		if (paymentDetails.getStreetNumber() > -1)
			streetNumber.setText(String.valueOf(paymentDetails.getStreetNumber()));
		
		streetComplement.setText(paymentDetails.getStreetComplement());
		neighborhood.setText(paymentDetails.getNeighborhood());
		city.setText(paymentDetails.getCity());
		
		for(int i=0; i < state.getCount(); i++) {
			if (state.getItemAtPosition(i).toString().equals(paymentDetails.getState())) {
				state.setSelection(i);
				break;
			}
		}
		
		zipCode.setText(paymentDetails.getZipCode());
		fixedPhone.setText(paymentDetails.getFixedPhone());
	}
	
	private void setListeners() {
		nextStep.setOnClickListener(this);
	}
	
	private void setPayment() {
		RadioButton checkedItem;
		PaymentMgr paymentMgr = PaymentMgr.getInstance();
		PaymentDetails paymentDetails = paymentMgr.getPaymentDetails();
		
		paymentDetails.setFullName(fullName.getEditableText().toString());
		paymentDetails.setEmail(email.getEditableText().toString());
		paymentDetails.setCellPhone(cellPhone.getEditableText().toString());
		
		checkedItem = (RadioButton) findViewById(identificationType.getCheckedRadioButtonId());
		paymentDetails.setPayerIdentificationType(checkedItem.getText().toString());
		
		paymentDetails.setPayerIdentificationNumber(identificationNumber.getEditableText().toString());
		paymentDetails.setStreetAddress(streetAddress.getEditableText().toString());
		
		String streeNumberStr = streetNumber.getEditableText().toString().trim();
		if (streeNumberStr.length() != 0) {
			int n = Integer.parseInt(streeNumberStr);
			paymentDetails.setStreetNumber(n);
		}
		
		paymentDetails.setStreetComplement(streetComplement.getEditableText().toString());
		paymentDetails.setNeighborhood(neighborhood.getEditableText().toString());
		paymentDetails.setCity(city.getEditableText().toString());
		
		paymentDetails.setState(state.getSelectedItem().toString());
		
		paymentDetails.setZipCode(zipCode.getEditableText().toString());
		paymentDetails.setFixedPhone(fixedPhone.getEditableText().toString());

		paymentMgr.savePaymentDetails(this);
	}

	
	private void showSummaryDialog( )
	{
		StringBuilder builder = new StringBuilder( );
		String separator1 = new String(" ");
		String separator2 = new String("\n");
		builder.append(separator2);
		PaymentMgr mgr = PaymentMgr.getInstance( );
		PaymentDetails details = mgr.getPaymentDetails( );
		
		//set up dialog
        Dialog summary = new Dialog(this);
        summary.setContentView(R.layout.payment_summary);
        summary.setTitle(R.string.paymentSummary);
        summary.setCancelable(true);

        //set up text
        TextView summaryTextView = (TextView) summary.findViewById(R.id.SummaryTextView);
        builder.append(getString(R.string.full_name));
        	builder.append(separator1);
        	builder.append(details.getFullName( ));
        	builder.append(separator2);
        	//CPF or RG
        builder.append(details.getPayerIdentificationType( ));
    		builder.append(":");
    		builder.append(separator1);
        	builder.append(details.getPayerIdentificationNumber( ));
        	builder.append(separator2);
        builder.append(getString(R.string.brand));
        	builder.append(separator1);
        	builder.append(details.getBrand( ));
        	builder.append(separator2);
        builder.append(getString(R.string.credit_card_number));
        	builder.append(separator1);
        	builder.append(details.getCreditCardNumber( ));
        	builder.append(separator2);
        builder.append(getString(R.string.expiration_date));
        	builder.append(separator1);
        	builder.append(details.getExpirationDate( ));
        	builder.append(separator2);
        builder.append(getString(R.string.secure_code));
        	builder.append(separator1);
        	builder.append(details.getSecureCode( ));
        	builder.append(separator2);
        builder.append(getString(R.string.payment_type));
        	builder.append(separator1);
        	builder.append(details.getPaymentType( ));
        	builder.append(separator2);
             
        summaryTextView.setText(builder.toString( ));
        
        //set up image view
        ImageView moipImg = (ImageView) summary.findViewById(R.id.SummaryImageView);
        moipImg.setImageResource(R.drawable.moiplabs);
        
        //set up buttom
        Button finishButton = (Button) summary.findViewById(R.id.FinishButton);
        finishButton.setOnClickListener(new OnClickListener() 
        {
        	@Override
        	public void onClick(View v) 
        	{
    			// Go to payment transaction depending on payment type
    			Intent intent = new Intent(getApplicationContext( ), DirectPaymentTransaction.class);
    			startActivity(intent);
    			Log.w("MENKI [Payer] ", "finishButton - onClick( ):");
        	}
        });
            
        summary.show();
	}

}
