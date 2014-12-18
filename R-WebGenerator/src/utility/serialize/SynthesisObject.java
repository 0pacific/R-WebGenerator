package utility.serialize;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.authority.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import role.*;
import role.RoleManager;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.port.*;
import tpp.portTrans.*;
import transition.*;
import utility.*;
import utility.serialize.Serializer;
import view.*;
import view.authEdit.*;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.accountTableDef.FrameAccountTableDefinition;
import view.tableList.accountTableDef.PanelAccountTableDefinition;
import view.tableList.fieldList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.constArrayEdit.FrameConstArrayEdit;
import view.transProcEdit.constArrayEdit.PanelConstArrayEdit;
import view.transProcEdit.constEdit.FrameConstEdit;
import view.transProcEdit.constEdit.PanelConstEdit;
import view.transProcEdit.fieldSelect.FrameFieldSelect;
import view.transProcEdit.fieldSelect.PanelFieldSelect;
import view.transProcEdit.rightSubPanels.PanelCreateReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelIaReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.serviceSelect.FrameForServiceSelection;
import view.transProcEdit.serviceSelect.PanelForServiceSelection;
import view.transProcEdit.tableSelect.FrameTableSelect;
import view.transProcEdit.tableSelect.PanelTableSelect;
import view.webPageDefinition.*;
import view.webPageDefinition.transAuthEdit.FrameTransAuthEdit;
import view.webPageDefinition.transAuthEdit.PanelTransAuthEdit;
import webPage.*;





public class SynthesisObject implements Serializable {
	// �}�l�[�W���n�C���X�^���X
	public WebPageManager webPageManager = WebPageManager.getInstance();
	public TableManager tableManager = TableManager.getInstance();
	public AuthorityManager authorityManager = AuthorityManager.getInstance();
	public RoleManager roleManager = RoleManager.getInstance();
	public TransitionManager transitionManager = TransitionManager.getInstance();

	// Web�y�[�W��`���
	public PanelWebPageDefAbove panelWebPageDefAbove = PanelWebPageDefAbove.getInstance();
	public PanelWebPageDefBottom panelWebPageDefBottom = PanelWebPageDefBottom.getInstance();
	public WebPagePanelManager webPagePanelManager = WebPagePanelManager.getInstance();

	// �J�ڌ����ҏW�t���[��
	public FrameTransAuthEdit frameTransAuthEdit = FrameTransAuthEdit.getInstance();
	public PanelTransAuthEdit panelTransAuthEdit = PanelTransAuthEdit.getInstance();
	
	// �y�[�W�G�������g��`���
	public Panel_PeEdit_Left panelPeEditLeft = Panel_PeEdit_Left.getInstance();
	public Panel_PeEdit_Right panelPeEditRight = Panel_PeEdit_Right.getInstance();
	public PanelPeEditBottom panelPeEditBottom = PanelPeEditBottom.getInstance();
	public PanelPeEditRightSupporterTableDisplay panelPeEditTb = PanelPeEditRightSupporterTableDisplay.getInstance();
	public PanelPeEditRightSupporterUpdateForm panelPeEditUf = PanelPeEditRightSupporterUpdateForm.getInstance();
	public PanelPeEditRightSupporterSaif panelPeEditSaif = PanelPeEditRightSupporterSaif.getInstance();
	
	// �J�ڃv���Z�X��`���
	public Panel_TpEdit_Above panelTpEditLeft = Panel_TpEdit_Above.getInstance();
	public Panel_TpEdit_Right panelTpEditRight = Panel_TpEdit_Right.getInstance();
	public Panel_TpEdit_Bottom panelTpEditBottom = Panel_TpEdit_Bottom.getInstance();

	// �萔�z���`�E�ҏW�t���[��
	public FrameConstArrayEdit frameConstArrayEdit = FrameConstArrayEdit.getInstance();
	public PanelConstArrayEdit panelConstArrayEdit = PanelConstArrayEdit.getInstance();

	// ���m�萔��`�E�ҏW�t���[��
	public FrameConstEdit frameConstEdit = FrameConstEdit.getInstance();
	public PanelConstEdit panelConstEdit = PanelConstEdit.getInstance();

	// �t�B�[���h�I���t���[��
	public FrameFieldSelect frameFieldSelect = FrameFieldSelect.getInstance();
	public PanelFieldSelect panelFieldSelect = PanelFieldSelect.getInstance();

	// �e�[�u���I���t���[��
	public FrameTableSelect frameTableSelect = FrameTableSelect.getInstance();
	public PanelTableSelect panelTableSelect = PanelTableSelect.getInstance();
	
	// �T�[�r�X�I���t���[��
	public FrameForServiceSelection frameServiceSelect = FrameForServiceSelection.getInstance();
	public PanelForServiceSelection panelServiceSelect = PanelForServiceSelection.getInstance();
	
	// �e��TPP�ҏW�p�l���i�E�p�l���ɑ傫���u������j
	public PanelCreateReflectionEdit panelCrEdit = PanelCreateReflectionEdit.getInstance();
	public PanelIaReflectionEdit panelIrEdit = PanelIaReflectionEdit.getInstance();
	public PanelServiceArgEdit panelServArgEdit = PanelServiceArgEdit.getInstance();
	
	// ���[����`���
	public Panel_RoleEdit_Above panelRoleEditAbove = Panel_RoleEdit_Above.getInstance();
	public Panel_RoleEdit_Bottom panelRoleEditBottom = Panel_RoleEdit_Bottom.getInstance();

	// �e�[�u���ꗗ���
	public PanelTableListAbove panelTableListAbove = PanelTableListAbove.getInstance();
	public PanelTableListBottom	panelTableListBottom = PanelTableListBottom.getInstance();

	// �A�J�E���g�e�[�u����`�t���[��
	public FrameAccountTableDefinition frameAtDef = FrameAccountTableDefinition.getInstance();
	public PanelAccountTableDefinition panelAtDef = PanelAccountTableDefinition.getInstance();

	// �t�B�[���h�ꗗ���
	public Panel_FieldList panelFieldList = Panel_FieldList.getInstance();

	// �t�B�[���h�ǉ��E�ҏW�t���[��
	public FieldEditFrame frameFieldEdit = FieldEditFrame.getInstance();
	public FieldEditPanel panelFieldEdit = FieldEditPanel.getInstance();
	
	// ������`���
	public PanelAuthEditAbove panelAuthEditAbove = PanelAuthEditAbove.getInstance(); 
	public PanelAuthEditBottom panelAuthEditBottom = PanelAuthEditBottom.getInstance(); 

	// �������
	public PanelGeneration panelGeneration = PanelGeneration.getInstance();

	
	
	
	
	/*
	 * SUMMARY :
	 * ���̃C���X�^���X���f�V���A���C�Y��������ɂ�������s����
	 */
	public void reflect() {
		// �}�l�[�W���n�C���X�^���X
		WebPageManager.updateInstance(webPageManager);
		TableManager.updateInstance(tableManager);
		AuthorityManager.updateInstance(authorityManager);
		RoleManager.updateInstance(roleManager);
		TransitionManager.updateInstance(transitionManager);

		// Web�y�[�W��`���
		PanelWebPageDefAbove.updateInstance(panelWebPageDefAbove);
		PanelWebPageDefBottom.updateInstance(panelWebPageDefBottom);
		WebPagePanelManager.updateInstance(webPagePanelManager);

		// �J�ڌ����ҏW�t���[��
		FrameTransAuthEdit.updateInstance(frameTransAuthEdit);
		PanelTransAuthEdit.updateInstance(panelTransAuthEdit);
		
		// �y�[�W�G�������g��`���
		Panel_PeEdit_Left.updateInstance(panelPeEditLeft);
		Panel_PeEdit_Right.updateInstance(panelPeEditRight);
		PanelPeEditBottom.updateInstance(panelPeEditBottom);
		PanelPeEditRightSupporterTableDisplay.updateInstance(panelPeEditTb);
		PanelPeEditRightSupporterUpdateForm.updateInstance(panelPeEditUf);
		PanelPeEditRightSupporterSaif.updateInstance(panelPeEditSaif);

		// �J�ڃv���Z�X��`���
		Panel_TpEdit_Above.updateInstance(panelTpEditLeft);
		Panel_TpEdit_Right.updateInstance(panelTpEditRight);
		Panel_TpEdit_Bottom.updateInstance(panelTpEditBottom);

		// �萔�z���`�E�ҏW�t���[��
		FrameConstArrayEdit.updateInstance(frameConstArrayEdit);
		PanelConstArrayEdit.updateInstance(panelConstArrayEdit);

		// ���m�萔��`�E�ҏW�t���[��
		FrameConstEdit.updateInstance(frameConstEdit);
		PanelConstEdit.updateInstance(panelConstEdit);

		// �t�B�[���h�I���t���[��
		FrameFieldSelect.updateInstance(frameFieldSelect);
		PanelFieldSelect.updateInstance(panelFieldSelect);

		// �e�[�u���I���t���[��
		FrameTableSelect.updateInstance(frameTableSelect);
		PanelTableSelect.updateInstance(panelTableSelect);
		
		// �T�[�r�X�I���t���[��
		FrameForServiceSelection.updateInstance(frameServiceSelect);
		PanelForServiceSelection.updateInstance(panelServiceSelect);
		
		// �e��TPP�ҏW�p�l���i�E�p�l���ɑ傫���u������j
		PanelCreateReflectionEdit.updateInstance(panelCrEdit);
		PanelIaReflectionEdit.updateInstance(panelIrEdit);
		PanelServiceArgEdit.updateInstance(panelServArgEdit);
		
		// ���[����`���
		Panel_RoleEdit_Above.updateInstance(panelRoleEditAbove);
		Panel_RoleEdit_Bottom.updateInstance(panelRoleEditBottom);

		// �e�[�u���ꗗ���
		PanelTableListAbove.updateInstance(panelTableListAbove);
		PanelTableListBottom.updateInstance(panelTableListBottom);

		// �A�J�E���g�e�[�u����`�t���[��
		FrameAccountTableDefinition.updateInstance(frameAtDef);
		PanelAccountTableDefinition.updateInstance(panelAtDef);

		// �t�B�[���h�ꗗ���
		Panel_FieldList.updateInstance(panelFieldList);

		// �t�B�[���h�ǉ��E�ҏW�t���[��
		FieldEditFrame.updateInstance(frameFieldEdit);
		FieldEditPanel.updateInstance(panelFieldEdit);
		
		// ������`���
		PanelAuthEditAbove.updateInstance(panelAuthEditAbove); 
		PanelAuthEditBottom.updateInstance(panelAuthEditBottom); 

		// �������
		PanelGeneration.updateInstance(panelGeneration);
	}
}
