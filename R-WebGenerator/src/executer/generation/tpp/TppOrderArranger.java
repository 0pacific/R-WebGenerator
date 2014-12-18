package executer.generation.tpp;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import authority.*;
import debug.*;
import executer.generation.mysql.*;
import executer.generation.mysql.authority.*;
import executer.generation.mysql.pageElement.*;
import executer.generation.mysql.tpp.*;
import gui.*;
import gui.arrow.*;
import mainFrame.*;
import pageElement.*;
import role.*;
import table.*;
import table.field.*;
import test.*;
import tpp.*;
import tpp.service.*;
import tpp.port.*;
import tpp.portTrans.*;
import transition.*;
import utility.*;
import utility.serialize.Serializer;
import view.*;
import view.authEdit.*;
import view.generation.*;
import view.peEdit.*;
import view.roleEdit.*;
import view.tableList.*;
import view.tableList.fieldList.*;
import view.tableList.fieldList.fieldEdit.*;
import view.transProcEdit.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.webPageDefinition.*;
import webPage.*;





public class TppOrderArranger {
	public static int serviceArrangeCounterForDebug = 0;
	public static int createReflectionArrangeCounterForDebug = 0;
	public static int iaReflectionArrangeCounterForDebug = 0;
	
	
	
	public void arrange() {
		TransitionManager tm = TransitionManager.getInstance();
		for(int i=0; i<tm.getTransitionNum(); i++) {
			Transition trans = tm.getTransition(i);
			TransitionProcess transProc = trans.transProc;

			// デバッグ出力
			Debug.today(trans.getTransNumber()+"番の遷移プロセスのTPPを整列開始します。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			transProc.debugTppArray();
			
			arrangeTppArray(transProc);

			// デバッグ出力
			Debug.today(trans.getTransNumber()+"番の遷移プロセスのTPPを整列終了しました。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
			transProc.debugTppArray();
		}
	}

	
	
	
	public boolean arrangeTppArray(TransitionProcess transProc) {
		boolean anyModificationDone = false;

		if(arrangeServicesAndArgs(transProc)) {
			arrangeTppArray(transProc);
			return true;
		}
		else if(arrangeCreateReflection(transProc)) {
			arrangeTppArray(transProc);
			return true;
		}
		else if(arrangeIaReflection(transProc)) {
			arrangeTppArray(transProc);
			return true;
		}
		/*
		 * フォームリフレクション・・・常に先頭にあるはずなので処理は不要
		 */

		/*
		 * ◆未実装
		if(tpp instanceof TppDeleteReflection) {
		}
		else if(tpp instanceof TppUpdateReflection) {
		}
		*/

		return false;
	}
	



	public boolean arrangeServicesAndArgs(TransitionProcess transProc) {
		// デバッグ（この関数の実行回数をカウントする。再帰がループせずに無事終わると実行回数は0に戻る）
		TppOrderArranger.serviceArrangeCounterForDebug++;
		Debug.notice("arrangeServicesAndArgs()..."+TppOrderArranger.serviceArrangeCounterForDebug+"回目の実行です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());

		// ◆暫定
		if(serviceArrangeCounterForDebug==100) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "arrangeServicesAndArgs()の再帰呼出回数が100に達しているため、異常終了します。西村へ連絡して下さい。");
			System.exit(0);
		}
		
		// なんらかのサービスおよびその引数について、１度でもTPP順序の修正が行われたか否か
		// 行われたならば、今回の呼び出しでの処理はそこで放棄し、もう一度再帰呼び出しを行う。
		// 行われないままだとこの関数の最後まで行って終了するようになっている
		boolean anyModificationDone = false;
		
		for(int i=0; i<transProc.getTppNum(); i++) {
			TransitionProcessPart tpp = transProc.getTpp(i);

			// サービスを見つけた
			if(tpp instanceof Service) {
				Service service = (Service)tpp;
				
				// まず、最初の引数から最後の引数までが、連続して順番に現れるように整列する
				int argNum = service.getArgNum();
				for(int j=0; j<argNum; j++) {
					/*
					 * ◆TPPでなくページエレメントの可能性も一応ある？検討しよう
					 */
					
					TransitionProcessPart inputTppAsThisArg = service.getArgTppIfExists(j);
					if(inputTppAsThisArg==null) {	// 引数が未定義の場合
						JOptionPane.showMessageDialog(MainFrame.getInstance(),transProc.belongingTrans.getDescription()+"の遷移プロセスにおいて、サービス「"+service.serviceName+"」の第"+(j+1)+"引数が定義されていないようです。確認して下さい。");
						Debug.notice(transProc.belongingTrans.getDescription()+"の遷移プロセスにおいて、サービス「"+service.serviceName+"」の第"+j+"引数（0始まり）が定義されていないようです。", "TppOrderArranger", Thread.currentThread().getStackTrace()[1].getMethodName());
						return false;
					}

					Debug.notice("サービス「"+service.serviceName+"」の第"+j+"引数（0始まり）は、"+inputTppAsThisArg.getTppo()+"番（0始まり）にあるようです。", "TppOrderArranger", Thread.currentThread().getStackTrace()[1].getMethodName());

					if(j==0) {
						// 最初の引数はスルー
						continue;
					}
					else {
						// 前の引数として定義されているTPPのTPPOを取得
						TransitionProcessPart inputTppAsPrevArg = service.getArgTppIfExists(j-1);

						// もし◆前の引数よりも前にあるなら◆１つ後に移動してやる
						if(inputTppAsThisArg.getTppo()<inputTppAsPrevArg.getTppo()) {
							Debug.notice("サービス引数を移動します　サービス「" + service.serviceName + "」の第" + j + "引数（0始まり）を、" + inputTppAsThisArg.getTppo() + "番から" + (inputTppAsPrevArg.getTppo()+1) + "番へ（「番」は0始まり）", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());

							ArrayListElemMover.move(transProc.tppArray, inputTppAsThisArg.getTppo(), inputTppAsPrevArg.getTppo()+1);
							anyModificationDone = true;

							transProc.debugTppArray();
						}
					}
				}

				// サービス本体が◆最後の引数であるTPPよりも前にあるなら◆最後の引数であるTPPの１つ後ろに移動してやる
				TransitionProcessPart lastArgTpp = service.getArgTppIfExists(argNum-1);
				if(service.getTppo()<lastArgTpp.getTppo()) {
					Debug.notice("サービス本体を移動します　サービス「" + service.serviceName + "」を、" + service.getTppo() + "番から" + (lastArgTpp.getTppo()+1) + "番へ（「番」は0始まり）", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());

					ArrayListElemMover.move(transProc.tppArray, service.getTppo(), lastArgTpp.getTppo()+1);
					anyModificationDone = true;

					transProc.debugTppArray();
				}
			}
			
			if(!anyModificationDone) {
				// 修正が一度も行われなかったので、今回の関数呼び出しを行った瞬間（一番外側のfor文に入る前）のTPP順序はまだ生きている。よって普通に次のTPPを見ていく
				continue;
			}

			// 修正が一度でも行われた...
			// 今回の関数呼び出しを行った瞬間（一番外側のfor文に入る前）のTPP順序はもはや崩れている。
			// よって再帰呼び出しを行い、整列の続きはそっちに委ねる。それが終わったらreturnするだけ。
			// つまり、今TPPを順番に見てきたが、今回の関数実行ではそれを放棄し、次回以降の関数実行でまた最初のTPPから見ていってもらうわけである。
			arrangeServicesAndArgs(transProc);

			// 修正が行われたことを伝えるためにtrueを返す（下の方に書いてある通り、ここでtrueを返すのも「１回目の実行」である場合のみ意味を持つ
			return true;
		}

		
		/*
		 * 最初のTPPから最後のTPPまで、修正が一度も行われなかった場合だけここに辿りつく
		 */
		
		// デバッグ（再帰が無事終わったので、カウンタを０に戻す。これで別の遷移プロセスについて整列するときに正しくカウントできる）
		TppOrderArranger.serviceArrangeCounterForDebug = 0;

		// 「最初のTPPから最後のTPPまで、修正が一度も行われなかった」ことを伝えるためにfalseを返しているが、
		// これは今回の実行が「１回目の実行」である場合、つまり再帰呼出が１度も行われなかった場合にのみ意味を持つ。
		// そのケースではまさに「遷移プロセス中のサービスと引数について修正が一度も行われていない」のだが、
		// 再帰的に実行している場合には、それより前の実行で修正が生じているのだから、ここでfalseを返してもそれより前の実行でtrueが返されるわけである。
		return false;
	}







	public boolean arrangeCreateReflection(TransitionProcess transProc) {
		// デバッグ（この関数の実行回数をカウントする。再帰がループせずに無事終わると実行回数は0に戻る）
		TppOrderArranger.createReflectionArrangeCounterForDebug++;
		Debug.notice("arrangeCreateReflection()..."+TppOrderArranger.createReflectionArrangeCounterForDebug+"回目の実行です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// Createリフレクションについて、１度でもTPP順序の修正が行われたか否か
		// 行われたならば、今回の呼び出しでの処理はそこで放棄し、もう一度再帰呼び出しを行う。
		// 行われないままだとこの関数の最後まで行って終了するようになっている
		boolean anyModificationDone = false;
		
		for(int i=0; i<transProc.getTppNum(); i++) {
			TransitionProcessPart tpp = transProc.getTpp(i);

			// Createリフレクションを見つけた
			if(tpp instanceof TppCreateReflection) {
				TppCreateReflection createReflection = (TppCreateReflection)tpp;
				TfdOutputer tfdOutputer = createReflection.getInputTfdOutputerIfExists();
				if(tfdOutputer==null) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), transProc.belongingTrans.getDescription()+"の遷移プロセスにおいて、「レコード作成処理」に渡す表データが定義されていないようです。確認して下さい。");
					return false;
				}
				
				// キャスト
				TransitionProcessPart tfdTpp = (TransitionProcessPart)tfdOutputer;
				
				// Createリフレクション本体が、入力TFDとして定義されたTPPよりも前にあるなら、１つ後ろに移動してやる
				int tfdTppo = tfdTpp.getTppo();
				if(createReflection.getTppo()<tfdTppo) {
					ArrayListElemMover.move(transProc.tppArray, createReflection.getTppo(), tfdTppo+1);
					anyModificationDone = true;

					Debug.notice("TPPを移動　Createリフレクションを、" + createReflection.getTppo() + "番から" + (tfdTppo+1) + "番へ", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				}
			}
			
			if(!anyModificationDone) {
				// 修正が一度も行われなかったので、今回の関数呼び出しを行った瞬間（一番外側のfor文に入る前）のTPP順序はまだ生きている。よって普通に次のTPPを見ていく
				continue;
			}

			// 修正が一度でも行われた...
			// 今回の関数呼び出しを行った瞬間（一番外側のfor文に入る前）のTPP順序はもはや崩れている。
			// よって再帰呼び出しを行い、整列の続きはそっちに委ねる。それが終わったらreturnするだけ。
			// つまり、今TPPを順番に見てきたが、今回の関数実行ではそれを放棄し、次回以降の関数実行でまた最初のTPPから見ていってもらうわけである。
			arrangeCreateReflection(transProc);

			// 修正が行われたことを伝えるためにtrueを返す（下の方に書いてある通り、ここでtrueを返すのも「１回目の実行」である場合のみ意味を持つ
			return true;
		}

		
		/*
		 * 最初のTPPから最後のTPPまで、修正が一度も行われなかった場合だけここに辿りつく
		 */
		
		// デバッグ（再帰が無事終わったので、カウンタを０に戻す。これで別の遷移プロセスについて整列するときに正しくカウントできる）
		TppOrderArranger.createReflectionArrangeCounterForDebug = 0;

		// 「最初のTPPから最後のTPPまで、修正が一度も行われなかった」ことを伝えるためにfalseを返しているが、
		// これは今回の実行が「１回目の実行」である場合、つまり再帰呼出が１度も行われなかった場合にのみ意味を持つ。
		// そのケースではまさに「遷移プロセス中のサービスと引数について修正が一度も行われていない」のだが、
		// 再帰的に実行している場合には、それより前の実行で修正が生じているのだから、ここでfalseを返してもそれより前の実行でtrueが返されるわけである。
		return false;
	}






	public boolean arrangeIaReflection(TransitionProcess transProc) {
		// デバッグ（この関数の実行回数をカウントする。再帰がループせずに無事終わると実行回数は0に戻る）
		TppOrderArranger.iaReflectionArrangeCounterForDebug++;
		Debug.notice("arrangeIaReflection()..."+TppOrderArranger.iaReflectionArrangeCounterForDebug+"回目の実行です。", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// IAリフレクションについて、１度でもTPP順序の修正が行われたか否か
		// 行われたならば、今回の呼び出しでの処理はそこで放棄し、もう一度再帰呼び出しを行う。
		// 行われないままだとこの関数の最後まで行って終了するようになっている
		boolean anyModificationDone = false;
		
		for(int i=0; i<transProc.getTppNum(); i++) {
			TransitionProcessPart tpp = transProc.getTpp(i);

			// IAリフレクションを見つけた
			if(tpp instanceof TppIAReflection) {
				TppIAReflection iaReflection = (TppIAReflection)tpp;

				// データテーブルTFDとして定義されたTPPを取得
				TfdOutputer dtTfdOutputer = iaReflection.getTfdOutputerAsDataTableTfdIfExists();
				if(dtTfdOutputer==null) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), transProc.belongingTrans.getDescription()+"の遷移プロセスにおいて、「個人権限アサイン処理」に渡すデータテーブル表データが定義されていないようです。確認して下さい。");
					return false;
				}
				
				// キャスト
				TransitionProcessPart dtTfdTpp = (TransitionProcessPart)dtTfdOutputer;
				
				// IAリフレクション本体が、データテーブルTFDとして定義されたTPPよりも前にあるなら、１つ後ろに移動してやる
				int dtTfdTppo = dtTfdTpp.getTppo();
				if(iaReflection.getTppo()<dtTfdTppo) {
					ArrayListElemMover.move(transProc.tppArray, iaReflection.getTppo(), dtTfdTppo+1);
					anyModificationDone = true;

					Debug.notice("TPPを移動　IAリフレクションを、" + iaReflection.getTppo() + "番から" + (dtTfdTppo+1) + "番へ", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				}


				// アカウントテーブルTFDとして定義されたTPPを取得
				TfdOutputer atTfdOutputer = iaReflection.getTfdOutputerAsAccountTableTfdIfExists();
				if(atTfdOutputer==null) {
					JOptionPane.showMessageDialog(MainFrame.getInstance(), transProc.belongingTrans.getDescription()+"の遷移プロセスにおいて、「個人権限アサイン処理」に渡すアカウントテーブル表データが定義されていないようです。確認して下さい。");
					return false;
				}
				
				// キャスト
				TransitionProcessPart atTfdTpp = (TransitionProcessPart)atTfdOutputer;
				
				// IAリフレクション本体が、アカウントテーブルTFDとして定義されたTPPよりも前にあるなら、１つ後ろに移動してやる
				int atTfdTppo = atTfdTpp.getTppo();
				if(iaReflection.getTppo()<atTfdTppo) {
					ArrayListElemMover.move(transProc.tppArray, iaReflection.getTppo(), atTfdTppo+1);
					anyModificationDone = true;

					Debug.notice("TPPを移動　IAリフレクションを、" + iaReflection.getTppo() + "番から" + (atTfdTppo+1) + "番へ", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
				}
			}
			
			if(!anyModificationDone) {
				// 修正が一度も行われなかったので、今回の関数呼び出しを行った瞬間（一番外側のfor文に入る前）のTPP順序はまだ生きている。よって普通に次のTPPを見ていく
				continue;
			}

			// 修正が一度でも行われた...
			// 今回の関数呼び出しを行った瞬間（一番外側のfor文に入る前）のTPP順序はもはや崩れている。
			// よって再帰呼び出しを行い、整列の続きはそっちに委ねる。それが終わったらreturnするだけ。
			// つまり、今TPPを順番に見てきたが、今回の関数実行ではそれを放棄し、次回以降の関数実行でまた最初のTPPから見ていってもらうわけである。
			arrangeIaReflection(transProc);

			// 修正が行われたことを伝えるためにtrueを返す（下の方に書いてある通り、ここでtrueを返すのも「１回目の実行」である場合のみ意味を持つ
			return true;
		}

		
		/*
		 * 最初のTPPから最後のTPPまで、修正が一度も行われなかった場合だけここに辿りつく
		 */
		
		// デバッグ（再帰が無事終わったので、カウンタを０に戻す。これで別の遷移プロセスについて整列するときに正しくカウントできる）
		TppOrderArranger.iaReflectionArrangeCounterForDebug = 0;

		// 「最初のTPPから最後のTPPまで、修正が一度も行われなかった」ことを伝えるためにfalseを返しているが、
		// これは今回の実行が「１回目の実行」である場合、つまり再帰呼出が１度も行われなかった場合にのみ意味を持つ。
		// そのケースではまさに「遷移プロセス中のサービスと引数について修正が一度も行われていない」のだが、
		// 再帰的に実行している場合には、それより前の実行で修正が生じているのだから、ここでfalseを返してもそれより前の実行でtrueが返されるわけである。
		return false;
	}
}






















