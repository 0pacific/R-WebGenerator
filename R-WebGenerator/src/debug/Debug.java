package debug;

import java.io.Serializable;

import javax.swing.JOptionPane;

import property.GeneratorProperty;

import mainFrame.MainFrame;

public class Debug implements Serializable {
	
	
	
	
	
	public static void out(Object msg) {
		System.out.println(msg);
	}

	public static void out(Object msg1, Object msg2) {
		Debug.out(msg1);
		Debug.out(msg2);
	}

	public static void out(Object msg, String className, String funcName) {
		Debug.out(msg.toString() + "..." + className + "::" + funcName + "()");
	}

	public static void debug_call(String className, String funcName) {
		Debug.out("CALL : " + className + "::" + funcName + "()");
	}
	
	public static void debug_return(String className, String funcName) {
		Debug.out("RETURN : " + className + "::" + funcName + "()");
	}

	public static void test() {
		Debug.out("test");
	}

	public static void test(int i) {
		Debug.out("test" + i);
	}

	public static void error() {
		Debug.out("◆ERROR◆");
	}

	public static void error(String msg) {
		Debug.out("◆ERROR◆　" + msg);
	}

	public static void error(Object msg, String className, String funcName) {
		Debug.out("◆ERROR◆　" + msg.toString(), className, funcName);
	}

	public static void notice(Object msg, String className, String funcName) {
		Debug.out("◆NOTICE◆　" + msg.toString(), className, funcName);
	}

	public static void today(Object msg, String className, String funcName) {
		Debug.out("◆TODAY◆　" + msg.toString(), className, funcName);
	}

	public static void varDump(Object msg, String className, String funcName) {
		Debug.out("\n◆VARDUMP開始◆\n" + msg.toString() + "\n◆VARDUMP終了◆\n", className, funcName);
	}

	public static void informError() {
		boolean japanese = GeneratorProperty.japanese();
		JOptionPane.showMessageDialog(MainFrame.getInstance(), japanese?"！エラーが発生しました":"!Unexpected error has occured.", japanese?"エラー":"Error", JOptionPane.ERROR_MESSAGE);
	}
}
