package pageElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import mainFrame.MainFrame;

import debug.Debug;

import tpp.TransitionProcessPart;
import tpp.port.PageElementPort;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import webPage.WebPage;

public abstract class PageElement implements Serializable {
	public WebPage parentPage;
	public int numberInParentPage;
	public HashMap<TransitionProcess, PageElementPort> inputPePortsHashMap;
	public HashMap<TransitionProcess, PageElementPort> outputPePortsHashMap;

	// ページエレメント主キー（実行プログラムの動作に必要であり、データベースに記録される。生成するときに初めて与えられ、コンストラクタでは初期化されない）
	public int pePrimaryKey;

	
	
	
	
	public PageElement(WebPage parentPage) {
		this.parentPage = parentPage;
		inputPePortsHashMap = new HashMap<TransitionProcess, PageElementPort>();
		outputPePortsHashMap = new HashMap<TransitionProcess, PageElementPort>();
		
		TransitionManager tm = TransitionManager.getInstance();

		// このページエレメントが置かれるWebページから【出発する】遷移プロセス全てを洗い出し、それぞれに対応する【出力】ページエレメントポートを初期化
		ArrayList<Transition> goingTransArray = tm.getTransArrayByStartPage(parentPage);
		for(int i=0; i<goingTransArray.size(); i++) {
			Transition goingTrans = goingTransArray.get(i);
			TransitionProcess goingTransProc = goingTrans.transProc;
			this.initOutputPePorts(goingTransProc);
		}
		// このページエレメントが置かれるWebページへ【来る】遷移プロセス全てを洗い出し、それぞれに対応する【入力】ページエレメントポートを初期化
		ArrayList<Transition> comingTransArray = tm.getTransArrayByEndPage(parentPage);
		for(int i=0; i<comingTransArray.size(); i++) {
			Transition comingTrans = comingTransArray.get(i);
			TransitionProcess comingTransProc = comingTrans.transProc;
			this.initInputPePorts(comingTransProc);
		}
	}
		

	public WebPage getParentPage() {
		return parentPage;
	}

	public int getNumberInParentPage() {
		return numberInParentPage;
	}

	public abstract void initInputPePorts(TransitionProcess transProc);
	public abstract void initOutputPePorts(TransitionProcess transProc);
	
	
	
	
	/*
	 * SUMMARY :
	 * PageElementクラスのサブクラスのインスタンスを受け取り、その種類に応じてDB中での名称を返す
	 * この名称は、DBの"page_element"テーブルの"kind"フィールドに記録されるものである
	 */
	public static String givePeExpression(PageElement pe) {
		if(pe instanceof PageElement_HyperLink) {
			return "Hyper Link";
		}
		else if(pe instanceof PageElementDisplayArea) {
			return "Display Area";
		}
		else if(pe instanceof PageElementTableDisplay) {
			return "Table Display";
		}
		else if(pe instanceof PageElementCreateForm) {
			return "Create Form";
		}
		else if(pe instanceof PageElementLoginForm) {
			return "Login Form";
		}
		else if(pe instanceof PageElementText) {
			return "Text";
		}
		else if(pe instanceof PageElementUpdateForm) {
			return "Update Form";
		}
		else if(pe instanceof PageElementSaif) {
			return "Service Argument Input Form";
		}
		/*
		 * ◆未実装：全種類のページエレメントに対応させる
		 */
		
		// エラー
		JOptionPane.showMessageDialog(MainFrame.getInstance(), "エラーが発生しました。");
		Debug.error("想定外のページ構成要素です。", "PageElement", Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
}