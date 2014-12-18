package executer.generation.tpp;

import tpp.TransitionProcessPart;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;

public class TppnAssigner {
	/*
	 * SUMMARY :
	 * 全遷移プロセスに、TPPNを与える
	 */
	public void assignTppn() {
		TransitionManager transManager = TransitionManager.getInstance();

		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			TransitionProcess transProc = trans.transProc;
			int transKey = trans.transKey;
			int firstTppn = transKey*1000;
			
			for(int j=0; j<transProc.getTppNum(); j++) {
				TransitionProcessPart tpp = transProc.getTpp(j);
				tpp.tppn = firstTppn + j;
			}
		}
	}
}
