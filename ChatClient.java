import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;
import java.awt.event.*;
import java.util.*;

public class ChatClient implements ActionListener
{
   JFrame frame1, frame2;
   JTextField outgoing2 , name;
   JTextArea incoming, outgoing;
   BufferedReader reader;
   PrintWriter writer;
   JPanel frame2Panel;
   Socket server;
   JButton send2;
   String cname;
 public class MsgRead extends Thread
 {
   public void run()
   {
      String messg;
	  try
	  {
	  while((messg=reader.readLine())!=null)
	  incoming.append("\n"+messg);
	   
	  }
	  catch(Exception ex)
	  {
	  ex.printStackTrace();
	  }
   }
 }
    public void go()
	{
	  frame1 = new JFrame("Connect");
	  JTextArea disp = new JTextArea(3,30);
	  outgoing2= new JTextField(20);
	  JButton send = new JButton("SEND");
	  send.addActionListener(new SendButtonListener());
	  JPanel frame1Panel = new JPanel();
	  name = new JTextField(20);
	  disp.append("             Enter The IP Address And Your Name");
	  frame1Panel.add(disp);
	  frame1Panel.add(outgoing2);
	  frame1Panel.add(name);
	  frame1Panel.add(send);
	  frame1.getContentPane().add(BorderLayout.CENTER,frame1Panel);
	  frame1.setSize(400,150); frame1.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
	  disp.setEditable(false);
	  frame1.setLocationRelativeTo(null);
	  frame1.setVisible(true);
	 }
	 
public void goframe2()
{
    frame2 = new JFrame("CHAT");
	incoming = new JTextArea(20,40);
	incoming.setLineWrap(true);
	incoming.setEditable(false);
	outgoing = new JTextArea(5,35);
	outgoing.setLineWrap(true);
	outgoing.setEditable(true);
	JButton clear = new JButton("Clear");
	clear.addActionListener(new Clear());
	send2 = new JButton("Send");
	JButton end = new JButton("Quit");
	frame2Panel = new JPanel();
	frame2Panel.add(clear);
	frame2Panel.add(incoming);
	frame2Panel.add(outgoing);
	frame2Panel.add(send2);
	frame2Panel.add(end);
	send2.addActionListener(this);
	end.addActionListener(new Quitb());
	frame2.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
	frame2.getContentPane().add(BorderLayout.CENTER, frame2Panel);
	frame2.setSize(500,530);
	frame2.setLocationRelativeTo(null);
	frame2.setVisible(true);
	
}	
public class Clear implements ActionListener
{
 public void actionPerformed(ActionEvent ev)
 {
     incoming.setText(null);
 }
}
public void actionPerformed(ActionEvent ev)
{
   String chat = outgoing.getText();
   if(!chat.equals(null))
   {
   //incoming.append("\n"+chat);
   writer.println(chat);
   outgoing.setText(null);
   writer.flush();
   }
} 
public class Quitb implements ActionListener
{
  public void actionPerformed(ActionEvent ev)
  {
  writer.println("\n"+cname+" LEFT THIS CHAT");
  writer.flush();
  try
  {
  server.close();
  }
  catch(Exception ex)
  {
   ex.printStackTrace();
  }
  System.exit(0);
  }
}
  
public class SendButtonListener implements ActionListener
{
   public void actionPerformed(ActionEvent ev)
   {
      try
	   { 
	        String iaddress = outgoing2.getText();
			 cname = name.getText();
	        server = new Socket(iaddress,2000);
	        InputStreamReader streamReader = new InputStreamReader(server.getInputStream());
	        reader = new BufferedReader(streamReader);
	        writer = new PrintWriter(server.getOutputStream());
	        System.out.println("Connected To Sever");
			writer.println(cname);
			writer.flush();
			frame1.setVisible(false);
			goframe2();
			(new MsgRead()).start();
	    }
	  catch(Exception ex)
	  {
	        ex.printStackTrace();
	  }
	}
}
public static void main(String[] args)
{
ChatClient per = new ChatClient();
per.go();

}
}