package xyz.rtsvk.comm.serial;

import com.fazecast.jSerialComm.SerialPort;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		Config cfg = Config.from(args);

		if (cfg.containsKey("default-config")) {
			Config.copyDefaultConfig(new File(cfg.getString("default-config")));
			return;
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