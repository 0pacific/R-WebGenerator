<?php
/*
 * Created on 2010/11/19
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class HyperLinkHtmlCreater {
	var $pePrimKey;





	public function __construct($pePrimKey) {
		$this->pePrimKey = $pePrimKey;
	}





	public function generate() {
		$queryHl = "select * from page_element_hyper_link where pePrimaryKey=" . $this->pePrimKey;
		$resultHl = mysql_query_logg($queryHl);

		// エラー処理
		if(mysql_num_rows($resultHl)!==1) {
			logg_debug_error("テーブル page_element_hyper_link から、ただ一つのハイパーリンクを発見できませんでした", "PageHtmlGenerator", __FUNCTION__);
		}
		// 遷移先ページの番号を用い遷移先ページのファイル名を取得
		$hlDestPageNumber = mysql_result($resultHl, 0, "destPageNumber");
		$fpm = new FilePathManager();
		$destPageFileName = $fpm->getPageFileName($hlDestPageNumber);

		// ハイパーリンクのテキスト（Webページに表示される）
		$hlText = mysql_result($resultHl, 0, "text");

		$action = $destPageFileName."?pepk=".$this->pePrimKey;
		$javaScriptCode = "(function(){document.PageElementsData.action=\"".htmlspecialchars($action)."\";"
							. "document.PageElementsData.submit();})();return false;";
		$html = "<a href='' onClick='" . $javaScriptCode . "'>" . $hlText . "</a>";
		return $html;
	}	
}
?>
