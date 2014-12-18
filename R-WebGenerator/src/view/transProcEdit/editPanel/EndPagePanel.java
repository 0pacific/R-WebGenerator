package view.transProcEdit.editPanel;

import property.GeneratorProperty;
import webPage.WebPage;

public class EndPagePanel extends TpPagePanel {
	public EndPagePanel(WebPage page) {
		super(page);
		setPanelBorder(GeneratorProperty.japanese() ? "‘JˆÚæ : "+page.pageFileName :"Destination : "+page.pageFileName);
		setPosX(670);
		setPosY(20);
	}
}
