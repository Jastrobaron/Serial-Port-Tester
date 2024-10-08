package xyz.rtsvk.comm.serial;

import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class StdoutDumpSerialPortDataListener implements SerialPortDataListener {
	@Override
	public int getListeningEvents() {
		return 0;
	}

	@Override
	public void serialEvent(SerialPortEvent e) {
		System.out.print(new String(e.getReceivedData()));
	}
}
