<?php

class ItemData {
	public $id;
	public $name;
	public $kana;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) == 2) {
			$itemData = new ItemData();
			$itemData->id = $datas[0];
			$itemData->name = $datas[1];
			$itemData->kana = "";		// TODO そのうち削除する
			return $itemData;
		}
		return null;
	}
}

class Item {
	const FILE_NAME = "data/item.txt";

	static function readAll() {

		if (file_exists(Item::FILE_NAME)) {
			$fileData = file_get_contents(Item::FILE_NAME);
			if ($fileData !== false) {
				$itemList = [];
				$lines = explode("\n", $fileData);
				for ($i = 0; $i < count($lines); $i++) {
					$itemData = ItemData::initFromFileString($lines[$i]);
					if (!is_null($itemData)) {
						$itemList[] = $itemData;
					}
				}
				return $itemList;
			}
		}
		return [];
	}

	static function append($itemName) {

		$existItemId = Item::searchItem($itemName);
		if ($existItemId != -1) {
			return $existItemId;
		}

		$allItems = Item::readAll();
		$newItemId = 0;
		if (count($allItems) > 0) {
			$newItemId = $allItems[count($allItems) - 1]->id;
		}
		$newItemId += 1;
		$newLine = (string)$newItemId . "," . $itemName . ",\n";
		file_put_contents(Item::FILE_NAME, $newLine, FILE_APPEND);

		return (string)$newItemId;
	}

	static function searchItem($itemName) {
		$allItems = Item::readAll();
		foreach($allItems as $item) {
			if (strcmp($item->name, $itemName) == 0) {
				return $item->id;
			}
		}
		return -1;
	}
}


?>
