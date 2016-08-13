package com.kbrobot.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kbrobot.service.RobotSimilarQuestionServiceI;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("robotSimilarQuestionEntityService")
@Transactional
public class RobotSimilarQuestionServiceImpl extends CommonServiceImpl implements RobotSimilarQuestionServiceI {
	
}