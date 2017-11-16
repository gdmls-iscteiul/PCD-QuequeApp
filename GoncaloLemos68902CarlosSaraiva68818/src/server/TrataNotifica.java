package server;

import java.io.IOException;
import java.net.Socket;

public class TrataNotifica extends Thread {

	private Server s;
	private String nome;
	private Socket socket;
	public TrataNotifica(Server s, String nome, Socket socket) {
		this.s = s;
		this.nome = nome;
		this.socket = socket;
	}
	
	public void run(){
		
		while(true){
			synchronized(this){
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("teste wait");
			for(int i=0; i<s.getNotifica().size();i++){
			
				if(s.getNotifica().get(i).getDestinarario().equals(nome)){ //se entra neste if é pq foi entregue
					System.out.println("entrei no if");
					try {
						s.getClientes().get(nome).writeObject(s.getNotifica().get(i));
						System.out.println("envieu a notificação para o user");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				
					s.getNotifica().remove(i); //apaga a mensagem depois de enviada
					i--;
					System.out.println("notifiquei o user: "+ nome);
				}	
			}
		}
	}

	public String getNome() {
		return nome;
	}	

}	
