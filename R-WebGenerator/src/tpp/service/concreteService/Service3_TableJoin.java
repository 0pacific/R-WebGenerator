package tpp.service.concreteService;

import java.util.ArrayList;

import property.GeneratorProperty;

import debug.Debug;
import tpp.TfdOutputer;
import tpp.port.TppPort;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service3_TableJoin extends TabularOutputService {

	public Service3_TableJoin(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		// 各インプットの種類
		String[] argTypes = {
				Service.IO_TYPE_TFD,
				Service.IO_TYPE_TFD,
		};
		this.argTypes = argTypes;

		// ★入力TPPポートの初期化にはargTypesが用いられるため、argTypesの初期化後でなければならない
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "表結合" : "Join";
		this.serviceType = Service.SERVICE_TYPE_TABLE_PROC;

		// 説明文
		description = GeneratorProperty.japanese()?
						"２つの表を横に結合します。左側の表、右側の表を順に選んで下さい。":
						"This service joins 2 tables. Define a table on the left and one on the right.";


		// 各インプットの名前
		String[] argNames = {
			japanese ? "テーブル（左）" : "Left Table",
			japanese ? "テーブル（右）" : "Right Table"
		};
		this.argNames = argNames;

		// 各インプットについての説明文
		String[] argDescs = {
			"表データ１・・・\n" +
			"左図の中から左側の表データを選択\n" +
			"（テーブル読出処理、サービスなど）",
			"表データ２・・・\n" +
			"左図の中から右側の表データを選択\n" +
			"（テーブル読出処理、サービスなど）"
		};
		this.argDescs = argDescs;

		// アウトプットの種類
		this.outputType = Service.IO_TYPE_TFD;
	}

	
	
	
	
	public int getOutFieldNum() {
		// 第一引数、第二引数が入ってくる入力TPPポート
		TppPort firstPort = this.getInputPort(0);
		TppPort secondPort = this.getInputPort(1);

		// 第一引数、第二引数のTFD出力型TPPを（定義されていれば）取得する
		TfdOutputer tfdOutputerFirst = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		TfdOutputer tfdOutputerSecond = transProc.portTransManager.getInputTfdOutputerTppIfExists(secondPort);
		if(tfdOutputerFirst==null || tfdOutputerSecond==null) {
			Debug.notice("表結合サービスが受け取る２つの表のどちらかないし両方が定義されていないようです", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return -1;
		}
		
		// ２つのTFD出力型TPPそれぞれの、出力フィールド数の和を返す
		return tfdOutputerFirst.getOutFieldNum() + tfdOutputerSecond.getOutFieldNum();
	}





	public ArrayList<String> getOutputFieldWebNames() {
		// 第一引数、第二引数が入ってくる入力TPPポート
		TppPort firstPort = this.getInputPort(0);
		TppPort secondPort = this.getInputPort(1);

		// 第一引数、第二引数のTFD出力型TPPを（定義されていれば）取得する
		TfdOutputer tfdOutputerFirst = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		TfdOutputer tfdOutputerSecond = transProc.portTransManager.getInputTfdOutputerTppIfExists(firstPort);
		if(tfdOutputerFirst==null || tfdOutputerSecond==null) {
			Debug.notice("表結合サービスが受け取る２つの表のどちらかないし両方が定義されていないようです", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		ArrayList<String> fieldNameArray1 = tfdOutputerFirst.getOutputFieldWebNames();
		ArrayList<String> fieldNameArray2 = tfdOutputerSecond.getOutputFieldWebNames();
		for(int i=0; i<fieldNameArray2.size(); i++) {
			fieldNameArray1.add(fieldNameArray2.get(i));
		}
		return fieldNameArray1;
	}





	public int getElementNumOfConstArrayArg(int argIndex) {
		// このサービスには必要ないので呼び出してる時点でおかしい
		Debug.error("想定外の呼び出しです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}

	
	
	
	public void initServiceNumber() {
		serviceNumber = 3;
	}
	
}