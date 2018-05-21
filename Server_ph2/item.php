<?php

class ItemData {
  public $id;
	public $name;

	static function initFromFileString($line) {
		$datas = explode(",", $line);
		if (count($datas) >= 2) {
      $itemData = new ItemData();
      $itemData->id = $datas[0];
			$itemData->name = $datas[1];
			return $itemData;
		}
		return null;
	}

  function toFileString() {
    $str = "";
    $str .= $this->id;
    $str .= ",";
    $str .= $this->name;
    $str .= "\n";
    return $str;
  }
}

class Item {

  const FILE_NAME = "../data/item.txt";

	static function readAll() {
		if (file_exists(Item::FILE_NAME)) {
			$fileData = file_get_contents(Post::FILE_NAME);
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

  static function writeAll($itemList) {
    $str = "";
    foreach($itemList as $itemData) {
      $str .= $itemData->toFileString();
    }
    file_put_contents(Item::FILE_NAME, $str);
  }

  static function delete($id) {
    $itemList = Item::readAll();
    $newItemList = Array();
    foreach($itemList as $itemData) {
      if (strcmp($itemList->id, $id) != 0) {
        $newItemList[] = $itemData;
      }
    }
    Item::writeAll($newItemList);
  }
}

?>
