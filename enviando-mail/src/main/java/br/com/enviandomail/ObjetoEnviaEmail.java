package br.com.enviandomail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ObjetoEnviaEmail {

	private String userName = "";
	private String senha = "";
	private String listaDestinatarios = "";
	private String nomeRemetente = "";
	private String assuntoEmail = "";
	private String textoEmail = "";


	public ObjetoEnviaEmail(String listaDestinatario, String nomeRemetente, String assuntoEmail, String textoEmail) {
		this.listaDestinatarios = listaDestinatario;
		this.nomeRemetente = nomeRemetente;
		this.assuntoEmail = assuntoEmail;
		this.textoEmail = textoEmail;
	}


	/*
	 * Método para enviar e-mail com TEXTO SIMPLES
	 * ou HTML
	 * */
	public void enviarEmail(boolean envioHtml) throws Exception {

		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true"); //Autorização
		properties.put("mail.smtp.starttls", "true"); //Autenticação
		properties.put("mail.smtp.host", "smtp.gmail.com"); //Servidor gmail Google
		properties.put("mail.smtp.port", "465"); //Porta do servidor
		properties.put("mail.smtp.socketFactory.port", "465"); //Expecifica a porta a ser conectada pelo socket
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //Classe socket de conexao ao SMTP;

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(userName, senha);
			}
		});

		Address[] toUser = InternetAddress.parse(listaDestinatarios);

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName, nomeRemetente));
		message.setRecipients(Message.RecipientType.TO, toUser); //Email de destino
		message.setSubject(assuntoEmail); //Assunto do E-mail
		message.setText(textoEmail);

		if(envioHtml) {
			message.setContent(textoEmail, "text/html; charset=utf-8");
		} else {
			message.setText(textoEmail);
		}

		Transport.send(message);

	}


	
	/*
	 * Método para enviar e-mail HTML e ANEXO
	 * */
	public void enviarEmailAnexo(boolean envioHtml) throws Exception {

		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true"); //Autorização
		properties.put("mail.smtp.starttls", "true"); //Autenticação
		properties.put("mail.smtp.host", "smtp.gmail.com"); //Servidor gmail Google
		properties.put("mail.smtp.port", "465"); //Porta do servidor
		properties.put("mail.smtp.socketFactory.port", "465"); //Expecifica a porta a ser conectada pelo socket
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //Classe socket de conexao ao SMTP;

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(userName, senha);
			}
		});

		Address[] toUser = InternetAddress.parse(listaDestinatarios);

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName, nomeRemetente));
		message.setRecipients(Message.RecipientType.TO, toUser); //Email de destino
		message.setSubject(assuntoEmail); //Assunto do E-mail
		message.setText(textoEmail);

		
		/*
		 * Parte 1 do email = Texto e descrição
		 * */
		MimeBodyPart corpoEmail = new MimeBodyPart();
		
		if(envioHtml) {
			corpoEmail.setContent(textoEmail, "text/html; charset=utf-8");
		} else {
			corpoEmail.setText(textoEmail);
		}
		
		List<FileInputStream> arquivos = new ArrayList<FileInputStream>();
		arquivos.add(simuladorDePDF());
		arquivos.add(simuladorDePDF());
		arquivos.add(simuladorDePDF());
		arquivos.add(simuladorDePDF());
		
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(corpoEmail);
		
		int index = 0;
		for(FileInputStream arquivo : arquivos) {
			
			/*
			 * Parte 2 do e-mail ANEXO
			 * */
			MimeBodyPart anexoEmail = new MimeBodyPart();
			
			anexoEmail.setDataHandler(new DataHandler(new ByteArrayDataSource(arquivo, "application/pdf")));
			anexoEmail.setFileName("anexoemail"+index+".pdf");
			
			multipart.addBodyPart(anexoEmail);
			
			index++;
		}	
		
		
		message.setContent(multipart);

		Transport.send(message);

	}




	/*
	 * Esse método simula o PDF ou qualquer
	 * arquivo que possa ser enviado por anexo no email.
	 * */
	private FileInputStream simuladorDePDF() throws Exception {
		Document document = new Document();
		File file = new File("fileanexo.pdf");
		file.createNewFile(); //Cria em tempo de execução
		PdfWriter.getInstance(document, new FileOutputStream(file));

		document.open();
		document.add(new Paragraph("Conteudo do PDF anexo com JavaMail"));
		document.close();

		return new FileInputStream(file);
	}


}
