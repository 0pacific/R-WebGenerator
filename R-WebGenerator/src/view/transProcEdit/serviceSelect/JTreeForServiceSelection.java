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
	// パネル上での位置
	public static final int posX = 10;
	public static final int posY = 10;

	// サイズ
	public static final int treeWidth = 200;
	public static final int treeHeight = 300;

	/*
	 * インスタンスは最後！
	 */
	private static JTreeForServiceSelection obj = new JTreeForServiceSelection();


	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private JTreeForServiceSelection() {
		boolean japanese = GeneratorProperty.japanese();
		
		// カテゴリ「四則演算」のノードとその子ノード（サービス名）
		DefaultMutableTreeNode node_calc = new DefaultMutableTreeNode(japanese?"四則演算":"Arithmetic Operation"); 
		String[] calcServNames = Service.getServNamesByCateg(japanese?"四則演算":"Arithmetic Operation");
		Debug.test(7777);
		Debug.test(calcServNames.length);
		for(int i=0; i<calcServNames.length; i++) {
			String servName = calcServNames[i];
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(servName);
			node_calc.add(node);
		}

		// カテゴリ「フィールド処理」のノードとその子ノード（サービス名）
		DefaultMutableTreeNode node_field = new DefaultMutableTreeNode(japanese?"フィールド処理":"Field Handling"); 
		String[] fieldServNames = Service.getServNamesByCateg(japanese?"フィールド処理":"Field Handling");
		for(int i=0; i<fieldServNames.length; i++) {
			String servName = fieldServNames[i];
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(servName);
			node_field.add(node);
		}

		// カテゴリ「テーブル処理」のノードとその子ノード（サービス名）
		DefaultMutableTreeNode node_table = new DefaultMutableTreeNode(japanese?"テーブル処理":"Table Handling"); 
		String[] tableServNames = Service.getServNamesByCateg(japanese?"テーブル処理":"Table Handling");
		for(int i=0; i<tableServNames.length; i++) {
			String servName = tableServNames[i];
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(servName);
			node_table.add(node);
		}
		// ルートノード「サービス」を作成 -> 各カテゴリノード追加
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(GeneratorProperty.japanese()?"サービス":"Service");
		root.add(node_calc);
		root.add(node_field);
		root.add(node_table);

		// ルートノードを指定しモデル作成
		DefaultTreeModel model = new DefaultTreeModel(root);
		setModel(model);

		// 全ノードを展開しておく
		int row = 0;
		while(row<getRowCount()) {
			expandRow(row);
			row++;
		}
	}
	
	

	
	
	/*
	 * PURPOSE :
	 * インスタンス修得
	 */
	public static JTreeForServiceSelection getInstance() {
		return JTreeForServiceSelection.obj;
	}
}
