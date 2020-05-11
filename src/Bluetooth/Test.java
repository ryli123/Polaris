package Bluetooth;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class Test {

	boolean scanFinished = false;
	RemoteDevice hc05device;
	String hc05Url;

	public static void main(String[] args) {
		try {
			new Test().go();
		} catch (Exception ex) {
			Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void go() throws Exception {
		//btspp://001403062AF3:1;authenticate=false;encrypt=false;master=false
		//btspp://001403062A7A:1;authenticate=false;encrypt=false;master=false

		//if you know your hc05Url this is all you need:
		hc05Url = "btspp://001403062AF3:1;authenticate=false;encrypt=false;master=false";
		StreamConnection streamConnection = (StreamConnection) Connector.open(hc05Url);
		OutputStream os = streamConnection.openOutputStream();
		InputStream is = streamConnection.openInputStream();


		System.out.println("connected!");

		os.write("q".getBytes()); //just send '0' to the device

		byte[] b = new byte[8];
		int cnt = 0;
		int size = 0;
		String s = "";
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		while (cnt < 30) {
			System.out.println("reading!" + cnt);
			size = is.read(b);
			System.out.println("read=" + size);
			s += new String(b);
			System.out.println(timestamp + s);
			cnt++;

			empty(b);
			Thread.sleep(1000);
		}        
		
		os.close();
		is.close();
		streamConnection.close();
	}
	
	private void empty(byte[] b) {
		b = new byte[0];
		b = new byte[4];
	}
}

//https://create.arduino.cc/projecthub/millerman4487/view-serial-monitor-over-bluetooth-fbb0e5
//https://www.teachmemicro.com/arduino-bluetooth/
//http://homepages.ihug.com.au/~npyner/Arduino/GUIDE_2BT.pdf
