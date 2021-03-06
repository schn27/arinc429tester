/* 
 * The MIT License
 *
 * Copyright 2016 Aleksandr Malikov <schn27@gmail.com>.
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
package schn27.arinc429tester;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import schn27.arinc429tester.binary.SerialReader;
import schn27.arinc429tester.serialize.Config;
import schn27.arinc429tester.serialize.State;
import schn27.serial.NotAvailableException;
import schn27.arinc429tester.binary.ReadFilePort;
import schn27.arinc429tester.serialize.Serializer;

/**
 *
 * @author amalikov
 */
public class Arinc429TableModel extends AbstractTableModel {
	public static final int TIME = 0;
	public static final int PERIOD = 1;
	public static final int LABEL = 2;
	public static final int SDI = 3;
	public static final int DATA = 4;
	public static final int SSM = 5;
	public static final int PARITY = 6;
	public static final int CALC = 7;
	public static final int COLUMN_COUNT = 8;
	
	public Arinc429TableModel() {
		sequence = new Sequence();
		filteredSequence = new Sequence();
		labelFilter = new LabelFilter();
		parityModeOdd = true;
		noSdiWords = new BitSet(256);
		periodDetector = new PeriodDetector();
		referenceTime = Instant.now();
		convertors = new HashMap<>();
		dataBitMarker = new DataBitMarker();
	}
	
	@Override
	public int getRowCount() {
		return filteredSequence.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case TIME:
			return String.format("Time (%s)", timeModeAbsolute ? "Abs" : "Start");
		case PERIOD:
			return String.format("Period, ms (%s)", periodModeRange ? "Range" : "Current");
		case LABEL:
			return String.format("Label (%s) 1-8", labelFilter.numberSystem);
		case SDI:
			return "SDI 10 9";
		case DATA:
			return "Data 29-11 (10 9)";
		case SSM:
			return "SSM 31 30";
		case PARITY:
			return String.format("Par (%s) 32", parityModeOdd ? "Odd*" : "Even");
		case CALC:
			return "Calc";
		}

		return null;
	}	
	
	@Override
	public Object getValueAt(int row, int col) {
		SequenceItem item = filteredSequence.get(row);
		
		switch (col) {
		case TIME:
			return getTimeTextFrom(item.tmword.timemark);
		case PERIOD:
			return getPeriodTextFrom(item.period, item.minPeriod, item.maxPeriod);
		case LABEL:
			return getLabelTextFrom(item.tmword.word);
		case SDI:
			return getSdiTextFrom(item.tmword.word);
		case DATA:
			return dataBitMarker.getHtmlText(getDataTextFrom(item.tmword.word));
		case SSM:
			return getSsmTextFrom(item.tmword.word);
		case PARITY:
			return getParityTextFrom(item.tmword.word);
		case CALC:
			return getCalcDataTextFrom(item.tmword.word);
		}
		
		return null;
	}
	
	public void clear() {
		sequence.clear();
		filteredSequence.clear();
		fireTableDataChanged();
	}
	
	public void put(TimeMarkedArinc429Word tmword) {
		int label = tmword.word.getLabel();
		periodDetector.put(label, tmword.timemark);
		SequenceItem item = new SequenceItem(tmword, periodDetector.get(label), periodDetector.getMin(label), periodDetector.getMax(label));
		sequence.put(item);
		putToFilteredSequence(item);
	}
	
	public LabelFilter getLabelFilter() {
		return labelFilter;
	}
	
	public void setLabelFilter(LabelFilter labelFilter) {
		this.labelFilter = labelFilter;
		filteredSequence.clear();
		fireTableStructureChanged();
		sequence.getList().forEach((item) -> putToFilteredSequence(item));
	}
	
	public void clearPeriodDetector() {
		periodDetector.clear();
	}
	
	public void toggleParityMode() {
		parityModeOdd = !parityModeOdd;
		fireTableStructureChanged();
	}
	
	public void toggleNoSdi(int row) {
		noSdiWords.flip(filteredSequence.get(row).tmword.word.getLabel());
		fireTableDataChanged();
	}
	
	public void toggleTimeMode() {
		timeModeAbsolute = !timeModeAbsolute;
		fireTableStructureChanged();
	}
	
	public void togglePeriodMode() {
		periodModeRange = !periodModeRange;
		fireTableStructureChanged();
	}
	
	public void setStartTime(Instant referenceTime) {
		this.referenceTime = referenceTime;
		if (!timeModeAbsolute) {
			fireTableDataChanged();
		}
	}
	
	public Convertor getConvertor(int row) {
		int label = filteredSequence.get(row).tmword.word.getLabel();
		Convertor conv = convertors.getOrDefault(label, null);
		return conv != null ? conv : new Convertor(label);
	}
	
	public void setConvertor(Convertor convertor) {
		if (convertor.type != Convertor.Type.NULL) {
			convertors.put(convertor.label, convertor);
		} else {
			convertors.remove(convertor.label);
		}
		
		fireTableDataChanged();
	}
	
	public void loadBinary(String fileName, SerialReader reader) {
		sequence.clear();
		filteredSequence.clear();
		ReadFilePort serial = new ReadFilePort(fileName);
		
		try {
			serial.open();
			while (!serial.eof()) {
				sequence.put(new SequenceItem(new TimeMarkedArinc429Word(Instant.MIN, reader.read(serial, 10))));
			}
		} catch(TimeoutException | NotAvailableException | InterruptedException ex) {
		} catch (IOException ex) {
			Logger.getLogger(Arinc429TableModel.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				serial.close();
			} catch (IOException ex) {
				Logger.getLogger(Arinc429TableModel.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		fireTableStructureChanged();
		sequence.getList().forEach((item) -> putToFilteredSequence(item));
	}
	
	public void loadState(String fileName) {
		State state = Serializer.loadState(fileName);
		if (state != null) {
			sequence = state.sequence;
			applyConfig(state.config);
		}
	}

	public void saveState(String fileName) {
		Serializer.saveState(fileName, new State(sequence, new Config(labelFilter, noSdiWords, convertors, dataBitMarker.getColors())));
	}

	public void loadConfig(String fileName) {
		applyConfig(Serializer.loadConfig(fileName));
	}

	public void saveConfig(String fileName) {
		Serializer.saveConfig(fileName, new Config(labelFilter, noSdiWords, convertors, dataBitMarker.getColors()));
	}

	public List<DataBitMarker.Entry> getDataBitsColors() {
		return dataBitMarker.getColors();
	}
	
	public void setDataBitsColors(List<DataBitMarker.Entry> dataBitsColors) {
		dataBitMarker.setColors(dataBitsColors);
		fireTableDataChanged();
	}
	
	private void applyConfig(Config config) {
		if (config != null) {
			noSdiWords = config.noSdiWords;
			convertors = config.convertors;
			setLabelFilter(config.labelFilter);
			setDataBitsColors(config.colors);
		}		
	}
	
	private void putToFilteredSequence(SequenceItem item) {
		if (labelFilter.isAccepted(item.tmword.word.getLabel())) {
			filteredSequence.put(item);
			fireTableRowsInserted(filteredSequence.size() - 1, filteredSequence.size() - 1);
		}
	}
	
	private String getTimeTextFrom(Instant time) {
		if (time.equals(Instant.MIN)) {
			return "n/a";
		} else if (timeModeAbsolute) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss.SSS", Locale.ENGLISH);
			return time.atZone(ZoneId.of("UTC").normalized()).format(formatter);
		} else {
			long t = Duration.between(referenceTime, time).toMillis();
			boolean negative = t < 0;
			t = Math.abs(t);
			
			return String.format("%s%02d:%02d:%02d.%03d", 
					negative ? "-" : "", 
					(int)(t / (60 * 60 * 1000)), 
					(int)((t / (60 * 1000)) % 60), 
					(int)((t / 1000) % 60),
					(int)(t % 1000));
		}
	}
	
	private String getPeriodTextFrom(int period, int minPeriod, int maxPeriod) {
		if ((periodModeRange && (minPeriod < 0 || maxPeriod < 0)) || (!periodModeRange && period < 0)) {
			return "n/a";
		}
		
		if (periodModeRange) {
			return String.format("%d .. %d", minPeriod, maxPeriod);
		} else {
			return String.format("%d", period);
		}
	}
	
	private String getLabelTextFrom(Arinc429Word word) {
		return labelFilter.numberSystem.integerToString(word.getLabel(), 8);
	}

	private String getSdiTextFrom(Arinc429Word word) {
		return !noSdiWords.get(word.getLabel()) ? String.format("%d %d", word.getSdi() >> 1, word.getSdi() & 1) : "-";
	}

	private String getDataTextFrom(Arinc429Word word) {
		if (!noSdiWords.get(word.getLabel())) {
			return String.format("%19s", Integer.toBinaryString(word.getData())).replace(' ', '0');
		} else {
			return String.format("%21s", Integer.toBinaryString((word.getData() << 2) + word.getSdi())).replace(' ', '0');
		}
	}

	private String getSsmTextFrom(Arinc429Word word) {
		return String.format("%d %d", word.getSsm() >> 1, word.getSsm() & 1);
	}

	private String getParityTextFrom(Arinc429Word word) {
		return String.format("%s %d", word.isParityCorrect(parityModeOdd) ? "OK" : "FAIL", word.getParity());
	}
	
	private String getCalcDataTextFrom(Arinc429Word word) {
		Convertor conv = convertors.getOrDefault(word.getLabel(), null);
		return conv != null ? String.format(Locale.ENGLISH, "%f", conv.getConverted(word)) : "";
	}
	
	private Sequence sequence;
	private final Sequence filteredSequence;
	private LabelFilter labelFilter;
	private boolean parityModeOdd;
	private BitSet noSdiWords;
	private final PeriodDetector periodDetector;
	private boolean timeModeAbsolute;
	private boolean periodModeRange;
	private Instant referenceTime;
	private Map<Integer, Convertor> convertors;
	private final DataBitMarker dataBitMarker;
}
