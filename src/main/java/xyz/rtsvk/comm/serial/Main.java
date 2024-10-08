package xyz.rtsvk.comm.serial;

import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Config cfg = Config.from(args);

		if (cfg.containsKey("default-config")) {

		}

		SerialPort port = SerialPort.getCommPort(cfg.getString("port"));
		port.setBaudRate(cfg.getInt("baud-rate"));
		port.openPort();

		port.addDataListener(new StdoutDumpSerialPortDataListener());

		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			byte[] buffer = sc.next().getBytes(StandardCharsets.UTF_8);
			port.writeBytes(buffer, buffer.length);
		}

		port.closePort();
	}
}