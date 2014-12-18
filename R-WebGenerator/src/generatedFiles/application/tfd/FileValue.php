<?php
/*
 * Created on 2010/09/05
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

class FileValue {
	var $fileName;

	// セルロケーション
	var $cellLocation;

	// 一時ファイルのパス（更新フォームから一時的にアップロードされたファイルのパスを記録しておきたい）
	var $tempFilePath;



	public function __construct($fileName, $cellLocation) {
		$this->fileName = $fileName;
		$this->cellLocation = $cellLocation;
		$this->tempFilePath = null;
	}






	/*
	 * SUMMARY :
	 * ファイルのパスを返却
	 */
	public function getFilePath() {
		$path = $this->getDirPath() . $this->fileName;
		return $path;
	}





	/*
	 * SUMMARY :
	 * ディレクトリのパスを返却
	 */
	public function getDirPath() {
		$path = DIRECTORY_FILES
		      . 'table_' . $this->cellLocation->getTableNumber()
		      . '/primKey_' . $this->cellLocation->getPrimKey()
		      . '/offset_' . $this->cellLocation->getOffset()
		      . '/';
		return $path;
	}





	public function makeDirectoryIfNotExists() {
		$dirPath = $this->getDirPath();
		if(!is_dir($dirPath)) {
			mkdir($dirPath, 0777, true);	// 入れ子構造のディレクトリの作成を許すためtrueを渡す
		}
	}
	



	public function getFileName() {
		return $this->fileName;
	}

	public function getCellLocation() {
		return $this->cellLocation;
	}

	public function setTempFilePath($tempFilePath) {
		$this->tempFilePath = $tempFilePath;
	}
}

?>
