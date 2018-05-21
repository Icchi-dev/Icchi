<?php

class ParameterData {
	public $pointPerItem;
	public $pointPerMinorItem;
	public $minorThreshold;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 3) {
			$parameterData = new ParameterData();
			$parameterData->pointPerItem = $datas[0];
			$parameterData->pointPerMinorItem = $datas[1];
			$parameterData->minorThreshold = $datas[2];
			return $parameterData;
		}
		return null;
	}
}

class Parameter {
	const FILE_NAME = "../data/parameter.txt";

	static function read() {
		if (file_exists(Parameter::FILE_NAME)) {
			$fileData = file_get_contents(Parameter::FILE_NAME);
			if ($fileData !== false) {
				return ParameterData::initFromFileString($fileData);
			}
		}
		return null;
	}

	static function write($pointPerItem, $pointPerMinorItem, $minorThreshold) {
		$str = $pointPerItem . "," . $pointPerMinorItem . "," . $minorThreshold;
		file_put_contents(Parameter::FILE_NAME, $str);
	}
}


?>
