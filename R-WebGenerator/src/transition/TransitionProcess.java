package transition;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import debug.Debug;

import tpp.*;
import tpp.portTrans.*;
import tpp.service.*;
import webPage.*;

public class TransitionProcess implements Serializable {
	public Transition belongingTrans;
	
	public ArrayList<TransitionProcessPart> tppArray = new ArrayList<TransitionProcessPart>();
	private WebPage startPage;
	private WebPage endPage;

	// 各TPPのポート間のデータ受渡（PortTrans）を管理するインスタンス
	public  PortTransManager portTransManager;
	
	// 遷移元・遷移先ページを表すパネルの左上頂点の座標
	private Point startPagePosition;
	private Point endPagePosition;
	public static final Point START_PAGE_POSITION_DEFAULT = new Point(50, 50);
	public static final Point END_PAGE_POSITION_DEFAULT = new Point(500, 50);
	

	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	public TransitionProcess(Transition transition, WebPage startPage, WebPage endPage) {
		this.belongingTrans = transition;
		this.startPage = startPage;
		this.endPage = endPage;

		startPagePosition = TransitionProcess.START_PAGE_POSITION_DEFAULT;
		endPagePosition = TransitionProcess.END_PAGE_POSITION_DEFAULT;

		// ポート間のデータ受渡を管理するインスタンス
		portTransManager = new PortTransManager(this);
	}
	

	
	
	public void debugTppArray() {
		Debug.notice("現在、"+getTppNum()+"個のTPPが次のように並んでいます。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		for(int tppOrder=0; tppOrder<getTppNum(); tppOrder++) {
			TransitionProcessPart tpp = getTpp(tppOrder);
			if(tpp instanceof Service) {
				Service service = (Service)tpp;
				Debug.notice(tppOrder+"番：サービス「"+service.serviceName+"」", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppCreateReflection) {
				TppCreateReflection creRef = (TppCreateReflection)tpp;
				Debug.notice(tppOrder+"番：Createリフレクション　テーブル「"+creRef.table.getTableName()+"」", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppCreateFormReflection) {
				TppCreateFormReflection creFormRef = (TppCreateFormReflection)tpp;
				Debug.notice(tppOrder+"番：Createフォームリフレクション　テーブル「"+creFormRef.createForm.table.getTableName()+"」", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppIAReflection) {
				Debug.notice(tppOrder+"番：IAリフレクション", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppConstArrayInt) {
				Debug.notice(tppOrder+"番：INT定数配列", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppConstArrayString) {
				Debug.notice(tppOrder+"番：STRING定数配列", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppConstInt) {
				Debug.notice(tppOrder+"番：INTモノ定数", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else if(tpp instanceof TppConstString) {
				Debug.notice(tppOrder+"番：STRINGモノ定数", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			else {
				Debug.notice(tppOrder+"番："+tpp.toString(), getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			}
		}
	}
	
	

	
	
	/*
	 * SUMMARY :
	 * TPP追加
	 */
	public void addTpp(TransitionProcessPart tpp) {
		tppArray.add(tpp);
	}
	public void addTpp(int index, TransitionProcessPart tpp) {
		tppArray.add(index, tpp);
	}
	
	
	
	
	
	public boolean removeTpp(TransitionProcessPart tpp) {
		boolean remove = tppArray.remove(tpp);
		if(!remove) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "エラーが発生しました。");
			Debug.error("削除しようとしたTPPが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		return true;
	}

	
	
	
	public PortTransManager getPortTransManager() {
		return portTransManager;
	}
	
	
	public WebPage getStartPage() {
		return startPage;
	}
	
	
	
	
	public WebPage getEndPage() {
		return endPage;
	}

	
	
	
	
	public Point getStartPagePosition() {
		return startPagePosition;
	}


	
	
	
	public Point getEndPagePosition() {
		return endPagePosition;
	}

	

	public TransitionProcessPart getTpp(int index) {
		return tppArray.get(index);
	}
	
	public int getTppNum() {
		return tppArray.size();
	}

	public int getTppo(TransitionProcessPart argTpp) {
		for(int i=0; i<getTppNum(); i++) {
			TransitionProcessPart tpp = getTpp(i);
			if(tpp==argTpp) {
				return i;
			}
		}

		Debug.error("TPPが見つかりません", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}
}
