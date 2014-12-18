package utility;

import java.util.ArrayList;

import tpp.TransitionProcessPart;

import debug.Debug;

public class ArrayListElemMover {
	public static boolean move(ArrayList<TransitionProcessPart> arrayList, int index, int destIndex) {
		int elemNum = arrayList.size();
		TransitionProcessPart elem = arrayList.get(index);


		// １つ以上前の要素のところに挿入し、そこを含む右側の要素は右にずらす
		if(destIndex<=index) {
			arrayList.remove(index);
			arrayList.add(destIndex, elem);
		}
		// 次の要素のとこに挿入する場合（結局何もしない、これを行うことはないはず）
		else if(destIndex==index+1) {
			// 何もしない
		}
		// ２つ以上後の要素のとこに挿入し、そこを含む右側の要素は右にずらす
		// 最後に移動する場合もこれに当たる
		else if(destIndex>index+1 && destIndex<=elemNum+1) {
			arrayList.remove(index);
			arrayList.add(destIndex-1, elem);
		}
		// それ以外のケース -> 想定外
		else {
			Debug.error("ArrayListの要素の移動において想定外の指定がありました", "ArrayListMover", Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		return true;
	}
}
