package schn27.arinc429tester.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import schn27.arinc429tester.Reader;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import schn27.arinc429tester.Arinc429TableModel;
import schn27.arinc429tester.Convertor;
import schn27.arinc429tester.SerialFactory;
import schn27.arinc429tester.TimeMarkedArinc429Word;

public class MainFrame extends javax.swing.JFrame {

	public MainFrame() {
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
		table.setModel(new Arinc429TableModel());
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
					LabelFilterDialog dlg = new LabelFilterDialog(MainFrame.this, true, m.getLabelFilter());
					Rectangle rect = dlg.getBounds();
					rect.x = e.getXOnScreen();
					rect.y = e.getYOnScreen();
					dlg.setBounds(rect);
					dlg.setVisible(true);
					m.setLabelFilter(dlg.getLabelFilter());
					updateStatusBar();					
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
				final Arinc429TableModel m = ((Arinc429TableModel)table.getModel());
				final int row = table.rowAtPoint(e.getPoint());
				switch (table.columnAtPoint(e.getPoint())) {
				case Arinc429TableModel.SDI:
					m.toggleNoSdi(row);
					break;
				case Arinc429TableModel.CALC:
					ConverterDialog dlg = new ConverterDialog(MainFrame.this, true, m.getConvertor(row), m.getLabelFilter().numberSystem);
					Rectangle rect = dlg.getBounds();
					rect.x = e.getXOnScreen();
					rect.y = e.getYOnScreen();
					dlg.setBounds(rect);
					dlg.setVisible(true);
					m.setConvertor(dlg.getConvertor());
					break;
				}
			}
		});		
	}	
	
	private void updateStatusBar() {
		statusBar.setText(((Arinc429TableModel)table.getModel()).getLabelFilter().toString());
	}
	
	private void updateOpenedState() {
		portName.setEnabled(reader == null);
		btnOpen.setText(reader == null ? "Open" : "Close");		
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
			((Arinc429TableModel)table.getModel()).clearPeriodDetector();
			((Arinc429TableModel)table.getModel()).clear();
			reader = new Reader(
					SerialFactory.create((String)portName.getSelectedItem()), 
					(TimeMarkedArinc429Word word) -> java.awt.EventQueue.invokeLater(() -> ((Arinc429TableModel)table.getModel()).put(word)),
					() -> java.awt.EventQueue.invokeLater(() -> {
						reader = null;
						updateOpenedState();
					}),
					!SerialFactory.isTimedSerial((String)portName.getSelectedItem())
			);
			(new Thread(reader)).start();
		} else {
			reader.stop();
			reader = null;
		}
		
		updateOpenedState();
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnResetTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetTimeActionPerformed
        ((Arinc429TableModel)table.getModel()).setStartTime(Instant.now());
    }//GEN-LAST:event_btnResetTimeActionPerformed

    private void btnResetPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetPeriodActionPerformed
        ((Arinc429TableModel)table.getModel()).clearPeriodDetector();
    }//GEN-LAST:event_btnResetPeriodActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpen;
    private javax.swing.JComboBox portName;
    private javax.swing.JLabel statusBar;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables

	private Reader reader;
}
