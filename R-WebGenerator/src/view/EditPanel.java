package view;

import java.io.Serializable;

import javax.swing.JPanel;

/*
 * ���g�̔z�u�ʒu�̍��W���L�^����K�v�̂���p�l��
 */
public class EditPanel extends JPanel implements Serializable {
	public int posX;
	public int posY;

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void move(int posX, int posY) {
		setPosX(posX);
		setPosY(posY);
	}
}
