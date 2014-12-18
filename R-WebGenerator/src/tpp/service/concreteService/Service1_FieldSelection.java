package tpp.service.concreteService;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import debug.Debug;
import tpp.TfdOutputer;
import tpp.TppConstArrayInt;
import tpp.port.TppPort;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service1_FieldSelection extends TabularOutputService {
	public Service1_FieldSelection(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		
		// 各引数の種類
		String[] argTypes = {
			Service.IO_TYPE_TFD,
			Service.IO_TYPE_OFFSETS
		};
		this.argTypes = argTypes;

		// ★入力TPPポートの初期化にはargTypesが用いられるため、argTypesの初期化後でなければならない
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "フィールド抽出" : "Fields Extraction";
		this.serviceType = Service.SERVICE_TYPE_FIELD_PROC;
		
		// 説明文
		description = GeneratorProperty.japanese()?
						"表から１つ以上の列を取り出し、新たな表を作ります。入力する表と、取り出したい各列の番号を指定して下さい。":
						"This service extracts at least 1 fields and make a new table. Define a table to be input and fields to be extracted.";


		// 各引数の名前
		String[] argNames = {
			japanese ? "表データ" : "Tabular Form Data",
			japanese ? "抽出フィールド" : "Fields to extract"
		};
		this.argNames = argNames;
		
		// 各引数についての説明文
		String[] argDescs = {
			"表データ",
			"抽出フィールド"
		};
		this.argDescs = argDescs;
		
		// 第0引数を定義せねば第1引数を定義できないことを記録
		ArrayList<Integer> precursiveArgIndexArray = new ArrayList<Integer>();
		precursiveArgIndexArray.add(new Integer(0));
		hashMapNeededArgForOtherArg.put(new Integer(1), precursiveArgIndexArray);
		
		// 第二引数は第一引数TFDのフィールドオフセット配列なので、そのことをハッシュマップに記録
		hmFldSel.put(new Integer(1), new Integer(0));
		
		// アウトプットの種類
		this.outputType = Service.IO_TYPE_TFD;
	}

	
	
	
	
	/*
	 * SUMMARY :
	 * 出力TFDのフィールド数を取得
	 */
	public int getOutFieldNum() {
		TppPort fieldSelectInputPort = this.getInputPort(1);
		TppConstArrayInt consArrInt = transProc.portTransManager.getInputTppConstArrayIntIfExists(fieldSelectInputPort);

		if(consArrInt==null) {
			Debug.notice(
					getClass().getSimpleName()+"型サービスの、フィールド選択型サービス引数がまだ定義されていないので出力フィールド数がわかりません。",
					getClass().getSimpleName(),
					Thread.currentThread().getStackTrace()[1].getMethodName()
			);
			return -1;
		}
		
		return consArrInt.getIntNum();
	}




	public ArrayList<String> getOutputFieldWebNames() {
		// 第一引数のTFDが入ってくる入力TPPポート
		TppPort firstPort = this.getInputPort(0);

		// 第一引数のTFD出力型TPPを（定義されていれば）取得する
		TfdOutputer tfdOutputerFirst = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		if(tfdOutputerFirst==null) {
			Debug.notice("フィールド抽出サービスが受け取る第一引数の表が定義されていないようです", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}
		
		ArrayList<String> allFieldWebNames = tfdOutputerFirst.getOutputFieldWebNames();
	
		// 第二引数で「どのフィールドを抽出するか」指定されているなら、それらフィールドの名前をallFieldWebNamesから抽出する
		TppPort fieldSelectInputPort = this.getInputPort(1);
		TppConstArrayInt consArrInt = transProc.portTransManager.getInputTppConstArrayIntIfExists(fieldSelectInputPort);
		if(consArrInt==null) {
			Debug.notice(
					getClass().getSimpleName()+"型サービスの、フィールド選択型サービス引数がまだ定義されていないので出力フィールド名がわかりません。",
					getClass().getSimpleName(),
					Thread.currentThread().getStackTrace()[1].getMethodName()
			);
			return null;
		}

		ArrayList<String> selectedFieldWebNames = new ArrayList<String>();
		for(int i=0; i<consArrInt.getIntNum(); i++) {
			int offset = consArrInt.getIntValue(i);
			String fieldName = allFieldWebNames.get(offset);
			selectedFieldWebNames.add(fieldName);
		}

		return selectedFieldWebNames;
	}

	
	
	
	
	public int getElementNumOfConstArrayArg(int argIndex) {
		// このサービスには必要ないので呼び出してる時点でおかしい
		JOptionPane.showMessageDialog(MainFrame.getInstance(), "エラーが発生しました。");
		Debug.error("想定外の呼び出しです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}





	public void initServiceNumber() {
		serviceNumber = 1;
	}
}
