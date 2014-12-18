package tpp.portTrans;

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
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.port.*;
import tpp.service.Service;
import transition.*;
import utility.*;
import view.*;
import view.authEdit.*;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;



public class PortTransManager implements Serializable {
	private TransitionProcess transProc;
	private ArrayList<PortTrans> portTransList;
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * コンストラクタ
	 */
	public PortTransManager(TransitionProcess transProc) {
		this.transProc = transProc;
		portTransList = new ArrayList<PortTrans>();
	}


	
	
	public boolean inputReserved(DataPort port) {
		// portを出力先としてデータトランスミッションが何回見つかるか、デバッグのため一応カウント（0か1にならないとおかしい）
		int foundCount = 0;
		
		for(int i=0; i<getPortTransNum(); i++) {
			PortTrans trans = getPortTrans(i);
			DataPort endPort = trans.getEndPort();
			if(endPort==port) {
				foundCount++;
			}
		}

		if(foundCount > 1) {
			Debug.error("PortTransManager inputReserved() : 指定したポートへの入力を２つ以上検知してしまいました。");
		}
		else if(foundCount==1) {
			return true;
		}
		
		return false;
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * 指定したポート「からの」（「への」）ポートトランスミッションがあれば取得する
	 */
	public PortTrans getPortTransByStartPortIfExists(DataPort outPort) {
		// 全ポートトランスミッションを虱潰しにチェック
		for(int i=0; i<getPortTransNum(); i++) {
			PortTrans pt = getPortTrans(i);

			// 出発ポートが指定されたポートと一致
			if(pt.getStartPort()==outPort) {
				return pt;
			}
		}

		// 見つからず
		Debug.notice("指定したデータポートから出発するポートトランスミッションがあるか調べましたが、ないようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	public PortTrans getOnePortTransByEndPortIfExists(DataPort inPort) {
		// 全ポートトランスミッションを虱潰しにチェック
		for(int i=0; i<getPortTransNum(); i++) {
			PortTrans pt = getPortTrans(i);

			// 到着ポートが指定されたポートと一致
			if(pt.getEndPort()==inPort) {
				return pt;
			}
		}

		// 見つからず
		Debug.notice("指定したデータポートへ到着するポートトランスミッションがあるか調べましたが、ないようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	/*
	 * SUMMARY :
	 * サービス引数が入る入力TPPポートを渡して、目的の種類のTPPがもしも引数として定義されていれば取得するという関数群
	 */
	public TransitionProcessPart getInputTppToTppPortIfExists(TppPort inputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(inputPort);
		
		// まだインプットが定義されていない
		if(portTrans==null) {
			Debug.notice("指定されたサービス引数ポートへの入力が定義されてないか調べましたが、まだ定義されていないようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		// 引数として定義されているTPPを取得
		TransitionProcessPart inputTpp = ((TppPort)portTrans.getStartPort()).getTpp();
		return inputTpp;
	}
	/*
	 * 以降の関数はいずれ消す（上のものだけ使って、TPPのタイプが合っているかは呼び出し側でチェックすれば良い）
	 */
	// INT定数配列を取得
	public TppConstArrayInt getInputTppConstArrayIntIfExists(TppPort serviceInputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(serviceInputPort);
		
		// まだINT定数配列が定義されていない
		if(portTrans==null) {
			Debug.notice("まだINT定数配列が定義されていません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		TppPort startTppPort = (TppPort)portTrans.getStartPort();
		TppConstArrayInt cai = (TppConstArrayInt)startTppPort.getTpp();	// ◆ここでエラーになるかも

		return cai;
	}
	// TFD出力型TPPを取得
	public TfdOutputer getInputTfdOutputerTppIfExists(TppPort serviceInputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(serviceInputPort);
		
		// まだTFD出力型TPPが定義されていない
		if(portTrans==null) {
			Debug.notice("まだTFD出力型TPPが定義されていません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		TppPort startTppPort = (TppPort)portTrans.getStartPort();
		TransitionProcessPart tpp = startTppPort.getTpp();

		return (TfdOutputer)tpp;
	}
	// STRING定数配列を取得
	public TppConstArrayString getInputStringTppIfExists(TppPort serviceInputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(serviceInputPort);
		
		// まだSTRING定数配列が定義されていない
		if(portTrans==null) {
			Debug.out("◆1210:まだSTRING定数配列のTPPが定義されていません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		TppPort startTppPort = (TppPort)portTrans.getStartPort();
		TransitionProcessPart tpp = startTppPort.getTpp();

		return (TppConstArrayString)tpp;
	}
	// TPP（限定しない）を取得
	public TransitionProcessPart getInputTppIfExists(TppPort serviceInputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(serviceInputPort);
		
		// 未定義
		if(portTrans==null) {
			Debug.notice("TPPが未定義です", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		TppPort startTppPort = (TppPort)portTrans.getStartPort();
		TransitionProcessPart tpp = startTppPort.getTpp();

		return tpp;
	}

	
	
	
	
	// Display Areaの入力ページエレメントポートを指定し、そこへ入力されるTPP出力型TPPがもし定義されていれば返す
	public TfdOutputer getTfdOutputerForDisplayAreaIfExists(PageElementPort daInputPort) {
		PortTrans portTrans = this.getOnePortTransByEndPortIfExists(daInputPort);
		
		// まだTFD出力型TPPが定義されていない
		if(portTrans==null) {
			Debug.notice("まだTFD出力型TPPが定義されていないようです", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		TppPort startTppPort = (TppPort)portTrans.getStartPort();
		TransitionProcessPart tpp = startTppPort.getTpp();

		return (TfdOutputer)tpp;
	}
	
	
	
	
	
	
	public PortTrans getPortTrans(int index) {
		return portTransList.get(index);
	}
	
	
	
	public int getPortTransNum() {
		return portTransList.size();
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * 指定したポートトランスミッションを直接追加
	 */
	public void addPortTrans(PortTrans pt) {
		portTransList.add(pt);
	}

	
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * 入力・出力ポートを指定してポートトランスミッションを追加
	 */
	public void addPortTrans(DataPort startPort, DataPort endPort) {
		PortTrans pt = new PortTrans(startPort, endPort);
		addPortTrans(pt);
	}

	
	
	
	
	/*
	 * 指定したポートトランスミッションが【あるなら】削除する
	 * あって削除したならtrue, なかったならfalseを返す
	 */
	public boolean removePortTrans(PortTrans portTrans) {
		boolean result = portTransList.remove(portTrans);
		if(!result) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "エラーが発生しました。");
			Debug.error("消そうとしたポートトランスミッションが見つかりませんでした。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		
		/*
		 * 消そうとしたポートトランスミッションは見つかり、削除された
		 * そのポートトランスミッションの終端がサービスの引数ポートであった場合、後続引数を削除せねばならない
		 * それをこれからやる
		 */

		DataPort endPort = portTrans.getEndPort();
		if(endPort instanceof TppPort) {
			TppPort endTppPort = (TppPort)endPort;
			TransitionProcessPart endTpp = endTppPort.getTpp();
			
			// 消そうとしているポートトランスミッションの終端はサービスの引数ポートであった
			if(endTpp instanceof Service) {
				Service endService = (Service)endTpp;

				// 削除するサービスの出力を、向こうのサービスは何番目の引数として受け取っている？
				int argIndex = endService.getArgIndexByArgPort(endTppPort);

				// 後続する引数を全て削除する（削除したポートトランスミッションの終端だった引数自体は、既に削除されているわけである）
				endService.removeFollowingArgsIfExist(argIndex);
			}
		}

		/*
		 * サービスの後続引数も無事削除された
		 */
		
		return true;
	}

	
	
	
	public ArrayList<PortTrans> getAllPortTransByStartPort(DataPort startPort) {
		ArrayList<PortTrans> portTransListToReturn = new ArrayList<PortTrans>();

		for(int i=0; i<getPortTransNum(); i++) {
			PortTrans portTrans = getPortTrans(i);
			if(portTrans.getStartPort()==startPort) {
				portTransListToReturn.add(portTrans);
			}
		}

		return portTransListToReturn;
	}
	public ArrayList<PortTrans> getAllPortTransByEndPort(DataPort endPort) {
		ArrayList<PortTrans> portTransListToReturn = new ArrayList<PortTrans>();

		for(int i=0; i<getPortTransNum(); i++) {
			PortTrans portTrans = getPortTrans(i);
			if(portTrans.getEndPort()==endPort) {
				portTransListToReturn.add(portTrans);
			}
		}

		return portTransListToReturn;
	}
	
	
	
	
	
	public void removeAllPortTransByStartPort(DataPort startPort) {
		ArrayList<PortTrans> portTransListToRemove = getAllPortTransByStartPort(startPort);
		for(int i=0; i<portTransListToRemove.size(); i++) {
			removePortTrans(portTransListToRemove.get(i));
		}
	}
	public void removeAllPortTransByEndPort(DataPort endPort) {
		ArrayList<PortTrans> portTransListToRemove = getAllPortTransByEndPort(endPort);
		for(int i=0; i<portTransListToRemove.size(); i++) {
			removePortTrans(portTransListToRemove.get(i));
		}
	}
	
	
	
	
	
	public boolean removeOnePortTransByEndPortIfExists(DataPort endPort) {
		PortTrans portTrans = getOnePortTransByEndPortIfExists(endPort);
		if(portTrans==null) {
			return false;
		}
		removePortTrans(portTrans);
		return true;

		/*
		 * ２つ以上のポートトランスミッションが見つかることは想定していない
		 */
	}
	



	public void varDump() {
		String log = "";

		log += "この遷移プロセス中の、ポートトランスミッションの数は　" + getPortTransNum() + "　です。\n";
		
		for(int i=0; i<getPortTransNum(); i++) {
			log += "　　" + i + "個目（0始まり）のポートトランスミッションについて出力：\n";

			PortTrans pt = getPortTrans(i);
			DataPort startPort = pt.getStartPort();
			DataPort endPort = pt.getEndPort();
			log += "　　　　出発ポート..." + startPort.getClass().getSimpleName() + "型です。\n";
			log += "　　　　到着ポート..." + endPort.getClass().getSimpleName() + "型です。\n";
		}

		log += "この遷移プロセス中の、ポートトランスミッションについてのデバッグ出力を終了します。";
		
		Debug.varDump(log, getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}
