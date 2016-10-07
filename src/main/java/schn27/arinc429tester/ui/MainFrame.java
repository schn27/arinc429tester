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
package schn27.arinc429tester.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import javax.swing.BoundedRangeModel;
import schn27.arinc429tester.Reader;
import javax.swing.JFileChooser;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import schn27.arinc429tester.Arinc429TableModel;
import schn27.arinc429tester.SerialFactory;
import schn27.arinc429tester.TimeMarkedArinc429Word;

public class MainFrame extends javax.swing.JFrame {

	public MainFrame() {
		initComponents();
		initPortList();
		initTable();
		updateStatusBar();

		portName.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				initPortList();
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {}
		});
	}

	private void initPortList() {
		Object selected = portName.getSelectedItem();
		
		portName.removeAllItems();
		SerialFactory.getList().forEach((port) -> portName.addItem(port));
		
		if (selected != null) {
			portName.setSelectedItem(selected);
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
				BoundedRangeModel m = tableScrollPane.getVerticalScrollBar().getModel();
				
				if (m.getMaximum() > 0 && (m.getValue() + m.getExtent()) * 100 / m.getMaximum() >= 95) {
					table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
				}				
			}

			@Override
			public void componentMoved(ComponentEvent ce) {}

			@Override
			public void componentShown(ComponentEvent ce) {}

			@Override
			public void componentHidden(ComponentEvent ce) {}
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
	
	private static String addExtension(String fileName, String extension) {
		return fileName.endsWith(extension) ? fileName : fileName + extension;
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
        btnLoadCfg = new javax.swing.JButton();
        btnSaveCfg = new javax.swing.JButton();
        btnLoad = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        dataBit1 = new javax.swing.JTextField();
        dataBit2 = new javax.swing.JTextField();
        dataBit3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Arinc429Tester");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(795, 200));

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
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        table.setDoubleBuffered(true);
        table.setName(""); // NOI18N
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setVerifyInputWhenFocusTarget(false);
        tableScrollPane.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
        }

        statusBar.setText("Message");
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

        btnLoadCfg.setText("Load config");
        btnLoadCfg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadCfgActionPerformed(evt);
            }
        });

        btnSaveCfg.setText("Save config");
        btnSaveCfg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCfgActionPerformed(evt);
            }
        });

        btnLoad.setText("Load");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jLabel1.setText("Data bits");

        dataBit1.setBackground(new java.awt.Color(153, 204, 255));
        dataBit1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dataBit1.setToolTipText("");
        dataBit1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        dataBit2.setBackground(new java.awt.Color(255, 153, 153));
        dataBit2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dataBit2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        dataBit3.setBackground(new java.awt.Color(153, 204, 0));
        dataBit3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dataBit3.setToolTipText("");
        dataBit3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
            .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(portName, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLoadCfg, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnResetTime, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSaveCfg)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dataBit1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dataBit2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dataBit3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnResetPeriod))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnResetTime)
                    .addComponent(btnLoadCfg)
                    .addComponent(btnLoad)
                    .addComponent(btnOpen))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnResetPeriod)
                    .addComponent(btnSaveCfg)
                    .addComponent(btnSave)
                    .addComponent(jLabel1)
                    .addComponent(dataBit1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataBit2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataBit3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON", "json"));
		fc.setDialogTitle("Save as");
		
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			((Arinc429TableModel)table.getModel()).saveState(addExtension(fc.getSelectedFile().getPath(), ".json"));
		}
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON", "json"));
		fc.setDialogTitle("Load from");
		
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			((Arinc429TableModel)table.getModel()).loadState(fc.getSelectedFile().getPath());
			updateStatusBar();
		}
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnLoadCfgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadCfgActionPerformed
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON", "json"));
		fc.setDialogTitle("Load config from");
		
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			((Arinc429TableModel)table.getModel()).loadConfig(fc.getSelectedFile().getPath());
			updateStatusBar();
		}
    }//GEN-LAST:event_btnLoadCfgActionPerformed

    private void btnSaveCfgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCfgActionPerformed
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("JSON", "json"));
		fc.setDialogTitle("Save config as");
		
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			((Arinc429TableModel)table.getModel()).saveConfig(addExtension(fc.getSelectedFile().getPath(), ".json"));
		}
    }//GEN-LAST:event_btnSaveCfgActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnLoadCfg;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveCfg;
    private javax.swing.JTextField dataBit1;
    private javax.swing.JTextField dataBit2;
    private javax.swing.JTextField dataBit3;
    private javax.swing.JComboBox portName;
    private javax.swing.JLabel statusBar;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables

	private Reader reader;
}
