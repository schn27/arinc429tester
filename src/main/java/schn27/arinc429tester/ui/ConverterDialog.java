/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schn27.arinc429tester.ui;

import schn27.arinc429tester.Convertor;
import schn27.arinc429tester.NumberSystem;

/**
 *
 * @author amalikov
 */
public class ConverterDialog extends javax.swing.JDialog {

	public ConverterDialog(java.awt.Frame parent, boolean modal, Convertor convertor, NumberSystem ns) {
		super(parent, modal);
		this.convertor = convertor;
		
		initComponents();
		
		label.setText(ns.integerToString(convertor.label, 8));
		
		if (convertor.type != Convertor.Type.NULL) {
			signBit.setText(Integer.toString(convertor.signBit));
			hiBit.setText(Integer.toString(convertor.hiBit));
			loBit.setText(Integer.toString(convertor.loBit));
			hiBitValue.setText(Double.toString(convertor.hiBitValue));
		}
		
		btnCode.setText(getConvertorTypeString(this.convertor.type));
	}
	
	public Convertor getConvertor() {
		return convertor;
	}
	
	private static Convertor.Type getConvertorTypeFromString(String text) {
		if (text.equalsIgnoreCase("Complement")) {
			return Convertor.Type.COMPLEMENT;
		} else if (text.equalsIgnoreCase("Direct")) {
			return Convertor.Type.DIRECT;
		} else if (text.equalsIgnoreCase("BCD")) {
			return Convertor.Type.BCD;
		} else {
			return Convertor.Type.NULL;
		}
	}
	
	private static String getConvertorTypeString(Convertor.Type type) {
		switch (type) {
		case COMPLEMENT:
			return "Complement";
		case DIRECT:
			return "Direct";
		case BCD:
			return "BCD";
		default:
			return "Complement";
		}
	}
	
	private static Convertor.Type getNextConvertorType(Convertor.Type type) {
		switch (type) {
		case COMPLEMENT:
			return Convertor.Type.DIRECT;
		case DIRECT:
			return Convertor.Type.COMPLEMENT;
		default:
			return Convertor.Type.COMPLEMENT;
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        label = new javax.swing.JTextField();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        signBit = new javax.swing.JTextField();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        hiBit = new javax.swing.JTextField();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        loBit = new javax.swing.JTextField();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        hiBitValue = new javax.swing.JTextField();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        btnCode = new javax.swing.JButton();
        javax.swing.JButton btnOK = new javax.swing.JButton();
        javax.swing.JButton btnDisable = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data convertor");
        setResizable(false);

        jPanel1.setLayout(new java.awt.GridLayout(6, 2, 0, 10));

        jLabel1.setText("Label");
        jPanel1.add(jLabel1);

        label.setEditable(false);
        label.setToolTipText("");
        jPanel1.add(label);

        jLabel2.setText("Sign bit");
        jPanel1.add(jLabel2);

        signBit.setToolTipText("");
        jPanel1.add(signBit);

        jLabel3.setText("Hi bit");
        jPanel1.add(jLabel3);

        hiBit.setToolTipText("");
        jPanel1.add(hiBit);

        jLabel4.setText("Lo bit");
        jPanel1.add(jLabel4);

        loBit.setToolTipText("");
        jPanel1.add(loBit);

        jLabel5.setText("Hi bit value");
        jPanel1.add(jLabel5);

        hiBitValue.setToolTipText("");
        jPanel1.add(hiBitValue);

        jLabel6.setText("Code");
        jPanel1.add(jLabel6);

        btnCode.setText("Complement");
        btnCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCodeActionPerformed(evt);
            }
        });
        jPanel1.add(btnCode);

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnDisable.setText("Disable");
        btnDisable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisableActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnDisable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnOK)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnDisable))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDisableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisableActionPerformed
        convertor = new Convertor(convertor.label);
		dispose();
    }//GEN-LAST:event_btnDisableActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
		try {
			convertor = new Convertor(
					convertor.label, 
					getConvertorTypeFromString(btnCode.getText()), 
					Integer.parseInt(signBit.getText()),
					Integer.parseInt(hiBit.getText()),
					Integer.parseInt(loBit.getText()),
					Double.parseDouble(hiBitValue.getText())
			);
		} catch (NumberFormatException ex) {
		}
		
		dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCodeActionPerformed
		btnCode.setText(getConvertorTypeString(getNextConvertorType(getConvertorTypeFromString(btnCode.getText()))));
    }//GEN-LAST:event_btnCodeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCode;
    private javax.swing.JTextField hiBit;
    private javax.swing.JTextField hiBitValue;
    private javax.swing.JTextField label;
    private javax.swing.JTextField loBit;
    private javax.swing.JTextField signBit;
    // End of variables declaration//GEN-END:variables

	private Convertor convertor;
}
