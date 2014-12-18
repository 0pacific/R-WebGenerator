package executer.generation;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;

import debug.Debug;
import gui.*;
import gui.arrow.*;
import pageElement.*;
import role.*;
import table.*;
import tpp.*;
import view.generation.*;
import view.tableList.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import executer.generation.mysql.*;
import executer.generation.pePrimKey.PageElementPrimaryKeyAssigner;
import executer.generation.tpp.TppOrderArranger;
import executer.generation.tpp.TppnAssigner;
import executer.generation.transPrimKey.TransitionPrimaryKeyAssigner;





public class GenerationExecuter implements Serializable {
	private static GenerationExecuter obj = new GenerationExecuter();

	
	
	
	
	private GenerationExecuter() {
	}

	
	
	
	/*
	 * SUMMARY :
	 * Web�A�v���̐��������s
	 */
	public boolean execute() {
		// �SWeb�y�[�W�̑S�y�[�W�G�������g�ɁA��L�[��^���Ă���
		PageElementPrimaryKeyAssigner pepka = new PageElementPrimaryKeyAssigner();
		pepka.assignKeys();

		// �S�J�ڂɁA��L�[��^���Ă���
		TransitionPrimaryKeyAssigner transKeyAssigner = new TransitionPrimaryKeyAssigner();
		transKeyAssigner.assignKeys();

		// TPP��K�؂ɕ��בւ���
		TppOrderArranger arranger = new TppOrderArranger();
		arranger.arrange();
		
		// �STPP�ɁATPPN��^���Ă���
		TppnAssigner tppnAssigner = new TppnAssigner();
		tppnAssigner.assignTppn();
		
		// �A�v����`SQL�̍쐬
		SqlGenerator sqlGenerator = new SqlGenerator();
		boolean result = sqlGenerator.execute();
		if(!result) {
			Debug.notice("�f�[�^�x�[�X�̑S�e�[�u�����쐬����SQL��SQL�t�@�C���ɏ����o���ۑ�����Ƃ�����Ƃ����s���܂����B�A�v�������𒆒f���܂��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
		
		// �eWeb�y�[�W�𐶐�
		WebPageGenerator wpg = new WebPageGenerator();
		boolean webPageGenResult = wpg.execute();
		if(!webPageGenResult) {
			Debug.error("Web�y�[�W�̐����Ɏ��s�������߁A�A�v�������𒆒f���܂��B", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}

		return true;
	}
	
	
	
	

	public static GenerationExecuter getInstance() {
		return GenerationExecuter.obj;
	}
}
