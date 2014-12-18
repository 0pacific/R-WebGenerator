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
	// マネージャ系インスタンス
	public WebPageManager webPageManager = WebPageManager.getInstance();
	public TableManager tableManager = TableManager.getInstance();
	public AuthorityManager authorityManager = AuthorityManager.getInstance();
	public RoleManager roleManager = RoleManager.getInstance();
	public TransitionManager transitionManager = TransitionManager.getInstance();

	// Webページ定義画面
	public PanelWebPageDefAbove panelWebPageDefAbove = PanelWebPageDefAbove.getInstance();
	public PanelWebPageDefBottom panelWebPageDefBottom = PanelWebPageDefBottom.getInstance();
	public WebPagePanelManager webPagePanelManager = WebPagePanelManager.getInstance();

	// 遷移権限編集フレーム
	public FrameTransAuthEdit frameTransAuthEdit = FrameTransAuthEdit.getInstance();
	public PanelTransAuthEdit panelTransAuthEdit = PanelTransAuthEdit.getInstance();
	
	// ページエレメント定義画面
	public Panel_PeEdit_Left panelPeEditLeft = Panel_PeEdit_Left.getInstance();
	public Panel_PeEdit_Right panelPeEditRight = Panel_PeEdit_Right.getInstance();
	public PanelPeEditBottom panelPeEditBottom = PanelPeEditBottom.getInstance();
	public PanelPeEditRightSupporterTableDisplay panelPeEditTb = PanelPeEditRightSupporterTableDisplay.getInstance();
	public PanelPeEditRightSupporterUpdateForm panelPeEditUf = PanelPeEditRightSupporterUpdateForm.getInstance();
	public PanelPeEditRightSupporterSaif panelPeEditSaif = PanelPeEditRightSupporterSaif.getInstance();
	
	// 遷移プロセス定義画面
	public Panel_TpEdit_Above panelTpEditLeft = Panel_TpEdit_Above.getInstance();
	public Panel_TpEdit_Right panelTpEditRight = Panel_TpEdit_Right.getInstance();
	public Panel_TpEdit_Bottom panelTpEditBottom = Panel_TpEdit_Bottom.getInstance();

	// 定数配列定義・編集フレーム
	public FrameConstArrayEdit frameConstArrayEdit = FrameConstArrayEdit.getInstance();
	public PanelConstArrayEdit panelConstArrayEdit = PanelConstArrayEdit.getInstance();

	// モノ定数定義・編集フレーム
	public FrameConstEdit frameConstEdit = FrameConstEdit.getInstance();
	public PanelConstEdit panelConstEdit = PanelConstEdit.getInstance();

	// フィールド選択フレーム
	public FrameFieldSelect frameFieldSelect = FrameFieldSelect.getInstance();
	public PanelFieldSelect panelFieldSelect = PanelFieldSelect.getInstance();

	// テーブル選択フレーム
	public FrameTableSelect frameTableSelect = FrameTableSelect.getInstance();
	public PanelTableSelect panelTableSelect = PanelTableSelect.getInstance();
	
	// サービス選択フレーム
	public FrameForServiceSelection frameServiceSelect = FrameForServiceSelection.getInstance();
	public PanelForServiceSelection panelServiceSelect = PanelForServiceSelection.getInstance();
	
	// 各種TPP編集パネル（右パネルに大きく置かれるやつ）
	public PanelCreateReflectionEdit panelCrEdit = PanelCreateReflectionEdit.getInstance();
	public PanelIaReflectionEdit panelIrEdit = PanelIaReflectionEdit.getInstance();
	public PanelServiceArgEdit panelServArgEdit = PanelServiceArgEdit.getInstance();
	
	// ロール定義画面
	public Panel_RoleEdit_Above panelRoleEditAbove = Panel_RoleEdit_Above.getInstance();
	public Panel_RoleEdit_Bottom panelRoleEditBottom = Panel_RoleEdit_Bottom.getInstance();

	// テーブル一覧画面
	public PanelTableListAbove panelTableListAbove = PanelTableListAbove.getInstance();
	public PanelTableListBottom	panelTableListBottom = PanelTableListBottom.getInstance();

	// アカウントテーブル定義フレーム
	public FrameAccountTableDefinition frameAtDef = FrameAccountTableDefinition.getInstance();
	public PanelAccountTableDefinition panelAtDef = PanelAccountTableDefinition.getInstance();

	// フィールド一覧画面
	public Panel_FieldList panelFieldList = Panel_FieldList.getInstance();

	// フィールド追加・編集フレーム
	public FieldEditFrame frameFieldEdit = FieldEditFrame.getInstance();
	public FieldEditPanel panelFieldEdit = FieldEditPanel.getInstance();
	
	// 権限定義画面
	public PanelAuthEditAbove panelAuthEditAbove = PanelAuthEditAbove.getInstance(); 
	public PanelAuthEditBottom panelAuthEditBottom = PanelAuthEditBottom.getInstance(); 

	// 生成画面
	public PanelGeneration panelGeneration = PanelGeneration.getInstance();

	
	
	
	
	/*
	 * SUMMARY :
	 * このインスタンスをデシリアライズした直後にこれを実行する
	 */
	public void reflect() {
		// マネージャ系インスタンス
		WebPageManager.updateInstance(webPageManager);
		TableManager.updateInstance(tableManager);
		AuthorityManager.updateInstance(authorityManager);
		RoleManager.updateInstance(roleManager);
		TransitionManager.updateInstance(transitionManager);

		// Webページ定義画面
		PanelWebPageDefAbove.updateInstance(panelWebPageDefAbove);
		PanelWebPageDefBottom.updateInstance(panelWebPageDefBottom);
		WebPagePanelManager.updateInstance(webPagePanelManager);

		// 遷移権限編集フレーム
		FrameTransAuthEdit.updateInstance(frameTransAuthEdit);
		PanelTransAuthEdit.updateInstance(panelTransAuthEdit);
		
		// ページエレメント定義画面
		Panel_PeEdit_Left.updateInstance(panelPeEditLeft);
		Panel_PeEdit_Right.updateInstance(panelPeEditRight);
		PanelPeEditBottom.updateInstance(panelPeEditBottom);
		PanelPeEditRightSupporterTableDisplay.updateInstance(panelPeEditTb);
		PanelPeEditRightSupporterUpdateForm.updateInstance(panelPeEditUf);
		PanelPeEditRightSupporterSaif.updateInstance(panelPeEditSaif);

		// 遷移プロセス定義画面
		Panel_TpEdit_Above.updateInstance(panelTpEditLeft);
		Panel_TpEdit_Right.updateInstance(panelTpEditRight);
		Panel_TpEdit_Bottom.updateInstance(panelTpEditBottom);

		// 定数配列定義・編集フレーム
		FrameConstArrayEdit.updateInstance(frameConstArrayEdit);
		PanelConstArrayEdit.updateInstance(panelConstArrayEdit);

		// モノ定数定義・編集フレーム
		FrameConstEdit.updateInstance(frameConstEdit);
		PanelConstEdit.updateInstance(panelConstEdit);

		// フィールド選択フレーム
		FrameFieldSelect.updateInstance(frameFieldSelect);
		PanelFieldSelect.updateInstance(panelFieldSelect);

		// テーブル選択フレーム
		FrameTableSelect.updateInstance(frameTableSelect);
		PanelTableSelect.updateInstance(panelTableSelect);
		
		// サービス選択フレーム
		FrameForServiceSelection.updateInstance(frameServiceSelect);
		PanelForServiceSelection.updateInstance(panelServiceSelect);
		
		// 各種TPP編集パネル（右パネルに大きく置かれるやつ）
		PanelCreateReflectionEdit.updateInstance(panelCrEdit);
		PanelIaReflectionEdit.updateInstance(panelIrEdit);
		PanelServiceArgEdit.updateInstance(panelServArgEdit);
		
		// ロール定義画面
		Panel_RoleEdit_Above.updateInstance(panelRoleEditAbove);
		Panel_RoleEdit_Bottom.updateInstance(panelRoleEditBottom);

		// テーブル一覧画面
		PanelTableListAbove.updateInstance(panelTableListAbove);
		PanelTableListBottom.updateInstance(panelTableListBottom);

		// アカウントテーブル定義フレーム
		FrameAccountTableDefinition.updateInstance(frameAtDef);
		PanelAccountTableDefinition.updateInstance(panelAtDef);

		// フィールド一覧画面
		Panel_FieldList.updateInstance(panelFieldList);

		// フィールド追加・編集フレーム
		FieldEditFrame.updateInstance(frameFieldEdit);
		FieldEditPanel.updateInstance(panelFieldEdit);
		
		// 権限定義画面
		PanelAuthEditAbove.updateInstance(panelAuthEditAbove); 
		PanelAuthEditBottom.updateInstance(panelAuthEditBottom); 

		// 生成画面
		PanelGeneration.updateInstance(panelGeneration);
	}
}
