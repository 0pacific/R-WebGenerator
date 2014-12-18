package executer.generation;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.io.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.authority.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.Field;
import test.Tester;
import tpp.*;
import transition.Transition;
import view.authEdit.PanelAuthEditBottom;
import view.authEdit.PanelAuthEditAbove;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.Panel_FieldList;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;





public class SqlGenerator implements Serializable {
	public static final String SQL_FILE_LOCATION = "src/generatedFiles/sql/appInfo.sql";
	
	public SqlGenerator() {
	}

	
	/*
	 * SUMMARY :
	 * データベース中の全テーブルを作成するSQLを作り、SQLファイルとして保存する
	 * 
	 * NOTICE :
	 * SQLファイルの保存場所は後で変更するかも
	 */
	public boolean execute() {
		// 全テーブルを一度削除（念のため）
		String sql =	"-- 全テーブルを（存在するなら）削除 --\n" +
						"\n" +
						"drop table if exists " +
						"create_auth, delete_auth, exwrite_lock, field_auth, field_info, general_role, " +
						"ia_assign, ia_assign_auth, ia_possesion, logout_trans, pages, " +
						"page_element, page_element_create_form, page_element_da, page_element_hyper_link, page_element_login_form," +
						"page_element_table_display, page_element_table_display_field, page_element_text, page_element_update_form," +
						"page_element_update_form_field, page_element_saif, role, table_list, " +
						"tpp, tpp_constant_array_int, tpp_constant_array_string, tpp_create_form_reflection, tpp_create_reflection, " +
						"tpp_da_input, tpp_et_load, tpp_ia_reflection, tpp_service_execution, tpp_service_execution_argument, " +
						"tpp_update_form_reflection, tpp_update_reflection, transition, transition_auth, ";

		// 対症療法（dt0～dt99、at0～at99が存在すれば全て削除する）
		for(int i=0; i<100; i++) {
			sql += "dt" + i + ", " + "at" + i + ", ";
		}

		// 最後の", "をカット
		sql = sql.substring(0, sql.length()-2);

		sql += ";\n" + "\n\n";

		String temp = "";
		
		// pages
		PageSqlCreator psc = new PageSqlCreator();
		sql += psc.getSql();
		sql += "\n\n";

		// logout_trans
		temp = new LogoutDestPageSqlGenerator().getSql();
		if(temp==null) {
			return false;
		}
		sql += temp + "\n\n";

		// table_list
		TableListSqlGenerator tableListSg = new TableListSqlGenerator();
		sql += tableListSg.getSql();
		sql += "\n\n";

		// 各データテーブルとアカウントテーブル
		TableSqlGenerator tableSg = new TableSqlGenerator();
		sql += tableSg.getSql();
		sql += "\n\n";

		// field_info
		FieldInfoSqlGenerator fieldInfoSg = new FieldInfoSqlGenerator();
		sql += fieldInfoSg.getSql();
		sql += "\n\n";

		// roles
		RoleSqlGenerator rsg = new RoleSqlGenerator();
		sql += rsg.getSql();
		sql += "\n\n";

		// general_role
		GeneralRoleSqlGenerator grsg = new GeneralRoleSqlGenerator();
		sql += grsg.getSql();
		sql += "\n\n";

		// create_auth
		CreateAuthSqlGenerator createAuthSg = new CreateAuthSqlGenerator();
		sql += createAuthSg.getSql();
		sql += "\n\n";

		// delete_auth
		DeleteAuthSqlGenerator deleteAuthSg = new DeleteAuthSqlGenerator();
		sql += deleteAuthSg.getSql();
		sql += "\n\n";

		// field_auth
		FieldAuthSqlGenerator fieldAuthSg = new FieldAuthSqlGenerator();
		sql += fieldAuthSg.getSql();
		sql += "\n\n";

		// ia_assign
		IaAssignSqlGenerator iaAssignSg = new IaAssignSqlGenerator();
		sql += iaAssignSg.getSql();
		sql += "\n\n";

		// ia_possesion
		sql += new IaPossesionSqlGenerator().getSql() + "\n\n";

		// ia_assign_auth
		sql += new IaAssignAuthSqlGenerator().getSql() + "\n\n";

		// transition_auth
		sql += new TransAuthSqlGenerator().getSql() + "\n\n";

		// exwrite_lock
		ExWriteLockSqlGenerator exWriteLockSg = new ExWriteLockSqlGenerator();
		sql += exWriteLockSg.getSql();
		sql += "\n\n";

		// page_element
		PageElementSqlCreator pesc = new PageElementSqlCreator();
		sql += pesc.getSql();
		sql += "\n\n";

		// page_element_hyper_link
		PageElementHyerLinkSqlGenerator pehlsg = new PageElementHyerLinkSqlGenerator();
		sql += pehlsg.getSql();
		sql += "\n\n";
		
		// page_element_saif
		PageElementSaifSqlGenerator saifSg = new PageElementSaifSqlGenerator();
		sql += saifSg.getSql();
		sql += "\n\n";

		// page_element_da
		PageElementDisplayAreaSqlGenerator pedasg = new PageElementDisplayAreaSqlGenerator();
		sql += pedasg.getSql();
		sql += "\n\n";

		// page_element_login_form
		PageElementLoginFormSqlGenerator pelfsg = new PageElementLoginFormSqlGenerator();
		sql += pelfsg.getSql();
		sql += "\n\n";

		// page_element_text
		PageElementTextSqlGenerator petxsg = new PageElementTextSqlGenerator();
		sql += petxsg.getSql();
		sql += "\n\n";

		// page_element_table_display
		PageElementTableDisplaySqlGenerator petdsg = new PageElementTableDisplaySqlGenerator();
		sql += petdsg.getSql();
		sql += "\n\n";

		// page_element_table_display_offset
		PageElementTableDisplayOffsetSqlGenerator tableDisplayOffsetSg = new PageElementTableDisplayOffsetSqlGenerator();
		sql += tableDisplayOffsetSg.getSql();
		sql += "\n\n";

		// page_element_create_form
		PageElementCreateFormSqlGenerator createFormSg = new PageElementCreateFormSqlGenerator();
		sql += createFormSg.getSql();
		sql += "\n\n";

		// page_element_update_form
		PageElementUpdateFormSqlGenerator updateFormSg = new PageElementUpdateFormSqlGenerator();
		sql += updateFormSg.getSql();
		sql += "\n\n";

		// page_element_update_form_field
		PageElementUpdateFormFieldSqlGenerator updateFormFieldSg = new PageElementUpdateFormFieldSqlGenerator();
		sql += updateFormFieldSg.getSql();
		sql += "\n\n";

		// transition
		TransSqlGenerator tsg = new TransSqlGenerator();
		sql += tsg.getSql();
		sql += "\n\n";

		// tpp
		TransProcSqlGenerator tpsg = new TransProcSqlGenerator();
		sql += tpsg.getSql();
		sql += "\n\n";
		
		// tpp_et_load
		TppDdtLoadSqlGenerator ddtLoadSg = new TppDdtLoadSqlGenerator(); 
		sql += ddtLoadSg.getSql();
		sql += "\n\n";

		// tpp_update_form_reflection
		TppUpdateFormReflectionSqlGenerator updateFormReflectionSg = new TppUpdateFormReflectionSqlGenerator();
		sql += updateFormReflectionSg.getSql();
		sql += "\n\n";
		
		// tpp_create_form_reflection
		sql += new TppCreateFormReflectionSqlGenerator().getSql() + "\n\n";

		// tpp_create_reflection
		temp = new TppCreateReflectionSqlGenerator().getSql();
		if(temp==null) {
			return false;
		}
		sql += temp + "\n\n";

		// tpp_ia_reflection
		temp = new TppIaReflectionSqlGenerator().getSql();
		if(temp==null) {
			return false;
		}
		sql += temp + "\n\n";
		
		// tpp_service_execution
		TppServiceExecutionSqlGenerator servExeSg = new TppServiceExecutionSqlGenerator(); 
		sql += servExeSg.getSql();
		sql += "\n\n";

		// tpp_service_execution_argument
		TppServiceArgsSqlGenerator servArgsSg = new TppServiceArgsSqlGenerator(); 
		sql += servArgsSg.getSql();
		sql += "\n\n";

		// tpp_constant_array_int
		TppConstIntAndConstArrayIntSqlGenerator caiSg = new TppConstIntAndConstArrayIntSqlGenerator(); 
		sql += caiSg.getSql();
		sql += "\n\n";

		// tpp_constant_array_string
		TppConstStringAndConstArrayStringSqlGenerator casSg = new TppConstStringAndConstArrayStringSqlGenerator(); 
		sql += casSg.getSql();
		sql += "\n\n";

		/*
		 * ◆未実装：データベースの全テーブルを作成するためにSQLをどんどん付加していく
		 */
		
		try {
			File sqlFile = new File(SqlGenerator.SQL_FILE_LOCATION);
			if(!sqlFile.exists()) {
				sqlFile.createNewFile();
			}
			FileWriter out = new FileWriter(sqlFile);

			try {
				out.write(sql);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
