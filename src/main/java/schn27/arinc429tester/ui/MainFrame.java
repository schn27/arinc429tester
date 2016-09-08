package schn27.arinc429tester.ui;

import schn27.arinc429tester.bl.Arinc429Word;
import schn27.arinc429tester.bl.Reader;
import schn27.serial.Com;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends javax.swing.JFrame {

	public MainFrame() {
		initComponents();
		List<String> ports = Com.getList();
		@SuppressWarnings("unchecked")
		DefaultComboBoxModel<String> m = (DefaultComboBoxModel<String>)portName.getModel();
		for (String port : ports) {
			m.addElement(port);
		}
		
		m.addElement("Fake");
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOpen = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        portName = new javax.swing.JComboBox();

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
                "Label", "SDI", "Pad", "SSM 30 31", "Parity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        table.setDoubleBuffered(true);
        table.setName(""); // NOI18N
        table.getTableHeader().setReorderingAllowed(false);
        table.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(portName, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(418, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOpen)
                    .addComponent(portName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        if (reader == null) {
			((DefaultTableModel)table.getModel()).setRowCount(0);
			reader = new Reader((String)portName.getSelectedItem(), this::addWord);
			(new Thread(reader)).start();
		} else {
			reader.stop();
			reader = null;
		}
		
		portName.setEnabled(reader == null);
		btnOpen.setText(reader == null ? "Open" : "Close");
    }//GEN-LAST:event_btnOpenActionPerformed

	private void addWord(Arinc429Word word) {
		String[] rowData = new String[6];
		rowData[0] = String.format("0%03o", word.getLabel() & 0xFF);
		rowData[1] = Byte.toString(word.getSDI());
		rowData[2] = String.format("0b%18s", Integer.toBinaryString(word.getPad())).replace(' ', '0');
		rowData[3] = String.format("%d %d", word.getSSM() >> 1, word.getSSM() & 1);
		rowData[4] = String.format("%s %d", word.isParityCorrect() ? "OK" : "FAIL", word.getParity());
		((DefaultTableModel)table.getModel()).addRow(rowData);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpen;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox portName;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

	private Reader reader;
}
