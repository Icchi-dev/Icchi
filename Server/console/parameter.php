<?php

class ParameterData {
	public $pointPerItem;
	public $pointPerMinorItem;
	public $minorThreshold;
}

class Parameter {
	const FILE_NAME = "../data/parameter.txt";

	static function write($pointPerItem, $pointPerMinorItem, $minorThreshold) {

		$str = $pointPerItem . "," . $pointPerMinorItem . "," . $minorThreshold;
		file_put_contents(Parameter::FILE_NAME, $str);
	}
}


?>
