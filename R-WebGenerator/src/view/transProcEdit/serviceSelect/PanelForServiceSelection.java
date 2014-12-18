package view.transProcEdit.serviceSelect;

import java.awt.Dimension;
import java.awt.event.*;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import tpp.service.Service;
import utility.SerializableSpringLayout;
import utility.Slpc;
import view.transProcEdit.Panel_TpEdit_Above;
import view.transProcEdit.serviceArgsWindow.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;

/*
 * Singleton
 */
public class PanelForServiceSelection extends JPanel implements TreeSelectionListener,ActionListener {
	// フレーム上での位置
	public static final int posX = 0;
	public static final int posY = 0;

	// サイズ
	public static final int panelWidth = FrameForServiceSelection.frameWidth;
	public static final int panelHeight = FrameForServiceSelection.frameHeight;
	
	// サービス名一覧のツリー、それを入れるスクロールペイン
	private JTreeForServiceSelection tree;
	private JScrollPane treeScrPane;
	
	// サービス説明のテキストプレーン
	private TextPaneForServDesc descPane;

	// サービス追加ボタン、そのサイズ
	private JButton addButton;
	public static final int addButtonWidth = 200;
	public static final int addButtonHeight = 50;
	
	// キャンセルボタン
	public JButton buttonCancel;
	
	// SpringLayout
	public SerializableSpringLayout springLayout;
	
	/*
	 * インスタンスは最後！
	 */
	private static PanelForServiceSelection obj = new PanelForServiceSelection();
	
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	private PanelForServiceSelection() {
		springLayout = new SerializableSpringLayout();
		setLayout(springLayout);
	}

	
	
	public void locateComps() {
		removeAll();
		
		boolean japanese = GeneratorProperty.japanese();
		
		// サービス名一覧ツリーを初期化
		tree = JTreeForServiceSelection.getInstance();
		tree.setPreferredSize(new Dimension(JTreeForServiceSelection.treeWidth, JTreeForServiceSelection.treeHeight));
		tree.addTreeSelectionListener(this);

		// サービス名一覧ツリーを格納するスクロールペインの初期化・追加
	    treeScrPane = new JScrollPane(tree);
	    treeScrPane.setPreferredSize(new Dimension(JTreeForServiceSelection.treeWidth, JTreeForServiceSelection.treeHeight));
	    Slpc.put(springLayout, "N", treeScrPane, "N", this, 20);
		Slpc.put(springLayout, "W", treeScrPane, "W", this, 20);
	    treeScrPane.getVerticalScrollBar().setUnitIncrement(10);
		add(treeScrPane);

		// サービス説明のテキストペインを初期化・追加
		descPane = TextPaneForServDesc.getInstance();
		descPane.setPreferredSize(new Dimension(FrameForServiceSelection.frameWidth-JTreeForServiceSelection.treeWidth-40,JTreeForServiceSelection.treeHeight));
		//descPane.setPreferredSize(new Dimension(FrameForServiceSelection.frameWidth-treeScrPane.getWidth()-20, treeScrPane.getHeight()));
		Slpc.put(springLayout, "N", descPane, "N", treeScrPane, 0);
		Slpc.put(springLayout, "W", descPane, "E", treeScrPane, 10);
		descPane.setEditable(false);
		add(descPane);

		// サービスの追加を決定するボタン
		addButton = new JButton(japanese?"選択中のサービスを追加":"Add Service");
		addButton.addActionListener(this);
		addButton.setActionCommand("サービス追加ボタン");
		Slpc.put(springLayout, "N", addButton, "S", treeScrPane, 20);
		Slpc.put(springLayout, "W", addButton, "W", this, 20);
		add(addButton);

		// キャンセルボタン
		buttonCancel = new JButton(japanese?"キャンセル":"Cancel");
		buttonCancel.addActionListener(this);
		buttonCancel.setActionCommand("キャンセル");
		Slpc.put(springLayout, "N", buttonCancel, "N", addButton, 0);
		Slpc.put(springLayout, "W", buttonCancel, "E", addButton, 20);
		add(buttonCancel);
		
		FrameForServiceSelection.getInstance().repaint();
		FrameForServiceSelection.getInstance().validate();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * ツリー選択イベントハンドラ
	 */
	public void valueChanged(TreeSelectionEvent e) {
		if(!(e.getSource() instanceof JTreeForServiceSelection)) {
			JOptionPane.showMessageDialog(this, "エラーが発生しました。");
			Debug.error("PanelForServiceSelection valueChanged() : サービス名一覧ツリー以外からのイベント通知です");
			return;
		}

		String selectedServName = tree.getLastSelectedPathComponent().toString();
		Service selectedServ = Service.getInstanceByServiceName(null, selectedServName);

		// サービス名以外の部分がクリックされた
		if(selectedServ==null) {
			return;
		}
		
		String desc = selectedServ.getDescription();
		descPane.setText(desc);

		repaint();
		validate();
	}

	
	
	
	/*
	 * PURPOSE :
	 * ボタンクリックイベントハンドラ
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		boolean japanese = GeneratorProperty.japanese();

		// サービス追加ボタンがクリックされた
		if(cmd.equals("サービス追加ボタン")) {
			String servName = tree.getLastSelectedPathComponent().toString();

			// 確認ダイアログ
			int confirm = JOptionPane.showConfirmDialog(
				this,
				japanese?"サービス「"+servName+"」を追加しますか？":"Add Service '"+servName+"'?",
				japanese?"サービスの追加":"Service Addition",
				JOptionPane.OK_CANCEL_OPTION
			);
			if(confirm==JOptionPane.CANCEL_OPTION) {	// キャンセル
				return;
			}
			Panel_TpEdit_Above.getInstance().addServiceAndServicePanelByServiceName(servName);

			
			FrameForServiceSelection.getInstance().setVisible(false);
			MainFrame.getInstance().setVisible(true);
			MainFrame.getInstance().setEnabled(true);
			MainFrame.getInstance().setFocusable(true);
			MainFrame.getInstance().requestFocus();

			JOptionPane.showMessageDialog(
				MainFrame.getInstance(),
				japanese?
						"サービス「"+servName+"」を表すパネルが追加されました。\nパネルをクリックし、引数の編集を行って下さい。":
						"Service '"+servName+"' added.\nClick Panel and Edit Args.",
				japanese?"サービス追加完了":"Service Addition",
				JOptionPane.PLAIN_MESSAGE,
				null
			);
		}
		// キャンセルボタンクリック
		else if(cmd.equals("キャンセル")) {
			FrameForServiceSelection.getInstance().setVisible(false);
			MainFrame.getInstance().setVisible(true);
			MainFrame.getInstance().setEnabled(true);
			MainFrame.getInstance().setFocusable(true);
			MainFrame.getInstance().requestFocus();
		}
	}
	
	


	/*
	 * PURPOSE :
	 * インスタンス取得
	 */
	public static PanelForServiceSelection getInstance() {
		return PanelForServiceSelection.obj;
	}


	public static void updateInstance(PanelForServiceSelection newObject) {
		PanelForServiceSelection.obj = newObject;
	}
}
