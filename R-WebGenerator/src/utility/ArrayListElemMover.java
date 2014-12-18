package utility;

import java.util.ArrayList;

import tpp.TransitionProcessPart;

import debug.Debug;

public class ArrayListElemMover {
	public static boolean move(ArrayList<TransitionProcessPart> arrayList, int index, int destIndex) {
		int elemNum = arrayList.size();
		TransitionProcessPart elem = arrayList.get(index);


		// �P�ȏ�O�̗v�f�̂Ƃ���ɑ}�����A�������܂މE���̗v�f�͉E�ɂ��炷
		if(destIndex<=index) {
			arrayList.remove(index);
			arrayList.add(destIndex, elem);
		}
		// ���̗v�f�̂Ƃ��ɑ}������ꍇ�i���ǉ������Ȃ��A������s�����Ƃ͂Ȃ��͂��j
		else if(destIndex==index+1) {
			// �������Ȃ�
		}
		// �Q�ȏ��̗v�f�̂Ƃ��ɑ}�����A�������܂މE���̗v�f�͉E�ɂ��炷
		// �Ō�Ɉړ�����ꍇ������ɓ�����
		else if(destIndex>index+1 && destIndex<=elemNum+1) {
			arrayList.remove(index);
			arrayList.add(destIndex-1, elem);
		}
		// ����ȊO�̃P�[�X -> �z��O
		else {
			Debug.error("ArrayList�̗v�f�̈ړ��ɂ����đz��O�̎w�肪����܂���", "ArrayListMover", Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		return true;
	}
}
