package tpp.service.concreteService;

import java.util.ArrayList;

import property.GeneratorProperty;

import debug.Debug;

import tpp.TfdOutputer;
import tpp.TppConstArrayInt;
import tpp.TppConstArrayString;
import tpp.TppConstInt;
import tpp.TppConstString;
import tpp.port.TppPort;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service6_DateListColumn extends TabularOutputService {
	public Service6_DateListColumn(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		// 各インプットの種類
		String[] argTypes = {
				Service.IO_TYPE_DATE,
				Service.IO_TYPE_DATE,
				Service.IO_TYPE_CONST_STRING,
		};
		this.argTypes = argTypes;

		// ★入力TPPポートの初期化にはargTypesが用いられるため、argTypesの初期化後でなければならない
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "日付連番フィールド作成" : "Date Range Field";
		this.serviceType = Service.SERVICE_TYPE_FIELD_PROC;

		// 説明文
		description = GeneratorProperty.japanese()?
						"開始日付と終了日付を受け取り、開始日付から終了日付までの日付を上から格納していった１列の表データを出力します。開始日付、終了日付それぞれを渡すサービス引数入力フォームないしサービスを指定して下さい。":
						"This service makes date range field. Define first date and last date.";

		// 各インプットの名前
		String[] argNames = {
			japanese ? "開始日付" : "First Date",
			japanese ? "終了日付" : "Last Date",
			japanese ? "フィールド名" : "Field Name"
		};
		this.argNames = argNames;

		// 各インプットについての説明文
		String[] argDescs = {
			"開始日付・・・\n" +
			"日付を出力するサービス、またはサービス引数入力フォームを選択",

			"終了日付・・・\n" +
			"日付を出力するサービス、またはサービス引数入力フォームを選択",

			"フィールド名・・・\n" +
			"出力されるフィールド（１つ）の名称を入力",
		};
		this.argDescs = argDescs;

		// アウトプットの種類
		this.outputType = Service.IO_TYPE_TFD;
	}
	
	
	
	
	
	public int getOutFieldNum() {
		return 1;
	}




	public ArrayList<String> getOutputFieldWebNames() {
		// 第三引数が入ってくる入力TPPポート
		TppPort thirdPort = this.getInputPort(2);

		// 第三引数のTPPを（定義されていれば）取得する
		TppConstString constString = (TppConstString)transProc.portTransManager.getInputTppIfExists(thirdPort);
		if(constString==null) {
			Debug.notice("定義されていないようです", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		ArrayList<String> fieldNameArray = new ArrayList<String>();
		fieldNameArray.add(constString.getValue());
		return fieldNameArray;
	}





	public int getElementNumOfConstArrayArg(int argIndex) {
		// このサービスには必要ないので呼び出してる時点でおかしい
		Debug.error("想定外の呼び出しです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}






	public void initServiceNumber() {
		serviceNumber = 6;
	}
}
