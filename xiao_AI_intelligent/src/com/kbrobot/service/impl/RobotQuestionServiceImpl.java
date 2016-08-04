package com.kbrobot.service.impl;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kbrobot.service.RobotQuestionServiceI;

@Service("robotQuestionService")
@Transactional
public class RobotQuestionServiceImpl extends CommonServiceImpl implements RobotQuestionServiceI {
	
}