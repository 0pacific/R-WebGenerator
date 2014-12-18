package tpp.service.concreteService;

import java.util.ArrayList;

import property.GeneratorProperty;

import debug.Debug;

import tpp.TfdOutputer;
import tpp.TppConstArrayInt;
import tpp.TppConstArrayString;
import tpp.TppConstInt;
import tpp.port.TppPort;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service5_PickRecord extends TabularOutputService {
	public Service5_PickRecord(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		// 各インプットの種類
		String[] argTypes = {
				Service.IO_TYPE_TFD,
				Service.IO_TYPE_CONST_INT,
		};
		this.argTypes = argTypes;

		// ★入力TPPポートの初期化にはargTypesが用いられるため、argTypesの初期化後でなければならない
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "定数個レコード抽出" : "Several Records Extraction";
		this.serviceType = Service.SERVICE_TYPE_TABLE_PROC;

		// 説明文
		description = GeneratorProperty.japanese()?
						"表データを受け取り、上から指定した数だけのレコードを抽出して新たな表データとして出力します。表データと、抽出するレコード数を指定して下さい。":
						"This service receives a table and extract several records on the top, then output a new table. Define a tabular form data and number of records.";

		// 各インプットの名前
		String[] argNames = {
			japanese ? "表データ" : "Tabular Form Data",
			japanese ? "レコード数" : "Quantity of records to be extracted"
		};
		this.argNames = argNames;

		// 各インプットについての説明文
		String[] argDescs = {
			"表データ・・・\n" +
			"左図の中から表を出力するものを選択\n" +
			"（テーブル読出処理、サービスなど）",

			"レコード数・・・\n" +
			"1以上の整数を入力"
		};
		this.argDescs = argDescs;

		// アウトプットの種類
		this.outputType = Service.IO_TYPE_TFD;
	}
	
	
	
	
	
	public int getOutFieldNum() {
		// 第一引数の入力TPPポート
		TppPort firstPort = this.getInputPort(0);

		// 第一引数のTFD出力型TPPを（定義されていれば）取得する
		TfdOutputer tfdOutputerFirst = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		if(tfdOutputerFirst==null) {
			Debug.notice("定数個レコード抽出サービスが受け取る表が定義されていないようです", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return -1;
		}
		
		return tfdOutputerFirst.getOutFieldNum();
	}




	public ArrayList<String> getOutputFieldWebNames() {
		// 第一引数が入ってくる入力TPPポート
		TppPort firstPort = this.getInputPort(0);

		// 第一引数のTFD出力型TPPを（定義されていれば）取得する
		TfdOutputer tfdOutputerFirst = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		if(tfdOutputerFirst==null) {
			Debug.notice("定数個レコード抽出サービスが受け取る表が定義されていないようです", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		ArrayList<String> fieldNameArray1 = tfdOutputerFirst.getOutputFieldWebNames();
		return fieldNameArray1;
	}





	public int getElementNumOfConstArrayArg(int argIndex) {
		// このサービスには必要ないので呼び出してる時点でおかしい
		Debug.error("想定外の呼び出しです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}






	public void initServiceNumber() {
		serviceNumber = 5;
	}
}
