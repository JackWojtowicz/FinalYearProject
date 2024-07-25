package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.pdftron.pdf.*;

@SpringBootApplication
public class FypApplication {

	public static void main(String[] args) {
		PDFNet.initialize("demo:1721938755338:7e6980fe0300000000a24325a9e9346fe237df5e68ca3d00df746734e5");
		try {
			SpringApplication.run(FypApplication.class, args);
		} catch (Exception e) {

		}
		PDFNet.terminate();
	}

}
