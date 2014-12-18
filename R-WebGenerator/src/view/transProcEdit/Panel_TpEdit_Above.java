package view.transProcEdit;

import gui.*;
import gui.arrow.*;
import pageElement.PageElement;
import pageElement.PageElementCreateForm;
import pageElement.PageElementDisplayArea;
import pageElement.PageElementSaif;
import pageElement.PageElementTableDisplay;

import table.*;
import table.field.Field;
import tpp.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.*;

import mainFrame.MainFrame;
import debug.Debug;

import tpp.*;
import tpp.port.DataPort;
import tpp.port.PageElementPort;
import tpp.port.TppPort;
import tpp.portTrans.PortTrans;
import tpp.portTrans.PortTransManager;
import tpp.service.Service;
import transition.Transition;
import transition.TransitionManager;
import transition.TransitionProcess;
import view.EditPanel;
import view.transProcEdit.editPanel.CreateFormReflectionPanel;
import view.transProcEdit.editPanel.CreateReflectionPanel;
import view.transProcEdit.editPanel.EndPagePanel;
import view.transProcEdit.editPanel.IaReflectionPanel;
import view.transProcEdit.editPanel.PageElementPanel;
import view.transProcEdit.editPanel.ServicePanel;
import view.transProcEdit.editPanel.StartPagePanel;
import view.transProcEdit.editPanel.TableReadingPanel;
import view.transProcEdit.editPanel.TpPagePanel;
import view.transProcEdit.editPanel.UpdateFormReflectionPanel;
import view.transProcEdit.rightSubPanels.PanelCreateReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelIaReflectionEdit;
import view.transProcEdit.rightSubPanels.PanelServiceArgEdit;
import view.transProcEdit.serviceArgsWindow.*;
import view.transProcEdit.subFrame.Frame_TransProcEdit_Sub;
import view.transProcEdit.subFrame.Panel_TransProcEdit_Sub;
import webPage.WebPage;
import utility.*;

/*
 * Singleton
 */
public class Panel_TpEdit_Above extends JPanel implements MouseListener,Serializable {
	public TransitionProcess editingTp;
	private StartPagePanel startPagePanel;
	private EndPagePanel endPagePanel;

	public static int special_flag = 0;
	
	// SprintLayout
	public SerializableSpringLayout layout;
	
	private JPanel selectedInputPanel = null;
	private JPanel selectedOutputPanel = null;
	
	// �T�[�r�X�p�l���z��
	private ArrayList<ServicePanel> csPanels = new ArrayList<ServicePanel>();
	// �e�[�u�����[�f�B���O�p�l���z��
	private ArrayList<TableReadingPanel> tblReadPanels = new ArrayList<TableReadingPanel>();
	// CreateForm���t���N�V�����p�l���z��
	public ArrayList<CreateFormReflectionPanel> panelArrayCreateFormReflection = new ArrayList<CreateFormReflectionPanel>();
	// Create���t���N�V�����p�l���z��
	public ArrayList<CreateReflectionPanel> panelArrayCreateReflection = new ArrayList<CreateReflectionPanel>();
	// UpdateForm���t���N�V�����p�l���z��
	public ArrayList<UpdateFormReflectionPanel> panelArrayUpdateFormReflection = new ArrayList<UpdateFormReflectionPanel>();
	// IA���t���N�V�����p�l���z��
	public ArrayList<IaReflectionPanel> panelArrayIaReflection = new ArrayList<IaReflectionPanel>();
	
	public final Color bgColor = Color.WHITE;
	
	// ��ݍ��ރX�N���[���y�C���̃T�C�Y
	public static final int spWidth = 840;

	// �p�l���T�C�Y
	public static final int panelWidth = 3000;
	public static final int panelHeight = 450;

	// �p�l������ɕ\�����镶����̃T�C�Y
	public static final int PAN_STR_SIZE = 12;
	
	// �T�[�r�X�p�l����e�[�u�����[�f�B���O�p�l���Ȃǂ̏�Ń}�E�X�v���X�������ہA�����̍��W�i���㒸�_����̑��΍��W�j���i�[
	// �}�E�X�����[�X���A���̒l���g���ړ�
	private int pressX;
	private int pressY;

	// ���[�h��\���萔
	public static final int MODE_NORMAL = 0;
	public static final int MODE_SELECT_TFD = 1;
	public static final int MODE_SELECT_OUTPUT_TFD = 2;
	public static final int MODE_SELECT_CREATE_REFLECTION_INPUT_TFD = 3;
	public static final int MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD = 4;
	public static final int MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD = 5;
	public static final int MODE_SELECT_SERVICE_ARG_INT = 6;
	public static final int MODE_SELECT_SERVICE_ARG_VARCHAR = 7;
	public static final int MODE_SELECT_SERVICE_ARG_DATETIME = 8;
	public static final int MODE_SELECT_SERVICE_ARG_DATE = 9;
	public static final int MODE_SELECT_SERVICE_ARG_TIME = 10;
	
	// ���[�h������
	public int mode = MODE_NORMAL;
	
	/*
	 * �C���X�^���X�͍Ō�I
	 */
	private static Panel_TpEdit_Above obj = new Panel_TpEdit_Above();
	
	
	
	

	
	
	
	
	private Panel_TpEdit_Above() {
		setLayout(null);
		setBackground(Color.WHITE);

		addMouseListener(this);

	    // KeyListener�͌����_�ŕ����R���|�[�l���g�Ɏ������Ă���
		setFocusable(true);	// KeyListener��ǉ�����Ƃ�����Ƃ�����Ȃ��Ƃ�
	}


		

	
	/*
	 * PURPOSE :
	 * TPP��\���e�p�l���̍Ĕz�u
	 */
	public void locateTppPanels() {
		// �S�R���|�[�l���g����
		removeAll();
		
		// �J�ڌ��E�J�ڐ�y�[�W�p�l���ȊO�̔z�u
		for(int i=0; i<editingTp.getTppNum(); i++) {
			TransitionProcessPart tpp = editingTp.getTpp(i);

			// TPP�u�T�[�r�X�ďo�v -> �T�[�r�X�p�l���z�u
			if(tpp instanceof Service) {
				ServicePanel csPanel = getCsPanelByCs((Service)tpp);
				csPanel.setBounds(csPanel.getPosX(), csPanel.getPosY(), ServicePanel.PANEL_WIDTH, ServicePanel.PANEL_HEIGHT);
				Debug.out("ServicePanel��add���܂��B x:" + csPanel.getPosX() + " y:" + csPanel.getPosY());
				add(csPanel);
			}
			// TPP�u�e�[�u�����[�f�B���O�v -> �e�[�u�����[�f�B���O�p�l���̔z�u
			else if(tpp instanceof TppTableReading) {
				TableReadingPanel trp = getTrPanelByTr((TppTableReading)tpp);
				trp.setBounds(trp.getPosX(), trp.getPosY(), TableReadingPanel.PANEL_WIDTH, TableReadingPanel.PANEL_HEIGHT);
				add(trp);
			}
			// CreateForm���t���N�V�����̃p�l���z�u
			else if(tpp instanceof TppCreateFormReflection) {
				CreateFormReflectionPanel cfrp = getCfrPanelByCfr((TppCreateFormReflection)tpp);
				cfrp.setBounds(cfrp.getPosX(), cfrp.getPosY(), CreateFormReflectionPanel.PANEL_WIDTH, CreateFormReflectionPanel.PANEL_HEIGHT);
				add(cfrp);
			}
			// Create���t���N�V�����̃p�l���z�u
			else if(tpp instanceof TppCreateReflection) {
				CreateReflectionPanel crp = getCrPanelByCr((TppCreateReflection)tpp);
				crp.setBounds(crp.getPosX(), crp.getPosY(), CreateReflectionPanel.PANEL_WIDTH, CreateReflectionPanel.PANEL_HEIGHT);
				add(crp);
			}
			// UpdateForm���t���N�V�����̃p�l���z�u
			else if(tpp instanceof TppUpdateFormReflection) {
				UpdateFormReflectionPanel ufrp = getUfrPanelByUfr((TppUpdateFormReflection)tpp);
				ufrp.setBounds(ufrp.getPosX(), ufrp.getPosY(), UpdateFormReflectionPanel.PANEL_WIDTH, UpdateFormReflectionPanel.PANEL_HEIGHT);
				add(ufrp);
			}
			// IA���t���N�V�����̃p�l���z�u
			else if(tpp instanceof TppIAReflection) {
				IaReflectionPanel irp = getIrPanelByIr((TppIAReflection)tpp);
				irp.setBounds(irp.getPosX(), irp.getPosY(), IaReflectionPanel.PANEL_WIDTH, IaReflectionPanel.PANEL_HEIGHT);
				add(irp);
				irp.updateTextPaneText();
			}
		}

		// �J�ڌ��y�[�W�p�l���̔z�u
		WebPage startPage = editingTp.getStartPage();
		startPagePanel = new StartPagePanel(startPage);
		int startPagePanelHeight = startPagePanel.calcHeight();
		startPagePanel.setBounds(startPagePanel.getPosX(), startPagePanel.getPosY(), TpPagePanel.panelWidth, startPagePanelHeight);
		add(startPagePanel);
		startPagePanel.addMouseListener(this);
		startPagePanel.locatePePanels();
		startPagePanel.setPePanelsPos();

		// �J�ڐ�y�[�W�p�l���̔z�u
		WebPage endPage = editingTp.getEndPage();
		endPagePanel = new EndPagePanel(endPage);
		int endPagePanelHeight = endPagePanel.calcHeight();
		endPagePanel.setBounds(endPagePanel.getPosX(), endPagePanel.getPosY(), TpPagePanel.panelWidth, endPagePanelHeight);
		add(endPagePanel);
		endPagePanel.addMouseListener(this);
		endPagePanel.locatePePanels();
		endPagePanel.setPePanelsPos();

		// �J�ڌ��E�J�ڐ�̑SPageElement�p�l���ցA�}�E�X���X�i�ǉ�
		for(int i=0; i<startPagePanel.pePanels.size(); i++) {
			PageElementPanel pep = startPagePanel.pePanels.get(i);
			pep.removeMouseListener(this);
			pep.addMouseListener(this);
		}
		for(int i=0; i<endPagePanel.pePanels.size(); i++) {
			PageElementPanel pep = endPagePanel.pePanels.get(i);
			pep.removeMouseListener(this);
			pep.addMouseListener(this);
		}

		// �W�F�l���[�^�p�l���ĕ`��E�Č���
		MainFrame.repaintAndValidate();
	}
	
	

	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;

		// �w�i�h��ׂ�
		g2.setColor(bgColor);
		g2.draw(new Line2D.Double(0, 0, getWidth(), getHeight()));

		// �|�[�g�g�����X�~�b�V������\������`��
		DirectionDecider dd = new DirectionDecider();
		PortTransManager ptm = editingTp.portTransManager;
		for(int i=0; i<ptm.getPortTransNum(); i++) {
			PortTrans pt = ptm.getPortTrans(i);
			DataPort startPort = pt.getStartPort();
			DataPort endPort = pt.getEndPort();

			
			
			EditPanel startPanel = null;
			if(startPort instanceof PageElementPort) {
				PageElement startPe = ((PageElementPort) startPort).getPageElement();
				startPanel = getEditPanelByPe(startPe);
			}
			else if(startPort instanceof TppPort) {
				TransitionProcessPart startTpp = ((TppPort) startPort).getTpp();
				
				// ���m�萔�A�萔�z���TPP......�p�l���ŕ\�����Ȃ��̂Ŗ����s�v -> ����
				if(startTpp instanceof TppConstInt || startTpp instanceof TppConstString || startTpp instanceof TppConstArrayInt || startTpp instanceof TppConstArrayString) {
					continue;
				}
				startPanel = getEditPanelByTpp(startTpp);
			}

			
			
			EditPanel endPanel = null;
			if(endPort instanceof PageElementPort) {
				PageElement endPe = ((PageElementPort) endPort).getPageElement();
				endPanel = getEditPanelByPe(endPe);
			}
			else if(endPort instanceof TppPort) {
				TransitionProcessPart endTpp = ((TppPort) endPort).getTpp();
				if(endTpp instanceof TppConstArrayInt) {
					continue;
				}
				endPanel = getEditPanelByTpp(endTpp);
			}
			
			
			
			// ���쐬
			Point[] pointPair = dd.decide(startPanel, endPanel);
			Point startPoint = pointPair[0];
			Point endPoint = pointPair[1];
			ArrowShape arrowShape = new ArrowShape(startPoint, endPoint);

			
			
			// ���`��
			BasicStroke wideStroke = new BasicStroke(2.0f);
			g2.setColor(Color.LIGHT_GRAY);
			g2.setStroke(wideStroke);
			g2.draw(arrowShape);
		}
	}
	
	
	
	
	
	public EditPanel getEditPanelByPe(PageElement pe) {
		// �J�ڌ��y�[�W��PageElementPanel��T��
		ArrayList<PageElementPanel> startPagePePanels = startPagePanel.pePanels;
		for(int i=0; i<startPagePePanels.size(); i++) {
			PageElementPanel pep = startPagePePanels.get(i);
			if(pep.pageElement==pe) {
				return pep;
			}
		}
		// �J�ڐ�y�[�W��PageElementPanel��T��
		ArrayList<PageElementPanel> endPagePePanels = endPagePanel.pePanels;
		for(int i=0; i<endPagePePanels.size(); i++) {
			PageElementPanel pep = endPagePePanels.get(i);
			if(pep.pageElement==pe) {
				return pep;
			}
		}

		// �G���[
		Debug.error("Panel_TpEdit_Left getEditPanelByPe() : PageElementPanel��������܂���");
		return null;
	}
	

	
	
	
	
	/*
	 * SUMMARY :
	 * �w�肵��TPP��\��EditPanel���擾
	 * �|�[�g�g�����X�~�b�V������\������`�悷��ۂɌĂяo��
	 */
	public EditPanel getEditPanelByTpp(TransitionProcessPart tpp) {
		for(int i=0; i<csPanels.size(); i++) {
			ServicePanel csp = csPanels.get(i);
			Service service = csp.getService();
			if(tpp==service) {
				return csp;
			}
		}
		for(int i=0; i<tblReadPanels.size(); i++) {
			TableReadingPanel trp = tblReadPanels.get(i);
			TppTableReading tr = trp.getTblRead();
			if(tpp==tr) {
				return trp;
			}
		}
		for(int i=0; i<this.panelArrayCreateFormReflection.size(); i++) {
			CreateFormReflectionPanel cfrp = panelArrayCreateFormReflection.get(i);
			TppCreateFormReflection cfr = cfrp.reflection;
			if(tpp==cfr) {
				return cfrp;
			}
		}
		for(int i=0; i<this.panelArrayCreateReflection.size(); i++) {
			CreateReflectionPanel crp = panelArrayCreateReflection.get(i);
			TppCreateReflection cr = crp.reflection;
			if(tpp==cr) {
				return crp;
			}
		}
		for(int i=0; i<this.panelArrayUpdateFormReflection.size(); i++) {
			UpdateFormReflectionPanel ufrp = panelArrayUpdateFormReflection.get(i);
			TppUpdateFormReflection ufr = ufrp.reflection;
			if(tpp==ufr) {
				return ufrp;
			}
		}
		for(int i=0; i<this.panelArrayIaReflection.size(); i++) {
			IaReflectionPanel irp = panelArrayIaReflection.get(i);
			TppIAReflection ir = irp.reflection;
			if(tpp==ir) {
				return irp;
			}
		}
		
		// �G���[
		JOptionPane.showMessageDialog(this, "�G���[���������܂����B");
		Debug.error("Tpp�p�l����������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}
	
	
	
	/*
	 * PURPOSE :
	 * �w��T�[�r�X��J�ڃv���Z�X�֒ǉ��A�����ăT�[�r�X�p�l����J�ڃv���Z�X�}�֒ǉ�
	 */
	public void addServiceAndServicePanelByServiceName(String serviceName) {
		Service service = Service.getInstanceByServiceName(editingTp, serviceName);

		// �ҏW����TransitionProcess�ɃT�[�r�X��ǉ�
		editingTp.addTpp(service);

		addServicePanelByService(service, 200, 300);

		// TPP�p�l�����Ĕz�u
		locateTppPanels();
	}
	
	
	
	
	
	public void addServicePanelByService(Service service, int panelPosX, int panelPosY) {
		ServicePanel csp = new ServicePanel(service);
		csp.setPosX(panelPosX);
		csp.setPosY(panelPosY);
		csp.addMouseListener(this);
		csPanels.add(csp);
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �w�肵���e�[�u������e�[�u�����[�f�B���OTPP���쐬���ҏW���J�ڃv���Z�X�֒ǉ��A�e�[�u�����[�f�B���O�p�l����J�ڃv���Z�X�}�֒ǉ�����
	 */
	public void addTableReadingAndTableReadingPanelByTable(SuperTable table) {
		// TPP�u�e�[�u�����[�f�B���O�v�̃C���X�^���X
		TppTableReading tr = new TppTableReading(editingTp, table);

		// �e�[�u�����[�f�B���O��ҏW���J�ڃv���Z�X�֒ǉ�
		editingTp.addTpp(tr);

		addTableReadingPanelByTableReading(tr, 30, 100);
		
		// TPP�p�l�����Ĕz�u
		locateTppPanels();
	}
	public void addTableReadingPanelByTableReading(TppTableReading tr, int panelPosX, int panelPosY) {
		TableReadingPanel trp = new TableReadingPanel(tr);
		trp.setPosX(panelPosX);
		trp.setPosY(panelPosY);
		trp.addMouseListener(this);
		tblReadPanels.add(trp);
	}
	
	
	
	
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �w�肵��Create�t�H�[���Ɋ�Â��Ă��쐬���A�ҏW���J�ڃv���Z�X�֒ǉ��ACreateFormReflection�p�l����J�ڃv���Z�X�}�֒ǉ�����
	 */
	public void addCreateFormReflectionPanel(TppCreateFormReflection reflection) {
		CreateFormReflectionPanel cfrp = new CreateFormReflectionPanel(reflection);
		cfrp.setPosX(50);
		cfrp.setPosY(200);
		cfrp.addMouseListener(this);
		panelArrayCreateFormReflection.add(cfrp);
		
		// TPP�p�l���Ĕz�u�͕s�v�i���̃��\�b�h���Ă΂��̂�Create�t�H�[�����ǉ����ꂽ�Ƃ��ŁA���̂Ƃ��ɑJ�ڃv���Z�X��`��ʂ͊J����Ă��Ȃ����߁j
	}
	
	
	
	
	
	/*
	 * PURPOSE :
	 * �w�肵��Update�t�H�[�����t���N�V������\���p�l���쐬�A�J�ڃv���Z�X�֒ǉ�
	 */
	public void addUpdateFormReflectionPanel(TppUpdateFormReflection reflection) {
		UpdateFormReflectionPanel ufrp = new UpdateFormReflectionPanel(reflection);
		ufrp.setPosX(100);
		ufrp.setPosY(200);
		ufrp.addMouseListener(this);
		panelArrayUpdateFormReflection.add(ufrp);
		
		// TPP�p�l���Ĕz�u�͕s�v�i���̃��\�b�h���Ă΂��̂�Update�t�H�[�����ǉ����ꂽ�Ƃ��ŁA���̂Ƃ��ɑJ�ڃv���Z�X��`��ʂ͊J����Ă��Ȃ����߁j
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �w�肳�ꂽCreate���t���N�V�����ɑΉ�����CreateReflection�p�l�����A�ҏW���̑J�ڃv���Z�X�}�֒ǉ�����
	 */
	public void addCreateReflectionPanel(TppCreateReflection reflection, int panelPosX, int panelPosY) {
		CreateReflectionPanel crp = new CreateReflectionPanel(reflection);
		crp.setPosX(panelPosX);
		crp.setPosY(panelPosY);
		crp.addMouseListener(this);
		panelArrayCreateReflection.add(crp);
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �w�肳�ꂽIA���t���N�V������\��IaReflection�p�l�����쐬���A�ҏW���̑J�ڃv���Z�X�}�֒ǉ�����
	 */
	public void addIaReflectionPanel(TppIAReflection reflection, int panelPosX, int panelPosY) {
		IaReflectionPanel irp = new IaReflectionPanel(reflection);
		irp.setPosX(panelPosX);
		irp.setPosY(panelPosY);
		irp.addMouseListener(this);
		panelArrayIaReflection.add(irp);
	}
	


	
	
	
	/*
	 * PURPOSE :
	 * �}�E�X�v���X�C�x���g�n���h��
	 * �ړ��\�ȃp�l�������v���X�����ہA���̑��΍��W�i���㒸�_����̍��W�j���L�^����
	 */
	public void mousePressed(MouseEvent e) {
		Object source = e.getSource();

		// �ړ��ł���R���|�[�l���g��Ńv���X���ꂽ�̂��ǂ����A���`�F�b�N
		boolean movable = source instanceof EditPanel
							&& !(source instanceof PageElementPanel);
		
		// �ړ��ł��Ȃ��R���|�[�l���g�������Ȃ�A�������Ȃ��Ă悢
		if(!movable) {
			return;
		}

		// �R���|�[�l���g�̈ړ��͒ʏ탂�[�h�ł̂݉\
		if(mode!=MODE_NORMAL) {
			return;
		}

		pressX = e.getX();
		pressY = e.getY();
	}

	
	
	
	
	/*
	 * PURPOSE :
	 * �}�E�X�����[�X�C�x���g�n���h��
	 * �ړ��\�ȃp�l�����������[�X���ꂽ�ہA�v���X�ʒu���΍��W�ƃ����[�X�ʒu���΍��W����X,Y�̈ړ��������v�Z���A�ړ�������
	 * �ړ�������Ƃ͂��Ȃ킿�APanel_TpEdit_Left��ɂ������΍��W��ύX����Ƃ�������
	 */
	public void mouseReleased(MouseEvent e) {
		JPanel source = (JPanel)e.getSource();

		// �ړ��ł���R���|�[�l���g��Ńv���X���ꂽ�̂��ǂ����A���`�F�b�N
		boolean movable = source instanceof EditPanel;

		// �ړ��ł��Ȃ��R���|�[�l���g�������Ȃ�A�������Ȃ��Ă悢
		if(!movable) {
			return;
		}

		// �R���|�[�l���g�̈ړ��͒ʏ탂�[�h�ł̂݉\
		if(mode!=MODE_NORMAL) {
			return;
		}

		
		
		/*
		 * ���̎��_�ŁAEditPanel�ɑ΂��ă}�E�X�����[�X���s��ꂽ�̂͊m��
		 */

		
		
		// �v���X���ꂽ�R���|�[�l���g�́A���ꂩ��ړ�����̂ň�U����
		remove(source);
		
		// �����[�X�ʒu���΍��W�i�ړ�����R���|�[�l���g�́A���㒸�_����̍��W�j
		int rlsX = e.getX();
		int rlsY = e.getY();

		// X,Y�����̈ړ�����
		int xDiff = rlsX - pressX;
		int yDiff = rlsY - pressY;

		
		// �J�ڌ��E�J�ڐ�y�[�W�p�l���̏ꍇ
		if(source instanceof TpPagePanel) {
			TpPagePanel tpPagePanel = (TpPagePanel)source;
			tpPagePanel.setPosX(tpPagePanel.getPosX() + xDiff);
			tpPagePanel.setPosY(tpPagePanel.getPosY() + yDiff);
			tpPagePanel.setBounds(tpPagePanel.getPosX(), tpPagePanel.getPosY(),
							TpPagePanel.panelWidth, tpPagePanel.calcHeight());
			add(tpPagePanel);

			// �S�y�[�W�G�������g�p�l���́A�w�ʃp�l�����㒸�_����̍��W���A�Đݒ肷��
			tpPagePanel.setPePanelsPos();
		}
		// �y�[�W�\���v�f�p�l�� -> �ړ��s��
		else if(source instanceof PageElementPanel) {
		}
		// ����ȊO��EditPanel�C���X�^���X�̏ꍇ
		else if(source instanceof EditPanel) {
			EditPanel sourcePanel = (EditPanel)source;
			sourcePanel.setPosX(sourcePanel.getPosX() + xDiff);
			sourcePanel.setPosY(sourcePanel.getPosY() + yDiff);

			// �p�l���̎�ނɂ���ăT�C�Y��K�؂ɐݒ�
			int WIDTH = 0;
			int HEIGHT = 0;
			if(sourcePanel instanceof ServicePanel) {
				WIDTH = ServicePanel.PANEL_WIDTH;
				HEIGHT = ServicePanel.PANEL_HEIGHT;
			}
			else if(source instanceof TableReadingPanel) {
				WIDTH = TableReadingPanel.PANEL_WIDTH;
				HEIGHT = TableReadingPanel.PANEL_HEIGHT;
			}
			else if(source instanceof CreateFormReflectionPanel) {
				WIDTH = CreateFormReflectionPanel.PANEL_WIDTH;
				HEIGHT = CreateFormReflectionPanel.PANEL_HEIGHT;
			}
			else if(source instanceof CreateReflectionPanel) {
				WIDTH = CreateReflectionPanel.PANEL_WIDTH;
				HEIGHT = CreateReflectionPanel.PANEL_HEIGHT;
			}
			else if(source instanceof UpdateFormReflectionPanel) {
				WIDTH = UpdateFormReflectionPanel.PANEL_WIDTH;
				HEIGHT = UpdateFormReflectionPanel.PANEL_HEIGHT;
			}
			else if(source instanceof IaReflectionPanel) {
				WIDTH = IaReflectionPanel.PANEL_WIDTH;
				HEIGHT = IaReflectionPanel.PANEL_HEIGHT;
			}

			sourcePanel.setBounds(sourcePanel.getPosX(), sourcePanel.getPosY(), WIDTH, HEIGHT);
			add(sourcePanel);
		}

		
		MainFrame.repaintAndValidate();
	}

	
	
	
	
	
	/*
	 * PURPOSE :
	 * �}�E�X�N���b�N�C�x���g�n���h��
	 * �E�T�[�r�X�p�l���̃N���b�N -> �E���Ɉ����ƕҏW�{�^����\������
	 */
	public void mouseClicked(MouseEvent e) {
		Object source = e.getSource();
		
		// �G���[�i�p�l���ȊO�ւ̃N���b�N�B���肦�Ȃ��͂������j
		if(!(source instanceof JPanel)) {
			Debug.error("Panel_TpEdit_Left mousePressed() : �z��O�̃C�x���g�������ł��BJPanel�ł͂���܂���B");
		}

		// �ʏ탂�[�h���̃N���b�N
		if(mode==Panel_TpEdit_Above.MODE_NORMAL) {
			// �T�[�r�X�p�l���̍��N���b�N
			if(source instanceof ServicePanel) {
				ServicePanel srcCsp = (ServicePanel)source;
				Service service = srcCsp.getService();
				Panel_TransProcEdit_Sub.getInstance().showCsArgs(service);
				
				if(special_flag==0) {
					special_flag = 1;
					mouseClicked(e);
				}
			}
			// Create���t���N�V�����p�l���̍��N���b�N
			else if(source instanceof CreateReflectionPanel) {
				CreateReflectionPanel sourceCrp = (CreateReflectionPanel)source;
				TppCreateReflection createReflection = sourceCrp.reflection;
				Panel_TransProcEdit_Sub.getInstance().displayPanelCreateReflectionEdit(createReflection);
			}
			// IA���t���N�V�����p�l���̍��N���b�N
			else if(source instanceof IaReflectionPanel) {
				IaReflectionPanel sourceIrp = (IaReflectionPanel)source;
				TppIAReflection iaReflection = sourceIrp.reflection;
				Panel_TransProcEdit_Sub.getInstance().displayPanelIaReflectionEdit(iaReflection);
			}
		
			Frame_TransProcEdit_Sub.repaintAndValidate();
			Frame_TransProcEdit_Sub.getInstance().requestFocus();

			// �E�N���b�N
			if(e.getButton()==MouseEvent.BUTTON3) {
				// �w�i�ւ̉E�N���b�N -> �������Ȃ�
				if(e.getSource() instanceof Panel_TpEdit_Above) {
					Debug.out("�w�i�ւ̉E�N���b�N�Ȃ̂ŃX���[���܂�");
					return;
				}

				Debug.out("RightClick!");

				// ���̓p�l���E�N���b�N -> ���̃p�l�����L�^
				if(selectedInputPanel==null) {
					selectedInputPanel = (JPanel)e.getSource();
				}
				// �o�̓p�l���E�N���b�N -> �K�v�ȏ������s��
				else {
					selectedOutputPanel = (JPanel)e.getSource();
				
					// �o�͐悪Service
					if(selectedOutputPanel instanceof ServicePanel) {
						Service selectedOutputCs = ((ServicePanel)selectedOutputPanel).getService();
					
						// PageElement�p�l�� -> Service�p�l��
						if(selectedInputPanel instanceof PageElementPanel) {
							PageElement selectedInputPageElement = ((PageElementPanel)selectedInputPanel).getPageElement();

							// TableDisplay -> Service
							if(selectedInputPageElement instanceof PageElementTableDisplay) {
								// TFD����ł��󂯎��T�[�r�X���`�F�b�N����
								boolean recTfd = selectedOutputCs.canReceiveData(Service.IO_TYPE_TFD);
								// ���b��@�_�C�A���O�{�b�N�X�ȂǂŒ��ӂ�����
								if(!recTfd) {
									return;
								}

								// ����ȏ�TFD�C���v�b�g���`�ł���̂��`�F�b�N����
								boolean recMoreTfd = selectedOutputCs.canReceiveMoreData(Service.IO_TYPE_TFD);
								// ���b��@�_�C�A���O�{�b�N�X�ȂǂŒ��ӂ�����
								if(!recMoreTfd) {
									return;
								}
							
								MainFrame.getInstance().setEnabled(false);
							}
						}
					}
					// �o�͐悪PageElement
					else if(selectedOutputPanel instanceof PageElementPanel) {
					}
				
					// �������I������̂ŁA�u�ǂ̃p�l�������͌��Ƃ��ĉE�N���b�N����Ă��邩�v�̋L�^����ɖ߂�
					selectedInputPanel = null;
				}
			}
		}
		// INT/VARCHAR/DATETIME/DATE/TIME�^�T�[�r�X�����I�����[�h�ł̃N���b�N
		else if(mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_INT
				|| mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_VARCHAR
				|| mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATETIME
				|| mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATE
				|| mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_TIME) {

			// �y�[�W�\���v�f�p�l�����I�����ꂽ�ꍇ
			if(source instanceof PageElementPanel) {
				PageElementPanel pePanel = (PageElementPanel)source;
				PageElement pageElement = pePanel.pageElement;

				// SAIF�ȊO�̃y�[�W�\���v�f -> �������Ȃ��ŏI���
				if(!(pageElement instanceof PageElementSaif)) {
					return;
				}

				/* SAIF��\���p�l�������� */

				PageElementSaif saif = (PageElementSaif)pageElement;
				String saifDataType = saif.getSaifKind();
				
			
				// ���I�����悤�Ƃ��Ă���f�[�^�^�iINT/VARCHAR/DATETIME/DATE/TIME�j�ƁA�I�����ꂽSAIF�̃f�[�^�^����v���Ă�����A�T�[�r�X�����Ƃ��Č��肳���
				if((mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_INT && saifDataType.equals(PageElementSaif.KIND_INT))
					|| (mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_VARCHAR && saifDataType.equals(PageElementSaif.KIND_VARCHAR))
					|| (mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATETIME && saifDataType.equals(PageElementSaif.KIND_DATETIME))
					|| (mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_DATE && saifDataType.equals(PageElementSaif.KIND_DATE))
					|| (mode==Panel_TpEdit_Above.MODE_SELECT_SERVICE_ARG_TIME && saifDataType.equals(PageElementSaif.KIND_TIME))) {
					PanelServiceArgEdit.getInstance().informSelectedSaifAsServiceArg(saif);
				}
			}
		}
		// TFD�o�͌^TPP��I������e���[�h�ł̃N���b�N
		else if(mode==Panel_TpEdit_Above.MODE_SELECT_TFD || mode==Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD) {
			// �T�[�r�X�p�l���̑I��
			if(source instanceof ServicePanel) {
				ServicePanel csp = (ServicePanel)source;
				Service serv = csp.getService();

				// �N���b�N���ꂽ�T�[�r�X�p�l���̏o�͂�TFD�ł͂Ȃ��ꍇ
				if(serv.outputType!=Service.IO_TYPE_TFD) {
					// ���{���́u�o�͂��\�`���ł͂���܂���v�Ƃ̌x���𔭂�����
					return;
				}
				
				// �I�����ꂽTFD�o�͌^TPP���A���[�h�ɉ����ēK�؂ȃp�l���֕�
				if(mode==Panel_TpEdit_Above.MODE_SELECT_TFD) {	// �T�[�r�X�̕\�f�[�^�����I��
					PanelServiceArgEdit.getInstance().informSelectedTfdAsServiceArg((TfdOutputer)serv);
				}
				else if(mode==Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD) {	// ���R�[�h�쐬�����ւ̓��͕\�f�[�^�I��
					PanelCreateReflectionEdit.getInstance().informSelectedTfdForCreateReflection((TfdOutputer)serv);
				}
			}
			// �e�[�u�����[�f�B���O�p�l���̑I��
			else if(source instanceof TableReadingPanel) {
				TableReadingPanel trp = (TableReadingPanel)source;
				TppTableReading tblRead = trp.getTblRead();
				
				// �I�����ꂽTFD�o�͌^TPP���A���[�h�ɉ����ēK�؂ȃp�l���֕�
				if(mode==Panel_TpEdit_Above.MODE_SELECT_TFD) {
					PanelServiceArgEdit.getInstance().informSelectedTfdAsServiceArg((TfdOutputer)tblRead);
				}
				else if(mode==Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD) {
					PanelCreateReflectionEdit.getInstance().informSelectedTfdForCreateReflection((TfdOutputer)tblRead);
				}
			}
			// Create�t�H�[�����t���N�V�����p�l���̑I��
			else if(source instanceof CreateFormReflectionPanel) {
				CreateFormReflectionPanel cfrp = (CreateFormReflectionPanel)source;
				TppCreateFormReflection reflection = cfrp.reflection;
				
				// �I�����ꂽTFD�o�͌^TPP���A���[�h�ɉ����ēK�؂ȃp�l���֕�
				if(mode==Panel_TpEdit_Above.MODE_SELECT_TFD) {
					PanelServiceArgEdit.getInstance().informSelectedTfdAsServiceArg((TfdOutputer)reflection);
				}
				else if(mode==Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD) {
					PanelCreateReflectionEdit.getInstance().informSelectedTfdForCreateReflection((TfdOutputer)reflection);
				}
			}
			// Create���t���N�V�����p�l���̑I��
			else if(source instanceof CreateReflectionPanel) {
				CreateReflectionPanel crp = (CreateReflectionPanel)source;
				TppCreateReflection reflection = crp.reflection;
				
				// �I�����ꂽTFD�o�͌^TPP���A���[�h�ɉ����ēK�؂ȃp�l���֕�
				if(mode==Panel_TpEdit_Above.MODE_SELECT_TFD) {
					PanelServiceArgEdit.getInstance().informSelectedTfdAsServiceArg((TfdOutputer)reflection);
				}
				else if(mode==Panel_TpEdit_Above.MODE_SELECT_CREATE_REFLECTION_INPUT_TFD) {
					PanelCreateReflectionEdit.getInstance().informSelectedTfdForCreateReflection((TfdOutputer)reflection);
				}
			}
		}
		// �T�[�r�X�o�͐�iTFD�j�I�����[�h�ł̃N���b�N
		else if(mode==Panel_TpEdit_Above.MODE_SELECT_OUTPUT_TFD) {
			// ���������@TFD�^���������T�[�r�X�̃T�[�r�X�p�l���I��
			//
			//

			
			// PageElement�p�l���̑I��
			if(source instanceof PageElementPanel) {
				// �I�����ꂽPageElement�̒��o
				PageElementPanel sourcePep = (PageElementPanel)source;
				PageElement sourcePe = sourcePep.pageElement;
				
				// ���ʃe�[�u���̑I��
				if(sourcePe instanceof PageElementDisplayArea) {
					PageElementPort sourcePeInputPort = sourcePe.inputPePortsHashMap.get(editingTp);
					PanelServiceArgEdit.getInstance().informSelectedPeInputPort(sourcePeInputPort);
				}
			}
			// �J�ڐ�y�[�W�p�l�����N���b�N���ꂽ�ꍇ
			else if(source instanceof EndPagePanel) {
				// �܂��A�Ƃ����Ɍ��ʃe�[�u���\���̃y�[�W�G�������g���쐬�A�J�ڐ�y�[�W�ɒǉ�
				EndPagePanel epp = (EndPagePanel)source;
				WebPage endPage = epp.getPage();
				PageElementDisplayArea rtd = new PageElementDisplayArea(endPage);
				endPage.addPageElement(rtd);
				
				// ��x�A�R���|�[�l���g�̔z�u���������i�������Ȃ��ƁA���ǉ��������ʃe�[�u���\���y�[�W�G�������g�̃p�l�����\������Ȃ��j
				locateTppPanels();

				PageElementPort rtdInputPort = rtd.inputPePortsHashMap.get(editingTp);
				PanelServiceArgEdit.getInstance().informSelectedPeInputPort(rtdInputPort);
			}
		}
		// IA���t���N�V�����̃f�[�^�e�[�u��TFD�A�A�J�E���g�e�[�u��TFD�I�����[�h�ł̃N���b�N
		else if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD || mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD) {
			// �e�[�u�����[�f�B���O�p�l���̑I��
			if(source instanceof TableReadingPanel) {
				TableReadingPanel trp = (TableReadingPanel)source;
				TppTableReading tblRead = trp.getTblRead();
				SuperTable table = tblRead.getTable();
				
				// �f�[�^�e�[�u��TFD�I�����[�h�ŁA����IA���t���N�V�����̃f�[�^�e�[�u���ƃe�[�u�����[�f�B���O�̃e�[�u��������
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.dataTable) {
						PanelIaReflectionEdit.getInstance().informSelectedDtTfd((TfdOutputer)tblRead);
					}
				}
				// �A�J�E���g�e�[�u��TFD�I�����[�h�ŁA����IA���t���N�V�����̃A�J�E���g�e�[�u���ƃe�[�u�����[�f�B���O�̃e�[�u��������
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.accountTable) {
						PanelIaReflectionEdit.getInstance().informSelectedAtTfd((TfdOutputer)tblRead);
					}
				}
			}
			// Create���t���N�V�����̑I��
			else if(source instanceof CreateReflectionPanel) {
				CreateReflectionPanel crp = (CreateReflectionPanel)source;
				TppCreateReflection cr = crp.reflection;
				SuperTable table = cr.table;
				
				// �f�[�^�e�[�u��TFD�I�����[�h�ŁA����IA���t���N�V�����̃f�[�^�e�[�u����Create���t���N�V�����̃e�[�u��������
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.dataTable) {
						PanelIaReflectionEdit.getInstance().informSelectedDtTfd((TfdOutputer)cr);
					}
				}
				// �A�J�E���g�e�[�u��TFD�I�����[�h�ŁA����IA���t���N�V�����̃A�J�E���g�e�[�u����Create���t���N�V�����̃e�[�u��������
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.accountTable) {
						PanelIaReflectionEdit.getInstance().informSelectedAtTfd((TfdOutputer)cr);
					}
				}
			}
			// Create�t�H�[�����t���N�V�����̑I��
			else if(source instanceof CreateFormReflectionPanel) {
				CreateFormReflectionPanel crp = (CreateFormReflectionPanel)source;
				TppCreateFormReflection cr = crp.reflection;
				SuperTable table = cr.createForm.table;
				
				// �f�[�^�e�[�u��TFD�I�����[�h�ŁA����IA���t���N�V�����̃f�[�^�e�[�u����Create�t�H�[�����t���N�V�����̃e�[�u��������
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_DT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.dataTable) {
						PanelIaReflectionEdit.getInstance().informSelectedDtTfd((TfdOutputer)cr);
					}
				}
				// �A�J�E���g�e�[�u��TFD�I�����[�h�ŁA����IA���t���N�V�����̃A�J�E���g�e�[�u����Create�t�H�[�����t���N�V�����̃e�[�u��������
				if(mode==Panel_TpEdit_Above.MODE_SELECT_IA_REFLECTION_INPUT_AT_TFD) {
					if(table==PanelIaReflectionEdit.getInstance().reflection.accountTable) {
						PanelIaReflectionEdit.getInstance().informSelectedAtTfd((TfdOutputer)cr);
					}
				}
			}

			// ��L�R��ވȊO�̃N���b�N�ɑ΂��Ă͉������Ȃ�
		}

		MainFrame.repaintAndValidate();
		Frame_TransProcEdit_Sub.repaintAndValidate();
		Frame_TransProcEdit_Sub.getInstance().requestFocus();
	}

		
	
	
	
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}

	
	
	
	public ServicePanel getCsPanelByCs(Service cs) {
		for(int i=0; i<csPanels.size(); i++) {
			ServicePanel csp = csPanels.get(i);
			Service compServ = csp.getService();
			if(compServ==cs) {
				return csp;
			}
		}

		Debug.error("Panel_TpEdit_Left getCsPanelByCs() : ServicePanel�C���X�^���X��������܂���ł���");
		return null;
	}

	
	
	
	
	public CreateFormReflectionPanel getCfrPanelByCfr(TppCreateFormReflection cfr) {
		for(int i=0; i<panelArrayCreateFormReflection.size(); i++) {
			CreateFormReflectionPanel cfrp = panelArrayCreateFormReflection.get(i);
			TppCreateFormReflection reflection = cfrp.reflection;
			if(reflection==cfr) {
				return cfrp;
			}
		}

		Debug.error("CreateFormReflectionPanel��������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}


	
	

	/*
	 * SUMMARY :
	 * Update�t�H�[�����t���N�V�������󂯎��A�����\���p�l����ԋp
	 */
	public UpdateFormReflectionPanel getUfrPanelByUfr(TppUpdateFormReflection ufr) {
		for(int i=0; i<panelArrayUpdateFormReflection.size(); i++) {
			UpdateFormReflectionPanel ufrp = panelArrayUpdateFormReflection.get(i);
			TppUpdateFormReflection reflection = ufrp.reflection;
			if(reflection==ufr) {
				return ufrp;
			}
		}

		Debug.error("UpdateFormReflectionPanel��������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	
	public CreateReflectionPanel getCrPanelByCr(TppCreateReflection createReflection) {
		for(int i=0; i<panelArrayCreateReflection.size(); i++) {
			CreateReflectionPanel crp = panelArrayCreateReflection.get(i);
			TppCreateReflection reflection = crp.reflection;
			if(reflection==createReflection) {
				return crp;
			}
		}

		Debug.error("CreateReflectionPanel��������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	
	public IaReflectionPanel getIrPanelByIr(TppIAReflection iaReflection) {
		for(int i=0; i<panelArrayIaReflection.size(); i++) {
			IaReflectionPanel irp = panelArrayIaReflection.get(i);
			TppIAReflection reflection = irp.reflection;
			if(reflection==iaReflection) {
				return irp;
			}
		}

		Debug.error("IaReflectionPanel��������܂���", getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
		return null;
	}

	
	
	
	
	public TableReadingPanel getTrPanelByTr(TppTableReading tr) {
		for(int i=0; i<tblReadPanels.size(); i++) {
			TableReadingPanel trp = tblReadPanels.get(i);
			TppTableReading tblRead = trp.getTblRead();
			if(tblRead==tr) {
				return trp;
			}
		}

		Debug.error("Panel_TpEdit_Left getTrPanelByTr() : TableReadingPanel�C���X�^���X��������܂���ł���");
		return null;
	}

	
	
	
	
	public void setEditingTp(TransitionProcess tp) {
		editingTp = tp;
	}
	
	
	
	
	
	public TransitionProcess getEditingTp() {
		return editingTp;
	}
	
	
	
	
	
	public static Panel_TpEdit_Above getInstance() {
		return Panel_TpEdit_Above.obj;
	}




	public static void updateInstance(Panel_TpEdit_Above newObject) {
		Panel_TpEdit_Above.obj = newObject;
	}


}
