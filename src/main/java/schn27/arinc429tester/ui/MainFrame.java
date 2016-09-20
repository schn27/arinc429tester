package schn27.arinc429tester.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.util.BitSet;
import schn27.arinc429tester.Reader;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import schn27.arinc429tester.Arinc429TableModel;
import schn27.arinc429tester.Arinc429Word;
import schn27.arinc429tester.FilteredSequence;
import schn27.arinc429tester.LabelFilter;
import schn27.arinc429tester.PeriodDetector;
import schn27.arinc429tester.Sequence;
import schn27.arinc429tester.SerialFactory;

public class MainFrame extends javax.swing.JFrame {

	public MainFrame() {
		filter = new LabelFilter();
		periodDetector = new PeriodDetector();
		sequence = new Sequence(periodDetector);
		filteredSequence = new FilteredSequence(sequence);
		filteredSequence.setFilter(filter);
		noSdiWords = new BitSet(256);
		
		initComponents();
		initPortList();
		initTable();
		updateStatusBar();
	}

	private void initPortList() {
		List<String> ports = SerialFactory.getList();
		DefaultComboBoxModel<String> m = (DefaultComboBoxModel<String>)portName.getModel();
		for (String port : ports) {
			m.addElement(port);
		}
	}

	private void initTable() {
		Arinc429TableModel tableModel = new Arinc429TableModel();
		tableModel.setSequence(filteredSequence);
		table.setModel(tableModel);
		setTableAutoScrollHandler();
		setTableHeaderClickHandler();
		setTableRowClickHandler();
	}

	private void setTableAutoScrollHandler() {
		table.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent ce) {
				Rectangle viewRect = tableScrollPane.getViewport().getViewRect();
				int last = table.rowAtPoint(new Point(0, viewRect.y + viewRect.height - 1));
				
				if (last >= table.getRowCount() - 2) {
					table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
				}				
			}

			@Override
			public void componentMoved(ComponentEvent ce) {
			}

			@Override
			public void componentShown(ComponentEvent ce) {
			}

			@Override
			public void componentHidden(ComponentEvent ce) {
			}
		});
	}
	
	private void setTableHeaderClickHandler() {
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final Arinc429TableModel m = ((Arinc429TableModel)table.getModel());
				switch (table.columnAtPoint(e.getPoint())) {
				case Arinc429TableModel.PARITY:
					m.toggleParityMode();
					break;
				case Arinc429TableModel.LABEL:
					LabelFilterDialog dlg = new LabelFilterDialog(MainFrame.this, true, filter);
					Rectangle rect = dlg.getBounds();
					rect.x = e.getXOnScreen();
					rect.y = e.getYOnScreen();
					dlg.setBounds(rect);
					dlg.setVisible(true);
					filter = dlg.getLabelFilter();
					m.setLabelNumberSystem(filter.numberSystem);
					updateStatusBar();
					filteredSequence.setFilter(filter);
					break;
				case Arinc429TableModel.TIME:
					m.toggleTimeMode();
					break;
				case Arinc429TableModel.PERIOD:
					m.togglePeriodMode();
					break;					
				}
			}
		});		
	}
	
	private void setTableRowClickHandler() {
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (table.columnAtPoint(e.getPoint()) == Arinc429TableModel.SDI) {
					int row = table.rowAtPoint(e.getPoint());
					noSdiWords.flip(filteredSequence.get(row).word.getLabel() & 0xFF);
					final Arinc429TableModel m = ((Arinc429TableModel)table.getModel());
					m.setNoSdiWords(noSdiWords);
				}
			}
		});		
	}	
	
	private void updateStatusBar() {
		statusBar.setText(filter.toString());
	}
	
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOpen = new javax.swing.JButton();
        tableScrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        portName = new javax.swing.JComboBox();
        statusBar = new javax.swing.JLabel();
        javax.swing.JButton btnResetTime = new javax.swing.JButton();
        javax.swing.JButton btnResetPeriod = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Arinc429Tester");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(300, 200));

        btnOpen.setText("Open");
        btnOpen.setName(""); // NOI18N
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        table.setDoubleBuffered(true);
        table.setName(""); // NOI18N
        table.getTableHeader().setReorderingAllowed(false);
        table.setVerifyInputWhenFocusTarget(false);
        tableScrollPane.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
        }

        statusBar.setText("345");
        statusBar.setToolTipText("");

        btnResetTime.setText("Reset Time");
        btnResetTime.setToolTipText("");
        btnResetTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetTimeActionPerformed(evt);
            }
        });

        btnResetPeriod.setText("Reset Period");
        btnResetPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetPeriodActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(portName, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnResetTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnResetPeriod)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(tableScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOpen)
                    .addComponent(portName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnResetTime)
                    .addComponent(btnResetPeriod))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollPane)
                .addGap(0, 0, 0)
                .addComponent(statusBar))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        if (reader == null) {
			filteredSequence.clear();
			reader = new Reader(SerialFactory.create((String)portName.getSelectedItem()), (Arinc429Word word) -> {
				java.awt.EventQueue.invokeLater(() -> {filteredSequence.put(word);});
			});
			(new Thread(reader)).start();
		} else {
			reader.stop();
			reader = null;
		}
		
		portName.setEnabled(reader == null);
		btnOpen.setText(reader == null ? "Open" : "Close");
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnResetTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetTimeActionPerformed
        ((Arinc429TableModel)table.getModel()).setStartTime(Instant.now());
    }//GEN-LAST:event_btnResetTimeActionPerformed

    private void btnResetPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetPeriodActionPerformed
        periodDetector.clear();
    }//GEN-LAST:event_btnResetPeriodActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpen;
    private javax.swing.JComboBox portName;
    private javax.swing.JLabel statusBar;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables

	private final PeriodDetector periodDetector;
	private final Sequence sequence;
	private final FilteredSequence filteredSequence;
	private Reader reader;
	private LabelFilter filter;
	private final BitSet noSdiWords;
}
