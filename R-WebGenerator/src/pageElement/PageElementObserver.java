package pageElement;

import java.io.Serializable;
import java.util.ArrayList;

import debug.Debug;
import table.SuperTable;
import tpp.TppCreateFormReflection;
import tpp.TppUpdateFormReflection;
import tpp.port.PageElementPort;
import tpp.port.TppPort;
import tpp.portTrans.PortTransManager;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import view.transProcEdit.Panel_TpEdit_Above;
import webPage.WebPage;
import webPage.WebPageManager;

/*
 * SUMMARY :
 * ページエレメントの追加や編集、削除を監視し、遷移プロセスや遷移構造などに対し適切な反映を施すクラス
 */
public class PageElementObserver implements Serializable {
	private static PageElementObserver obj = new PageElementObserver();

	
	
	
	private PageElementObserver() {
	}
	
	
	
	
	public void informPeAddition(PageElement pe) {
		if(pe instanceof PageElementCreateForm) {
			informCreateFormAddition((PageElementCreateForm)pe);
		}
		else if(pe instanceof PageElementUpdateForm) {
			informUpdateFormAddition((PageElementUpdateForm)pe);
		}
		else if(pe instanceof PageElementLoginForm) {
			informLoginFormAddition((PageElementLoginForm)pe);
		}
		else if(pe instanceof PageElement_HyperLink) {
			informHyperLinkAddition((PageElement_HyperLink)pe);
		}
		else if(pe instanceof PageElementTableDisplay) {
			// 何もしない
		}
		else if(pe instanceof PageElementDisplayArea) {
			// 何もしない
		}
		else if(pe instanceof PageElementText) {
			// 何もしない
		}
		else if(pe instanceof PageElementSaif) {
			informSaifAddition((PageElementSaif)pe);
		}
		else {
			Debug.error("想定外のページエレメントです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}

	
	
	
	public void informPeEdition(PageElement pe) {
		// ◆未実装
	}

	
	
	
	




	public void informHyperLinkAddition(PageElement_HyperLink hyperLink) {
		// 新しく遷移を作って追加
		Transition trans = new Transition(hyperLink.parentPage, hyperLink, hyperLink.getDestPage());
		TransitionManager.getInstance().addTransition(trans);
	}

	
	
	public void informLoginFormAddition(PageElementLoginForm loginForm) {
		// 新しく遷移を作って追加
		Transition trans = new Transition(loginForm.parentPage, loginForm, loginForm.destWebPage);
		TransitionManager.getInstance().addTransition(trans);
	}
	
	
	public void informSaifAddition(PageElementSaif saif) {
		// 親ページから出て行く全遷移の遷移プロセスに対応した【出力】ページエレメントポートをSAIFに作る
		ArrayList<Transition> goingTransArray = TransitionManager.getInstance().getTransArrayByStartPage(saif.parentPage);
		for(int i=0; i<goingTransArray.size(); i++) {
			Transition trans = goingTransArray.get(i);
			TransitionProcess transProc = trans.transProc;
			saif.initOutputPePorts(transProc);
		}
	}
	
	
	public void informCreateFormAddition(PageElementCreateForm createForm) {
		// CreateFormをトリガーとした遷移を追加
		Transition trans = new Transition(createForm.parentPage, createForm, createForm.destPage);
		TransitionManager.getInstance().addTransition(trans);

		// 追加した遷移の遷移プロセスに対応した【出力】ページエレメントポートをCreateフォームに作る
		TransitionProcess transProc = trans.transProc;
		createForm.initOutputPePorts(transProc);

		// Create Form Reflection を最初のTPPとして追加し、対応するパネルを作る
		TppCreateFormReflection reflection = new TppCreateFormReflection(transProc, createForm);
		transProc.addTpp(reflection);
		Panel_TpEdit_Above.getInstance().addCreateFormReflectionPanel(reflection);
		
		// ポートトランスミッションを作成して追加
		// （Createフォームの出力ページエレメントポートから、リフレクションの入力TPPポートへ）
		PageElementPort startPort = createForm.outputPePortsHashMap.get(transProc);
		TppPort endPort = reflection.inputPorts.get(0);
		transProc.portTransManager.addPortTrans(startPort, endPort);
	}




	public void informUpdateFormAddition(PageElementUpdateForm updateForm) {
		// updateFormをトリガーとした遷移を追加
		Transition trans = new Transition(updateForm.parentPage, updateForm, updateForm.destPage);
		TransitionManager.getInstance().addTransition(trans);

		// 追加した遷移の遷移プロセスに対応した【出力】ページエレメントポートをUpdateフォームに作る
		TransitionProcess transProc = trans.transProc;
		updateForm.initOutputPePorts(transProc);
		
		// Update Form Reflection を最初のTPPとして追加
		TppUpdateFormReflection reflection = new TppUpdateFormReflection(transProc, updateForm);
		transProc.addTpp(reflection);
		Panel_TpEdit_Above.getInstance().addUpdateFormReflectionPanel(reflection);

		// ポートトランスミッションを作成して追加
		// （Updateフォームの出力ページエレメントポートから、Updateフォームリフレクションの入力TPPポートへ）
		PageElementPort startPort = updateForm.outputPePortsHashMap.get(transProc);
		TppPort endPort = reflection.inputPorts.get(0);
		transProc.portTransManager.addPortTrans(startPort, endPort);
	}


	
	
	
	public void informPeRemovement(PageElement pe) {
		if(pe instanceof PageElementCreateForm) {
			informCreateFormRemovement((PageElementCreateForm)pe);
		}
		else if(pe instanceof PageElementUpdateForm) {
			informUpdateFormRemovement((PageElementUpdateForm)pe);
		}
		else if(pe instanceof PageElementLoginForm) {
			informLoginFormRemovement((PageElementLoginForm)pe);
		}
		else if(pe instanceof PageElement_HyperLink) {
			informHyperLinkRemovement((PageElement_HyperLink)pe);
		}
		else if(pe instanceof PageElementTableDisplay) {
			// 何もしない
		}
		else if(pe instanceof PageElementDisplayArea) {
			informDisplayAreaRemovement((PageElementDisplayArea)pe);
		}
		else if(pe instanceof PageElementText) {
			// 何もしない
		}
		else {
			Debug.error("想定外のページエレメントです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		}
	}
	
	
	
	
	public void informTableRemovement(SuperTable table) {
		WebPageManager wpm = WebPageManager.getInstance();
		for(int i=0; i<wpm.getPageNum(); i++) {
			WebPage webPage = wpm.getPage(i);
			
			for(int j=0; j<webPage.getPageElementNum(); j++) {
				PageElement pe = webPage.getPageElement(j);
				
				if(pe instanceof PageElementCreateForm) {
					PageElementCreateForm createForm = (PageElementCreateForm)pe;
					if(createForm.table==table) {
						webPage.removePageElement(createForm);
					}
				}
				else if(pe instanceof PageElementUpdateForm) {
					PageElementUpdateForm updateForm = (PageElementUpdateForm)pe;
					if(updateForm.table==table) {
						webPage.removePageElement(updateForm);
					}
				}
				else if(pe instanceof PageElementLoginForm) {
					PageElementLoginForm loginForm = (PageElementLoginForm)pe;
					if(loginForm.accountTable==table) {
						webPage.removePageElement(loginForm);
					}
				}
				else if(pe instanceof PageElement_HyperLink) {
					// 何もしない
				}
				else if(pe instanceof PageElementTableDisplay) {
					PageElementTableDisplay tableDisplay = (PageElementTableDisplay)pe;
					if(tableDisplay.table==table) {
						webPage.removePageElement(tableDisplay);
					}
				}
				else if(pe instanceof PageElementDisplayArea) {
				}
				else if(pe instanceof PageElementText) {
				}
				else {
					Debug.error("想定外のページエレメントです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				}
			}
		}
	}
	
	
	
	
	
	public void informCreateFormRemovement(PageElementCreateForm createForm) {
		// このCreateフォームによる遷移も削除
		TransitionManager tm = TransitionManager.getInstance();
		Transition transByCreateForm = tm.getTransitionByTriggerPe(createForm);
		tm.removeTransition(transByCreateForm);
	}

	
	
	
	
	public void informUpdateFormRemovement(PageElementUpdateForm updateForm) {
		// このUpdateフォームによる遷移も削除
		TransitionManager tm = TransitionManager.getInstance();
		Transition transByUpdateForm = tm.getTransitionByTriggerPe(updateForm);
		tm.removeTransition(transByUpdateForm);
	}
	



	public void informLoginFormRemovement(PageElementLoginForm loginForm) {
		// このLoginフォームによる遷移も削除
		TransitionManager tm = TransitionManager.getInstance();
		Transition transByLoginForm = tm.getTransitionByTriggerPe(loginForm);
		tm.removeTransition(transByLoginForm);
	}

	
	
	
	
	public void informHyperLinkRemovement(PageElement_HyperLink hyperLink) {
		// このハイパーリンクによる遷移も削除
		TransitionManager tm = TransitionManager.getInstance();
		Transition transByHyperLink = tm.getTransitionByTriggerPe(hyperLink);
		tm.removeTransition(transByHyperLink);
	}

	
	
	
	public void informDisplayAreaRemovement(PageElementDisplayArea displayArea) {
		WebPage parentPage = displayArea.parentPage;

		// 親だったページ◆へ◆向かう全遷移について処理
		ArrayList<Transition> transArrayToParentPage = TransitionManager.getInstance().getTransArrayByEndPage(parentPage);
		for(int i=0; i<transArrayToParentPage.size(); i++) {
			Transition trans = transArrayToParentPage.get(i);

			// この遷移の遷移プロセス
			TransitionProcess transProc = trans.transProc;

			// DisplayAreaへ向かうポートトランスミッションを全て削除
			PortTransManager ptm = transProc.portTransManager;
			PageElementPort inputPortInThisTransProc = displayArea.inputPePortsHashMap.get(transProc);
			ptm.removeAllPortTransByEndPort(inputPortInThisTransProc);
		}
	}
	
	
	
	
	public static PageElementObserver getInstance() {
		return PageElementObserver.obj;
	}
}
