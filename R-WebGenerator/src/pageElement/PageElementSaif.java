package pageElement;

import table.field.Field;
import tpp.port.PageElementPort;
import transition.TransitionProcess;
import webPage.WebPage;

public class PageElementSaif extends PageElement {
	public static final String KIND_INT = Field.DATATYPE_INT;
	public static final String KIND_VARCHAR = Field.DATATYPE_VARCHAR;
	public static final String KIND_DATETIME = Field.DATATYPE_DATETIME;
	public static final String KIND_DATE = Field.DATATYPE_DATE;
	public static final String KIND_TIME = Field.DATATYPE_TIME;
	public String saifKind;		// 上の５つのどれかにする

	
	public String saifName;
	
	
	public PageElementSaif(WebPage parentPage, String saifName, String saifKind) {
		super(parentPage);
		this.saifName = saifName;
		this.saifKind = saifKind;
	}

	public String getSaifName() {
		return this.saifName;
	}

	public String getSaifKind() {
		return Field.getDataTypeExpression(this.saifKind);
	}
	
	public void initInputPePorts(TransitionProcess transProc) {
		// ページエレメントポートは存在しない
	}

	public void initOutputPePorts(TransitionProcess transProc) {
		// 出力ポートのみの初期化となる
		PageElementPort outputPort = new PageElementPort(this);
		outputPePortsHashMap.put(transProc, outputPort);
	}
}
