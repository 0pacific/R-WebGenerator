package executer.generation.transPrimKey;

import pageElement.PageElement;
import transition.Transition;
import transition.TransitionManager;
import webPage.WebPage;
import webPage.WebPageManager;

public class TransitionPrimaryKeyAssigner {
	/*
	 * SUMMARY :
	 * 全遷移に、主キーを与える
	 */
	public void assignKeys() {
		// 遷移の主キーは1から順に与えていく
		int key = 1;
	
		TransitionManager transManager = TransitionManager.getInstance();

		for(int i=0; i<transManager.getTransitionNum(); i++) {
			Transition trans = transManager.getTransition(i);
			trans.transKey = key;
			key++;
		}
	}
}
