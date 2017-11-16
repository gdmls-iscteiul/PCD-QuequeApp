package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.swing.DefaultListModel;

import Layout.ContactosInterface;

public class Client {
	
	private ObjectOutputStream out;
	private ObjectInputStream in;  //	NAO SEI SE FAZ MAL TER TIPOS DIFERENTES
	private Socket socket;
	private static  String nome;  //mudei isto depois de estar a funcar ATENÇAO
	private ArrayList<String>onlines =new ArrayList <String>();
	private ArrayList<String>notificacoes =new ArrayList <String>();
	private HashMap <String,ArrayList<String>>historico=new HashMap <String, ArrayList<String>>();
	private ArrayList<String> conversas;
	private DefaultListModel<String> contactos= new DefaultListModel<String>();
	
	public Client(String nome){
	this.nome=nome;
 }

	public void connectToServer() throws IOException {
		 
		InetAddress endereco =InetAddress.getByName(null);
		 socket = new Socket(endereco, 3805);
		 System.out.println("O cliente se conectou ao servidor!");
		 
		 in = new ObjectInputStream(socket.getInputStream());
		 out = new ObjectOutputStream(socket.getOutputStream());
		 
		 Mensagem id=new Mensagem("/id", nome, "ninguem");
		 out.writeObject(id);
		 
		 lerConversas();
		 lerContactos();
		 
		 //Recebedor r=new Recebedor(this,in);
		 //r.start(); //está à escuta de mensagens no in do cliente
		 
		
	
		
		
	}
	
	
	//Manda msg do cliente para o servidor
	public  void sendMessage(Mensagem m) throws IOException{  
	out.writeObject(m);
	if(m.getMensagem().equals("/fim"))
		socket.close();
	System.out.println("Acabei de enviar a seguinte msg para o servidor: " + m.getMensagem());
	
	}
	
	public ArrayList<String> getNotificacoes(){
		return notificacoes;
	}
	
	
	public ArrayList<String> getOnlines(){
		return onlines;
	}
	
	
	public void lerConversas(){
		File file=new File(ContactosInterface.getFolder().getName()+"/" + "mensagens.txt");
		
		try {
			Scanner ficheiro= new Scanner(file);
			
			while(ficheiro.hasNext()){
					
				String linha=ficheiro.nextLine();
				String[]campos= linha.split("-");
				String destino= campos[0];
				String mensagem=campos[1];
				
				if(getHistorico().containsKey(destino)){
					getHistorico().get(destino).add(mensagem);
				}
				else{
					
						conversas=new ArrayList<String>();
						conversas.add(mensagem);
						getHistorico().put(destino, conversas);
						
				}
			}		
			
			ficheiro.close();
			
			}catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
			}
	
	//corro o hashMap e guarda todas as conversa no ficheiro quando o client faz log out
	public void escreverConversas() {

		File ficheiro = new File(ContactosInterface.getFolder().getName() + "/" + "mensagens.txt");
		try {	
			FileWriter f = new FileWriter(ficheiro);  //se der problemas de repetição é so tirar este true
		for (Map.Entry<String, ArrayList<String>> entry : getHistorico().entrySet()) {
		    String destinatario = entry.getKey();
		    ArrayList<String> conversa = entry.getValue();
		    for(int i=0;i<conversa.size();i++){
		    	String m=conversa.get(i);
		    	f.write(destinatario + "-" + m + '\n');
		    }
		}  
		    
		    
				
			

			f.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	
	public void lerContactos() {
		File file=new File(ContactosInterface.getFolder().getName()+"/" + "contactos.txt");
		
		try {
		Scanner ficheiro= new Scanner(file);
		
		while(ficheiro.hasNext()){
				
			String linha=ficheiro.nextLine();
			String[]campos= linha.split(";");
			String nome= campos[0];
			getContactos().addElement(nome);
			
			
			
			}		
		
		ficheiro.close();
		
		}catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		}
	
	//escreve os contactos nos ficheiros
	public void escreverContactos(){ 
		
			File ficheiro = new File(ContactosInterface.getFolder().getName()+"/" + "contactos.txt");
			try {
				FileWriter f = new FileWriter(ficheiro);
				
				for(int i=0; i<contactos.size();i++){
					String user=contactos.get(i);
					f.write(user + '\n');
				}
				f.close();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
	}
	
	
	
	
	
	public ObjectOutputStream getOut() {
		return out;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public Socket getSocket() {
		return socket;
	}

	public String getName(){
		return nome;
	}

	
	
	public ArrayList<String> getConversas() {
		return conversas;
	}

	public DefaultListModel<String> getContactos() {
		return contactos;
	}

	public static void main(String[]args){
		try{
			new Client(nome).connectToServer();
		}catch (IOException e){
				
		}
		
		
		}

	public HashMap<String, ArrayList<String>> getHistorico() {
		return historico;
	}
	
	

}
