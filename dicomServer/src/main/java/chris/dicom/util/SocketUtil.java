package chris.dicom.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SocketUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean keepAlive=true;
		Socket socket=null;
		try {
			socket = new Socket("127.0.0.1", 2202);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			while (keepAlive) {
			
			OutputStream output = socket.getOutputStream();
		
			 // output.write("where Online payment?".getBytes());
			output.write("close".getBytes());
			InputStream input = socket.getInputStream();  
		/*	  Scanner sc = new Scanner(input); 
			 
			  StringBuffer sb = new StringBuffer(); 
	            if(sc.hasNext()) { 
	                sb.append(sc.nextLine()); 
	                System.out.println(sb.toString()); 
	            } */
	            try {
					
					 System.out.println(input.available());
	            if(input.available()>0){
	           		String text = new String(input.readNBytes(input.available()-1), StandardCharsets.UTF_8);
	                System.out.println(text);
	                keepAlive=false;
	                } else{
	                TimeUnit.SECONDS.sleep(1);
	                }
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					  keepAlive=false;
				}
	           
	            }    	
	            
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
	}
	
	public String communicate_Brain(String question){
	String reply="";
	int wait_time=0;
	boolean keepAlive=true;
		Socket socket=null;
		try {
			socket = new Socket("127.0.0.1", 2202);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("ERROR: Connection to Brain Refused");
        	reply="ERROR: Connection to Brain Refused";
        	
            keepAlive=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Connection to Brain Refused");
        	reply="ERROR: Connection to Brain Refused";
        	
            keepAlive=false;
			//e.printStackTrace();
		}
		
		if(socket!=null) {
		try {
			
			while (keepAlive) {
			
			OutputStream output = socket.getOutputStream();
			output.write(question.getBytes());
			 // output.write("where Online payment?".getBytes());
			//output.write("close".getBytes());
			InputStream input = socket.getInputStream();  
		/*	  Scanner sc = new Scanner(input); 
			 
			  StringBuffer sb = new StringBuffer(); 
	            if(sc.hasNext()) { 
	                sb.append(sc.nextLine()); 
	                System.out.println(sb.toString()); 
	            } */
	            try {
					
					 System.out.println(input.available());
					 wait_time++;
	            if(input.available()>0){
	           		reply = new String(input.readNBytes(input.available()-1), StandardCharsets.UTF_8);
	                System.out.println(reply);
	                keepAlive=false;
	                } else{
	                TimeUnit.SECONDS.sleep(1);
	                if(wait_time>10) {
	                	System.out.println("ERROR: System Busy");
	                	reply="ERROR: System Busy";
	                	
		                keepAlive=false;
	                }
	                }
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					  keepAlive=false;
				}
	           
	            }    	
	            
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		}
	
	return reply;
	}

}
