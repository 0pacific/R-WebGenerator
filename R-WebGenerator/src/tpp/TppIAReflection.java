package tpp;

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
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
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






public class TppIAReflection extends TransitionProcessPart {
	public DataTable dataTable;
	public AccountTable accountTable;
	
	public TppIAReflection(TransitionProcess transProc) {
		super(transProc);
		initInputPorts();
		initOutputPort();
	}

	public void initInputPorts() {
		// 入力TPPポートは２つ（データテーブルTFD、アカウントテーブルTFDの順）
		this.inputPorts = new ArrayList<TppPort>();
		this.inputPorts.add(new TppPort(this));
		this.inputPorts.add(new TppPort(this));
	}

	public TppPort getDataTableTfdInputPort() {
		return this.inputPorts.get(0);
	}
	public TppPort getAccountTableTfdInputPort() {
		return this.inputPorts.get(1);
	}


	
	
	
	public TfdOutputer getTfdOutputerAsDataTableTfdIfExists() {
		TppPort dtTfdInputPort = this.getDataTableTfdInputPort();
		PortTransManager ptm = this.transProc.portTransManager;

		TfdOutputer tfdOutputerAsDtTfd = ptm.getInputTfdOutputerTppIfExists(dtTfdInputPort);	// 存在しなければnullがかえる
		return tfdOutputerAsDtTfd;
	}
	
	public TfdOutputer getTfdOutputerAsAccountTableTfdIfExists() {
		TppPort atTfdInputPort = this.getAccountTableTfdInputPort();
		PortTransManager ptm = this.transProc.portTransManager;

		TfdOutputer tfdOutputerAsAtTfd = ptm.getInputTfdOutputerTppIfExists(atTfdInputPort);	// 存在しなければnullがかえる
		return tfdOutputerAsAtTfd;
	}

	
	
	

	public void initOutputPort() {
		// 出力TPPポートは存在しない
	}





	/*
	 * 	SUMMARY :
	 *  もしもデータテーブルTFDが定義されていたら、それを削除する（ポートトランスミッションの削除）
	 *  あって削除したならtrue、なかったならfalseを返す
	 */
	public boolean removeDataTableTfdInputPortTransIfExists() {
		PortTransManager portTransManager = this.transProc.portTransManager;
		PortTrans ptToDtTfdInputPort = portTransManager.getOnePortTransByEndPortIfExists(this.getDataTableTfdInputPort()); 
		
		// あった場合
		if(ptToDtTfdInputPort!=null) {
			boolean remove = portTransManager.removePortTrans(ptToDtTfdInputPort);
			if(!remove) {
				Debug.error("IAリフレクションのデータテーブルが再定義されたのでデータテーブルTFDから0番入力TPPポートへのポートトランスミッションを削除しようとしましたが、そのポートトランスミッションは元々なかったというおかしな事態です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return false;
			}
			return true;
		}

		// なかった場合
		return false;
	}
	/*
	 * 	SUMMARY :
	 *  もしもアカウントテーブルTFDが定義されていたら、それを削除する（ポートトランスミッションの削除）
	 *  あって削除したならtrue、なかったならfalseを返す
	 */
	public boolean removeAccountTableTfdInputPortTransIfExists() {
		PortTransManager portTransManager = this.transProc.portTransManager;
		PortTrans ptToAtTfdInputPort = portTransManager.getOnePortTransByEndPortIfExists(this.getAccountTableTfdInputPort()); 
		
		// あった場合
		if(ptToAtTfdInputPort!=null) {
			boolean remove = portTransManager.removePortTrans(ptToAtTfdInputPort);
			if(!remove) {
				Debug.error("IAリフレクションのアカウントテーブルが再定義されたのでアカウントテーブルTFDから1番入力TPPポートへのポートトランスミッションを削除しようとしましたが、そのポートトランスミッションは元々なかったというおかしな事態です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				return false;
			}
			return true;
		}

		// なかった場合
		return false;
	}





	public void removeFromTransProc() {
		// まず、このIAリフレクション自身を削除
		this.transProc.removeTpp(this);
		
		PortTransManager ptm = this.transProc.portTransManager;
		
		// データテーブルTFDを受け取るポートトランスミッションを削除
		ptm.removeAllPortTransByEndPort(getDataTableTfdInputPort());

		// アカウントテーブルTFDを受け取るポートトランスミッションを削除
		ptm.removeAllPortTransByEndPort(getAccountTableTfdInputPort());

		/*
		 * IAリフレクションに出力は存在しないので、出力を受け取るポートトランスミッションの削除は考えなくて良い
		 */
	}

}
