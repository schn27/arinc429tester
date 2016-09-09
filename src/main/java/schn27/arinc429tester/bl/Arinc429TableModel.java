/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.bl;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author amalikov
 */
public class Arinc429TableModel extends AbstractTableModel implements SequenceChangedListener {
	public static final int LABEL = 0;
	public static final int SDI = 1;
	public static final int PAD = 2;
	public static final int SSM = 3;
	public static final int PARITY = 4;
	public static final int COLUMN_COUNT = 5;
	
	public Arinc429TableModel() {
		this.sequence = null;
		parityModeOdd = true;
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
		case LABEL:
			return "Label";
		case SDI:
			return "SDI";
		case PAD:
			return "PAD";
		case SSM:
			return "SSM 30 31";
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
		case LABEL:
			return getLabelTextFrom(value.word);
		case SDI:
			return getSdiTextFrom(value.word);
		case PAD:
			return getPadTextFrom(value.word);
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
	
	private String getLabelTextFrom(Arinc429Word word) {
		return String.format("%03o", word.getLabel() & 0xFF);
	}

	private String getSdiTextFrom(Arinc429Word word) {
		return String.format("%d", word.getSDI());
	}

	private String getPadTextFrom(Arinc429Word word) {
		return String.format("%18s", Integer.toBinaryString(word.getPad())).replace(' ', '0');
	}

	private String getSsmTextFrom(Arinc429Word word) {
		return String.format("%d %d", word.getSSM() >> 1, word.getSSM() & 1);
	}

	private String getParityTextFrom(Arinc429Word word) {
		return String.format("%s %d", word.isParityCorrect(parityModeOdd) ? "OK" : "FAIL", word.getParity());
	}
	
	private Sequence sequence;
	private boolean parityModeOdd;
}