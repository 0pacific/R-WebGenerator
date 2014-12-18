package view.transProcEdit;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import tpp.service.Service;
import utility.Slpc;
import view.transProcEdit.rightSubPanels.PanelCreateReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelIaReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.transProcEdit.subFrame.Panel_TransProcEdit_Sub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import property.GeneratorProperty;

import mainFrame.MainFrame;
import utility.*;

/*
 * Singleton
 */
public class Panel_TpEdit_Right extends JPanel implements ListSelectionListener, ActionListener,Serializable {
	private JButton additionButton;

	public static final String[] serviceNameList = {
		GeneratorProperty.japanese()?"�t�B�[���h���o":"Fields Extraction",
		GeneratorProperty.japanese()?"�t�B�[���h���v�l�v�Z":"Fields Summing",
		GeneratorProperty.japanese()?"�\����":"Join"
	};

	private JList serviceSelectJList;
	private JScrollPane serviceSelectScrollPane;

	private JTextArea serviceDescTa;


	public SerializableSpringLayout layout;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static Panel_TpEdit_Right obj = new Panel_TpEdit_Right();

	

	
	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private Panel_TpEdit_Right() {
		setBackground(Color.LIGHT_GRAY);
		layout = new SerializableSpringLayout();
		setLayout(layout);
		
		// �T�[�r�X�I�����X�g�ƁA������ރX�N���[���y�C��
		serviceSelectJList = new JList(serviceNameList);
		serviceSelectJList.addListSelectionListener(this);
		serviceSelectScrollPane = new JScrollPane();
		serviceSelectScrollPane.getViewport().setView(serviceSelectJList);
		serviceSelectScrollPane.setPreferredSize(new Dimension(150, 100));
		
		// �T�[�r�X�����e�L�X�g�G���A
		serviceDescTa = new JTextArea(4, 15);
		serviceDescTa.setLineWrap(true);
		
		// �ǉ����s�{�^��
		additionButton = new JButton();
		additionButton.addActionListener(this);
	}

	
	
	
	
	
	
	
	
	

	
	
	
	

	
	
	
	
	/*
	 * PURPOSE :
	 * �T�[�r�X��ǉ����邽�߂̓��̓R���|�[�l���g��z�u����
	 */
	public void locateForService() {
		// �S�R���|�[�l���g����
		removeAll();

		// JTextPane�u�T�[�r�X��I�����ĉ������v
		JTextPane jtp_service = new JTextPane();
		jtp_service.setText("�T�[�r�X��I�����ĉ�����");
		jtp_service.setEditable(false);
		jtp_service.setBounds(10, 30, 200, 20);
		add(jtp_service);

		// �T�[�r�X���I�����X�g�i���񂾃X�N���[���y�C���j
		if(serviceSelectJList.getSelectedIndex()==-1) {
			serviceSelectJList.setSelectedIndex(0);
		}
		serviceSelectScrollPane.setBounds(10, 70, 150, 150);
		add(serviceSelectScrollPane);
		
		// �T�[�r�X����JTextArea
		String selectedServiceName = (String)serviceSelectJList.getSelectedValue();
		String selectedServiceDesc
			= Service.getInstanceByServiceName(
										Panel_TpEdit_Above.getInstance().getEditingTp(), selectedServiceName)
										.getDescription();
		serviceDescTa.setText(selectedServiceDesc);
		serviceDescTa.setBounds(10, 240, 250, 60);
		add(serviceDescTa);
		
		// �ǉ��{�^�����T�[�r�X�ǉ��p�ɃJ�X�^�}�C�Y���A�ǉ�
		additionButton.setText("�T�[�r�X��ǉ�����");
		additionButton.setActionCommand("TPP�ǉ� - Service");
		additionButton.setBounds(10, 320, 200, 30);
		add(additionButton);
		
		// �ĕ`��E�Č���
		MainFrame.getInstance().repaintGeneratorPanel();
		MainFrame.getInstance().validate();
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * ListSelectionEvent�n���h��
	 */
	public void valueChanged(ListSelectionEvent e) {
		// �T�[�r�X�I��JList�̑I�����ڕύX
		if(e.getSource()==serviceSelectJList) {
			locateForService();
		}
	}

	
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equals("TPP�ǉ� - Service")) {
			String serviceName = (String)serviceSelectJList.getSelectedValue();
			Panel_TpEdit_Above.getInstance().addServiceAndServicePanelByServiceName(serviceName);
		}
	}
	
	
	
	
	
	public static Panel_TpEdit_Right getInstance() {
		return Panel_TpEdit_Right.obj;
	}




	public static void updateInstance(Panel_TpEdit_Right newObject) {
		Panel_TpEdit_Right.obj = newObject;
	}
}
