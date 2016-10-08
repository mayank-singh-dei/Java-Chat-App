import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.awt.event.*;
import java.util.*;

public class ChatServer implements ActionListener
{
Socket[] client= new Socket[50];
Socket newclient;
String[] cname = new String[50];
Scanner[] reader = new Scanner[50];
Scanner ok=null;
clientin[] ClientIn= new clientin[50];
PrintWriter[] writer = new PrintWriter[50];
JTextArea incoming, outgoing,cstatus;
Integer control=1, cn=0,dec=1;;
JButton send2;
JFrame frame1,frame2;
JFrame rframe = new JFrame("Remove Client");
JButton[] f2b = new JButton[50];
JButton[] rb = new JButton[50];
JPanel frame2Panel ;
JPanel rframePanel = new JPanel();
public class clientin extends Thread
{
 Integer curr;
   public clientin(int i,Scanner sc)
   {
     curr=i;
	 reader[curr]=sc;
   }
   public void run()
   {
     String messg;
	 incoming.append("\n"+cname[curr]+" Added To Chat");
	 reader[curr].nextLine();
	  for(int i=0;i<cn;i++)
     {	 
	 sendmsg(cname[curr],i,"ADDED TO CHAT"); 
	 }
	 
	 while((messg=reader[curr].nextLine())!=null)
	 {
	  incoming.append("\n"+cname[curr]+":      "+messg);	 
	 for(int i=0;i<cn;i++)
     {
	 sendmsg(cname[curr],i,messg); 
	 }
	 }
   }
}
public class SockOpen extends Thread
{
public void run()
{  
    try
	{
   ServerSocket server= new ServerSocket(2000);
   Socket newlclient;
   while(true)
   {
   newlclient= server.accept();
   newclient=newlclient;
   if(control==1)
   add();
   else
   {
   dec=0;
   gocontrolframe();
      while(dec==0)
      {
      }
     if(control==1)
     {
	 control=2;
	 add();
	 }
	 control=2;
	
   }
   
   
   }
   }
   catch(Exception ex)
       {
        ex.printStackTrace();
        }
  }
}
public void gorframe()
{
		rframe.getContentPane().add(BorderLayout.CENTER, rframePanel);
		rframe.setSize(400,220);
		 rframe.setLocationRelativeTo(null);
		rframe.setVisible(true);
}
public void gocontrolframe()
{
      frame1 = new JFrame("CONNECTION REQUEST");
	  JTextArea connect = new JTextArea(4,20);
	  JButton accept = new JButton("Accept");
	  JButton reject = new JButton("Reject");
     accept.addActionListener(new addrejcl());
	 reject.addActionListener(new addrejcl());
	 JPanel frame1Panel = new JPanel();
	 frame1Panel.add(connect);
	 frame1Panel.add(accept);
	 frame1Panel.add(reject);
	 String rt="Unknown";
	 try
	 {
	 ok = new Scanner(newclient.getInputStream());
	 cname[cn]= ok.next();
	 }
	 catch(Exception ex)
       {
        ex.printStackTrace();
        }
	 connect.append(cname[cn]+" Is Requesting To Join The Chat\n\n");
	 connect.setEditable(false);
	 frame1.getContentPane().add(BorderLayout.CENTER,frame1Panel);
	 frame1.setSize(320,200);
	 accept.setActionCommand("accept");
	 reject.setActionCommand("reject");
	  frame1.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
	  frame1.setLocationRelativeTo(null);
	 frame1.setVisible(true);
}
public class addrejcl implements ActionListener
{
  public void actionPerformed(ActionEvent ev)
  {
    String ghy = ev.getActionCommand();
	if(ghy.equals("accept"))
	  {
	  control=1;
	  dec=1;
	  }
	else
	{
	   dec=1;
	    try
	   {
	   PrintWriter ryu= new PrintWriter(newclient.getOutputStream());
	   ryu.println("\n\nYou Are Not Added To Chat");
	   ryu.flush();
	   newclient.close();
	   newclient=null;
	   }
	   catch(Exception ex)
       {
        ex.printStackTrace();
        }
	}
	frame1.setVisible(false);
  }
}
public void add()
{
   try
   {
     
	writer[cn]=new PrintWriter(newclient.getOutputStream());
	if(control==1)
	
	{
	ok =new Scanner(newclient.getInputStream());
    cname[cn]= ok.next();
	}
	}
	catch(Exception ex)
       {
        ex.printStackTrace();
        }
    client[cn] = newclient;
    f2b[cn] = new JButton(cname[cn]);
    rb[cn]=new JButton(cname[cn]);
    f2b[cn].addActionListener(new sendSpecific());
    rb[cn].addActionListener(new remvclnt());
    f2b[cn].setActionCommand(cn.toString());
    rb[cn].setActionCommand(cn.toString());
    frame2Panel.add(f2b[cn]);
	frame2.setVisible(true);
	rframePanel.add(rb[cn]);
	int j = cn;
	ClientIn[j]=(new clientin(j,ok));
	ClientIn[j].start();
    cn++;
   
}
public class remvclnt implements ActionListener
{
    public void actionPerformed(ActionEvent evt)
	{
	    Integer rc =Integer.parseInt(evt.getActionCommand());
		for(int l=0;l<cn;l++)
		sendmsg("Server",l,cname[rc]+" IS REMOVED FROM CHAT"); 
		incoming.append("\n"+cname[rc]+" Is Now removed From Chat");
		try
	{
	client[rc].close();
	client[rc]=null;
	reader[rc]=null;
	writer[rc]=null;
	}
	catch(Exception ex)
       {
        ex.printStackTrace();
        }
		frame2Panel.remove(f2b[rc]);
		rframePanel.remove(rb[rc]);
		frame2.setVisible(false);
		rframe.setVisible(false);
		frame2.setVisible(true);
		rframe.setVisible(true);
		
	}
}
public class sendSpecific implements ActionListener
{
  public void actionPerformed(ActionEvent evt)
  {
    String str = evt.getActionCommand();
	Integer n = Integer.parseInt(str);
	String chat = outgoing.getText();
	sendmsg("Server",n,chat); 
	incoming.append("\nTo "+cname[n]+"       :"+chat);
	outgoing.setText(null);
  }
}

 public void goframe2()
{
    (new SockOpen()).start();
//frame
    frame2 = new JFrame("CHAT");
	 frame2Panel = new JPanel();
//incoming and outgoing
    cstatus = new JTextArea(2,40);
	cstatus.setText("                         For New Clients Control Is At Automatic Mode");
    incoming = new JTextArea(20,40);
    incoming.setLineWrap(true);
    incoming.setEditable(false);
	cstatus.setEditable(false);
    outgoing = new JTextArea(5,35);
    outgoing.setLineWrap(true);
    outgoing.setEditable(true);
	
	frame2Panel.add(cstatus);
//upper buttons
    JButton clear= new JButton("Clear");
	clear.setActionCommand("clear");
	clear.addActionListener(new UpperButton());
	
	 JButton remc= new JButton("RemoveClient");
	remc.setActionCommand("rem");
	remc.addActionListener(new UpperButton());
	
	JButton auto = new JButton("AutoControl");
	auto.setActionCommand("auto");
	auto.addActionListener(new UpperButton());
	
	JButton manu = new JButton("ManualControl");
	manu.setActionCommand("manu");
	manu.addActionListener(new UpperButton());
	
	JButton quit = new JButton("Quit");
	quit.setActionCommand("quit");
	quit.addActionListener(new UpperButton());
	
	frame2Panel.add(clear);
	frame2Panel.add(auto);
	frame2Panel.add(manu);
	frame2Panel.add(remc);
	frame2Panel.add(quit);
	
//adding incoming and outgoing;
   frame2Panel.add(incoming);
   frame2Panel.add(outgoing);
   
 //adding send button  
    send2 = new JButton("Send");
    frame2Panel.add(send2);
    send2.addActionListener(this);
    frame2.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame2.getContentPane().add(BorderLayout.CENTER, frame2Panel);
    frame2.setSize(500,565);
    frame2.setLocationRelativeTo(null);
    frame2.setVisible(true);
    
}   
public class UpperButton implements ActionListener
{
   public void actionPerformed(ActionEvent ev)
   {
     String stry=ev.getActionCommand();
	 if(stry.equals("clear"))
	 incoming.setText(null);
	 else if(stry.equals("auto"))
	 {
	 control=1;
	 cstatus.setText("                         For New Clients Control Is At Automatic Mode");
	 }
	 else if(stry.equals("manu"))
     {
	 cstatus.setText("                         For New Clients Control Is At Manual Mode");
	 control=2;
	 }
	 else if(stry.equals("quit"))
	 System.exit(0);
	 else if(stry.equals("rem"))
	 {
	 gorframe();
	 }
	}  
}
public void actionPerformed(ActionEvent ev)
{
   String chat = outgoing.getText();
   if(!chat.equals(null))
   {
   incoming.append("\n"+chat);
   for(int i=0;i<cn;i++)
   sendmsg("Server",i,chat);
   outgoing.setText(null);
   }
}
public void sendmsg(String clname,int i,String chat) 
{
try{
  writer[i].println("\n"+clname+" :      "+chat);
  writer[i].flush();
  }
catch(Exception ex)
  {
   ex.printStackTrace();
  }
}
 public static void main(String[] args)
{
  ChatServer hok = new ChatServer();
  hok.goframe2(); 
} 
}