package server;

import java.io.IOException;
import java.net.Socket;

public class TrataEnviar extends Thread {

	private Server s;
	private String nome;
	private Socket socket;
	private boolean isFirst=true;
	
	
	public TrataEnviar(Server s,String nome,Socket socket) {
		this.s = s;
		this.nome=nome;
		this.socket=socket;
	}

		   	
		
@Override
	public void run() {
		while(true){
			try {
				//a primeira vez que abro a thread verifica se tenho mensagens pendentes
				if(isFirst==true){
					for(int i=0; i<s.getMensagens().size();i++){
						
						if(s.getMensagens().get(i).getDestinarario().equals(nome)){ //se entra neste if é pq foi entregue
							s.getClientes().get(nome).writeObject(s.getMensagens().get(i)); //procura o destino no hash e envia pelo out dele
							System.out.println("envie a mensagem para o user"+ nome);
							
							//notificaçao
							String aQuemFoiEntregue=s.getMensagens().get(i).getDestinarario(); 
							String quemENotificado=s.getMensagens().get(i).getOrigem();          
							Mensagem m=new Mensagem("A Mensagem foi entregue ao: "+ aQuemFoiEntregue,"servidor",quemENotificado);
							s.getNotifica().add(m);
							notifyThreadNotifica();
							s.getMensagens().remove(i); //apaga a mensagem depois de enviada
							i--;
						}	 
					}
				}
				isFirst=false;
				
				
				synchronized(this){
					this.wait();
				}
				System.out.println("a thread do: "+ nome + " acordou");
				
				
				
				//corre o vetor e manda a msg para o seu destino
				for(int i=0; i<s.getMensagens().size();i++){
					if(s.getMensagens().get(i).getDestinarario().equals(nome)){ //se entra neste if é pq foi entregue
						s.getClientes().get(nome).writeObject(s.getMensagens().get(i)); //procura o destino no hash e envia pelo out dele
						System.out.println("envie a mensagem para o user"+ nome);
						
						
//						adiciona um mensagem de notificaçao a um array para ser enviado por outra thread
						String aQuemFoiEntregue=s.getMensagens().get(i).getDestinarario(); 
						String quemENotificado=s.getMensagens().get(i).getOrigem();          
						Mensagem m=new Mensagem("A Mensagem foi entregue ao : "+ aQuemFoiEntregue,"servidor",quemENotificado);
						s.getNotifica().add(m);		
						notifyThreadNotifica();
						s.getMensagens().remove(i);
						System.out.println("add a notificaçao ao array");
						
						
					} 
				}
			
			}catch (IOException | InterruptedException  e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				try {
					for(int i=0;i<s.getThreads().size();i++){
						if(s.getThreads().get(i).getNome().equals(nome))
						s.getThreads().remove(i);
						System.out.println("removi a thread do array");
					}
					socket.close();
					} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("erro no trataEnviar");
				break;
			}
		}
}


//notifica a thread que trata das notificações
public synchronized void notifyThreadNotifica(){
	for(int i=0; i<s.getNotifica().size();i++){
		String destino=s.getNotifica().get(i).getDestinarario(); //nome da thread que quero notificar
		for(int j=0; j<s.getThreadsNotifica().size();j++){
				if(destino.equals(s.getThreadsNotifica().get(j).getNome())){
				System.out.println("vou notificar a thread");
				
				synchronized(s.getThreadsNotifica().get(j)){
					s.getThreadsNotifica().get(j).notify();
					}
			}	
		}	

	}
}








public String getNome() {
	return nome;
}	
		

	


}