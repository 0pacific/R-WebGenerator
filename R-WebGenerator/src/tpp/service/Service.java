package tpp.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import property.GeneratorProperty;

import mainFrame.MainFrame;

import authority.AuthoritySet;

import debug.Debug;

import role.NormalRole;
import tpp.TransitionProcessPart;
import tpp.port.TppPort;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import tpp.service.concreteService.Service1_FieldSelection;
import tpp.service.concreteService.Service2_FieldSumming;
import tpp.service.concreteService.Service3_TableJoin;
import tpp.service.concreteService.Service4_BlankTable;
import tpp.service.concreteService.Service5_PickRecord;
import tpp.service.concreteService.Service6_DateListColumn;
import transition.TransitionProcess;

public abstract class Service extends TransitionProcessPart {
	// インプット・アウトプットのタイプを表す定数
	public static final String IO_TYPE_CONST_INT = "CONST_INT";
	public static final String IO_TYPE_CONST_STRING = "CONST_STRING";
	public static final String IO_TYPE_CONST_INT_ARRAY = "CONST_INT_ARRAY";
	public static final String IO_TYPE_CONST_STRING_ARRAY = "CONST_STRING_ARRAY";
	public static final String IO_TYPE_DATETIME = "IO_TYPE_DATETIME";
	public static final String IO_TYPE_DATE = "IO_TYPE_DATE";
	public static final String IO_TYPE_TIME = "IO_TYPE_TIME";
	public static final String IO_TYPE_OFFSETS = "CONST_OFFSETS";
	public static final String IO_TYPE_TFD = "TFD";

	// サービスの種類を表す定数
	public static String SERVICE_TYPE_CALC = GeneratorProperty.japanese()?"四則演算":"Arithmetic Operation";				// 四則演算
	public static String SERVICE_TYPE_FIELD_PROC = GeneratorProperty.japanese()?"フィールド処理":"Field Handling";;		// フィールドの処理（フィールド抽出など）
	public static String SERVICE_TYPE_TABLE_PROC = GeneratorProperty.japanese()?"テーブル処理":"Table Handling";		// テーブル全体を扱う処理（表結合など）

	// ある引数を入力する際に他の引数に入ってくる値を読み出すことが必要なケースがある。
	// 例：ブランク表作成サービスで第三引数のフィールド名配列を入力する際、フィールド数はいくつかと考えると第二引数の列数を読まなければならない。
	// そういったケースで後者の引数が未定義であることを示すために、この定数が関数から返ってくる
	public static final int ARG_NOT_YET_DEFINED = 38294825;
	
	
	// サービス名
	public String serviceName;

	// サービス番号（実行プログラムの方でサービスにつけている番号と合わせること）
	public int serviceNumber;
	
	// サービスの種類
	protected String serviceType;
	
	// サービス説明
	protected String description;

	// 各サービス引数の名前、種類、説明
	public String[] argNames;
	public String[] argTypes;
	public String[] argDescs;
	
	// 出力の種類
	public String outputType;
	
	// ある引数がフィールド選択対象として別のTFD型引数を必要とする場合、それを記録するハッシュマップ
	// フィールド選択引数の番号, TFD型引数の番号　というのが組
	public HashMap<Integer, Integer> hmFldSel = new HashMap<Integer, Integer>();

	// ある引数がSTRING定数配列ないしINT定数配列であり、かつその要素数が可変で他のINTモノ定数型引数の値とされるケースがあるため、
	// その対応を記録するのに使われるハッシュマップ。キーは前者の引数の番号、値は後者の引数の番号
	public HashMap<Integer, Integer> hashMapConstArrayElemNum = new HashMap<Integer, Integer>();

	// ある引数を編集を定義するために、まず他のある引数（１つ以上）が定義されている必要があるケースが存在する。
	// その対応を記録するのに使われるハッシュマップ。キーは前者の引数の番号、値は後者の引数達の番号配列
	public HashMap<Integer, ArrayList<Integer>> hashMapNeededArgForOtherArg = new HashMap<Integer, ArrayList<Integer>>();
	
	
	
	
	
	
	public Service(TransitionProcess transProc) {
		super(transProc);
		initServiceNumber();

		boolean japanese = GeneratorProperty.japanese();
	}
	
	
	
	

	// argIndex番（0始まり）の引数を定義した後にしか定義できない全ての引数の番号を配列で返す
	public ArrayList<Integer> getFollowingArgIndexArray(int argIndex) {
		// 最後に返却する配列
		ArrayList<Integer> argIndexArray = new ArrayList<Integer>();
		
		Iterator iterator = hashMapNeededArgForOtherArg.keySet().iterator();
		while(iterator.hasNext()) {
			Integer thisArgIndex = (Integer)iterator.next();
			ArrayList<Integer> precursiveArgIndexes = hashMapNeededArgForOtherArg.get(thisArgIndex);
			for(int i=0; i<precursiveArgIndexes.size(); i++) {
				Integer idx = precursiveArgIndexes.get(i);
				if(idx.intValue()==argIndex) {
					argIndexArray.add(new Integer(thisArgIndex));
				}
			}
		}

		return argIndexArray;
	}
	
	
	
	
	/*
	 * SUMMARY :
	 * 引数を番号で指定し、その引数を定義しなければ定義できない全引数について、◆定義済であれば◆削除する
	 */
	public void removeFollowingArgsIfExist(int argIndex) {
		ArrayList<Integer> followingArgIndexArray = getFollowingArgIndexArray(argIndex);
		for(int i=0; i<followingArgIndexArray.size(); i++) {
			Integer followingArgIndex = followingArgIndexArray.get(i);
			Debug.today("サービス「"+serviceName+"」の"+argIndex+"番（0-start)の引数が削除されたので、後続する"+followingArgIndex+"番（0-start）の引数も削除します。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			removeArgIfDefined(followingArgIndex.intValue());
		}
	}
	
	
	


	/*
	 * PURPOSE :
	 * 引数の種類を表す文字列（IO_TYPE_CONST_INTなど）を、ディベロッパー向けの表現（"整数"など）に変換
	 */
	public static String convArgTypeExp(String argType) {
		// 各種類に応じて、ディベロッパーが理解しやすい表現を返す
		if(argType.equals(Service.IO_TYPE_CONST_INT)) {
			return "定数（整数）";
		}
		else if(argType.equals(Service.IO_TYPE_CONST_STRING)) {
			return "定数（文字列）";
		}
		else if(argType.equals(Service.IO_TYPE_CONST_INT_ARRAY)) {
			return "定数（整数配列）";
		}
		else if(argType.equals(Service.IO_TYPE_CONST_STRING_ARRAY)) {
			return "定数（文字列配列）";
		}
		else if(argType.equals(Service.IO_TYPE_OFFSETS)) {
			return "１つのテーブルからフィールドを１つ以上選択";
		}
		else if(argType.equals(Service.IO_TYPE_TFD)) {
			return "表形式データ（テーブル読出処理や、表を出力するサービスを指定）";
		}

		// エラー
		Debug.error("不正なサービス種類です", "Service", Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * 指定した種類のサービス達の、名前一覧を返却する
	 */
	public static String[] getServNamesByCateg(String categName) {
		boolean japanese = GeneratorProperty.japanese();
		
		String[] servNames = new String[0];
		
Debug.out(categName+"だよーん");
Debug.out(Service.SERVICE_TYPE_TABLE_PROC);
Debug.out(Service.SERVICE_TYPE_FIELD_PROC);
Debug.out(Service.SERVICE_TYPE_CALC);
		// テーブル処理
		if(categName.equals(Service.SERVICE_TYPE_TABLE_PROC)) {
			String[] list = {
					japanese?"表結合":"Join",
					japanese?"ブランク表データ作成":"Blank Table",
					japanese?"定数個レコード抽出":"Several Records Extraction"
			};
			servNames = list;
		}
		// フィールド処理
		else if(categName.equals(Service.SERVICE_TYPE_FIELD_PROC)) {
			String[] list = {
					japanese?"フィールド抽出":"Fields Extraction",
					japanese?"日付連番フィールド作成":"Date Range Field"
			};
			servNames = list;
		}
		// 四則演算
		else if(categName.equals(Service.SERVICE_TYPE_CALC)) {
			String[] list = {
					japanese?"フィールド合計値計算":"Fields Summing"
			};
			servNames = list;
		}
		// 不正なサービスカテゴリ
		else {
			Debug.error("不正なサービス種類です", "Service", Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}
		
		return servNames;
	}

	
	
	
	
	public boolean removeArgIfDefined(int argIndex) {
		TppPort argPort = getInputPort(argIndex);
		PortTransManager ptm = transProc.portTransManager;

		// 引数を削除（存在するとは限らない）
		boolean remove = ptm.removeOnePortTransByEndPortIfExists(argPort);
		if(!remove) {
			Debug.notice("第"+argIndex+"引数（0始まり）が定義済なら削除しようとしましたが、未定義だったようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		/*
		 * 引数が存在したので削除した
		 */

		Debug.notice("第"+argIndex+"引数（0始まり）が定義済だったので削除しました。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// この引数を定義しなければ定義できない引数（後続する引数と呼ぶ）を全て洗い出す。そして定義されているなら削除する
		removeFollowingArgsIfExist(argIndex);
		
		return true;
	}
	
	
	
	
	public boolean argDefined(int argIndex) {
		TppPort argPort = getInputPort(argIndex);
		PortTrans portTrans = transProc.portTransManager.getOnePortTransByEndPortIfExists(argPort);

		// 定義済
		if(portTrans instanceof PortTrans) {
			return true;
		}

		// 未定義
		return false;
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * 属する遷移プロセスにおいて、指定した番号の引数ポートへ既にデータが対応しているかを返す
	 * （つまり、引数が既に指定されているかを教える）
	 */
	public boolean inputPortReserved(int inputPortIndex) {
		TppPort inputPort = this.getInputPort(inputPortIndex);
		PortTransManager ptm = getTransProc().getPortTransManager();
		boolean reserved = ptm.inputReserved(inputPort);
		return reserved;
	}
	
	
	
	
	
	
	public static Service getInstanceByServiceName(TransitionProcess tp, String serviceName) {
		boolean japanese = GeneratorProperty.japanese();
		
		Service cs = null;
		if(serviceName.equals(japanese?"フィールド抽出":"Fields Extraction")) {
			cs = new Service1_FieldSelection(tp);
		}
		else if(serviceName.equals(japanese?"フィールド合計値計算":"Fields Summing")) {
			cs = new Service2_FieldSumming(tp);
		}
		else if(serviceName.equals(japanese?"表結合":"Join")) {
			cs = new Service3_TableJoin(tp);
		}
		else if(serviceName.equals(japanese?"ブランク表データ作成":"Blank Table")) {
			cs = new Service4_BlankTable(tp);
		}
		else if(serviceName.equals(japanese?"定数個レコード抽出":"Several Records Extraction")) {
			cs = new Service5_PickRecord(tp);
		}
		else if(serviceName.equals(japanese?"日付連番フィールド作成":"Date Range Field")) {
			cs = new Service6_DateListColumn(tp);
		}
		else {
			Debug.notice("指定された名前のサービスがないようです。", "Service", Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		return cs;
	}

	public String getName() {
		return serviceName;
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * インプットとして１つ以上の指定タイプデータを受け取るサービスか否かを返す
	 * 
	 * NOTICE :
	 * ・指定タイプに合致するインプット達が既に埋まっているかどうかは関知しない
	 * ・dataTypeには、Serviceクラスのstatic定数を指定する
	 */
	public boolean canReceiveData(String dataType) {
		for(int i=0; i<argTypes.length; i++) {
			String type = argTypes[i];
			if(type.equals(dataType)) {
				return true;
			}
		}

		return false;
	}

	
	
	
	public int getArgIndexByArgPort(TppPort argPort) {
		for(int i=0; i<getArgNum(); i++) {
			if(getInputPort(i)==argPort) {
				return i;
			}
		}

		JOptionPane.showMessageDialog(MainFrame.getInstance(), "エラーが発生しました。");
		Debug.error("引数ポートが見つかりません。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return -1;
	}
	
	
	
	
	/*
	 * PURPOSE :
	 * インプットとして指定タイプデータをあと１つでも定義できるか否か、を返す
	 */
	public boolean canReceiveMoreData(String dataType) {
		// 指定タイプデータを１つもインプットできないサービスである
		if(!canReceiveData(dataType)) {
			return false;
		}

		// 各インプットポートについて繰り返し
		for(int i=0; i<getArgNum(); i++) {
			// i番目のインプットは指定タイプであり、かつまだ埋められていない
			if(argTypes[i].equals(dataType) && !inputPortReserved(i)) {
				return true;
			}
		}

		// 指定タイプのデータを１つ以上インプットできるサービスだが、既に全て埋まっている
		return false;
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * インプットポートを初期化（個数は各サービスについてあらかじめ定まったもの）
	 */
	public void initInputPorts() {
		inputPorts = new ArrayList<TppPort>();
		for(int i=0; i<argTypes.length; i++) {
			inputPorts.add(new TppPort(this));
		}
	}
	public void initOutputPort() {
		this.outputPort = new TppPort(this);
	}



	// 以下、Getter&Setter
	
	public String getDescription() {
		return description;
	}

	/*
	 * PURPOSE :
	 * 引数の数を取得
	 * 
	 * NOTICE :
	 * 定義済の引数の数を取得するわけではない
	 */
	public int getArgNum() {
		return argTypes.length;
	}
	
	public String[] getArgNames() {
		return argNames;
	}

	public String[] getArgTypes() {
		return argTypes;
	}

	/*
	 * PURPOSE :
	 * 引数説明一覧取得
	 */
	public String[] getArgDescs() {
		return argDescs;
	}

	
	
	
	public void removeService() {
		// 全ての引数について、その引数が定義済ならポートトランスミッションを削除
		for(int i=0; i<getArgNum(); i++) {
			removeArgIfDefined(i);
		}

		// このサービスの出力を受け取っているポートトランスミッションを全て削除
		// このとき、受け取っているのが別のサービスであった場合、その別のサービスの後続する引数も削除しなければならないが、それはPortTransManagerインスタンスの方でやってくれる
		TppPort outPort = getOutputPort();
		this.transProc.portTransManager.removeAllPortTransByStartPort(outPort);

		this.transProc.removeTpp(this);
	}
	

	
	
	public TransitionProcessPart getArgTppIfExists(int argIndex) {
		TppPort argPort = this.getInputPort(argIndex);
		TransitionProcessPart inputTpp = this.transProc.portTransManager.getInputTppIfExists(argPort);
		if(inputTpp==null) {
			Debug.notice("サービス「" + this.serviceName + "」の第" + argIndex + "引数（0始まり）のTPPが未定義のようです。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return null;
		}

		return inputTpp;
	}
	



	public abstract int getElementNumOfConstArrayArg(int argIndex);
	public abstract void initServiceNumber();
}
