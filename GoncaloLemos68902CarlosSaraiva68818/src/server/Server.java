package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Server {

public static final int port=8080;
private ObjectInputStream in;
private ObjectOutputStream out;
private Socket connection;


private ArrayList<Mensagem> mensagens=new ArrayList<Mensagem>();
private HashMap<String,ObjectOutputStream> clientes=new HashMap<String,ObjectOutputStream>(); //key nome object out 
private ArrayList<ObjectOutputStream>broadCast=new ArrayList<ObjectOutputStream>();
private ArrayList <Mensagem> notifica=new ArrayList <Mensagem>();
private ArrayList <TrataEnviar> threads=new ArrayList <TrataEnviar>();
private ArrayList <TrataNotifica> threadsNotifica=new ArrayList <TrataNotifica>();

//inicia o servidor e espera que o cliente se conecte, depois trata-o
public void startServing() throws Exception{
	
	ServerSocket s = new ServerSocket(3805);
    System.out.println("porta 8080 esta aberta!");
	//TrataNotifica n=new TrataNotifica(this);
	//n.start();
	 try {
		 while(true){
			 connection= s.accept();
			 
			 System.out.println("Nova conexão com o cliente " +  connection.getInetAddress().getHostAddress());
			 System.out.println(connection.getLocalAddress());
		 
			 
			 out = new ObjectOutputStream(connection.getOutputStream());
			 in=new ObjectInputStream(connection.getInputStream());
			 
			 Mensagem m=(Mensagem) in.readObject();
			 if(m.getMensagem().equals("/id")){   
				 
				 //adiciona um cliente ao hashmap em que o objecto é o output desse cliente
				 clientes.put(m.getOrigem(),out);
				 System.out.println("adicionei o cliente ao hashmap");
				 
				 //manda uma mensagem para todos os users a dizer quem esta agora online
					for (Map.Entry<String,ObjectOutputStream> entry : getClientes().entrySet()) {
					    String destinatario = entry.getKey();
					    ObjectOutputStream destinoOUT = entry.getValue();
					    destinoOUT.writeObject(new Mensagem("O user: "+ m.getOrigem()+ " esta agora online \n","servidor","null"));
					}
					
				}
			 
			
			 //cria as threads
			
			TrataReceber trata= new TrataReceber(in,this,connection);
			trata.start();
			TrataEnviar trata2 = new TrataEnviar(this, m.getOrigem(),connection);	
			trata2.start();
			threads.add(trata2);
			TrataNotifica trata3=new TrataNotifica(this,m.getOrigem(),connection);
			trata3.start();
			threadsNotifica.add(trata3);
			
		 } 
		
	} finally {
			
			 connection.close();
			 System.out.println("ficou of");	
			 
			 System.out.println("a fechar a conecção...");
			 { s.close(); }}
}	

	//verifica se ha mensagens para alguma thread que chame esta função, se sim manda e volta a wait, se nao fica em wait
public synchronized void endWait(String destino) throws InterruptedException{
	int w=0;
	for(int i=0; i<mensagens.size();i++){
		if(mensagens.get(i).getDestinarario().equals(destino)){
			w++;
		}		
			if(w==0){
				wait();
			}
	}
			
} 
	
//public synchronized void notifyOneThread(){
//	//dar uma listas de threads
//			
//	for(int i=0; i<mensagens.size();i++){
//		String destino=mensagens.get(i).getDestinarario(); //nome da thread que quero notificar
//		for(int j=0; j<threads.size();j++){
//			if(destino.equals(threads.get(i))){
//				threads.get(i).setWait(false);
//				threads.get(i).notify();
//			}
//		}
//	}
//}

//public synchronized ArrayList<ObjectOutputStream> getBroadcast() {
//	return broadCast;
//}


public synchronized HashMap<String, ObjectOutputStream> getClientes() {
	return clientes;
}

public synchronized void addClientes(String nome,ObjectOutputStream out) {
	clientes.put(nome, out);
	notifyAll();
}

public synchronized ArrayList<Mensagem> getMensagens() {
	return mensagens;
}

public synchronized void addMensagem(Mensagem m) {
	mensagens.add(m);
	notifyAll();
}

public ArrayList<Mensagem> getNotifica() {
	return notifica;
}

public static int getPort() {
	return port;
}

public ObjectInputStream getIn() {
	return in;
}

public ObjectOutputStream getOut() {
	return out;
}




public ArrayList<TrataNotifica> getThreadsNotifica() {
	return threadsNotifica;
}

public static void main(String[]args) throws Exception{
	try{
		new Server().startServing();
	}catch (IOException e){
	}
	
	
	}

public Socket getConnection() {
	return connection;
}

public ArrayList<TrataEnviar> getThreads() {
	return threads;
}



}

