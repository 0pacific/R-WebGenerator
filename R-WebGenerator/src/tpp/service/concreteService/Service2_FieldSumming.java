package tpp.service.concreteService;

import java.util.ArrayList;

import property.GeneratorProperty;

import debug.Debug;

import tpp.service.Service;
import tpp.service.TabularOutputService;
import transition.TransitionProcess;

public class Service2_FieldSumming extends TabularOutputService implements ConstArrayElementNumGetter {
	public Service2_FieldSumming(TransitionProcess transProc) {
		super(transProc);

		boolean japanese = GeneratorProperty.japanese();

		// 各インプットの種類
		String[] argTypes = {
				Service.IO_TYPE_TFD,
				Service.IO_TYPE_OFFSETS,
				Service.IO_TYPE_CONST_STRING
		};
		this.argTypes = argTypes;

		// ★入力TPPポートの初期化にはargTypesが用いられるため、argTypesの初期化後でなければならない
		initInputPorts();
		initOutputPort();

		this.serviceName = japanese ? "フィールド合計値計算" : "Fields Summing";
		this.serviceType = Service.SERVICE_TYPE_CALC;

		// 説明文
		description = GeneratorProperty.japanese()?
						"表の１つ以上の列の合計を表す、新たな１つの列を作ります。入力する表と、合計したい各列の番号を指定して下さい。":
						"This service makes a new field which stores summed values of at least 1 fields of a table.Define a table to be input and fields to be summed up.";
		

		// 各インプットの名前
		String[] argNames = {
			japanese ? "テーブル" : "Table",
			japanese ? "被演算フィールド" : "Fields to be summed up",
			japanese ? "合計値フィールド名" : "Result Field's name"
		};
		this.argNames = argNames;

		// 各インプットについての説明文
		String[] argDescs = {
			"表データ・・・\n" +
			"左図の中から表を出力するものを選択\n" +
			"（テーブル読出処理、サービスなど）",

			"被演算フィールド・・・\n" +
			"どのフィールドの値を合計するか、\n" +
			"１つ以上のフィールドを選択",

			"合計フィールド名・・・\n" +
			"出力する表データ（１フィールド）\n" +
			"のフィールド名を入力"
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
	
	
	
	
	
	public int getOutFieldNum() {
		return 1;
	}




	public ArrayList<String> getOutputFieldWebNames() {
		/*
		 * ◆未実装
		 */
		return null;
	}





	public int getElementNumOfConstArrayArg(int argIndex) {
		if(argIndex==2) {
			return 1;
		}

		Debug.error("指定されたサービス引数番号が想定外です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}






	public void initServiceNumber() {
		serviceNumber = 2;
	}
}
