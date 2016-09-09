/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.ui;

import javax.swing.table.DefaultTableModel;
import schn27.arinc429tester.bl.LabelFilterConfig;
import schn27.arinc429tester.bl.NumberSystem;

/**
 *
 * @author amalikov
 */
public class LabelFilterDialog extends javax.swing.JDialog {

	/**
	 * Creates new form LabelFilterDialog
	 */
	public LabelFilterDialog(java.awt.Frame parent, boolean modal, LabelFilterConfig config) {
		super(parent, modal);
		this.config = config;
		initComponents();
		updateModeButton();
		updateSystemButton();
		writeTable();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        btnMode = new javax.swing.JButton();
        btnSystem = new javax.swing.JButton();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        filterTable = new javax.swing.JTable();
        javax.swing.JButton btnOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Label filter");
        setName("labelFilterDialog"); // NOI18N
        setResizable(false);

        btnMode.setText("Include");
        btnMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModeActionPerformed(evt);
            }
        });

        btnSystem.setText("BIN");
        btnSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSystemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnMode, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSystem, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMode)
                    .addComponent(btnSystem))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Labels"));

        filterTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        filterTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        filterTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        filterTable.setAutoscrolls(false);
        filterTable.setFillsViewportHeight(true);
        filterTable.setGridColor(new java.awt.Color(0, 0, 0));
        filterTable.setRowSelectionAllowed(false);
        filterTable.getTableHeader().setResizingAllowed(false);
        filterTable.getTableHeader().setReorderingAllowed(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterTable, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(filterTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOk)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOk)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModeActionPerformed
        config.includeMode = !config.includeMode;
		updateModeButton();
    }//GEN-LAST:event_btnModeActionPerformed

    private void btnSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSystemActionPerformed
		readTable();
		
		switch (config.numberSystem) {
		case BIN:
			config.numberSystem = NumberSystem.OCT;
			break;
		case OCT:
			config.numberSystem = NumberSystem.DEC;
			break;
		case DEC:
			config.numberSystem = NumberSystem.HEX;
			break;
		case HEX:
			config.numberSystem = NumberSystem.BIN;
			break;
		default:
			config.numberSystem = NumberSystem.OCT;
		}
		
		writeTable();
		updateSystemButton();
    }//GEN-LAST:event_btnSystemActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        readTable();
		dispose();
    }//GEN-LAST:event_btnOkActionPerformed

	private void updateModeButton() {
		btnMode.setText(config.includeMode ? "Include" : "Exclude");
	}
	
	private void updateSystemButton() {
		btnSystem.setText(config.numberSystem.toString());
	}
	
	private void writeTable() {
		DefaultTableModel m = (DefaultTableModel)filterTable.getModel();
		int row = 0;
		int col = 0;
		for (int label : config.labels) {
			m.setValueAt(label != -1 ? config.numberSystem.integerToString(label, 8) : "", row, col);
			
			if (++col >= 4) {
				col = 0;
				if (++row >= 4) {
					break;
				}
			}
		}
	}
	
	private void readTable() {
		DefaultTableModel m = (DefaultTableModel)filterTable.getModel();
		int row = 0;
		int col = 0;
		for (int i = 0; i < config.labels.length; ++i) {
			String str = (String)m.getValueAt(row, col);
			config.labels[i] = parseValue((String)m.getValueAt(row, col));
			
			if (++col >= 4) {
				col = 0;
				if (++row >= 4) {
					break;
				}
			}
		}		
	}
	
	private int parseValue(String str) {
		try {
			return (str == null || str.isEmpty()) ? -1 : config.numberSystem.parseInteger(str);
		} catch (NumberFormatException ex) {
			return -1;
		}
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMode;
    private javax.swing.JButton btnSystem;
    private javax.swing.JTable filterTable;
    // End of variables declaration//GEN-END:variables

	private final LabelFilterConfig config;
}
