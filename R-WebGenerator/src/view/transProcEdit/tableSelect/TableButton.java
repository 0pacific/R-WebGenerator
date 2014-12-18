package view.transProcEdit.tableSelect;

import gui.*;
import gui.arrow.*;

import table.*;
import tpp.*;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.serviceSelect.FrameForServiceSelection;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class TableButton extends JButton {
	private SuperTable table;

	public TableButton(SuperTable table) {
		this.table = table;
		setText(table.getTableName());
	}

	public SuperTable getTable() {
		return this.table;
	}
}
