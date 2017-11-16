package Layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import server.Client;
import server.Mensagem;
import server.Recebedor;

public class ContactosInterface extends JFrame {

	
	
	//private DefaultListModel<String> contactos= new DefaultListModel<String>();
	//private ArrayList<String> conversas;
	//private HashMap <String,ArrayList<String>>historico=new HashMap <String, ArrayList<String>>();
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList <String> amigos;
	private  JTextArea textArea;
	private static  String userConversa;
	private static File folder;
	private static Client c;
	private  JTextArea textAreamsg;
	private  JTextField textField;
	private JTextField textFieldaddC;
	//private ArrayList <Mensagem>
	
	
	public static void main(String[] args) throws IOException {
		 folder=new File(args[0]);
		
		
		 	//inicia normamente a frame
			if (folder.exists()==true){
				c=new Client (args[0]);
				c.connectToServer();
				ContactosInterface frame = new ContactosInterface();
				frame.setVisible(true);
				
				
			}
			//cria as pastas caso o cliente nunca tenha entrado
			else{
				folder.mkdir();
				Writer writer=null;
				Writer writer2=null;
				Writer writer3=null;
				
		try {
			 writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream(folder.getPath()+"/contactos.txt"), "utf-8"));
			 writer.write("");
			 writer2 = new BufferedWriter(new OutputStreamWriter(
				          new FileOutputStream(folder.getPath()+"/grupos.txt"), "utf-8"));
			writer.write("");
				 writer3 = new BufferedWriter(new OutputStreamWriter(
					      new FileOutputStream(folder.getPath()+"/mensagens.txt"), "utf-8"));
			writer.write("");
			c=new Client (args[0]);
			c.connectToServer();
			ContactosInterface frame = new ContactosInterface();
			frame.setVisible(true);   
			
		
		} catch (IOException ex) {
	    // report
	  } finally {
		   try {writer.close();writer3.close();writer2.close();} catch (Exception ex) {/*ignore*/}
		}
	  }	
		
		
		
}
	public ContactosInterface() {
		//inicio a thread
		//Recebedor r=new Recebedor(getClient(),getClient().getIn(),this);
		//r.start();
		//desenho a frame
		setTitle(getClient().getName());
		setIconImage(Toolkit.getDefaultToolkit().getImage("queque.jpg"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 536, 496);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 165, 0));
		contentPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(139, 69, 19), new Color(255, 165, 0), new Color(255, 222, 173), null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//lerContactos();
		//lerGrupos();
		//lerConversas();
		
//		//botao para adicionar grupos
//		JButton addGrupos = new JButton("");
//		addGrupos.setIcon(new ImageIcon("adgroupo.jpg"));
//		addGrupos.setBounds(207, 16, 101, 85);
//		contentPane.add(addGrupos);
//		addGrupos.addActionListener(new ActionListener() {    
//			public void actionPerformed(ActionEvent arg0) {
//				setVisible(false);
//				AdGrupoInterface frame = new AdGrupoInterface();
//				frame.setVisible(true);
//			}
//		});
		
		
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {  
				try{
					String cliente=getClient().getName();
					getClient().escreverConversas();
					getClient().escreverContactos();
					Mensagem m= new Mensagem("/FIM",cliente,"NINGUEM");
					getClient().sendMessage(m);
					getClient().getSocket().close();
					getClient().getIn().close();
					getClient().getOut().close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}});
		
		
		
		
		//botao de adicionar contactos
		JButton addContactos = new JButton("");
		addContactos.setIcon(new ImageIcon("adcontac.jpg"));
		addContactos.setBounds(10, 16, 101, 85);
		contentPane.add(addContactos);
		addContactos.addActionListener(new ActionListener() {    
			public void actionPerformed(ActionEvent arg0) {
				String NovoContacto=textFieldaddC.getText();
				c.getContactos().addElement(NovoContacto);
			}
		});
		
		textFieldaddC = new JTextField();
		textFieldaddC.setColumns(10);
		textFieldaddC.setBounds(130, 30,200 , 30);
		contentPane.add(textFieldaddC);
		
		
	
		
		//JLIST com os contactos, quando carregado carrega as conversas com esse user para memoria
		amigos=new JList<String>(c.getContactos());          //JLIST
		amigos.setVisibleRowCount(10);
		amigos.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		amigos.setBounds(10, 124, 140, 200);
		amigos.setBackground(Color.LIGHT_GRAY);
		contentPane.add(amigos);
		amigos.addMouseListener(new MouseListener() {    

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
				userConversa=amigos.getSelectedValue();
				textAreamsg.setText("");
				if(c.getHistorico().get(userConversa)!=null){
					for(int i=0;i<c.getHistorico().get(userConversa).size();i++){
						c.getHistorico().get(userConversa).get(i);
						setTexto(c.getHistorico().get(userConversa).get(i));
							
					}}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});		
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.setBounds(180, 165, 300, 230);
		contentPane.add(panel1);
		textAreamsg = new JTextArea();
		panel1.add(textAreamsg);
		JScrollPane scroll = new JScrollPane(textAreamsg);
		panel1.add(scroll);
		textField = new JTextField();
		textField.setBounds(180, 400, 300, 40);
		contentPane.add(textField);
		textAreamsg.setEditable(false);
		
textField.addActionListener(new ActionListener() {
			
		
			
			
			
			
			
			//Manda a mensagem para o servidor, e escreve em memoria dizendo quem é o destino e a mensagem
			public void actionPerformed(ActionEvent arg0) {
				String input = textField.getText();
				textAreamsg.append(getFolder().getName() + ":" + input + "\n");
				textAreamsg.setLineWrap(true);
				textAreamsg.setWrapStyleWord(true);
				String origem = getFolder().getName();
				String destino = getUserConversa();
				String mensagem = textField.getText();
				Mensagem m = new Mensagem(c.getName()+": "+mensagem, origem, destino);

				try {
					getClient().sendMessage(m);
					textArea.append("A mensagem para o user: " + m.getDestinarario()+ " está pendente" + "\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
				}
				if(getClient().getHistorico().containsKey(destino)){
					getClient().getHistorico().get(destino).add( c.getName()+": "+textField.getText());
				}
				else{
					ArrayList <String> conv=new ArrayList <String>();
					conv.add(c.getName()+": "+textField.getText());
					getClient().getHistorico().put(destino,conv );
				}
				//escreverConversas(ContactosInterface.getFolder().getName() + ":" + input, destino);
				textField.setText("");

			}
		});
		
		
		
		
		
		
		
		//Codigo para vizualizar as notificações
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBounds(10, 330,140, 120);
		contentPane.add(panel);
		textArea = new JTextArea();
		textArea.setBackground(Color.LIGHT_GRAY);
		panel.add(textArea);
		panel.add(new JScrollPane(textArea) );
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		
		
		
		//SÓ IMAGENS
		
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("quequedoneyellow_30.jpg"));
		label.setBounds(409, 138, 142, 215);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		label_1.setBounds(438, 78, 72, 80);
		contentPane.add(label_1);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		lblNewLabel.setBounds(100, 78, 54, 48);
		contentPane.add(lblNewLabel);
		
		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		label_2.setBounds(235, 53, 54, 48);
		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("");
		label_3.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		label_3.setBounds(-14, 99, 75, 69);
		contentPane.add(label_3);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		lblNewLabel_1.setBounds(-14, 205, 75, 46);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		lblNewLabel_2.setBounds(108, 432, 72, 60);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		lblNewLabel_3.setBounds(-37, 373, 63, 48);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		lblNewLabel_4.setBounds(409, 369, 81, 77);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("");
		lblNewLabel_5.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		lblNewLabel_5.setBounds(365, 16, 54, 97);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("");
		lblNewLabel_6.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		lblNewLabel_6.setBounds(-14, 0, 46, 60);
		contentPane.add(lblNewLabel_6);
		
		JLabel label_4 = new JLabel("");
		label_4.setIcon(new ImageIcon("quequedoneyellow_40.jpg"));
		label_4.setBounds(480, 335, 88, 52);
		contentPane.add(label_4);
	
		
		
		//inicio a thread
				Recebedor r=new Recebedor(getClient(),getClient().getIn(),this);
				r.start();
		
	}
	
	
	
	
	//le o ficheiro conversas sempre que abro esta frame mete-as num array de conversas e associas a um amigo no hashmap
//	public void lerConversas(){
//		File file=new File(getFolder().getName()+"/" + "mensagens.txt");
//		
//		try {
//			Scanner ficheiro= new Scanner(file);
//			
//			while(ficheiro.hasNext()){
//					
//				String linha=ficheiro.nextLine();
//				String[]campos= linha.split("-");
//				String destino= campos[0];
//				String mensagem=campos[1];
//				
//				if(c.getHistorico().containsKey(destino)){
//					c.getHistorico().get(destino).add(mensagem);
//				}
//				else{
//					
//						conversas=new ArrayList<String>();
//						conversas.add(mensagem);
//						c.getHistorico().put(destino, conversas);
//						
//				}
				//quando recebes uma mensagem de users que nao tem adicionados, adiciona automaticamente
//				int b=0; 
//				for(int i=0; i<contactos.size();i++){
//					if(contactos.getElementAt(i).equals(destino)){
//							b++;
//					}
//				}
//					if(b==0 && getClient().getName().equals(destino)==false){
//					contactos.addElement(destino);
//					}	
//			
//				
//			
//			}		
//			
//			ficheiro.close();
//			
//			}catch (FileNotFoundException e) {
//				
//				e.printStackTrace();
//			}
//			}
	
	
	
	
	//lê os contactos que estao no ficheiro e adiciona ao array
//	public void lerContactos() {
//		File file=new File(getFolder().getName()+"/" + "contactos.txt");
//		
//		try {
//		Scanner ficheiro= new Scanner(file);
//		
//		while(ficheiro.hasNext()){
//				
//			String linha=ficheiro.nextLine();
//			String[]campos= linha.split(";");
//			String nome= campos[0];
//			c.getContactos().addElement(nome);
//			
//			
//			
//			}		
//		
//		ficheiro.close();
//		
//		}catch (FileNotFoundException e) {
//			
//			e.printStackTrace();
//		}
//		}
	
	
//	public void lerGrupos() {
//		File file=new File(getFolder().getName()+"/" + "grupos.txt");
//		
//		try {
//		Scanner ficheiro= new Scanner(file);
//		
//		while(ficheiro.hasNext()){
//				
//			String linha=ficheiro.nextLine();
//			String[]campos= linha.split(";");
//			String nome= campos[0];
//			grupos.addElement(nome);
//			
//			
//			
//			}		
//		
//		ficheiro.close();
//		
//		}catch (FileNotFoundException e) {
//			
//			e.printStackTrace();
//		}
//		}
		public static String getUserConversa(){
			return userConversa;
		}
//		public HashMap<String, ArrayList<String>> getHistorico() {
//			return historico;
//		}
		
		public JList<String> getAmigos() {
			return amigos;
		}
		public  JTextArea getTextArea() {
			return textArea;
		}
		
		public  JTextArea getTextAreamsg() {
			return textAreamsg;
		}
			public static File getFolder() {
			return folder;
		}
		
		public static Client getClient(){
			return c;
		}
//		public DefaultListModel<String> getContactos() {
//			return contactos;
//		}
		
		public static Client getC() {
			return c;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		public   void setTexto(String texto) {
			textAreamsg.append(texto + "\n");
		}
		public static void setUserConversa(String userConversa) {
			ContactosInterface.userConversa = userConversa;
		}

		
}