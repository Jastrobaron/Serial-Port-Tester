package xyz.rtsvk.comm.serial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Config extends LinkedHashMap<String, Object> {

	private static final String ARG_PREFIX = "--";
	private static final String ARG_ASSIGN = "=";
	private static final String COMMENT = "#";

	public Config() {
		super();
	}

	public static Config from(String[] args) {
		Config cfg = new Config();

		// parse input args
		cfg.putAll(parse(args, true));

		if (cfg.containsKey("config")) {
			String fileName = String.valueOf(cfg.remove("config"));
			File file = new File(fileName);

			if (!file.exists()) {
				System.err.println("Specified config file does not exist. Skipping...");
				return cfg;
			}

			try {
				Scanner reader = new Scanner(new FileInputStream(file));
				StringBuilder content = new StringBuilder();
				while (reader.hasNextLine())
					content.append(reader.nextLine()).append("\n");
				cfg.putAll(parse(content.toString().split("\n"), false));
			}
			catch (Exception e) {
				System.err.println("Error loading the config file: " + e.getMessage());
			}
		}

		return cfg;
	}

	private static Config parse(String[] args, boolean checkPrefix) {
		Config cfg = new Config();

		for (String arg : args) {
			if (arg.isEmpty()) continue;

			if (checkPrefix) {
				if (!arg.startsWith(ARG_PREFIX)) continue;
				else arg = arg.substring(ARG_PREFIX.length());
			}
			else if (arg.startsWith(COMMENT)) continue;

			int commentIdx = arg.indexOf(COMMENT);
			commentIdx = commentIdx > 0 ? commentIdx : arg.length();

			int eqIdx = arg.indexOf(ARG_ASSIGN);
			eqIdx = eqIdx > 0 ? eqIdx : arg.length();

			String key = arg.substring(0, eqIdx);
			String value = arg.substring((eqIdx+1) % arg.length(), commentIdx);

			cfg.put(key, value);
		}

		return cfg;
	}

	public static Config copyDefaultConfig(File fileToWrite) {

	}

	public static Config writeConfig(Config config, String filename) throws IOException {
		FileWriter writer = new FileWriter(filename);
		for (Map.Entry<String, Object> entry : config.entrySet()) {
			writer.append(entry.getKey());
			writer.append("=");
			writer.append(entry.getValue().toString());
			writer.append("\n");
		}
		writer.close();
		return config;
	}

	public String getStringOrDefault(String key, String def) {
		Object value = this.get(key);
		return value != null ? String.valueOf(value) : def;
	}

	public String getString(String key) {
		return getStringOrDefault(key, null);
	}

	public int getIntOrDefault(String key, int n) {
		Object value = this.get(key);
		return value != null ? Integer.parseInt(value.toString()) : n;
	}

	public int getInt(String key) {
		return getIntOrDefault(key, 0);
	}

	public boolean getBooleanOrDefault(String key, boolean b) {
		Object value = this.get(key);
		return value != null ? Boolean.parseBoolean(value.toString()) : b;
	}

	public boolean getBoolean(String key) {
		return getBooleanOrDefault(key, false);
	}
}
