package server;

import java.io.Serializable;

public class Mensagem implements Serializable {

	private static final long serialVersionUID = -8867854099306604818L;
	
	
	private String destino;
	private String mensagem;
	private String origem;
	
	public Mensagem(String mensagem, String origem,String destino) {
		
		this.destino = destino;
		this.mensagem = mensagem;
		this.origem=origem;
	}


	public synchronized  String getDestinarario() {
		return destino;
	}


	public synchronized  String getMensagem() {
		return mensagem;
	}
	
	public synchronized  String getOrigem(){
		return origem;
	}
	


}
