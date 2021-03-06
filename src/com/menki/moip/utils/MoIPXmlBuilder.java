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

package com.menki.moip.utils;

import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.xmlpull.v1.XmlSerializer;

import android.text.format.Time;
import android.util.Xml;

import com.menki.moip.paymentmethods.PagamentoDireto;
import com.menki.moip.paymentmethods.PagamentoDireto.OwnerIdType;

public class MoIPXmlBuilder 
{
	
	/**
	 * MoIP Payment TAGs
	 */
	private static final String TAG_ENVIARINSTRUCAO = "EnviarInstrucao";
	private static final String TAG_INSTRUCAOUNICA = "InstrucaoUnica";
	private static final String TAG_RAZAO = "Razao";
	private static final String TAG_VALORES = "Valores";
	private static final String TAG_VALOR = "Valor";
	private static final String TAG_IDPROPRIO = "IdProprio";
	private static final String TAG_PAGAMENTODIRETO = "PagamentoDireto";
	private static final String TAG_FORMA = "Forma";
	private static final String TAG_INSTITUICAO = "Instituicao";
	private static final String TAG_CARTAOCREDITO = "CartaoCredito";
	private static final String TAG_NUMERO = "Numero";
	private static final String TAG_EXPIRACAO = "Expiracao";
	private static final String TAG_CODIGOSEGURANCA = "CodigoSeguranca";
	private static final String TAG_PORTADOR = "Portador";
	private static final String TAG_NOME = "Nome";
	private static final String TAG_IDENTIDADE = "Identidade";
	private static final String TAG_TELEFONE = "Telefone";
	private static final String TAG_DATANASCIMENTO = "DataNascimento";
	private static final String TAG_PARCELAMENTO = "Parcelamento";
	private static final String TAG_PARCELAS = "Parcelas";
	private static final String TAG_RECEBIMENTO = "Recebimento";
	private static final String TAG_PAGADOR = "Pagador";
	//private static final String TAG_LOGINMOIP = "LoginMoIP";
	private static final String TAG_EMAIL = "Email";
	private static final String TAG_TELEFONECELULAR = "TelefoneCelular";
	//private static final String TAG_APELIDO = "Apelido";
	private static final String TAG_ENDERECOCOBRANCA = "EnderecoCobranca";
	private static final String TAG_LOGRADOURO = "Logradouro";
	private static final String TAG_COMPLEMENTO = "Complemento";
	private static final String TAG_BAIRRO = "Bairro";
	private static final String TAG_CIDADE = "Cidade";
	private static final String TAG_ESTADO = "Estado";
	private static final String TAG_PAIS = "Pais";
	private static final String TAG_CEP = "CEP";
	private static final String TAG_TELEFONEFIXO = "TelefoneFixo";
	
	/**
	 * MoIP Payment Attributes
	 */
	private static final String ATTR_MOEDA = "moeda";
	private static final String ATTR_TIPO = "Tipo";
	
	/**
	 * <p>Creating XML Message for Direct Payment</p>
	 *
	 *	  <EnviarInstrucao>
	 *	  <InstrucaoUnica>
	 *	     <Razao>Pagamento direto com cartão de crédito</Razao>
	 *	     <Valores>
	 *	        <Valor moeda="BRL">213.25</Valor>
	 *	     </Valores>
	 *	     <IdProprio>dir_card_2</IdProprio>
	 *	     <PagamentoDireto>
	 *	        <Forma>CartaoCredito</Forma>
	 *	        <Instituicao>AmericanExpress</Instituicao>
	 *	        <CartaoCredito>
	 *	         <Numero>345678901234564</Numero>
	 *	         <Expiracao>08/11</Expiracao>
	 *	         <CodigoSeguranca>1234</CodigoSeguranca>
	 *	         <Portador>
	 *	             <Nome>Nome do Portador</Nome>
	 *	             <Identidade Tipo="CPF">111.111.111-11</Identidade>
	 *	             <Telefone>(11)1111-1111</Telefone>
	 *	             <DataNascimento>30/11/1980</DataNascimento>
	 *	         </Portador>
	 *	        </CartaoCredito>
	 *	        <Parcelamento>
	 *	         <Parcelas>2</Parcelas>
	 *	         <Recebimento>AVista</Recebimento>
	 *	        </Parcelamento>
	 *	     </PagamentoDireto>
	 *	     <Pagador>
	 *	         <Nome>Luiz Inácio Lula da Silva</Nome>
	 *	         <LoginMoIP>lula</LoginMoIP>
	 *	         <Email>presidente@planalto.gov.br</Email>
	 *	         <TelefoneCelular>(61)9999-9999</TelefoneCelular>
	 *	         <Apelido>Lula</Apelido>
	 *	         <Identidade>111.111.111-11</Identidade>
	 *	         <EnderecoCobranca>
	 *	             <Logradouro>Praça dos Três Poderes</Logradouro>
	 *	             <Numero>0</Numero>
	 *	             <Complemento>Palácio do Planalto</Complemento>
	 *	             <Bairro>Zona Cívico-Administrativa</Bairro>
	 *	             <Cidade>Brasília</Cidade>
	 *	             <Estado>DF</Estado>
	 *	             <Pais>BRA</Pais>
	 *	             <CEP>70100-000</CEP>
	 *	             <TelefoneFixo>(61)3211-1221</TelefoneFixo>
	 *	         </EnderecoCobranca>
	 *	     </Pagador>
	 *	  </InstrucaoUnica>
	 *	  </EnviarInstrucao>
	 * @param pagamentoDireto 
	 */
	public String getDirectPaymentMessage(PagamentoDireto pagamentoDireto)
	{
        XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		
		//Set payment type string
		String ownerIdType = null;
		if (pagamentoDireto.getOwnerIdType() == OwnerIdType.CPF)
		{
			ownerIdType = "CPF";
		}
		else
		{
			ownerIdType = "RG";
		}
			
	    try 
	    {
	        serializer.setOutput(writer);
	        
	        serializer.startDocument("UTF-8", true);
	        	serializer.startTag("", TAG_ENVIARINSTRUCAO);
	        		serializer.startTag("", TAG_INSTRUCAOUNICA);
	        			serializer.startTag("", TAG_RAZAO);
	        				serializer.text("Pagamento direto com cartao de credito");
	        			serializer.endTag("", TAG_RAZAO);
	        			serializer.startTag("", TAG_VALORES);
	        				serializer.startTag("", TAG_VALOR);
	        				serializer.attribute("", ATTR_MOEDA, "BRL");
	        					serializer.text(pagamentoDireto.getValue());
	        				serializer.endTag("", TAG_VALOR);
	        			serializer.endTag("", TAG_VALORES);
	        			serializer.startTag("", TAG_PAGAMENTODIRETO);
	        				serializer.startTag("", TAG_FORMA);
	        					serializer.text("CartaoCredito");
	        				serializer.endTag("", TAG_FORMA);
	        				serializer.startTag("", TAG_INSTITUICAO);
	        					serializer.text(pagamentoDireto.getBrand());
	        				serializer.endTag("", TAG_INSTITUICAO);
	        				serializer.startTag("", TAG_CARTAOCREDITO);
	        					serializer.startTag("", TAG_NUMERO);
	        						serializer.text(pagamentoDireto.getCreditCardNumber());
	        					serializer.endTag("", TAG_NUMERO);
	        					serializer.startTag("", TAG_EXPIRACAO);
	        						serializer.text(pagamentoDireto.getExpirationDate());
	        					serializer.endTag("", TAG_EXPIRACAO);
	        					serializer.startTag("", TAG_CODIGOSEGURANCA);
	        						serializer.text(pagamentoDireto.getSecureCode());
	        					serializer.endTag("", TAG_CODIGOSEGURANCA);
	        					serializer.startTag("", TAG_PORTADOR);
	        						serializer.startTag("", TAG_NOME);
	        							serializer.text(pagamentoDireto.getOwnerName());
	        						serializer.endTag("", TAG_NOME);
	        						serializer.startTag("", TAG_IDENTIDADE);
	        						serializer.attribute("", ATTR_TIPO, ownerIdType);
	        							serializer.text(pagamentoDireto.getOwnerIdNumber());
	        						serializer.endTag("", TAG_IDENTIDADE);
	        						serializer.startTag("", TAG_TELEFONE);
	        							serializer.text(pagamentoDireto.getCellPhone());
	        						serializer.endTag("", TAG_TELEFONE);
	        						serializer.startTag("", TAG_DATANASCIMENTO);
	        							serializer.text(pagamentoDireto.getOwnerBirthDate()); 
	        						serializer.endTag("", TAG_DATANASCIMENTO);
	        					serializer.endTag("", TAG_PORTADOR);
	        				serializer.endTag("", TAG_CARTAOCREDITO);
	        				serializer.startTag("", TAG_PARCELAMENTO);
	        					serializer.startTag("", TAG_PARCELAS);
	        							serializer.text(pagamentoDireto.getInstallmentsQuantity());
	        					serializer.endTag("", TAG_PARCELAS);
	        					//RECEBIMENTO refers to the seller, not the customer
	        					serializer.startTag("", TAG_RECEBIMENTO);
	        						String receb = "AVista";
	        						//if (details.get(R.id.payment_type).equalsIgnoreCase(String.valueOf(R.id.radio_installment_payment)))
	        							//receb = "APrazo";
	        						serializer.text(receb);
	        					serializer.endTag("", TAG_RECEBIMENTO);
	        				serializer.endTag("", TAG_PARCELAMENTO);
	        			serializer.endTag("", TAG_PAGAMENTODIRETO);
	        			serializer.startTag("", TAG_PAGADOR);
	        				serializer.startTag("", TAG_NOME);
	        					serializer.text(pagamentoDireto.getOwnerName());
	        				serializer.endTag("", TAG_NOME);
	        				//serializer.startTag("", TAG_LOGINMOIP);
	        					//serializer.text("LOGINMOIP");
	        				//serializer.endTag("", TAG_LOGINMOIP);
	        				serializer.startTag("", TAG_EMAIL);
	        					serializer.cdsect("xxxxx@email.com"/*details.get(R.id.email)*/);
	        				serializer.endTag("", TAG_EMAIL);
	        				serializer.startTag("", TAG_TELEFONECELULAR);
	        					serializer.text(pagamentoDireto.getCellPhone());
	        				serializer.endTag("", TAG_TELEFONECELULAR);
	        				//serializer.startTag("", TAG_APELIDO);
        						//serializer.text("APELIDO");
        					//serializer.endTag("", TAG_APELIDO);
	        				serializer.startTag("", TAG_IDENTIDADE);
        						serializer.text(pagamentoDireto.getOwnerIdNumber());
        					serializer.endTag("", TAG_IDENTIDADE);
        					serializer.startTag("", TAG_ENDERECOCOBRANCA);
        						serializer.startTag("", TAG_LOGRADOURO);
        							serializer.text(pagamentoDireto.getStreetAddress());
        						serializer.endTag("", TAG_LOGRADOURO);
        						serializer.startTag("", TAG_NUMERO);
        							//Integer nro = Integer.parseInt(details.get(R.id.street_number));
    								serializer.text(pagamentoDireto.getStreetNumberAddress()/*nro.toString( )*/);
    							serializer.endTag("", TAG_NUMERO);
    							serializer.startTag("", TAG_COMPLEMENTO);
									serializer.text(pagamentoDireto.getAddressComplement());
								serializer.endTag("", TAG_COMPLEMENTO);
								serializer.startTag("", TAG_BAIRRO);
									serializer.text(pagamentoDireto.getNeighborhood());
								serializer.endTag("", TAG_BAIRRO);
								serializer.startTag("", TAG_CIDADE);
									serializer.text(pagamentoDireto.getCity());
								serializer.endTag("", TAG_CIDADE);
								serializer.startTag("", TAG_ESTADO);
									serializer.text(pagamentoDireto.getState());
								serializer.endTag("", TAG_ESTADO);
								serializer.startTag("", TAG_PAIS);
									serializer.text("BRA"); //pagamentoDireto.getCountry()
								serializer.endTag("", TAG_PAIS);
								serializer.startTag("", TAG_CEP);
									serializer.text(pagamentoDireto.getZipCode());
								serializer.endTag("", TAG_CEP);
								serializer.startTag("", TAG_TELEFONEFIXO);
									serializer.text(pagamentoDireto.getFixedPhone());
								serializer.endTag("", TAG_TELEFONEFIXO);
							serializer.endTag("", TAG_ENDERECOCOBRANCA);
	        			serializer.endTag("", TAG_PAGADOR);
		        	

		        	Time now = new Time();
		        	String hash = MoIPXmlBuilder.md5(serializer.toString() + now.toString());
					serializer.startTag("", TAG_IDPROPRIO);
						serializer.text(hash); //TODO: check meaning
					serializer.endTag("", TAG_IDPROPRIO);
				serializer.endTag("", TAG_INSTRUCAOUNICA);
	        serializer.endTag("", TAG_ENVIARINSTRUCAO);
	        serializer.endDocument();
	        
	        serializer.flush();
	        writer.close();
	    }
	    catch(Exception e) 
	    {
			e.printStackTrace();
	    }
	    
		return writer.toString();
	}
	
	
	public static final String md5(final String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest
	                .getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	 
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();
	 
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	//TODO: implement getXXXXPaymentMessage( )
}
