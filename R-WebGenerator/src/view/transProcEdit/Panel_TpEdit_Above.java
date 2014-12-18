package view.transProcEdit;

import gui.*;
import gui.arrow.*;
import pageElement.PageElement;
import pageElement.PageElementCreateForm;
import pageElement.PageElementDisplayArea;
import pageElement.PageElementSaif;
import pageElement.PageElementTableDisplay;

import table.*;
import table.field.Field;
import tpp.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.*;

import mainFrame.MainFrame;
import debug.Debug;

import tpp.*;
import tpp.port.DataPort;
import tpp.port.PageElementPort;
import tpp.port.TppPort;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import tpp.service.Service;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import view.EditPanel;
import view.transProcEdit.editPanel.CreateFormReflectionPanel;
import view.transProcEdit.editPanel.CreateReflectionPanel;
import view.transProcEdit.editPanel.EndPagePanel;
import view.transProcEdit.editPanel.IaReflectionPanel;
import view.transProcEdit.editPanel.PageElementPanel;
import view.transProcEdit.editPanel.ServicePanel;
import view.transProcEdit.editPanel.StartPagePanel;
import view.transProcEdit.editPanel.TableReadingPanel;
import view.transProcEdit.editPanel.TpPagePanel;
import view.transProcEdit.editPanel.UpdateFormReflectionPanel;
import view.transProcEdit.rightSubPanels.PanelCreateReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelIaReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.transProcEdit.subFrame.Panel_TransProcEdit_Sub;
import webPage.WebPage;
import utility.*;

/*
 * Singleton
 */
public class Panel_TpEdit_Above extends JPanel implements MouseListener,Serializable {
	public TransitionProcess editingTp;
	private StartPagePanel startPagePanel;
	private EndPagePanel endPagePanel;

	public static int special_flag = 0;
	
	// SprintLayout
	public SerializableSpringLayout layout;
	
	private JPanel selectedInputPanel = null;
	private JPanel selectedOutputPanel = null;
	
	// サービスパネル配列
	private ArrayList<ServicePanel> csPanels = new ArrayList<ServicePanel>();
	// テーブルリーディングパネル配列
	private ArrayList<TableReadingPanel> tblReadPanels = new ArrayList<TableReadingPanel>();
	// CreateFormリフレクションパネル配列
	public ArrayList<CreateFormReflectionPanel> panelArrayCreateFormReflection = new ArrayList<CreateFormReflectionPanel>();
	// Createリフレクションパネル配列
	public ArrayList<CreateReflectionPanel> panelArrayCreateReflection = new ArrayList<CreateReflectionPanel>();
	// UpdateFormリフレクションパネル配列
	public ArrayList<UpdateFormReflectionPanel> panelArrayUpdateFormReflection = new ArrayList<UpdateFormReflectionPanel>();
	// IAリフレクションパネル配列
	public ArrayList<IaReflectionPanel> panelArrayIaReflection = new ArrayList<IaReflectionPanel>();
	
	public final Color bgColor = Color.WHITE;
	
	// 包み込むスクロールペインのサイズ
	public static final int spWidth = 840;

	// パネルサイズ
	public static final int panelWidth = 3000;
	public static final int panelHeight = 450;

	// パネル左上に表示する文字列のサイズ
	public static final int PAN_STR_SIZE = 12;
	
	// サービスパネルやテーブルリーディングパネルなどの上でマウスプレスをした際、そこの座標（左上頂点からの相対座標）を格納
	// マウスリリース時、この値を使い移動
	private int pressX;
	private int pressY;

	// モードを表す定数
	public static final int MODE_NORMAL = 0;
	public static final int MODE_SELECT_TFD = 1;
	public static final int MODE_SELECT_OUTPUT_TFD = 2;
	public static final int MODE_SELECT_CREATE_REFLECTION_INPUT_TFD = 3;
	public static final int MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD = 4;
	public static final int MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD = 5;
	public static final int MODE_SELECT_SERVICE_ARG_INT = 6;
	public static final int MODE_SELECT_SERVICE_ARG_VARCHAR = 7;
	public static final int MODE_SELECT_SERVICE_ARG_DATETIME = 8;
	public static final int MODE_SELECT_SERVICE_ARG_DATE = 9;
	public static final int MODE_SELECT_SERVICE_ARG_TIME = 10;
	
	// モード初期化
	public int mode = MODE_NORMAL;
	
	/*
	 * インスタンスは最後！
	 */
	private static Panel_TpEdit_Above obj = new Panel_TpEdit_Above();
	
	
	
	

	
	
	
	
	private Panel_TpEdit_Above() {
		setLayout(null);
		setBackground(Color.WHITE);

		addMouseListener(this);

	    // KeyListenerは現時点で複数コンポーネントに実装している
		setFocusable(true);	// KeyListenerを追加するときいるとかいらないとか
	}


		

	
	/*
	 * PURPOSE :
	 * TPPを表す各パネルの再配置
	 */
	public void locateTppPanels() {
		// 全コンポーネント除去
		removeAll();
		
		// 遷移元・遷移先ページパネル以外の配置
		for(int i=0; i<editingTp.getTppNum(); i++) {
			TransitionProcessPart tpp = editingTp.getTpp(i);

			// TPP「サービス呼出」 -> サービスパネル配置
			if(tpp instanceof Service) {
				ServicePanel csPanel = getCsPanelByCs((Service)tpp);
				csPanel.setBounds(csPanel.getPosX(), csPanel.getPosY(), ServicePanel.PANEL_WIDTH, ServicePanel.PANEL_HEIGHT);
				Debug.out("ServicePanelをaddします。 x:" + csPanel.getPosX() + " y:" + csPanel.getPosY());
				add(csPanel);
			}
			// TPP「テーブルリーディング」 -> テーブルリーディングパネルの配置
			else if(tpp instanceof TppTableReading) {
				TableReadingPanel trp = getTrPanelByTr((TppTableReading)tpp);
				trp.setBounds(trp.getPosX(), trp.getPosY(), TableReadingPanel.PANEL_WIDTH, TableReadingPanel.PANEL_HEIGHT);
				add(trp);
			}
			// CreateFormリフレクションのパネル配置
			else if(tpp instanceof TppCreateFormReflection) {
				CreateFormReflectionPanel cfrp = getCfrPanelByCfr((TppCreateFormReflection)tpp);
				cfrp.setBounds(cfrp.getPosX(), cfrp.getPosY(), CreateFormReflectionPanel.PANEL_WIDTH, CreateFormReflectionPanel.PANEL_HEIGHT);
				add(cfrp);
			}
			// Createリフレクションのパネル配置
			else if(tpp instanceof TppCreateReflection) {
				CreateReflectionPanel crp = getCrPanelByCr((TppCreateReflection)tpp);
				crp.setBounds(crp.getPosX(), crp.getPosY(), CreateReflectionPanel.PANEL_WIDTH, CreateReflectionPanel.PANEL_HEIGHT);
				add(crp);
			}
			// UpdateFormリフレクションのパネル配置
			else if(tpp instanceof TppUpdateFormReflection) {
				UpdateFormReflectionPanel ufrp = getUfrPanelByUfr((TppUpdateFormReflection)tpp);
				ufrp.setBounds(ufrp.getPosX(), ufrp.getPosY(), UpdateFormReflectionPanel.PANEL_WIDTH, UpdateFormReflectionPanel.PANEL_HEIGHT);
				add(ufrp);
			}
			// IAリフレクションのパネル配置
			else if(tpp instanceof TppIAReflection) {
				IaReflectionPanel irp = getIrPanelByIr((TppIAReflection)tpp);
				irp.setBounds(irp.getPosX(), irp.getPosY(), IaReflectionPanel.PANEL_WIDTH, IaReflectionPanel.PANEL_HEIGHT);
				add(irp);
				irp.updateTextPaneText();
			}
		}

		// 遷移元ページパネルの配置
		WebPage startPage = editingTp.getStartPage();
		startPagePanel = new StartPagePanel(startPage);
		int startPagePanelHeight = startPagePanel.calcHeight();
		startPagePanel.setBounds(startPagePanel.getPosX(), startPagePanel.getPosY(), TpPagePanel.panelWidth, startPagePanelHeight);
		add(startPagePanel);
		startPagePanel.addMouseListener(this);
		startPagePanel.locatePePanels();
		startPagePanel.setPePanelsPos();

		// 遷移先ページパネルの配置
		WebPage endPage = editingTp.getEndPage();
		endPagePanel = new EndPagePanel(endPage);
		int endPagePanelHeight = endPagePanel.calcHeight();
		endPagePanel.setBounds(endPagePanel.getPosX(), endPagePanel.getPosY(), TpPagePanel.panelWidth, endPagePanelHeight);
		add(endPagePanel);
		endPagePanel.addMouseListener(this);
		endPagePanel.locatePePanels();
		endPagePanel.setPePanelsPos();

		// 遷移元・遷移先の全PageElementパネルへ、マウスリスナ追加
		for(int i=0; i<startPagePanel.pePanels.size(); i++) {
			PageElementPanel pep = startPagePanel.pePanels.get(i);
			pep.removeMouseListener(this);
			pep.addMouseListener(this);
		}
		for(int i=0; i<endPagePanel.pePanels.size(); i++) {
			PageElementPanel pep = endPagePanel.pePanels.get(i);
			pep.removeMouseListener(this);
			pep.addMouseListener(this);
		}

		// ジェネレータパネル再描画・再検証
		MainFrame.repaintAndValidate();
	}
	
	

	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;

		// 背景塗り潰し
		g2.setColor(bgColor);
		g2.draw(new Line2D.Double(0, 0, getWidth(), getHeight()));

		// ポートトランスミッションを表す矢印を描画
		DirectionDecider dd = new DirectionDecider();
		PortTransManager ptm = editingTp.portTransManager;
		for(int i=0; i<ptm.getPortTransNum(); i++) {
			PortTrans pt = ptm.getPortTrans(i);
			DataPort startPort = pt.getStartPort();
			DataPort endPort = pt.getEndPort();

			
			
			EditPanel startPanel = null;
			if(startPort instanceof PageElementPort) {
				PageElement startPe = ((PageElementPort) startPort).getPageElement();
				startPanel = getEditPanelByPe(startPe);
			}
			else if(startPort instanceof TppPort) {
				TransitionProcessPart startTpp = ((TppPort) startPort).getTpp();
				
				// モノ定数、定数配列のTPP......パネルで表現しないので矢印も不要 -> 次へ
				if(startTpp instanceof TppConstInt || startTpp instanceof TppConstString || startTpp instanceof TppConstArrayInt || startTpp instanceof TppConstArrayString) {
					continue;
				}
				startPanel = getEditPanelByTpp(startTpp);
			}

			
			
			EditPanel endPanel = null;
			if(endPort instanceof PageElementPort) {
				PageElement endPe = ((PageElementPort) endPort).getPageElement();
				endPanel = getEditPanelByPe(endPe);
			}
			else if(endPort instanceof TppPort) {
				TransitionProcessPart endTpp = ((TppPort) endPort).getTpp();
				if(endTpp instanceof TppConstArrayInt) {
					continue;
				}
				endPanel = getEditPanelByTpp(endTpp);
			}
			
			
			
			// 矢印作成
			Point[] pointPair = dd.decide(startPanel, endPanel);
			Point startPoint = pointPair[0];
			Point endPoint = pointPair[1];
			ArrowShape arrowShape = new ArrowShape(startPoint, endPoint);

			
			
			// 矢印描画
			BasicStroke wideStroke = new BasicStroke(2.0f);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setStroke(wideStroke);
			g2.draw(arrowShape);
		}
	}
	
	
	
	
	
	public EditPanel getEditPanelByPe(PageElement pe) {
		// 遷移元ページのPageElementPanelを探索
		ArrayList<PageElementPanel> startPagePePanels = startPagePanel.pePanels;
		for(int i=0; i<startPagePePanels.size(); i++) {
			PageElementPanel pep = startPagePePanels.get(i);
			if(pep.pageElement==pe) {
				return pep;
			}
		}
		// 遷移先ページのPageElementPanelを探索
		ArrayList<PageElementPanel> endPagePePanels = endPagePanel.pePanels;
		for(int i=0; i<endPagePePanels.size(); i++) {
			PageElementPanel pep = endPagePePanels.get(i);
			if(pep.pageElement==pe) {
				return pep;
			}
		}

		// エラー
		Debug.error("Panel_TpEdit_Left getEditPanelByPe() : PageElementPanelが見つかりません");
		return null;
	}
	

	
	
	
	
	/*
	 * SUMMARY :
	 * 指定したTPPを表すEditPanelを取得
	 * ポートトランスミッションを表す矢印を描画する際に呼び出す
	 */
	public EditPanel getEditPanelByTpp(TransitionProcessPart tpp) {
		for(int i=0; i<csPanels.size(); i++) {
			ServicePanel csp = csPanels.get(i);
			Service service = csp.getService();
			if(tpp==service) {
				return csp;
			}
		}
		for(int i=0; i<tblReadPanels.size(); i++) {
			TableReadingPanel trp = tblReadPanels.get(i);
			TppTableReading tr = trp.getTblRead();
			if(tpp==tr) {
				return trp;
			}
		}
		for(int i=0; i<this.panelArrayCreateFormReflection.size(); i++) {
			CreateFormReflectionPanel cfrp = panelArrayCreateFormReflection.get(i);
			TppCreateFormReflection cfr = cfrp.reflection;
			if(tpp==cfr) {
				return cfrp;
			}
		}
		for(int i=0; i<this.panelArrayCreateReflection.size(); i++) {
			CreateReflectionPanel crp = panelArrayCreateReflection.get(i);
			TppCreateReflection cr = crp.reflection;
			if(tpp==cr) {
				return crp;
			}
		}
		for(int i=0; i<this.panelArrayUpdateFormReflection.size(); i++) {
			UpdateFormReflectionPanel ufrp = panelArrayUpdateFormReflection.get(i);
			TppUpdateFormReflection ufr = ufrp.reflection;
			if(tpp==ufr) {
				return ufrp;
			}
		}
		for(int i=0; i<this.panelArrayIaReflection.size(); i++) {
			IaReflectionPanel irp = panelArrayIaReflection.get(i);
			TppIAReflection ir = irp.reflection;
			if(tpp==ir) {
				return irp;
			}
		}
		
		// エラー
		JOptionPane.showMessageDialog(this, "エラーが発生しました。");
		Debug.error("Tppパネルが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	
	
	/*
	 * PURPOSE :
	 * 指定サービスを遷移プロセスへ追加、続けてサービスパネルを遷移プロセス図へ追加
	 */
	public void addServiceAndServicePanelByServiceName(String serviceName) {
		Service service = Service.getInstanceByServiceName(editingTp, serviceName);

		// 編集中のTransitionProcessにサービスを追加
		editingTp.addTpp(service);

		addServicePanelByService(service, 200, 300);

		// TPPパネルを再配置
		locateTppPanels();
	}
	
	
	
	
	
	public void addServicePanelByService(Service service, int panelPosX, int panelPosY) {
		ServicePanel csp = new ServicePanel(service);
		csp.setPosX(panelPosX);
		csp.setPosY(panelPosY);
		csp.addMouseListener(this);
		csPanels.add(csp);
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * 指定したテーブルからテーブルリーディングTPPを作成し編集中遷移プロセスへ追加、テーブルリーディングパネルを遷移プロセス図へ追加する
	 */
	public void addTableReadingAndTableReadingPanelByTable(SuperTable table) {
		// TPP「テーブルリーディング」のインスタンス
		TppTableReading tr = new TppTableReading(editingTp, table);

		// テーブルリーディングを編集中遷移プロセスへ追加
		editingTp.addTpp(tr);

		addTableReadingPanelByTableReading(tr, 30, 100);
		
		// TPPパネルを再配置
		locateTppPanels();
	}
	public void addTableReadingPanelByTableReading(TppTableReading tr, int panelPosX, int panelPosY) {
		TableReadingPanel trp = new TableReadingPanel(tr);
		trp.setPosX(panelPosX);
		trp.setPosY(panelPosY);
		trp.addMouseListener(this);
		tblReadPanels.add(trp);
	}
	
	
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * 指定したCreateフォームに基づいてを作成し、編集中遷移プロセスへ追加、CreateFormReflectionパネルを遷移プロセス図へ追加する
	 */
	public void addCreateFormReflectionPanel(TppCreateFormReflection reflection) {
		CreateFormReflectionPanel cfrp = new CreateFormReflectionPanel(reflection);
		cfrp.setPosX(50);
		cfrp.setPosY(200);
		cfrp.addMouseListener(this);
		panelArrayCreateFormReflection.add(cfrp);
		
		// TPPパネル再配置は不要（このメソッドが呼ばれるのはCreateフォームが追加されたときで、そのときに遷移プロセス定義画面は開かれていないため）
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * 指定したUpdateフォームリフレクションを表すパネル作成、遷移プロセスへ追加
	 */
	public void addUpdateFormReflectionPanel(TppUpdateFormReflection reflection) {
		UpdateFormReflectionPanel ufrp = new UpdateFormReflectionPanel(reflection);
		ufrp.setPosX(100);
		ufrp.setPosY(200);
		ufrp.addMouseListener(this);
		panelArrayUpdateFormReflection.add(ufrp);
		
		// TPPパネル再配置は不要（このメソッドが呼ばれるのはUpdateフォームが追加されたときで、そのときに遷移プロセス定義画面は開かれていないため）
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * 指定されたCreateリフレクションに対応したCreateReflectionパネルを、編集中の遷移プロセス図へ追加する
	 */
	public void addCreateReflectionPanel(TppCreateReflection reflection, int panelPosX, int panelPosY) {
		CreateReflectionPanel crp = new CreateReflectionPanel(reflection);
		crp.setPosX(panelPosX);
		crp.setPosY(panelPosY);
		crp.addMouseListener(this);
		panelArrayCreateReflection.add(crp);
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * 指定されたIAリフレクションを表すIaReflectionパネルを作成し、編集中の遷移プロセス図へ追加する
	 */
	public void addIaReflectionPanel(TppIAReflection reflection, int panelPosX, int panelPosY) {
		IaReflectionPanel irp = new IaReflectionPanel(reflection);
		irp.setPosX(panelPosX);
		irp.setPosY(panelPosY);
		irp.addMouseListener(this);
		panelArrayIaReflection.add(irp);
	}
	


	
	
	
	/*
	 * PURPOSE :
	 * マウスプレスイベントハンドラ
	 * 移動可能なパネル等をプレスした際、その相対座標（左上頂点からの座標）を記録する
	 */
	public void mousePressed(MouseEvent e) {
		Object source = e.getSource();

		// 移動できるコンポーネント上でプレスされたのかどうか、をチェック
		boolean movable = source instanceof EditPanel
							&& !(source instanceof PageElementPanel);
		
		// 移動できないコンポーネントだったなら、何もしなくてよい
		if(!movable) {
			return;
		}

		// コンポーネントの移動は通常モードでのみ可能
		if(mode!=MODE_NORMAL) {
			return;
		}

		pressX = e.getX();
		pressY = e.getY();
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * マウスリリースイベントハンドラ
	 * 移動可能なパネル等がリリースされた際、プレス位置相対座標とリリース位置相対座標からX,Yの移動距離を計算し、移動させる
	 * 移動させるとはすなわち、Panel_TpEdit_Left上における絶対座標を変更するということ
	 */
	public void mouseReleased(MouseEvent e) {
		JPanel source = (JPanel)e.getSource();

		// 移動できるコンポーネント上でプレスされたのかどうか、をチェック
		boolean movable = source instanceof EditPanel;

		// 移動できないコンポーネントだったなら、何もしなくてよい
		if(!movable) {
			return;
		}

		// コンポーネントの移動は通常モードでのみ可能
		if(mode!=MODE_NORMAL) {
			return;
		}

		
		
		/*
		 * この時点で、EditPanelに対してマウスリリースが行われたのは確実
		 */

		
		
		// プレスされたコンポーネントは、これから移動するので一旦除去
		remove(source);
		
		// リリース位置相対座標（移動するコンポーネントの、左上頂点からの座標）
		int rlsX = e.getX();
		int rlsY = e.getY();

		// X,Y方向の移動距離
		int xDiff = rlsX - pressX;
		int yDiff = rlsY - pressY;

		
		// 遷移元・遷移先ページパネルの場合
		if(source instanceof TpPagePanel) {
			TpPagePanel tpPagePanel = (TpPagePanel)source;
			tpPagePanel.setPosX(tpPagePanel.getPosX() + xDiff);
			tpPagePanel.setPosY(tpPagePanel.getPosY() + yDiff);
			tpPagePanel.setBounds(tpPagePanel.getPosX(), tpPagePanel.getPosY(),
							TpPagePanel.panelWidth, tpPagePanel.calcHeight());
			add(tpPagePanel);

			// 全ページエレメントパネルの、背面パネル左上頂点からの座標を、再設定する
			tpPagePanel.setPePanelsPos();
		}
		// ページ構成要素パネル -> 移動不可
		else if(source instanceof PageElementPanel) {
		}
		// それ以外のEditPanelインスタンスの場合
		else if(source instanceof EditPanel) {
			EditPanel sourcePanel = (EditPanel)source;
			sourcePanel.setPosX(sourcePanel.getPosX() + xDiff);
			sourcePanel.setPosY(sourcePanel.getPosY() + yDiff);

			// パネルの種類によってサイズを適切に設定
			int WIDTH = 0;
			int HEIGHT = 0;
			if(sourcePanel instanceof ServicePanel) {
				WIDTH = ServicePanel.PANEL_WIDTH;
				HEIGHT = ServicePanel.PANEL_HEIGHT;
			}
			else if(source instanceof TableReadingPanel) {
				WIDTH = TableReadingPanel.PANEL_WIDTH;
				HEIGHT = TableReadingPanel.PANEL_HEIGHT;
			}
			else if(source instanceof CreateFormReflectionPanel) {
				WIDTH = CreateFormReflectionPanel.PANEL_WIDTH;
				HEIGHT = CreateFormReflectionPanel.PANEL_HEIGHT;
			}
			else if(source instanceof CreateReflectionPanel) {
				WIDTH = CreateReflectionPanel.PANEL_WIDTH;
				HEIGHT = CreateReflectionPanel.PANEL_HEIGHT;
			}
			else if(source instanceof UpdateFormReflectionPanel) {
				WIDTH = UpdateFormReflectionPanel.PANEL_WIDTH;
				HEIGHT = UpdateFormReflectionPanel.PANEL_HEIGHT;
			}
			else if(source instanceof IaReflectionPanel) {
				WIDTH = IaReflectionPanel.PANEL_WIDTH;
				HEIGHT = IaReflectionPanel.PANEL_HEIGHT;
			}

			sourcePanel.setBounds(sourcePanel.getPosX(), sourcePanel.getPosY(), WIDTH, HEIGHT);
			add(sourcePanel);
		}

		
		MainFrame.repaintAndValidate();
	}

	
	
	
	
	
	/*
	 * PURPOSE :
	 * マウスクリックイベントハンドラ
	 * ・サービスパネルのクリック -> 右側に引数と編集ボタンを表示する
	 */
	public void mouseClicked(MouseEvent e) {
		Object source = e.getSource();
		
		// エラー（パネル以外へのクリック。ありえないはずだが）
		if(!(source instanceof JPanel)) {
			Debug.error("Panel_TpEdit_Left mousePressed() : 想定外のイベント発生源です。JPanelではありません。");
		}

		// 通常モード時のクリック
		if(mode==Panel_TpEdit_Above.MODE_NORMAL) {
			// サービスパネルの左クリック
			if(source instanceof ServicePanel) {
				ServicePanel srcCsp = (ServicePanel)source;
				Service service = srcCsp.getService();
				Panel_TransProcEdit_Sub.getInstance().showCsArgs(service);
				
				if(special_flag==0) {
					special_flag = 1;
					mouseClicked(e);
				}
			}
			// Createリフレクションパネルの左クリック
			else if(source instanceof CreateReflectionPanel) {
				CreateReflectionPanel sourceCrp = (CreateReflectionPanel)source;
				TppCreateReflection createReflection = sourceCrp.reflection;
				Panel_TransProcEdit_Sub.getInstance().displayPanelCreateReflectionEdit(createReflection);
			}
			// IAリフレクションパネルの左クリック
			else if(source instanceof IaReflectionPanel) {
				IaReflectionPanel sourceIrp = (IaReflectionPanel)source;
				TppIAReflection iaReflection = sourceIrp.reflection;
				Panel_TransProcEdit_Sub.getInstance().displayPanelIaReflectionEdit(iaReflection);
			}
		
			Frame_TransProcEdit_Sub.repaintAndValidate();
			Frame_TransProcEdit_Sub.getInstance().requestFocus();

			// 右クリック
			if(e.getButton()==MouseEvent.BUTTON3) {
				// 背景への右クリック -> 何もしない
				if(e.getSource() instanceof Panel_TpEdit_Above) {
					Debug.out("背景への右クリックなのでスルーします");
					return;
				}

				Debug.out("RightClick!");

				// 入力パネル右クリック -> そのパネルを記録
				if(selectedInputPanel==null) {
					selectedInputPanel = (JPanel)e.getSource();
				}
				// 出力パネル右クリック -> 必要な処理を行う
				else {
					selectedOutputPanel = (JPanel)e.getSource();
				
					// 出力先がService
					if(selectedOutputPanel instanceof ServicePanel) {
						Service selectedOutputCs = ((ServicePanel)selectedOutputPanel).getService();
					
						// PageElementパネル -> Serviceパネル
						if(selectedInputPanel instanceof PageElementPanel) {
							PageElement selectedInputPageElement = ((PageElementPanel)selectedInputPanel).getPageElement();

							// TableDisplay -> Service
							if(selectedInputPageElement instanceof PageElementTableDisplay) {
								// TFDを一つでも受け取るサービスかチェックする
								boolean recTfd = selectedOutputCs.canReceiveData(Service.IO_TYPE_TFD);
								// ◆暫定　ダイアログボックスなどで注意したい
								if(!recTfd) {
									return;
								}

								// これ以上TFDインプットを定義できるのかチェックする
								boolean recMoreTfd = selectedOutputCs.canReceiveMoreData(Service.IO_TYPE_TFD);
								// ◆暫定　ダイアログボックスなどで注意したい
								if(!recMoreTfd) {
									return;
								}
							
								MainFrame.getInstance().setEnabled(false);
							}
						}
					}
					// 出力先がPageElement
					else if(selectedOutputPanel instanceof PageElementPanel) {
					}
				
					// 処理が終わったので、「どのパネルが入力元として右クリックされているか」の記録を空に戻す
					selectedInputPanel = null;
				}
			}
		}
		// INT/VARCHAR/DATETIME/DATE/TIME型サービス引数選択モードでのクリック
		else if(mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_INT
				|| mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_VARCHAR
				|| mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATETIME
				|| mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATE
				|| mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_TIME) {

			// ページ構成要素パネルが選択された場合
			if(source instanceof PageElementPanel) {
				PageElementPanel pePanel = (PageElementPanel)source;
				PageElement pageElement = pePanel.pageElement;

				// SAIF以外のページ構成要素 -> 何もしないで終わり
				if(!(pageElement instanceof PageElementSaif)) {
					return;
				}

				/* SAIFを表すパネルだった */

				PageElementSaif saif = (PageElementSaif)pageElement;
				String saifDataType = saif.getSaifKind();
				
			
				// 今選択しようとしているデータ型（INT/VARCHAR/DATETIME/DATE/TIME）と、選択されたSAIFのデータ型が一致していたら、サービス引数として決定される
				if((mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_INT && saifDataType.equals(PageElementSaif.KIND_INT))
					|| (mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_VARCHAR && saifDataType.equals(PageElementSaif.KIND_VARCHAR))
					|| (mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATETIME && saifDataType.equals(PageElementSaif.KIND_DATETIME))
					|| (mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATE && saifDataType.equals(PageElementSaif.KIND_DATE))
					|| (mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_TIME && saifDataType.equals(PageElementSaif.KIND_TIME))) {
					PanelServiceArgEdit.getInstance().informSelectedSaifAsServiceArg(saif);
				}
			}
		}
		// TFD出力型TPPを選択する各モードでのクリック
		else if(mode==Panel_TpEdit_Above.MODE_SELECT_TFD || mode==Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD) {
			// サービスパネルの選択
			if(source instanceof ServicePanel) {
				ServicePanel csp = (ServicePanel)source;
				Service serv = csp.getService();

				// クリックされたサービスパネルの出力がTFDではない場合
				if(serv.outputType!=Service.IO_TYPE_TFD) {
					// ◆本当は「出力が表形式ではありません」との警告を発したい
					return;
				}
				
				// 選択されたTFD出力型TPPを、モードに応じて適切なパネルへ報告
				if(mode==Panel_TpEdit_Above.MODE_SELECT_TFD) {	// サービスの表データ引数選択
					PanelServiceArgEdit.getInstance().informSelectedTfdAsServiceArg((TfdOutputer)serv);
				}
				else if(mode==Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD) {	// レコード作成処理への入力表データ選択
					PanelCreateReflectionEdit.getInstance().informSelectedTfdForCreateReflection((TfdOutputer)serv);
				}
			}
			// テーブルリーディングパネルの選択
			else if(source instanceof TableReadingPanel) {
				TableReadingPanel trp = (TableReadingPanel)source;
				TppTableReading tblRead = trp.getTblRead();
				
				// 選択されたTFD出力型TPPを、モードに応じて適切なパネルへ報告
				if(mode==Panel_TpEdit_Above.MODE_SELECT_TFD) {
					PanelServiceArgEdit.getInstance().informSelectedTfdAsServiceArg((TfdOutputer)tblRead);
				}
				else if(mode==Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD) {
					PanelCreateReflectionEdit.getInstance().informSelectedTfdForCreateReflection((TfdOutputer)tblRead);
				}
			}
			// Createフォームリフレクションパネルの選択
			else if(source instanceof CreateFormReflectionPanel) {
				CreateFormReflectionPanel cfrp = (CreateFormReflectionPanel)source;
				TppCreateFormReflection reflection = cfrp.reflection;
				
				// 選択されたTFD出力型TPPを、モードに応じて適切なパネルへ報告
				if(mode==Panel_TpEdit_Above.MODE_SELECT_TFD) {
					PanelServiceArgEdit.getInstance().informSelectedTfdAsServiceArg((TfdOutputer)reflection);
				}
				else if(mode==Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD) {
					PanelCreateReflectionEdit.getInstance().informSelectedTfdForCreateReflection((TfdOutputer)reflection);
				}
			}
			// Createリフレクションパネルの選択
			else if(source instanceof CreateReflectionPanel) {
				CreateReflectionPanel crp = (CreateReflectionPanel)source;
				TppCreateReflection reflection = crp.reflection;
				
				// 選択されたTFD出力型TPPを、モードに応じて適切なパネルへ報告
				if(mode==Panel_TpEdit_Above.MODE_SELECT_TFD) {
					PanelServiceArgEdit.getInstance().informSelectedTfdAsServiceArg((TfdOutputer)reflection);
				}
				else if(mode==Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD) {
					PanelCreateReflectionEdit.getInstance().informSelectedTfdForCreateReflection((TfdOutputer)reflection);
				}
			}
		}
		// サービス出力先（TFD）選択モードでのクリック
		else if(mode==Panel_TpEdit_Above.MODE_SELECT_OUTPUT_TFD) {
			// ◆未実装　TFD型引数を持つサービスのサービスパネル選択
			//
			//

			
			// PageElementパネルの選択
			if(source instanceof PageElementPanel) {
				// 選択されたPageElementの抽出
				PageElementPanel sourcePep = (PageElementPanel)source;
				PageElement sourcePe = sourcePep.pageElement;
				
				// 結果テーブルの選択
				if(sourcePe instanceof PageElementDisplayArea) {
					PageElementPort sourcePeInputPort = sourcePe.inputPePortsHashMap.get(editingTp);
					PanelServiceArgEdit.getInstance().informSelectedPeInputPort(sourcePeInputPort);
				}
			}
			// 遷移先ページパネルがクリックされた場合
			else if(source instanceof EndPagePanel) {
				// まず、とっさに結果テーブル表示のページエレメントを作成、遷移先ページに追加
				EndPagePanel epp = (EndPagePanel)source;
				WebPage endPage = epp.getPage();
				PageElementDisplayArea rtd = new PageElementDisplayArea(endPage);
				endPage.addPageElement(rtd);
				
				// 一度、コンポーネントの配置をし直す（こうしないと、今追加した結果テーブル表示ページエレメントのパネルが表示されない）
				locateTppPanels();

				PageElementPort rtdInputPort = rtd.inputPePortsHashMap.get(editingTp);
				PanelServiceArgEdit.getInstance().informSelectedPeInputPort(rtdInputPort);
			}
		}
		// IAリフレクションのデータテーブルTFD、アカウントテーブルTFD選択モードでのクリック
		else if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD || mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD) {
			// テーブルリーディングパネルの選択
			if(source instanceof TableReadingPanel) {
				TableReadingPanel trp = (TableReadingPanel)source;
				TppTableReading tblRead = trp.getTblRead();
				SuperTable table = tblRead.getTable();
				
				// データテーブルTFD選択モードで、かつIAリフレクションのデータテーブルとテーブルリーディングのテーブルが同一
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.dataTable) {
						PanelIaReflectionEdit.getInstance().informSelectedDtTfd((TfdOutputer)tblRead);
					}
				}
				// アカウントテーブルTFD選択モードで、かつIAリフレクションのアカウントテーブルとテーブルリーディングのテーブルが同一
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.accountTable) {
						PanelIaReflectionEdit.getInstance().informSelectedAtTfd((TfdOutputer)tblRead);
					}
				}
			}
			// Createリフレクションの選択
			else if(source instanceof CreateReflectionPanel) {
				CreateReflectionPanel crp = (CreateReflectionPanel)source;
				TppCreateReflection cr = crp.reflection;
				SuperTable table = cr.table;
				
				// データテーブルTFD選択モードで、かつIAリフレクションのデータテーブルとCreateリフレクションのテーブルが同一
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.dataTable) {
						PanelIaReflectionEdit.getInstance().informSelectedDtTfd((TfdOutputer)cr);
					}
				}
				// アカウントテーブルTFD選択モードで、かつIAリフレクションのアカウントテーブルとCreateリフレクションのテーブルが同一
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.accountTable) {
						PanelIaReflectionEdit.getInstance().informSelectedAtTfd((TfdOutputer)cr);
					}
				}
			}
			// Createフォームリフレクションの選択
			else if(source instanceof CreateFormReflectionPanel) {
				CreateFormReflectionPanel crp = (CreateFormReflectionPanel)source;
				TppCreateFormReflection cr = crp.reflection;
				SuperTable table = cr.createForm.table;
				
				// データテーブルTFD選択モードで、かつIAリフレクションのデータテーブルとCreateフォームリフレクションのテーブルが同一
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.dataTable) {
						PanelIaReflectionEdit.getInstance().informSelectedDtTfd((TfdOutputer)cr);
					}
				}
				// アカウントテーブルTFD選択モードで、かつIAリフレクションのアカウントテーブルとCreateフォームリフレクションのテーブルが同一
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.accountTable) {
						PanelIaReflectionEdit.getInstance().informSelectedAtTfd((TfdOutputer)cr);
					}
				}
			}

			// 上記３種類以外のクリックに対しては何もしない
		}

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
		Frame_TransProcEdit_Sub.getInstance().requestFocus();
	}

		
	
	
	
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}

	
	
	
	public ServicePanel getCsPanelByCs(Service cs) {
		for(int i=0; i<csPanels.size(); i++) {
			ServicePanel csp = csPanels.get(i);
			Service compServ = csp.getService();
			if(compServ==cs) {
				return csp;
			}
		}

		Debug.error("Panel_TpEdit_Left getCsPanelByCs() : ServicePanelインスタンスが見つかりませんでした");
		return null;
	}

	
	
	
	
	public CreateFormReflectionPanel getCfrPanelByCfr(TppCreateFormReflection cfr) {
		for(int i=0; i<panelArrayCreateFormReflection.size(); i++) {
			CreateFormReflectionPanel cfrp = panelArrayCreateFormReflection.get(i);
			TppCreateFormReflection reflection = cfrp.reflection;
			if(reflection==cfr) {
				return cfrp;
			}
		}

		Debug.error("CreateFormReflectionPanelが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}


	
	

	/*
	 * SUMMARY :
	 * Updateフォームリフレクションを受け取り、それを表すパネルを返却
	 */
	public UpdateFormReflectionPanel getUfrPanelByUfr(TppUpdateFormReflection ufr) {
		for(int i=0; i<panelArrayUpdateFormReflection.size(); i++) {
			UpdateFormReflectionPanel ufrp = panelArrayUpdateFormReflection.get(i);
			TppUpdateFormReflection reflection = ufrp.reflection;
			if(reflection==ufr) {
				return ufrp;
			}
		}

		Debug.error("UpdateFormReflectionPanelが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	
	public CreateReflectionPanel getCrPanelByCr(TppCreateReflection createReflection) {
		for(int i=0; i<panelArrayCreateReflection.size(); i++) {
			CreateReflectionPanel crp = panelArrayCreateReflection.get(i);
			TppCreateReflection reflection = crp.reflection;
			if(reflection==createReflection) {
				return crp;
			}
		}

		Debug.error("CreateReflectionPanelが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	
	public IaReflectionPanel getIrPanelByIr(TppIAReflection iaReflection) {
		for(int i=0; i<panelArrayIaReflection.size(); i++) {
			IaReflectionPanel irp = panelArrayIaReflection.get(i);
			TppIAReflection reflection = irp.reflection;
			if(reflection==iaReflection) {
				return irp;
			}
		}

		Debug.error("IaReflectionPanelが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	
	public TableReadingPanel getTrPanelByTr(TppTableReading tr) {
		for(int i=0; i<tblReadPanels.size(); i++) {
			TableReadingPanel trp = tblReadPanels.get(i);
			TppTableReading tblRead = trp.getTblRead();
			if(tblRead==tr) {
				return trp;
			}
		}

		Debug.error("Panel_TpEdit_Left getTrPanelByTr() : TableReadingPanelインスタンスが見つかりませんでした");
		return null;
	}

	
	
	
	
	public void setEditingTp(TransitionProcess tp) {
		editingTp = tp;
	}
	
	
	
	
	
	public TransitionProcess getEditingTp() {
		return editingTp;
	}
	
	
	
	
	
	public static Panel_TpEdit_Above getInstance() {
		return Panel_TpEdit_Above.obj;
	}




	public static void updateInstance(Panel_TpEdit_Above newObject) {
		Panel_TpEdit_Above.obj = newObject;
	}


}
