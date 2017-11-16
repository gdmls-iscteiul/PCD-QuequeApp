package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class TrataReceber extends Thread {


private ObjectInputStream in;
private Server servidor;
private Socket socket;

public TrataReceber(ObjectInputStream in, Server servidor,Socket socket) {
  this.in = in;
  this.servidor = servidor;
  this.socket=socket;
  
}

public void run() {

		while(true){
			try {
		
			Mensagem msg = (Mensagem)in.readObject();
			
			//se a mensagem for /fim ele tira o cliente do hash map
			if(msg.getMensagem().equals("/FIM")){
				System.out.println("removi o seguinte user do server: "+ msg.getOrigem());
				servidor.getClientes().remove(msg.getOrigem());
				for(int i=0;i<servidor.getThreads().size();i++){
					if(servidor.getThreads().get(i).getNome().equals(msg.getOrigem())){
						servidor.getThreads().remove(i);
						System.out.println("removi a thread do array");
					}	
				}
			
				for(int i=0;i<servidor.getThreadsNotifica().size();i++){
					if(servidor.getThreadsNotifica().get(i).getNome().equals(msg.getOrigem())){
						servidor.getThreadsNotifica().remove(i);
						System.out.println("removi a threadNotifica do array");
					}	
				}	
				
					
			
			}
			
			
			
			
			
			
			
			
			//recebe a mensagem e adiciona a um vetor
			else{
				System.out.println("Recebi uma mensagem: "+ msg.getMensagem()+ " do user "+ msg.getOrigem() + " para o user "+ msg.getDestinarario());
				if(msg.getMensagem()!=null){
					servidor.addMensagem(msg);
					System.out.println("adicionei a mensagem recebida ao arrayList");
					notifyThread();
					
				}	
			}
		
		
			
		} catch (IOException e) {
			try {
				
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
  } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		
  }
		}
    
  }
	

public synchronized void notifyThread(){
	for(int i=0; i<servidor.getMensagens().size();i++){
		String destino=servidor.getMensagens().get(i).getDestinarario(); //nome da thread que quero notificar
		for(int j=0; j<servidor.getThreads().size();j++){
				System.out.println(servidor.getThreads().size());
			if(destino.equals(servidor.getThreads().get(j).getNome())){
				System.out.println("vou notificar a thread");
				
				synchronized(servidor.getThreads().get(j)){
					servidor.getThreads().get(j).notify();
					}
			}	
		}	

	}
}












public  ObjectInputStream getIn(){
		return in;
	}

}

