<?php

function getGuestRoleNumber() {
	$query = 'SELECT * FROM ' . TABLE_GUEST_ROLE_NUMBER;
	$result = mysql_query_logg($query);

	// �G���[ : ���ʂ̍s�����P�łȂ�
	if(mysql_num_rows($result)!==1) {
		error_logg('�O���[�o�����\�b�h ' . __FUNCTION__ . '() : ���ʃ��\�[�X�̍s�����P�ƂȂ��Ă��܂���');
		return null;
	}

	$number = mysql_result($result, 0, 0);

	return $number;
}

?>