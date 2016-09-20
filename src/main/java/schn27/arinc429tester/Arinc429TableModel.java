/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.BitSet;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author amalikov
 */
public class Arinc429TableModel extends AbstractTableModel implements SequenceChangedListener {
	public static final int TIME = 0;
	public static final int PERIOD = 1;
	public static final int LABEL = 2;
	public static final int SDI = 3;
	public static final int DATA = 4;
	public static final int SSM = 5;
	public static final int PARITY = 6;
	public static final int COLUMN_COUNT = 7;
	
	public Arinc429TableModel() {
		this.sequence = null;
		parityModeOdd = true;
		labelNumberSystem = NumberSystem.OCT;
		referenceTime = Instant.now();
	}
	
	@Override
	public int getRowCount() {
		if (sequence == null) {
			return 0;
		}
		
		return sequence.size();
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
			return String.format("Label (%s)", labelNumberSystem);
		case SDI:
			return "SDI 10 9";
		case DATA:
			return "Data";
		case SSM:
			return "SSM 31 30";
		case PARITY:
			return String.format("Par (%s)", parityModeOdd ? "Odd*" : "Even");
		}

		return null;
	}	
	
	@Override
	public Object getValueAt(int row, int col) {
		if (sequence == null) {
			return null;
		}
		
		TimeMarkedArinc429Word value = sequence.get(row);
		
		switch (col) {
		case TIME:
			return getTimeTextFrom(value.timemark);
		case PERIOD:
			return getPeriodTextFrom(value.period, value.minPeriod, value.maxPeriod);
		case LABEL:
			return getLabelTextFrom(value.word);
		case SDI:
			return getSdiTextFrom(value.word);
		case DATA:
			return getDataTextFrom(value.word);
		case SSM:
			return getSsmTextFrom(value.word);
		case PARITY:
			return getParityTextFrom(value.word);		
		}
		
		return null;
	}
	
	public void setSequence(Sequence sequence) {
		if (this.sequence != null) {
			this.sequence.removeListener(this);
		}
		
		this.sequence = sequence;
		fireTableDataChanged();
		
		this.sequence.addListener(this);
	}
	
	public NumberSystem getLabelNumberSystem() {
		return labelNumberSystem;
	}
	
	public void setLabelNumberSystem(NumberSystem labelNumberSystem) {
		this.labelNumberSystem = labelNumberSystem;
		fireTableStructureChanged();
	}
	
	@Override
	public void onSequenceAdded(int size) {
		fireTableRowsInserted(size - 1, size - 1);
	}	

	@Override
	public void onSequenceCleared() {
		fireTableDataChanged();
	}
	
	public void toggleParityMode() {
		parityModeOdd = !parityModeOdd;
		fireTableStructureChanged();
	}
	
	public void setNoSdiWords(BitSet noSdiWords) {
		this.noSdiWords = noSdiWords;
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
	
	private String getTimeTextFrom(Instant time) {
		if (timeModeAbsolute) {
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
		return labelNumberSystem.integerToString(word.getLabel() & 0xFF, 8);
	}

	private String getSdiTextFrom(Arinc429Word word) {
		return (noSdiWords == null || !noSdiWords.get(word.getLabel() & 0xFF)) ? String.format("%d %d", word.getSdi() >> 1, word.getSdi() & 1) : "-";
	}

	private String getDataTextFrom(Arinc429Word word) {
		if (noSdiWords == null || !noSdiWords.get(word.getLabel() & 0xFF)) {
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
	
	private Sequence sequence;
	private boolean parityModeOdd;
	private NumberSystem labelNumberSystem;
	private BitSet noSdiWords;
	private boolean timeModeAbsolute;
	private boolean periodModeRange;
	private Instant referenceTime;
}
