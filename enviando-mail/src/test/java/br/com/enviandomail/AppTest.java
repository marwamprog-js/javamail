package br.com.enviandomail;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

	private String userName = ""; //endereço de email que vai enviar os emails
	private String senha = ""; //Senha do endereco de email

	@Test
	public void testeEmail() {


		try {
			StringBuilder builder = new StringBuilder();

			builder.append("Olá, <br/><br/>");
			builder.append("Você está recebendo o acesso ao curso de Java. <br/><br/>");
			builder.append("Para ter acesso clique no botão abaixo. <br/><br/>");
			builder.append("<a href=\"https://www.devmedia.com.br/cursos/java\" target=\"_blank\" >Acessar portal</a>");

			ObjetoEnviaEmail enviaEmail = 
					new ObjetoEnviaEmail(
							"", 
							"Programador Java Teste Email", 
							"Testando email com o Java", 
							builder.toString()
							); 

			enviaEmail.enviarEmailAnexo(true);


			/*
			 * Caso o email não esteja sendo enviado então
			 * coloque um tempo de espera, mas isso só pode ser 
			 * usado para testes.
			 * */
//			Thread.sleep(5000);
		}catch (Exception e) {
			e.printStackTrace();
		}





	}
}
