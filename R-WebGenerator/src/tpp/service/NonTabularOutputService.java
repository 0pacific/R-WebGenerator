package tpp.service;

import transition.TransitionProcess;

public abstract class NonTabularOutputService extends Service {
	public static final String OUTPUT_TYPE_NONTABULAR_INT = "NONTABULAR_INT";
	public static final String OUTPUT_TYPE_NONTABULAR_VARCHAR = "NONTABULAR_VARCHAR";

	public String outputType;
	
	public NonTabularOutputService(TransitionProcess transProc, String outputType) {
		super(transProc);
		this.outputType = outputType;
	}



	public String getOutputType() {
		return this.outputType;
	}
}
