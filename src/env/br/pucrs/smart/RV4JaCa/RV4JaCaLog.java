package br.pucrs.smart.RV4JaCa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class RV4JaCaLog {
	
	PrintWriter pw;
	FileWriter fw;
	File file;

	public RV4JaCaLog(String fileName) {

		file = new File("log/"+fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void add(String message) {
		try {
			fw = new FileWriter(file, true);
			pw = new PrintWriter(fw);
			pw.println(message);
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
