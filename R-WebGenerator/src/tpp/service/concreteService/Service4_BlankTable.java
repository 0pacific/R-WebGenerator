package tpp.service.concreteService;

import java.util.ArrayList;

import property.GeneratorProperty;

import debug.Debug;

import tpp.TppConstArrayInt;
import tpp.TppConstArrayString;
import tpp.TppConstInt;
import tpp.port.TppPort;
import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service4_BlankTable extends TabularOutputService implements ConstArrayElementNumGetter {
	public Service4_BlankTable(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		// 各インプットの種類
		String[] argTypes = {
				Service.IO_TYPE_CONST_INT,
				Service.IO_TYPE_CONST_INT,
				Service.IO_TYPE_CONST_STRING_ARRAY
		};
		this.argTypes = argTypes;

		// ★入力TPPポートの初期化にはargTypesが用いられるため、argTypesの初期化後でなければならない
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "ブランク表データ作成" : "Blank Table";
		this.serviceType = Service.SERVICE_TYPE_TABLE_PROC;

		// 説明文
		description = GeneratorProperty.japanese()?
						"データタイプも値も持たず、全ての欄が空欄となった表を作成します。行数、列数、各列の名前を指定して下さい。":
						"This service makes a blank table whose fields don't have any data types, any values. Define Numbers of Record/Field, and a name of each field.";
		

		// 各インプットの名前
		String[] argNames = {
			japanese ? "行数" : "Records Quantity",
			japanese ? "列数" : "Fields Quantity",
			japanese ? "各フィールドの名称" : "Fields' names"
		};
		this.argNames = argNames;

		// 各インプットについての説明文
		String[] argDescs = {
			"行数・・・\n" +
			"1以上の整数を入力。または、\n" +
			"左図の中から整数を出力するものを選択\n" +
			"（サービス、整数入力フォームなど）",

			"列数・・・\n" +
			"1以上の整数を入力。または、\n" +
			"左図の中から整数を出力するものを選択\n" +
			"（サービス、整数入力フォームなど）",

			"各フィールドの名称・・・\n" +
			"各列の名称を入力\n" +
			"列数の入力後に入力して下さい。"
		};
		this.argDescs = argDescs;

		// 第1引数を定義せねば第2引数を定義できないことを記録
		ArrayList<Integer> precursiveArgIndexArray = new ArrayList<Integer>();
		precursiveArgIndexArray.add(new Integer(1));
		hashMapNeededArgForOtherArg.put(new Integer(2), precursiveArgIndexArray);

		// 第三引数の要素数は第二引数の列数そのものなので、そのことをハッシュマップに記録
		this.hashMapConstArrayElemNum.put(new Integer(2), new Integer(1));

		// アウトプットの種類
		this.outputType = Service.IO_TYPE_TFD;
	}
	
	
	
	
	
	public int getOutFieldNum() {
		TppPort fieldNumInputPort = this.getInputPort(1);
		TppConstInt constInt = (TppConstInt)transProc.portTransManager.getInputTppToTppPortIfExists(fieldNumInputPort);

		if(constInt==null) {
			Debug.notice("ブランク表作成サービスの列数が定義されておらず出力TFDの列数がわからない状態で、getOutFieldNum()が呼び出されました。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return -1;
		}

		int fieldNum = constInt.value;
		return fieldNum;
	}




	public ArrayList<String> getOutputFieldWebNames() {
		TppPort fieldNamesInputPort = this.getInputPort(2);
		TppConstArrayString constArrayString = (TppConstArrayString)transProc.portTransManager.getInputTppToTppPortIfExists(fieldNamesInputPort);

		if(constArrayString==null) {
			Debug.notice("ブランク表作成サービスの第三引数（フィールド名称配列）が定義されておらず出力TFDのフィールド名がわからない状態です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		ArrayList<String> fieldNameArray = constArrayString.valueArray;
		return fieldNameArray;
	}





	public int getElementNumOfConstArrayArg(int argIndex) {
		if(argIndex==2) {
			return this.getOutFieldNum();	// 列数が定義されていないためまだわからない場合は、-1が返ってくる
		}

		// ここでも-1が返るが、エラー出力なのでエラーだということはわかるだろう
		Debug.error("指定されたサービス引数番号が想定外です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}






	public void initServiceNumber() {
		serviceNumber = 4;
	}
}
