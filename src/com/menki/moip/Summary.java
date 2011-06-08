/**
 * Copyright (c) 2011, MENKI MOBILE SOLUTIONS - http://www.menkimobile.com.br
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation and/or 
 *   other materials provided with the distribution.
 * * Neither the name of the MENKI MOBILE SOLUTIONS nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software without 
 *   specific prior written permission.
 *   
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 *  SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 *  TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 *  BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 *  ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF 
 *  SUCH DAMAGE. 
 *  
 *  @version 0.0.1
 */

package com.menki.moip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.menki.moip.paymentmethods.PagamentoDireto;
import com.menki.moip.paymentmethods.PagamentoDireto.OwnerIdType;

public class Summary extends Activity implements OnClickListener {
	private Button finish;
	private TextView summaryTextView;
	private ProgressDialog dialog;
	private PagamentoDireto pagamentoDireto;
	private PayTask payTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary);
		
		pagamentoDireto = getIntent().getParcelableExtra("PagamentoDireto");
		
		dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.waiting_server));
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		
		finish = (Button) findViewById(R.id.finish_button);
		finish.setOnClickListener(this);
		
		summaryTextView = (TextView) findViewById(R.id.SummaryTextView);
		summaryTextView.setText(summaryString());
		
		payTask = new PayTask();
	}

	public PagamentoDireto getPagamentoDireto() {
		return pagamentoDireto;
	}
	
	public void finishIt() {
		finish();
	}
	
    private class PayTask extends AsyncTask<String, Void, Void>{
		private final ProgressDialog dialog = new ProgressDialog(Summary.this);
		
		protected void onPreExecute(){
			this.dialog.setMessage("Por favor, aguarde...");
			this.dialog.show();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			getPagamentoDireto().pay();
			
			return null;
		}
		
		protected void onPostExecute(final Void unused){
			if(this.dialog.isShowing())
				this.dialog.dismiss();
			
			finishIt();
		}
	}

	private CharSequence summaryString() 
	{
		char separator1 = ' ';
		char separator2 = '\n';
		StringBuilder builder = new StringBuilder(separator2);
		
		//Set identification type string
		String idType = null;		
		if (this.pagamentoDireto.getOwnerIdType().equals(OwnerIdType.CPF))
		{
			idType = getString(R.string.cpf);
		}
		else
		{
			idType = getString(R.string.rg);
		}
		
		//Set payment type string
		String paymentType = null;
		if (Integer.parseInt(this.pagamentoDireto.getInstallmentsQuantity()) == 1)
		{
			paymentType = getString(R.string.cash_payment);
		}
		else
		{
			paymentType = getString(R.string.installment_payment);
		}
		
		
		builder.
			// Value
			append(getString(R.string.value) + separator1).
			append(String.valueOf(this.pagamentoDireto.getValue()) + separator2).
			//Full Name
			append(getString(R.string.owner_name) + separator1).
			append(this.pagamentoDireto.getOwnerName() + separator2). 
			//CPF or RG
			append(idType + ':' + separator1).
			append(this.pagamentoDireto.getOwnerIdNumber() + separator2).
	      	//Brand
	      	append(getString(R.string.brand) + separator1).
	    	append(this.pagamentoDireto.getBrand() + separator2).
	    	//Credit card number
	    	append(getString(R.string.credit_card_number) + separator1).
	    	append(this.pagamentoDireto.getCreditCardNumber() + separator2).
	    	//Expiration date
	    	append(getString(R.string.expiration_date) + separator1).
	    	append(this.pagamentoDireto.getExpirationDate() + separator2).
	    	//Secure code
	    	append(getString(R.string.secure_code) + separator1).
	    	append(this.pagamentoDireto.getSecureCode() + separator2).
	    	//Payment type
	    	append(getString(R.string.payment_type) + separator1).
	    	append(paymentType + separator2);
		
			if (Integer.parseInt(this.pagamentoDireto.getInstallmentsQuantity()) > 1)
			{
				builder.append(getString(R.string.installments) + separator1).
		    	append(this.pagamentoDireto.getInstallmentsQuantity() + separator2);
			}
    	
    	return builder;
	}

	public void onClick(View v) {
		switch(v.getId())
		{
			case(R.id.finish_button):
				//guiThread.post(updateTask);
				payTask.execute();
				break;
		}		
	}
}