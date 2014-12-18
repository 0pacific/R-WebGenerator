package executer.generation;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;

import debug.Debug;
import gui.*;
import gui.arrow.*;
import pageElement.*;
import role.*;
import table.*;
import tpp.*;
import view.generation.*;
import view.tableList.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import executer.generation.mysql.*;
import executer.generation.pePrimKey.PageElementPrimaryKeyAssigner;
import executer.generation.tpp.TppOrderArranger;
import executer.generation.tpp.TppnAssigner;
import executer.generation.transPrimKey.TransitionPrimaryKeyAssigner;





public class GenerationExecuter implements Serializable {
	private static GenerationExecuter obj = new GenerationExecuter();

	
	
	
	
	private GenerationExecuter() {
	}

	
	
	
	/*
	 * SUMMARY :
	 * Webアプリの生成を実行
	 */
	public boolean execute() {
		// 全Webページの全ページエレメントに、主キーを与えておく
		PageElementPrimaryKeyAssigner pepka = new PageElementPrimaryKeyAssigner();
		pepka.assignKeys();

		// 全遷移に、主キーを与えておく
		TransitionPrimaryKeyAssigner transKeyAssigner = new TransitionPrimaryKeyAssigner();
		transKeyAssigner.assignKeys();

		// TPPを適切に並べ替える
		TppOrderArranger arranger = new TppOrderArranger();
		arranger.arrange();
		
		// 全TPPに、TPPNを与えておく
		TppnAssigner tppnAssigner = new TppnAssigner();
		tppnAssigner.assignTppn();
		
		// アプリ定義SQLの作成
		SqlGenerator sqlGenerator = new SqlGenerator();
		boolean result = sqlGenerator.execute();
		if(!result) {
			Debug.notice("データベースの全テーブルを作成するSQLをSQLファイルに書き出し保存するという作業が失敗しました。アプリ生成を中断します。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		
		// 各Webページを生成
		WebPageGenerator wpg = new WebPageGenerator();
		boolean webPageGenResult = wpg.execute();
		if(!webPageGenResult) {
			Debug.error("Webページの生成に失敗したため、アプリ生成を中断します。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		return true;
	}
	
	
	
	

	public static GenerationExecuter getInstance() {
		return GenerationExecuter.obj;
	}
}
