package view.transProcEdit.serviceSelect;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.*;

import debug.Debug;

import property.GeneratorProperty;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import tpp.service.Service;
import view.transProcEdit.serviceArgsWindow.*;

/*
 * Singleton
 */
public class JTreeForServiceSelection extends JTree {
	// �p�l����ł̈ʒu
	public static final int posX = 10;
	public static final int posY = 10;

	// �T�C�Y
	public static final int treeWidth = 200;
	public static final int treeHeight = 300;

	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static JTreeForServiceSelection obj = new JTreeForServiceSelection();


	
	
	
	/*
	 * PURPOSE :
	 * �R���X�g���N�^
	 */
	private JTreeForServiceSelection() {
		boolean japanese = GeneratorProperty.japanese();
		
		// �J�e�S���u�l�����Z�v�̃m�[�h�Ƃ��̎q�m�[�h�i�T�[�r�X���j
		DefaultMutableTreeNode node_calc = new DefaultMutableTreeNode(japanese?"�l�����Z":"Arithmetic Operation"); 
		String[] calcServNames = Service.getServNamesByCateg(japanese?"�l�����Z":"Arithmetic Operation");
		Debug.test(7777);
		Debug.test(calcServNames.length);
		for(int i=0; i<calcServNames.length; i++) {
			String servName = calcServNames[i];
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(servName);
			node_calc.add(node);
		}

		// �J�e�S���u�t�B�[���h�����v�̃m�[�h�Ƃ��̎q�m�[�h�i�T�[�r�X���j
		DefaultMutableTreeNode node_field = new DefaultMutableTreeNode(japanese?"�t�B�[���h����":"Field Handling"); 
		String[] fieldServNames = Service.getServNamesByCateg(japanese?"�t�B�[���h����":"Field Handling");
		for(int i=0; i<fieldServNames.length; i++) {
			String servName = fieldServNames[i];
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(servName);
			node_field.add(node);
		}

		// �J�e�S���u�e�[�u�������v�̃m�[�h�Ƃ��̎q�m�[�h�i�T�[�r�X���j
		DefaultMutableTreeNode node_table = new DefaultMutableTreeNode(japanese?"�e�[�u������":"Table Handling"); 
		String[] tableServNames = Service.getServNamesByCateg(japanese?"�e�[�u������":"Table Handling");
		for(int i=0; i<tableServNames.length; i++) {
			String servName = tableServNames[i];
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(servName);
			node_table.add(node);
		}
		// ���[�g�m�[�h�u�T�[�r�X�v���쐬 -> �e�J�e�S���m�[�h�ǉ�
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(GeneratorProperty.japanese()?"�T�[�r�X":"Service");
		root.add(node_calc);
		root.add(node_field);
		root.add(node_table);

		// ���[�g�m�[�h���w�肵���f���쐬
		DefaultTreeModel model = new DefaultTreeModel(root);
		setModel(model);

		// �S�m�[�h��W�J���Ă���
		int row = 0;
		while(row<getRowCount()) {
			expandRow(row);
			row++;
		}
	}
	
	

	
	
	/*
	 * PURPOSE :
	 * �C���X�^���X�C��
	 */
	public static JTreeForServiceSelection getInstance() {
		return JTreeForServiceSelection.obj;
	}
}
