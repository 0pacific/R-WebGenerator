package view.adminAuthEdit.editPage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import mainFrame.MainFrame;
import debug.Debug;
import property.GeneratorProperty;
import utility.SerializableSpringLayout;
import account.Account;
import account.AccountManager;

public class IDPassEditPanel  extends JPanel implements ItemListener,ActionListener,Serializable{
	
	private int inputMode;
	private final static int ADMIN_MODE = 0;
	private final static int CREATER_MODE = 1;
	
	
	
	//ID���͗�
	public JTextPane textID;
	public JTextField fieldID;
	//�p�X���[�h���͗�
	public JTextPane textPass;
	public JPasswordField fieldPass1;
	public JPasswordField fieldPass2;
	
	public JTextPane textCaution;
	
	//����{�^��
	public JButton btnOK;
	//�L�����Z���{�^��
	public JButton btnCancel;
	
	public SerializableSpringLayout springLayout;
	
	public static final Color BG_COLOR = Color.WHITE;
	
	private static IDPassEditPanel obj = new IDPassEditPanel();
	
	
	
	
	private IDPassEditPanel(){
		boolean japanese = GeneratorProperty.japanese();
		
		textID = new JTextPane();
		textID.setText("ID:");
		textID.setEditable(false);
		
		fieldID = new JTextField();
		
		textPass = new JTextPane();
		textPass.setText(japanese? "�p�X���[�h�i�m�F�̂��߁A�����͂��Ă��������j:":"Password(Please Input twice)");
		textPass.setEditable(false);

		textCaution = new JTextPane();
		textCaution.setText(japanese? "�p�X���[�h����v���Ă��܂���B���͂��Ȃ����Ă��������B":"Please re-write passwords because passwords are not same.");
		textCaution.setEditable(false);
		textCaution.setForeground(Color.WHITE);
		
		fieldPass1 = new JPasswordField();
		fieldPass2 = new JPasswordField();
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(this);
		btnOK.setEnabled(false);
		btnOK.setActionCommand("OK");
		
		btnCancel = new JButton(japanese? "�L�����Z��":"Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("�L�����Z��");
	}
	
	public void openAdmin(){
		inputMode = ADMIN_MODE;
		locateComps();
	}
	
	public void openCreater(){
		inputMode = CREATER_MODE;
		locateComps();
	}
	
	
	public void locateComps(){
		
		removeAll();
		
		//ID���͗�
		springLayout.putConstraint(SpringLayout.NORTH, textID, 20, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, textID, 20, SpringLayout.WEST, this);
		add(textID);
		
		fieldID.setPreferredSize(new Dimension(100, 20));
		springLayout.putConstraint(SpringLayout.NORTH, fieldID, 0, SpringLayout.SOUTH, textID);
		springLayout.putConstraint(SpringLayout.WEST, fieldID, 20, SpringLayout.WEST, this);
		add(fieldID);
		
		fieldID.setText("");
		
		//�p�X���[�h���͗�
		springLayout.putConstraint(SpringLayout.NORTH, textPass, 20, SpringLayout.NORTH, fieldID);
		springLayout.putConstraint(SpringLayout.WEST, textPass, 20, SpringLayout.WEST, this);
		add(textPass);
		
		fieldID.setPreferredSize(new Dimension(100, 20));
		springLayout.putConstraint(SpringLayout.NORTH, fieldPass1, 0, SpringLayout.SOUTH, textPass);
		springLayout.putConstraint(SpringLayout.WEST, fieldPass1, 20, SpringLayout.WEST, this);
		add(fieldPass1);
		
		fieldID.setPreferredSize(new Dimension(100, 20));
		springLayout.putConstraint(SpringLayout.NORTH, fieldPass2, 0, SpringLayout.SOUTH, fieldPass1);
		springLayout.putConstraint(SpringLayout.WEST, fieldPass2, 20, SpringLayout.WEST, this);
		add(fieldPass2);

		springLayout.putConstraint(SpringLayout.NORTH, textCaution, 20, SpringLayout.NORTH, fieldPass2);
		springLayout.putConstraint(SpringLayout.WEST, textCaution, 20, SpringLayout.WEST, this);
		add(btnOK);
		
		springLayout.putConstraint(SpringLayout.NORTH, btnOK, 20, SpringLayout.NORTH, textCaution);
		springLayout.putConstraint(SpringLayout.WEST, btnOK, 20, SpringLayout.WEST, this);
		add(btnOK);
		
		springLayout.putConstraint(SpringLayout.NORTH, btnCancel, 0, SpringLayout.NORTH, btnOK);
		springLayout.putConstraint(SpringLayout.WEST, btnCancel, 20, SpringLayout.WEST, btnOK);
		add(btnCancel);
		
		addAction();
		
		
	}
	
	
	private void addAction() {
		
		fieldID.addActionListener(this);
		fieldID.setActionCommand("ID�t�B�[���h�ύX");
		
		fieldPass1.addActionListener(this);
		fieldPass1.setActionCommand("�p�X���[�h�t�B�[���h�ύX");
		
		fieldPass2.addActionListener(this);
		fieldPass2.setActionCommand("�p�X���[�h�t�B�[���h�ύX");
		
		btnOK.addActionListener(this);
		btnOK.setActionCommand("OK");
		
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("�L�����Z��");
	}


	public void actionPerformed(ActionEvent e) {
		boolean japanese = GeneratorProperty.japanese();
		String cmd = e.getActionCommand();
		int i = 0;
		
		if(cmd.equals("ID�t�B�[���h�ύX")){
			if(fieldID.getText().length()==0){
				//ID�t�B�[���h����Ȃ��OK�{�^���͉����Ȃ�
				btnOK.setEnabled(false);
			}
			else btnOK.setEnabled(true);
		}
		else if(cmd.equals("�p�X���[�h�t�B�[���h�ύX")){
			char[] pass1 = fieldPass1.getPassword();
			char[] pass2 = fieldPass2.getPassword();
			for(i = 0;i<pass1.length;i++){
				//�p�X���[�h�s��v
				if(!(pass1[i]==pass2[i])){
					textCaution.setForeground(Color.RED);
					btnOK.setEnabled(false);
					break;
				}
			}

			if(i+1==pass1.length&&pass1.length==pass2.length){
				//�p�X���[�h��v
				btnOK.setEnabled(true);
			}
		}
		else if(cmd.equals("OK")){
			
			Account account = new Account();
			
			String ID = fieldID.getText();
			String password = new String(fieldPass1.getPassword());
			
			account.setAccount(ID,password);
			
			if(inputMode==ADMIN_MODE){
				//�Ǘ��ғo�^�̏ꍇ
				AccountManager.getInstance().setAdmin(account);
			}
			else if(inputMode==CREATER_MODE){
				//�쐬�ғo�^�̏ꍇ
				AccountManager.getInstance().setCreater(account);
			}
			else{

				Debug.error("�s���ȃA�J�E���g�o�^�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			
			}
			frameClosed();
		}
		else if(cmd.equals("�L�����Z��")){
			frameClosed();
		}
		// �G���[
		else {
			JOptionPane.showMessageDialog(this, "�G���[���������܂����B");
			Debug.error("�s���ȃR�}���h�ł��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return;
		}
		
		
	}

	public void frameClosed() {
		IDPassEditFrame.repaintAndValidate();
		
		IDPassEditFrame.getInstance().setVisible(false);
		MainFrame.getInstance().setEnabled(true);
		MainFrame.getInstance().requestFocus();
		
	}

	public void itemStateChanged(ItemEvent arg0) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}
	
	public static IDPassEditPanel getInstance(){
		return IDPassEditPanel.obj;
	}

}
