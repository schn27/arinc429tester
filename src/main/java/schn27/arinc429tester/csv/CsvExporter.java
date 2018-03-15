/*
 * The MIT License
 *
 * Copyright 2018 Malikov.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package schn27.arinc429tester.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import schn27.arinc429tester.Arinc429TableModel;

/**
 *
 * @author Malikov
 */
public class CsvExporter {
	private static final String SEPARATOR = ";";
	private static final String LINEENDING = "\r\n";
	
	private final String fileName;
	private final Arinc429TableModel model;
	
	public static void export(String fileName, Arinc429TableModel model) {
		try {
			(new CsvExporter(fileName, model)).doExport();
		} catch (IOException ex) {
		}
	}
	
	private void doExport() throws IOException {
		try (BufferedWriter wr = new BufferedWriter(new FileWriter(fileName))) {
			wr.write(getTitle());
			
			int rows = model.getRowCount();
			for (int row = 0; row < rows; ++row) {
				wr.write(getRow(row));
			}
		}
	}
	
	private String getTitle() {
		StringBuilder sb = new StringBuilder();
		
		int columnCount = model.getColumnCount();
		for (int i = 0; i < columnCount; ++i) {
			if (i != 0) {
				sb.append(SEPARATOR);
			}
			
			sb.append("\"");
			sb.append(model.getColumnName(i));
			sb.append("\"");
		}
		
		return sb.append(LINEENDING).toString();
	}
	
	private String getRow(int row) {
		StringBuilder sb = new StringBuilder();

		int columnCount = model.getColumnCount();
		for (int col = 0; col < columnCount; ++col) {
			if (col != 0) {
				sb.append(SEPARATOR);
			}
			
			String value = model.getValueAt(row, col).toString();
				
			sb.append("\"");
			sb.append(model.getValueAt(row, col).toString().replaceAll("\\<[^>]*>", ""));
			sb.append("\"");
		}
		
		return sb.append(LINEENDING).toString();
	}
	
	private CsvExporter(String fileName, Arinc429TableModel model) {
		this.fileName = fileName;
		this.model = model;
	}
}
