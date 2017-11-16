package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import Layout.ContactosInterface;

public class Recebedor extends Thread {


	private ObjectInputStream in;
	private Client c ;
	private ContactosInterface frame;
	
	public Recebedor(Client c, ObjectInputStream in,ContactosInterface frame) {
		  this.in=in;
		  this.c=c;
		  this.frame=frame;
	}
	
	//escreve para os ficheiros as conversas recebidas nesta thread
//	public void escreverConversas(Mensagem mensagem){
//		
//		
//		File ficheiro = new File(ContactosInterface.getFolder().getName()+"/" + "mensagens.txt");
//		try {
//			FileWriter f = new FileWriter(ficheiro, true);
//			f.write(mensagem.getOrigem() +"-"+ mensagem.getOrigem() +": "+ mensagem.getMensagem() + '\n');
//			
//			f.close();
//		} catch (IOException e) {
//		
//			e.printStackTrace();
//		}
//}
	
	
	
	
	
@Override
public void run(){
	try {
		while(true){
				Mensagem msg=(Mensagem) in.readObject();
				
				//verificar se estou a receber uma mensagem ou uma notificaçao
				if(msg.getOrigem().equals("servidor")){
					frame.getTextArea().append(msg.getMensagem());
				}
				
				else{
				
				
				
				
				
				//isto é so para ver se ja sou amigo da pessoa que esta a mandar a msg, se nao for passo a ser
					int w=0;
					for(int i=0;i<c.getContactos().size();i++){
						if(c.getContactos().get(i).equals(msg.getOrigem())){ //se entrar no if é pq ja tem adicionado
							w=1;
							break;
						}
					}		
							if(w==0 && msg.getOrigem().equals("servidor")==false ){  //se entraer no if adiciona o amigo
								c.getContactos().addElement(msg.getOrigem());
								String userConversa=msg.getOrigem();
								frame.getTextAreamsg().setText("");
								if(c.getHistorico().get(userConversa)!=null){
									for(int i=0;i<c.getHistorico().get(userConversa).size();i++){
										c.getHistorico().get(userConversa).get(i);
										frame.setTexto(c.getHistorico().get(userConversa).get(i));
									}	
									for(int i=0;i<c.getContactos().size();i++){
										if(c.getContactos().getElementAt(i).equals(userConversa)){ //coloca logo selecionado
											frame.getAmigos().setSelectedIndex(i);
											ContactosInterface.setUserConversa(userConversa);
											break;
										}
									}
								}		
							}
					
					
							
					//trato aqui a mensagem adicionando ao vetor de mensagens
					if(c.getHistorico().containsKey(msg.getOrigem())){
						c.getHistorico().get(msg.getOrigem()).add(msg.getMensagem());
						System.out.println(msg.getMensagem());
						System.out.println("Recebeu uma mensagem do user: " +msg.getOrigem());
						//c.getNotificacoes().add("Recebeu uma mensagem do user: " +msg.getOrigem());
						String userConversa=msg.getOrigem();
						frame.getTextAreamsg().setText("");
						if(c.getHistorico().get(userConversa)!=null){
							for(int i=0;i<c.getHistorico().get(userConversa).size();i++){
								c.getHistorico().get(userConversa).get(i);
								frame.setTexto(c.getHistorico().get(userConversa).get(i));
							}	
							for(int i=0;i<c.getContactos().size();i++){
								if(c.getContactos().getElementAt(i).equals(userConversa)){ //coloca logo selecionado
									frame.getAmigos().setSelectedIndex(i);
									ContactosInterface.setUserConversa(userConversa);
									break;
								}
							}
						
						}}		
					
					//caso o user seja meu amigos mas ainda nao me tenha mandado mensagens
					else{
						ArrayList <String> conversa=new ArrayList <String>();
						conversa.add(msg.getMensagem());
						c.getHistorico().put(msg.getOrigem(),conversa);	
						System.out.println("Recebeu uma mensagem do user: " +msg.getOrigem());
						String userConversa=msg.getOrigem();
						frame.getTextAreamsg().setText("");
						if(c.getHistorico().get(userConversa)!=null){
							for(int i=0;i<c.getHistorico().get(userConversa).size();i++){
								c.getHistorico().get(userConversa).get(i);
								frame.setTexto(c.getHistorico().get(userConversa).get(i));
							}	
							for(int i=0;i<c.getContactos().size();i++){
								if(c.getContactos().getElementAt(i).equals(userConversa)){ //coloca logo selecionado
									frame.getAmigos().setSelectedIndex(i);
									ContactosInterface.setUserConversa(userConversa);	
									break;
								}
							}
						}	
					}
			}	
		}			
			} catch (IOException | ClassNotFoundException e) {
		// TODO Auto-generated catch block
			
			//e.printStackTrace();
			} 	
		}




}






