package com.kbrobot.service.impl;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.service.RobotQuestionServiceI;

@Service("robotQuestionService")
@Transactional
public class RobotQuestionServiceImpl extends CommonServiceImpl implements RobotQuestionServiceI {

	@Override
	public void updateRobotQuestionMatchTimes(String id) {
		RobotQuestionEntity entity = this.get(RobotQuestionEntity.class, id);
		entity.setMatchTimes(entity.getMatchTimes()+1);
		this.saveOrUpdate(entity);
		
	}
	
}