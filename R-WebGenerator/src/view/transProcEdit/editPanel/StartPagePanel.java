package view.transProcEdit.editPanel;

import property.GeneratorProperty;
import webPage.WebPage;

public class StartPagePanel extends TpPagePanel {
	public StartPagePanel(WebPage page) {
		super(page);

		boolean japanese = GeneratorProperty.japanese();
		
		setPanelBorder(page.pageFileName+ (japanese?" ‘JˆÚŒ³":" Departure"));
		setPosX(20);
		setPosY(20);
	}
}
